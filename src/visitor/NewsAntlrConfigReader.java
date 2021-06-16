package visitor;

import edu.calpoly.csc305.config.grammars.AggregatorConfigLexer;
import edu.calpoly.csc305.config.grammars.AggregatorConfigParser;
import edu.calpoly.csc305.config.grammars.AggregatorConfigParserBaseVisitor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import logic.NewsProcessor;
import logic.NewsQuery;
import logic.Processor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class NewsAntlrConfigReader implements AntlrConfigReader<List<NewsQuery>> {

  private final List<String> files;
  private final Logger logger;

  /**
   * Constructs a NewsAntlrConfigReader.
   *
   * @param files - Array of filenames for config files to read from
   * @param logger - java.util.logging.Logger to log errors
   */
  public NewsAntlrConfigReader(List<String> files, Logger logger) {
    this.files = files;
    this.logger = logger;
  }

  /**
   * Parses a config file using the ANTLR generated files,
   * and applies the given Visitor.
   *
   * @param visitor - AggregatorConfigParserBaseVisitor of List of NewsProcessor to apply
   *                to the data generated from the config file
   * @return the result of applying the visitor
   */
  public List<NewsQuery> getData(
      AggregatorConfigParserBaseVisitor<List<NewsQuery>> visitor) {
    List<ParseTree> parseTrees = parseFiles(files, logger);
    return gatherVisitorResults(parseTrees, visitor);
  }

  private static List<ParseTree> parseFiles(List<String> filenames, Logger logger) {
    List<ParseTree> parseTrees = new ArrayList<>();

    for (String filename : filenames) {
      try {
        CommonTokenStream tokens = new CommonTokenStream(
            new AggregatorConfigLexer(CharStreams.fromFileName(filename)));
        AggregatorConfigParser parser = new AggregatorConfigParser(tokens);
        ParseTree parseTree = parser.sources();

        if (parser.getNumberOfSyntaxErrors() == 0) {
          parseTrees.add(parseTree);
        }
      } catch (IOException e) {
        logger.warning(e.getMessage());
      }
    }
    return parseTrees;
  }

  private static List<NewsQuery> gatherVisitorResults(
      List<ParseTree> parseTrees,
      AggregatorConfigParserBaseVisitor<List<NewsQuery>> visitor) {
    List<NewsQuery> results = new ArrayList<>();

    for (ParseTree parseTree : parseTrees) {
      results.addAll(parseTree.accept(visitor));
    }

    return results;
  }
}
