package visitor;

import edu.calpoly.csc305.config.grammars.AggregatorConfigParser;
import edu.calpoly.csc305.config.grammars.AggregatorConfigParserBaseVisitor;
import logic.ArticleParser;
import logic.NewsApiParser;
import logic.SimpleParser;

public class ParserVisitor
    extends AggregatorConfigParserBaseVisitor<ArticleParser> {

  /**
   * Visit a parse tree produced by AggregatorConfigParser.NewsApiFormatContext.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  @Override
  public ArticleParser visitNewsApiFormat(AggregatorConfigParser.NewsApiFormatContext ctx) {
    return new NewsApiParser();
  }

  /**
   * Visit a parse tree produced by AggregatorConfigParser.SimpleFormatContext}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  @Override
  public ArticleParser visitSimpleFormat(AggregatorConfigParser.SimpleFormatContext ctx) {
    return new SimpleParser();
  }
}
