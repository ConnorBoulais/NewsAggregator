package visitor;

import edu.calpoly.csc305.config.grammars.AggregatorConfigParser;
import edu.calpoly.csc305.config.grammars.AggregatorConfigParserBaseVisitor;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import logic.NewsQuery;

public class ListQueryVisitor
    extends AggregatorConfigParserBaseVisitor<List<NewsQuery>> {

  private final Logger logger;
  private final String apiKey;

  /**
   * Constructs a ListProcessorVisitor.
   *
   * @param logger - java.util.logging.Logger to log errors
   * @param apiKey - String holding a NewsAPI key
   */
  public ListQueryVisitor(Logger logger, String apiKey) {
    super();
    this.logger = logger;
    this.apiKey = apiKey;
  }

  /**
   * Visit a parse tree produced by {@link AggregatorConfigParser#sources}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  @Override
  public List<NewsQuery> visitSources(AggregatorConfigParser.SourcesContext ctx) {
    List<NewsQuery> newsQueries = new ArrayList<>();
    for (AggregatorConfigParser.SourceTypeContext sourceType : ctx.sourceType()) {
      newsQueries.add(
          new NewsQuery(
            sourceType.accept(new ProcessorVisitor(logger, apiKey)),
            sourceType.accept(new DelayVisitor())
          ));
    }
    return newsQueries;
  }
}
