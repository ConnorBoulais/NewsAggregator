package logic;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class NewsApiParserTest {

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

    List<Article> actualArticles = new NewsApiParser().parseArticles(
      new ByteArrayInputStream(inputJson.getBytes()),
      logger
    );

    assertEquals(expectedArticles, actualArticles);
  }

  @Test
  void testMissingTitle() {

    //build input json objects
    JSONObject articleJson = new JSONObject(Map.of (
        "description", "des1",
        "url", "url1",
        "publishedAt", validPubTime
    ));
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("status", "ok");
    jsonObject.put("totalResults", "0");
    jsonObject.put("articles", Collections.singletonList(articleJson));

    //build json string from json objects
    String inputJson = jsonObject.toString();

    //build expected data
    List<String> expectedLogs = Collections.singletonList("article with missing field: title");
    List<Article> expectedArticles = new LinkedList<>();

    //get the news
    List<Article> actualArticles = new NewsApiParser().parseArticles(
      new ByteArrayInputStream(inputJson.getBytes()),
      logger
    );

    //compare
    assertEquals(expectedArticles, actualArticles);
    assertEquals(expectedLogs, logHandler.getLogs());
  }

  @Test
  void testInvalidFieldType(){

    //build input json objects
    JSONObject descriptionJsonObject = new JSONObject(Map.of("id", "id1", "name", "name1"));
    JSONObject articleJson = new JSONObject(Map.of(
      "title", "title1",
      "url", "url1",
      "publishedAt", validPubTime
    ));
    articleJson.put("description", descriptionJsonObject);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("status", "ok");
    jsonObject.put("totalResults", "0");
    jsonObject.put("articles", Collections.singletonList(articleJson));

    //convert input json object to json string
    String inputJson = jsonObject.toString();

    //build expected data
    List<String> expectedLogs = Collections.singletonList("article with invalid field: description");
    List<Article> expectedArticles = new LinkedList<>();

    //get the news
    List<Article> actualArticles = new NewsApiParser().parseArticles(
      new ByteArrayInputStream(inputJson.getBytes()),
      logger
    );

    //compare
    assertEquals(expectedArticles, actualArticles);
    assertEquals(expectedLogs, logHandler.getLogs());
  }

  @Test
  void testNullField(){

    //build input json objects
    JSONObject articleJson = new JSONObject(Map.of(
      "description", "des1",
      "url", "url1",
      "publishedAt", validPubTime
    ));
    articleJson.put("title", JSONObject.NULL);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("status", "ok");
    jsonObject.put("totalResults", "0");
    jsonObject.put("articles", Collections.singletonList(articleJson));

    //convert input json object to json string
    String inputJson = jsonObject.toString();

    //build expected data
    List<String> expectedLogs = Collections.singletonList("article with invalid field: title");
    List<Article> expectedArticles = new LinkedList<>();

    //get the news
    List<Article> actualArticles = new NewsApiParser().parseArticles(
      new ByteArrayInputStream(inputJson.getBytes()),
      logger
    );

    //compare
    assertEquals(expectedArticles, actualArticles);
    assertEquals(expectedLogs, logHandler.getLogs());
  }
}