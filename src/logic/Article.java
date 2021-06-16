package logic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Article {
  private final String title;
  private final String description;
  private final String url;
  private final LocalDateTime publishedAt;

  /**
   * Creates an Article object.
   *
   * @param description Description of article
   * @param publishedAt Published date of article
   * @param title Title of article
   * @param url Link to article
   */
  public Article(@JsonProperty("title") String title,
                 @JsonProperty("description") String description,
                 @JsonProperty("url") String url,
                 @JsonProperty("publishedAt") String publishedAt) {
    this.title = title;
    this.description = description;
    this.url = url;
    this.publishedAt =
      publishedAt == null ? null :
        LocalDateTime.parse(publishedAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  }

  /**
   * Get the URL of article.
   *
   * @return String of article's URL
   */
  public String getUrl() {
    return url;
  }

  /**
   * Get the title of the article.
   *
   * @return String of article's title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Get the published time of the article.
   *
   * @return String of article's published time
   */
  public LocalDateTime getPublishedAt() {
    return publishedAt;
  }

  /**
   * Get the description of the article.
   *
   * @return String of article's description.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Get the string representation of the Article.
   *
   * @return string
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("\n\n").append(title).append("\n");
    builder.append(description).append("\n");
    builder.append(publishedAt.toString()).append("\n");
    builder.append(url);
    return builder.toString();
  }

  /**
   * Determine if two Articles are equal by comparing
   * their attributes.
   *
   * @param obj object to compare to this Article
   * @return Boolean
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Article)) {
      return false;
    }
    Article other = ((Article) obj);
    return
      title.equals(other.title)
      && description.equals(other.description)
      && url.equals(other.url)
      && publishedAt.equals(other.publishedAt);
  }

  /**
   * Generate a hash value for an article.
   *
   * @return hash value integer
   */
  @Override
  public int hashCode() {
    return Objects.hash(
      title, description, url, publishedAt
    );
  }
}
