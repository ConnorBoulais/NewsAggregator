package visitor;

import edu.calpoly.csc305.config.grammars.AggregatorConfigParser;
import edu.calpoly.csc305.config.grammars.AggregatorConfigParserBaseVisitor;
import logic.AndExpression;
import logic.EmptyFilterExpression;
import logic.FilterExpression;
import logic.KeywordExpression;
import logic.OrExpression;

public class FilterVisitor extends AggregatorConfigParserBaseVisitor<FilterExpression> {

  /**
   * Visit a parse tree produced by AggregatorConfigParser.FilterContext.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  @Override
  public FilterExpression visitFilter(AggregatorConfigParser.FilterContext ctx) {
    if (ctx.selector() != null) {
      return ctx.selector().accept(this);
    }
    return new EmptyFilterExpression();
  }

  /**
   * Visit a parse tree produced by AggregatorConfigParser.NestedExpressionContext.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  @Override
  public FilterExpression visitNestedExpression(
      AggregatorConfigParser.NestedExpressionContext ctx) {
    return ctx.selector().accept(this);
  }

  /**
   * Visit a parse tree produced by AggregatorConfigParser.AndExpressionContext.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  @Override
  public FilterExpression visitAndExpression(AggregatorConfigParser.AndExpressionContext ctx) {
    return new AndExpression(ctx.lft.accept(this), ctx.rht.accept(this));
  }

  /**
   * Visit a parse tree produced by AggregatorConfigParser.OrExpressionContext.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  @Override
  public FilterExpression visitOrExpression(AggregatorConfigParser.OrExpressionContext ctx) {
    return new OrExpression(ctx.lft.accept(this), ctx.rht.accept(this));
  }

  /**
   * Visit a parse tree produced by AggregatorConfigParser.KeywordExpressionContext.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  @Override
  public FilterExpression visitKeywordExpression(
      AggregatorConfigParser.KeywordExpressionContext ctx) {
    return new KeywordExpression(ctx.KEYWORD().toString().trim());
  }
}
