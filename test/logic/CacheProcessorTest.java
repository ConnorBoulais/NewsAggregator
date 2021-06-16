package logic;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.logging.Logger;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CacheProcessorTest {

  private static Logger logger;
  private static LogHandler logHandler;
  private final String validPubTime = "2020-09-21T19:58:27Z";
  private final String validOldPubTime = "2018-09-21T19:58:27Z";

  @BeforeAll
  static void initAll() {
    logger = Logger.getLogger(NewsApiParserTest.class.getName() + ".testName");
    logger.setUseParentHandlers(false);
    logHandler = new LogHandler();
    logger.addHandler(logHandler);
  }

  @Test
  void testSingleCall() {

    Article article1 = new Article("title1", "des1",  "url1",  validPubTime);
    Article article2 = new Article("title2", "des2",  "url2",  validPubTime);
    Article article3 = new Article("title3", "des3",  "url3",  validPubTime);
    Article article4 = new Article("title4", "des4",  "url4",  validPubTime);

    List<Article> expectedArticles = Arrays.asList(article1, article2, article3, article4);

    Processor mockProcessor = mock(Processor.class);
    when(mockProcessor.extract())
        .thenReturn(expectedArticles);

    List<Article> actualArticles = new CacheProcessor(mockProcessor).extract();

    assertEquals(expectedArticles, actualArticles);
  }

  @Test
  void testMultipleCalls() {

    Article article1 = new Article("title1", "des1",  "url1",  validPubTime);
    Article article2 = new Article("title2", "des2",  "url2",  validPubTime);
    Article article3 = new Article("title3", "des3",  "url3",  validPubTime);
    Article article4 = new Article("title4", "des4",  "url4",  validPubTime);

    List<Article> allArticles = new LinkedList<>(Arrays.asList(article1, article2, article3, article4));
    List<Article> list2 = new LinkedList<>(Arrays.asList(article2, article4));
    List<Article> list3 = new LinkedList<>(Arrays.asList(article1, article3));


    Processor mockProcessor = mock(Processor.class);
    when(mockProcessor.extract())
      .thenReturn(allArticles, list2, list3);

    Processor cacheProcessor = new CacheProcessor(mockProcessor);
    List<Article> firstCall = cacheProcessor.extract();
    List<Article> secondCall = cacheProcessor.extract();
    List<Article> thirdCall = cacheProcessor.extract();

    assertEquals(allArticles, firstCall);
    assertEquals(Collections.EMPTY_LIST, secondCall);
    assertEquals(Collections.EMPTY_LIST, thirdCall);
  }

  @Test
  void testCacheSizeControl() {

    Article article1 = new Article("title1", "des1",  "url1",  validOldPubTime);
    Article article2 = new Article("title2", "des2",  "url2",  validOldPubTime);
    Article article3 = new Article("title3", "des3",  "url3",  validPubTime);
    Article article4 = new Article("title4", "des4",  "url4",  validPubTime);
    Article article5 = new Article("title5", "des5",  "url5",  validPubTime);

    List<Article> list1 = new LinkedList<>(Arrays.asList(article1, article2, article3, article4));
    List<Article> list2 = new LinkedList<>(Arrays.asList(article4, article5));
    List<Article> list3 = new LinkedList<>(Arrays.asList(article1, article2));

    Processor mockProcessor = mock(Processor.class);
    when(mockProcessor.extract())
      .thenReturn(list1, list3, list2, list3);

    Processor cacheProcessor = new CacheProcessor(mockProcessor);
    List<Article> firstCall = cacheProcessor.extract();
    List<Article> secondCall = cacheProcessor.extract();
    List<Article> thirdCall = cacheProcessor.extract();
    List<Article> fourthCall = cacheProcessor.extract();

    assertEquals(list1, firstCall);
    assertEquals(Collections.EMPTY_LIST, secondCall);
    assertEquals(Collections.singletonList(article5), thirdCall);
    assertEquals(list3, fourthCall);
  }
}