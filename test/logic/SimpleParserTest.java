package logic;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.logging.Logger;
import static org.junit.jupiter.api.Assertions.*;

class SimpleParserTest {

  private static Logger logger;
  private static LogHandler logHandler;
  private final String validPubTime = "2020-09-21T19:58:27Z";

  @BeforeAll
  static void initAll() {
    logger = Logger.getLogger(SimpleParserTest.class.getName() + ".testName");
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
    JSONObject articleJson = new JSONObject(Map.of( "title", "title1",
      "description", "des1", "url", "url1", "publishedAt", validPubTime));

    List<Article> expectedArticles = Collections.singletonList(
      new Article("title1", "des1", "url1", validPubTime)
    );

    //convert input json object to json string
    String inputJson = articleJson.toString();

    List<String> expectedLogs = new LinkedList<>();
    List<Article> actualArticles = new SimpleParser().parseArticles(
      new ByteArrayInputStream(inputJson.getBytes()),
      logger
    );

    assertEquals(expectedArticles, actualArticles);
    assertEquals(expectedLogs, logHandler.getLogs());
  }

  @Test
  void testMissingField(){

    //build input json objects
    JSONObject articleJson = new JSONObject(Map.of(
      "title", "title1", "description", "des1", "url", "url1")
    );

    //convert input json object to json string
    String inputJson = articleJson.toString();

    List<String> expectedLogs = Collections.singletonList("article with missing field: publishedAt");
    List<Article> expectedArticles = new LinkedList<>();
    List<Article> actualArticles = new SimpleParser().parseArticles(
      new ByteArrayInputStream(inputJson.getBytes()),
      logger
    );

    assertEquals(expectedArticles, actualArticles);
    assertEquals(expectedLogs, logHandler.getLogs());
  }

  @Test
  void testInvalidField(){

    //build input json objects
    JSONObject articleJson = new JSONObject(Map.of(
      "title", "title1", "description", "des1", "url", "url1")
    );
    JSONObject testObj = new JSONObject(Map.of("test1", "t1", "test2", "t2"));
    articleJson.put("publishedAt", testObj);

    //convert input json object to json string
    String inputJson = articleJson.toString();

    List<String> expectedLogs = Collections.singletonList("article with invalid field: publishedAt");
    List<Article> expectedArticles = new LinkedList<>();
    List<Article> actualArticles = new SimpleParser().parseArticles(
      new ByteArrayInputStream(inputJson.getBytes()),
      logger
    );

    assertEquals(expectedArticles, actualArticles);
    assertEquals(expectedLogs, logHandler.getLogs());
  }

  @Test
  void testNullField(){

    //build input json objects
    JSONObject articleJson = new JSONObject(Map.of(
      "title", "title1", "description", "des1", "url", "url1")
    );
    articleJson.put("publishedAt", JSONObject.NULL);

    //convert input json object to json string
    String inputJson = articleJson.toString();

    List<String> expectedLogs = Collections.singletonList("article with invalid field: publishedAt");
    List<Article> expectedArticles = new LinkedList<>();
    List<Article> actualArticles = new SimpleParser().parseArticles(
      new ByteArrayInputStream(inputJson.getBytes()),
      logger
    );

    assertEquals(expectedArticles, actualArticles);
    assertEquals(expectedLogs, logHandler.getLogs());
  }
}