package logic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewsApiResponse {

  private final String status;
  private final int totalResults;
  private final List<Article> articles;


  /**
   * Creates a NewsAPIResponse.
   *
   * @param status status of the response
   * @param totalResults number of results in the response.
   * @param articles list of articles in the response
   */
  public NewsApiResponse(@JsonProperty("status") String status,
                         @JsonProperty("totalResults") int totalResults,
                         @JsonProperty("articles") List<Article> articles) {
    this.status = status;
    this.totalResults = totalResults;
    this.articles = articles.stream().filter(
      a ->
        a.getTitle() != null
        && a.getDescription() != null
        && a.getUrl() != null
        && a.getPublishedAt() != null
      ).collect(Collectors.toList());
  }

  /**
   * Get the articles in the response.
   *
   * @return List of articles
   */
  public List<Article> getArticles() {
    return articles;
  }

  /**
   * Get the response status.
   *
   * @return status string
   */
  public String getStatus() {
    return status;
  }

  /**
   * Get the total results in the response.
   *
   * @return total results integer
   */
  public int getTotalResults() {
    return totalResults;
  }

  /**
   * Determine if two NewsAPIResponses are equal by comparing
   * their attributes.
   *
   * @return Boolean
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof NewsApiResponse)) {
      return false;
    }
    NewsApiResponse other = ((NewsApiResponse) obj);
    return this.articles.equals(other.articles);
  }

  /**
   * Generate a hash value for a NewsAPIResponse.
   *
   * @return hash value integer
   */
  @Override
  public int hashCode() {
    return Objects.hash(articles);
  }
}