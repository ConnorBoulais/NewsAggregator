package logic;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import visitor.ListQueryVisitor;
import visitor.NewsAntlrConfigReader;

public class Driver {

  private static void scheduleQueries(
      List<NewsQuery> newsQueries,
      BlockingQueue<List<Article>> blockingQueue,
      ScheduledExecutorService scheduler) {

    for (NewsQuery newsQuery : newsQueries) {

      if (newsQuery.getDelay() <= 0) {

        scheduler.execute(
            () -> {
              try {
                blockingQueue.put(newsQuery.getProcessor().extract());
              } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                Thread.currentThread().interrupt();
              }
            }
        );
      } else {

        scheduler.scheduleWithFixedDelay(
            () -> {
              try {
                blockingQueue.put(newsQuery.getProcessor().extract());
              } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                Thread.currentThread().interrupt();
              }
            },
            0, newsQuery.getDelay(), TimeUnit.MILLISECONDS
        );
      }
    }
  }

  private static void schedulePrintThread(
      BlockingQueue<List<Article>> blockingQueue,
      ScheduledExecutorService scheduler) {

    scheduler.execute(
        () -> {
          while (!Thread.interrupted()) {
            try {
              List<Article> articles = blockingQueue.take();
              if (!articles.isEmpty()) {
                System.out.println(articles);
              }
            } catch (InterruptedException e) {
              System.out.println(e.getMessage());
              Thread.currentThread().interrupt();
            }
          }
      }
    );
  }

  /**
   * Demonstrates the functionality of NewsProcessor and
   * subsequent visitor classes, by reading config files,
   * creating processors for each source, and displaying
   * the articles retrieved from each source.
   *
   * @param args - Array of config file paths
   */
  public static void main(String[] args) {

    String apiKey = args[0];
    Logger logger = Logger.getLogger(Driver.class.toString());
    List<String> files = Arrays.asList(args).subList(1, args.length);

    NewsAntlrConfigReader newsAntlrConfigReader = new NewsAntlrConfigReader(files, logger);
    List<NewsQuery> newsQueries = newsAntlrConfigReader.getData(
        new ListQueryVisitor(logger, apiKey)
    );

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(
        Runtime.getRuntime().availableProcessors()
    );

    BlockingQueue<List<Article>> blockingQueue = new LinkedBlockingQueue<>();

    scheduleQueries(newsQueries, blockingQueue, scheduler);

    schedulePrintThread(blockingQueue, scheduler);
  }
}