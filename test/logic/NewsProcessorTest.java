package logic;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.logging.Logger;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NewsProcessorTest {

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
  void testValidInput(){
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

    List<Article> actualArticles = new NewsProcessor(
      mockDataSource,
      new NewsApiParser(),
      logger
    ).extract();

    assertEquals(expectedArticles, actualArticles);
  }
}