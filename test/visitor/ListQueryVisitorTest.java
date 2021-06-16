package visitor;

import edu.calpoly.csc305.config.grammars.AggregatorConfigLexer;
import edu.calpoly.csc305.config.grammars.AggregatorConfigParser;
import edu.calpoly.csc305.config.grammars.AggregatorConfigParserBaseVisitor;
import logic.*;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static org.junit.jupiter.api.Assertions.*;

class ListQueryVisitorTest {

  @Test
  void testEmptyInput(){
    String[] input = {""};

    Logger logger = Logger.getLogger(ListQueryVisitorTest.class.getName());

    List<NewsQuery> actualProcessors = getData(new ListQueryVisitor(logger, "testKey"), input, logger);

    assertTrue(actualProcessors.isEmpty());
  }

  @Test
  void testEverySourceType(){
    String[] input = {
      "file\n" +
      "name: NewsAPI Fixed\n" +
      "format: newsapi\n" +
      "source: inputs/newsapi.json\n" +
      "filter:\n\n\n" +
      "url\n" +
      "name: NewsAPI Headlines (Live)\n" +
      "format: newsapi\n" +
      "source: http://newsapi.org\n" +
      "filter:\n\n\n" +
      "file\n" +
      "name: Simple Fixed\n" +
      "format: simple\n" +
      "source: inputs/simple.json\n" +
      "filter:\n\n\n"};

    Logger logger = Logger.getLogger(ListQueryVisitorTest.class.getName());

    List<Processor> expectedProcessors = Arrays.asList(
      new NewsProcessor(
        new FileSource("inputs/newsapi.json", logger),
        new NewsApiParser(),
        logger
      ),
      new NewsProcessor(
        new UrlSource("http://newsapi.org", logger),
        new NewsApiParser(),
        logger
      ),
      new NewsProcessor(
        new FileSource("inputs/simple.json", logger),
        new SimpleParser(),
        logger
      )
    );

    List<NewsQuery> actualQueries = getData(new ListQueryVisitor(logger, "testKey"), input, logger);

    assertEquals(expectedProcessors.size(), actualQueries.size());
    List<Pair<Processor, Processor>> pairs = IntStream.range(0, Math.min(expectedProcessors.size(), actualQueries.size()))
      .mapToObj(i -> new Pair<>(expectedProcessors.get(i), actualQueries.get(i).getProcessor()))
      .collect(Collectors.toList());

    for (Pair<Processor, Processor> pair : pairs) {
      assertTrue(processorsEqual(pair.a, pair.b));
    }
  }

  //UTILITY METHODS

  private Boolean processorsEqual(Processor p1, Processor p2){
    DataSource s1 = p1.getDataSource();
    DataSource s2 = p2.getDataSource();
    return
      s1.getClass() == s2.getClass() &&
      p1.getParser().getClass() == p2.getParser().getClass();
  }

  private List<NewsQuery> getData(AggregatorConfigParserBaseVisitor<List<NewsQuery>> visitor,
                                      String[] data,
                                      Logger logger){
    List<ParseTree> parseTrees = parseFiles(data, logger);
    return gatherVisitorResults(parseTrees, visitor);
  }

  private static List<ParseTree> parseFiles(String[] data, Logger logger) {
    List<ParseTree> parseTrees = new ArrayList<>();
    for (String str : data) {
      try {
        CommonTokenStream tokens = new CommonTokenStream(
          new AggregatorConfigLexer(CharStreams.fromString(str)));
        AggregatorConfigParser parser = new AggregatorConfigParser(tokens);
        ParseTree parseTree = parser.sources();

        if (parser.getNumberOfSyntaxErrors() == 0) {
          parseTrees.add(parseTree);
        }
      } catch (Exception e) {
        logger.warning(e.getMessage());
      }
    }
    return parseTrees;
  }

  private static List<NewsQuery> gatherVisitorResults(List<ParseTree> parseTrees,
                                                          AggregatorConfigParserBaseVisitor<List<NewsQuery>> visitor) {
    List<NewsQuery> results = new ArrayList<>();
    for (ParseTree parseTree : parseTrees) {
      results.addAll(parseTree.accept(visitor));
    }
    return results;
  }
}