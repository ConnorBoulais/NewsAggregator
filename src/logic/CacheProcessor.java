package logic;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CacheProcessor implements Processor {

  private final Processor processor;
  private HashSet<Article> seenArticles;

  /**
   * Constructs a CacheProcessor given another processor.
   *
   * @param processor - Processor to decorate cache functionality over
   */
  public CacheProcessor(Processor processor) {
    this.processor = processor;
    this.seenArticles = new HashSet<>();
  }

  /**
   * Extracts articles from the stored processor, while filtering out
   * articles that are currently in the cache. Also, this function updates
   * the cache by adding newly encountered articles, and removing cached
   * articles that are older than the oldest new article.
   *
   * @return List of Article extracted from the processor, will cached
   *      articles filtered out.
   */
  public List<Article> extract() {
    List<Article> newArticles = processor.extract();
    newArticles.removeIf(a -> seenArticles.contains(a));
    seenArticles.addAll(newArticles);
    removeOldArticles(newArticles);
    return newArticles;
  }

  private void removeOldArticles(List<Article> newArticles) {
    if (!newArticles.isEmpty()) {

      Optional<Article> oldestNewTime = newArticles.stream()
          .min(Comparator.comparing(Article::getPublishedAt));

      if (oldestNewTime.isPresent()) {
        LocalDateTime finalTime = oldestNewTime.get().getPublishedAt();

        this.seenArticles = this.seenArticles.stream()
          .filter(
            a -> a.getPublishedAt().isAfter(finalTime) || a.getPublishedAt().isEqual(finalTime))
          .collect(Collectors.toCollection(HashSet::new));
      }
    }
  }

  /**
   * TESTING METHOD - gets the DataSource of the processor.
   *
   * @return DataSource of processor
   */
  public DataSource getDataSource() {
    return this.processor.getDataSource();
  }

  /**
   * TESTING METHOD - gets the Parser of the processor.
   *
   * @return Parser of the processor
   */
  public ArticleParser getParser() {
    return processor.getParser();
  }
}
