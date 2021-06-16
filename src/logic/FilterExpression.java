package logic;

public interface FilterExpression {
  /**
   * determines if an article matches this filter expression.
   *
   * @param article - article to evaluate
   * @return boolean for whether article matched
   *      filter expression or not
   */
  boolean evaluate(Article article);
}

