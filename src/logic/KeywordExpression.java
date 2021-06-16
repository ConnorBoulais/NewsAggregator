package logic;

public class KeywordExpression implements FilterExpression {

  private final String keyword;

  /**
   * Constructs a KeywordExpression.
   *
   * @param keyword - String for keyword
   */
  public KeywordExpression(String keyword) {
    this.keyword = keyword.toLowerCase();
  }

  /**
   * Determines if the keyword is present in either the article's
   * title, description, url, or publish time.
   *
   * @param article - article to evaluate
   * @return boolean for whether the keyword exists in
   *      any of the article's fields
   */
  @Override
  public boolean evaluate(Article article) {
    return article.getTitle().toLowerCase().contains(keyword)
      || article.getDescription().toLowerCase().contains(keyword)
      || article.getUrl().toLowerCase().contains(keyword)
      || article.getPublishedAt().toString().toLowerCase().contains(keyword);
  }
}


