package logic;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArticleDeserializer extends StdDeserializer<Article> {

  private transient Logger logger;

  protected ArticleDeserializer() {
    this(null);
  }

  protected ArticleDeserializer(Class<?> vc) {
    super(vc);
  }

  /**
   * Construct an ArticleDeserializer.
   *
   * @param vc - Class vc for super class
   * @param logger - java.util.logging.Logger
   */
  public ArticleDeserializer(Class<?> vc, Logger logger) {
    super(vc);
    this.logger = logger;
  }

  /**
   * Deserialize a JSON node into an Article object.
   *
   * @param jp - JsonParser from Jackson
   * @param ctxt - DeserializationContext object from Jackson
   * @return new Article object
   */
  @Override
  public Article deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException {

    JsonNode node = jp.getCodec().readTree(jp);

    String title = getRequiredString(node, "title");
    String description = getRequiredString(node, "description");
    String url = getRequiredString(node, "url");
    String publishedAt = getRequiredString(node, "publishedAt");

    return new Article(title, description, url, publishedAt);
  }

  private String getRequiredString(JsonNode node, String field) {
    if (!node.has(field)) {
      String message = String.format("article with missing field: %s", field);
      logger.log(Level.WARNING, message);
      return null;
    }
    if (node.get(field).isNull() || !node.get(field).isTextual()) {
      String message = String.format("article with invalid field: %s", field);
      logger.log(Level.WARNING, message);
      return null;
    }
    return node.get(field).asText();
  }
}