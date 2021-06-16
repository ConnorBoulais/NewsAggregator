package logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FilterExpressionTest {

  private final String validPubTime = "2020-09-21T19:58:27Z";

  @Test
  void testKeywordExpression() {
    Article article = new Article(
        "Election 2020", "The election is still going",
        "cnn.com", validPubTime
    );

    KeywordExpression keywordExpression1 = new KeywordExpression("election");
    KeywordExpression keywordExpression2 = new KeywordExpression("cnn");
    KeywordExpression keywordExpression3 = new KeywordExpression("nytimes");

    assertTrue(keywordExpression1.evaluate(article));
    assertTrue(keywordExpression2.evaluate(article));
    assertFalse(keywordExpression3.evaluate(article));
  }

  @Test
  void testAndExpression() {
    Article article = new Article(
        "Election 2020", "The election is still going",
        "cnn.com", validPubTime
    );

    KeywordExpression keywordExpression1 = new KeywordExpression("election");
    KeywordExpression keywordExpression2 = new KeywordExpression("2020");
    KeywordExpression keywordExpression3 = new KeywordExpression("nytimes");
    AndExpression andExpression1 = new AndExpression(keywordExpression1, keywordExpression2);
    AndExpression andExpression2 = new AndExpression(keywordExpression1, keywordExpression3);

    assertTrue(andExpression1.evaluate(article));
    assertFalse(andExpression2.evaluate(article));
  }

  @Test
  void testOrExpression() {
    Article article = new Article(
        "Election 2020", "The election is still going",
        "cnn.com", validPubTime
    );

    KeywordExpression keywordExpression1 = new KeywordExpression("election");
    KeywordExpression keywordExpression2 = new KeywordExpression("2020");
    KeywordExpression keywordExpression3 = new KeywordExpression("nytimes");
    KeywordExpression keywordExpression4 = new KeywordExpression("wsj");
    OrExpression orExpression1 = new OrExpression(keywordExpression1, keywordExpression2);
    OrExpression orExpression2 = new OrExpression(keywordExpression1, keywordExpression3);
    OrExpression orExpression3 = new OrExpression(keywordExpression3, keywordExpression4);

    assertTrue(orExpression1.evaluate(article));
    assertTrue(orExpression2.evaluate(article));
    assertFalse(orExpression3.evaluate(article));
  }

  @Test
  void testEmptyExpression() {
    Article article = new Article(
        "Election 2020", "The election is still going",
        "cnn.com", validPubTime
    );
    EmptyFilterExpression emptyFilterExpression = new EmptyFilterExpression();

    assertTrue(emptyFilterExpression.evaluate(article));
  }
}