package logic;

public class AndExpression implements FilterExpression {

  private final FilterExpression leftExpression;
  private final FilterExpression rightExpression;

  /**
   * Constructs and AndExpression.
   *
   * @param leftExpression - FilterExpression for left of And
   * @param rightExpression - FilterExpression for right of And
   */
  public AndExpression(FilterExpression leftExpression,
                       FilterExpression rightExpression) {
    this.leftExpression = leftExpression;
    this.rightExpression = rightExpression;
  }

  /**
   * Evaluates the AndExpression on a given article.
   *
   * @param article - article to evaluate
   * @return boolean for whether article matched FilterExpression or not
   */
  @Override
  public boolean evaluate(Article article) {
    return leftExpression.evaluate(article) && rightExpression.evaluate(article);
  }
}


