package visitor;

import edu.calpoly.csc305.config.grammars.AggregatorConfigParser;
import edu.calpoly.csc305.config.grammars.AggregatorConfigParserBaseVisitor;

public class DelayVisitor
    extends AggregatorConfigParserBaseVisitor<Integer> {

  /**
   * Visit a parse tree produced by AggregatorConfigParser.FileSourceTypeContext.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  @Override
  public Integer visitFileSourceType(AggregatorConfigParser.FileSourceTypeContext ctx) {
    if (ctx.delay() != null) {
      return ctx.delay().accept(this);
    }
    return 0;
  }

  /**
   * Visit a parse tree produced by AggregatorConfigParser.UrlSourceTypeContext.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  @Override
  public Integer visitUrlSourceType(AggregatorConfigParser.UrlSourceTypeContext ctx) {
    if (ctx.delay() != null) {
      return ctx.delay().accept(this);
    }
    return 0;
  }

  /**
   * Visit a parse tree produced by AggregatorConfigParser.DelayContext.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  @Override
  public Integer visitDelay(AggregatorConfigParser.DelayContext ctx) {
    try {
      return Integer.parseInt(ctx.NUM().toString());
    } catch (NumberFormatException e) {
      return 0;
    }
  }
}
