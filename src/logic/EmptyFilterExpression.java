package logic;

public class EmptyFilterExpression implements FilterExpression {

  /**
   * Evaluates an EmptyFilterExpression, which always returns true.
   *
   * @param article - article to evaluate
   * @return always returns true
   */
  @Override
  public boolean evaluate(Article article) {
    return true;
  }
}
