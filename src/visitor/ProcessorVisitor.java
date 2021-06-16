package visitor;

import edu.calpoly.csc305.config.grammars.AggregatorConfigParser;
import edu.calpoly.csc305.config.grammars.AggregatorConfigParserBaseVisitor;
import java.util.logging.Logger;
import logic.CacheProcessor;
import logic.FileSource;
import logic.FilterProcessor;
import logic.NewsProcessor;
import logic.Processor;
import logic.UrlSource;

public class ProcessorVisitor
    extends AggregatorConfigParserBaseVisitor<Processor> {

  private final Logger logger;
  private final String apiKey;

  /**
   * Constructs a ProcessorVisitor.
   *
   * @param logger - java.util.logging.Logger to log errors
   * @param apiKey - String holding a NewsAPI key
   */
  public ProcessorVisitor(Logger logger, String apiKey) {
    this.logger = logger;
    this.apiKey = apiKey;
  }

  /**
   * Visit a parse tree produced by {@link AggregatorConfigParser#fileSourceType}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  @Override
  public Processor visitFileSourceType(AggregatorConfigParser.FileSourceTypeContext ctx) {
    return
      new CacheProcessor(
        new FilterProcessor(
          new NewsProcessor(
            new FileSource(ctx.source().LINE().toString().trim(), logger),
            ctx.format().format_option().accept(new ParserVisitor()),
            logger
          ),
          ctx.filter().accept(new FilterVisitor())
        )
      );
  }

  /**
   * Visit a parse tree produced by {@link AggregatorConfigParser#urlSourceType}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  @Override
  public Processor visitUrlSourceType(AggregatorConfigParser.UrlSourceTypeContext ctx) {
    return
      new CacheProcessor(
        new FilterProcessor(
          new NewsProcessor(
            new UrlSource(
              ctx.source().LINE().toString().trim().replace("{NEWS_API_KEY}", apiKey),
              logger
            ),
            ctx.format().format_option().accept(new ParserVisitor()),
            logger
          ),
          ctx.filter().accept(new FilterVisitor())
        )
      );
  }
}
