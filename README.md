# NewsAggregator

This is a java program that collects and displays news from different sources (files, urls, etc.). This program was developed to demonstrate multiple different software design patterns which are detailed below.

The project also uses ANTLR to generate code that reads and parses input data from files and URLs.

For a more detailed report of the design patterns used here, please see the DesignAnalysis.pdf file at the root of this repository.

# Design Patterns

This program implements the following design patterns:

## Decorator

We want to perform different types of operations on the news articles we receive, including filtering based on a given filter expression and caching recently seen articles to be removed from subsequent calls. To address this problem, I used an instance of the Decorator pattern by creating multiple processor classes, with each class performing a unique operation, that can be combined into a single processor object that is capable of executing a sequence of operations.

Sample Decorator Class Constructor (This Processer Adds Cacheing Functionality to the Decorator Chain):
```
/**
   * Constructs a CacheProcessor given another processor.
   *
   * @param processor - Processor to decorate cache functionality over
   */
  public CacheProcessor(Processor processor) {
    this.processor = processor;
    this.seenArticles = new HashSet<>();
  }
```

Benefits of Decorator:
 - Allows for the combining of different operations into a single processor
 - Allows the program to determine the behavior of processors at runtime, by using different sequences of decorators
 - Maintains the Single Responsibility principle by providing separate classes for different processor operations
 - Maintains the Open-Closed principle by allowing the extension of processor functionality by adding new processor decorators, without needing to modify existing processor classes.
 
 ## Composite
 
One of our processors is a FilterProcessor, which we want to filter out articles based on a given filter expression. This filter expression includes keywords, AND and OR operators, and parentheses to string together more complex expressions. The Composite design pattern is used to create a tree of FilterExpression nodes that can filter a list of articles.

Sample FilterExpression Evaluate Method:
```
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
```

Benefits of Composite:
 - Does not require any complex loop to filter articles, as the FilterExpression objects themselves cover the whole tree by passing filter requests down to their children.
 - Maintains the Open-Closed principle by allowing for the addition of new filtering mechanisms without changing the existing filter operations. Simply create a new class that implements FilterExpression and add it to the tree
 
 ## Dependency Inversion
 
We want our NewsProcessor to be able to read JSON from different types of sources, including files and URLs. I employed the Dependency Inversion principle of the S.O.L.I.D. framework to create a DataSource interface that decouples the NewsProcessor class from any specific type of JSON source. As a result, the NewsProcessor depends only on the abstracted DataSource interface, rather than any concrete source type.

The DataSource Interface:
```
public interface DataSource {
  /**
   * Converts the DataSource to an InputStream.
   *
   * @return InputStream representing the DataSource
   */
  InputStream openStream();
}
```

Benefits of Dependency Inversion:
 - Allows for adding new types of data sources without changing the NewsProcessor class, maintaining the Open-Closed principle
 - Allows for testing the NewsProcessor class without any external dependencies, because a mocked data source can be passed to the NewsProcessor instead of a valid file or URL.

## Visitor

The visitor pattern is used to process the data returned by the generated ANTLR parsing code. There are visitors which visit each node in the parse tree to construct the base news processers and filter out specific news articles.

