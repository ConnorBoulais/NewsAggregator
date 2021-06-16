package visitor;

import edu.calpoly.csc305.config.grammars.AggregatorConfigParserBaseVisitor;

public interface AntlrConfigReader<T> {
  /**
   * Parses a config file using the ANTLR generated files,
   * and applies the given Visitor.
   *
   * @param visitor - AggregatorConfigParserBaseVisitor of T to apply
   *                to the data generated from the config file
   * @return the result of applying the visitor
   */
  T getData(AggregatorConfigParserBaseVisitor<T> visitor);
}