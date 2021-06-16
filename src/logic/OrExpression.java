package logic;

public class OrExpression implements FilterExpression {

  private final FilterExpression leftExpression;
  private final FilterExpression rightExpression;

  /**
   * Constructs and OrExpression.
   *
   * @param leftExpression - FilterExpression for left of Or
   * @param rightExpression - FilterExpression for right of Or
   */
  public OrExpression(FilterExpression leftExpression, FilterExpression rightExpression) {
    this.leftExpression = leftExpression;
    this.rightExpression = rightExpression;
  }

  /**
   * Evaluates the OrExpression on a given article.
   *
   * @param article - article to evaluate
   * @return boolean for whether article matched FilterExpression or not
   */
  @Override
  public boolean evaluate(Article article) {
    return leftExpression.evaluate(article) || rightExpression.evaluate(article);
  }
}
