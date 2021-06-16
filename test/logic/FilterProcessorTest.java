package logic;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FilterProcessorTest {

  private static Logger logger;
  private static LogHandler logHandler;
  private final String validPubTime = "2020-09-21T19:58:27Z";

  @BeforeAll
  static void initAll() {
    logger = Logger.getLogger(NewsApiParserTest.class.getName() + ".testName");
    logger.setUseParentHandlers(false);
    logHandler = new LogHandler();
    logger.addHandler(logHandler);
  }

  @AfterEach
  void tearDown() {
    logHandler.clear();
  }

  @Test
  void testWithNestedFilter(){
    //build input json objects
    JSONObject article1Json = new JSONObject(Map.of( "title", "title1",
      "description", "des1", "url", "url1", "publishedAt", validPubTime));
    JSONObject article2Json = new JSONObject(Map.of("title", "title2",
      "description", "des2", "url", "url2", "publishedAt", validPubTime));
    JSONObject article3Json = new JSONObject(Map.of( "title", "title3",
      "description", "des3", "url", "url3", "publishedAt", validPubTime));
    JSONObject article4Json = new JSONObject(Map.of("title", "title4",
      "description", "des4", "url", "url4", "publishedAt", validPubTime));

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("status", "ok");
    jsonObject.put("totalResults", "2");
    jsonObject.put("articles", Arrays.asList(article1Json, article2Json, article3Json, article4Json));

    List<Article> expectedArticles = Arrays.asList(
      new Article("title2", "des2", "url2", validPubTime),
      new Article("title4", "des4", "url4", validPubTime)
    );

    //convert input json object to json string
    String inputJson = jsonObject.toString();

    DataSource mockDataSource = mock(DataSource.class);
    when(mockDataSource.openStream())
      .thenReturn(new ByteArrayInputStream(inputJson.getBytes()));

    FilterExpression filterExpression =
      new OrExpression(
        new AndExpression(
          new KeywordExpression("url2"),
          new KeywordExpression("des2")
        ),
        new KeywordExpression("title4")
    );

    List<Article> actualArticles = new FilterProcessor(
      new NewsProcessor(
        mockDataSource,
        new NewsApiParser(),
        logger),
      filterExpression).extract();

    assertEquals(expectedArticles, actualArticles);
  }

  @Test
  void testWithAndFilter(){
    //build input json objects
    JSONObject article1Json = new JSONObject(Map.of( "title", "Fires in California",
        "description", "Fires damage California", "url", "cnn.com",
        "publishedAt", validPubTime));
    JSONObject article2Json = new JSONObject(Map.of("title", "Election 2020",
        "description", "The election is still going", "url", "nytimes.com",
        "publishedAt", validPubTime));

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("status", "ok");
    jsonObject.put("totalResults", "2");
    jsonObject.put("articles", Arrays.asList(article1Json, article2Json));

    List<Article> expectedArticles = Arrays.asList(
        new Article("Fires in California", "Fires damage California",
          "cnn.com", validPubTime)
    );

    //convert input json object to json string
    String inputJson = jsonObject.toString();

    DataSource mockDataSource = mock(DataSource.class);
    when(mockDataSource.openStream())
      .thenReturn(new ByteArrayInputStream(inputJson.getBytes()));

    AndExpression andExpression = new AndExpression(
      new KeywordExpression("cnn.com"),
      new KeywordExpression("Fire")
    );

    List<Article> actualArticles = new FilterProcessor(
      new NewsProcessor(
        mockDataSource,
        new NewsApiParser(),
        logger),
      andExpression).extract();

    assertEquals(expectedArticles, actualArticles);
  }

  @Test
  void testNoFilter(){
    //build input json objects
    JSONObject article1Json = new JSONObject(Map.of( "title", "title1",
      "description", "des1", "url", "url1", "publishedAt", validPubTime));
    JSONObject article2Json = new JSONObject(Map.of("title", "title2",
      "description", "des2", "url", "url2", "publishedAt", validPubTime));

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("status", "ok");
    jsonObject.put("totalResults", "2");
    jsonObject.put("articles", Arrays.asList(article1Json, article2Json));

    List<Article> expectedArticles = Arrays.asList(
      new Article("title1", "des1", "url1", validPubTime),
      new Article("title2", "des2", "url2", validPubTime)
    );

    //convert input json object to json string
    String inputJson = jsonObject.toString();

    DataSource mockDataSource = mock(DataSource.class);
    when(mockDataSource.openStream())
      .thenReturn(new ByteArrayInputStream(inputJson.getBytes()));

    List<Article> actualArticles = new FilterProcessor(
        new NewsProcessor(
          mockDataSource,
          new NewsApiParser(),
          logger),
        new EmptyFilterExpression()).extract();

    assertEquals(expectedArticles, actualArticles);
  }
}