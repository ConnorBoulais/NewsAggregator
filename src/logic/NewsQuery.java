package logic;

public class NewsQuery {

  private final Processor processor;
  private final int delay;

  /**
   * Constructs a NewsQuery that holds a processor
   * and a delay.
   *
   * @param processor - Processor to extract articles from
   * @param delay - integer representing milliseconds between
   *              consecutive calls, a delay of 0 indicates to
   *              make only a single call
   */
  public NewsQuery(Processor processor, int delay) {
    this.processor = processor;
    this.delay = delay;
  }

  /**
   * Grabs the processor of the NewsQuery.
   *
   * @return Processor
   */
  public Processor getProcessor() {
    return processor;
  }

  /**
   * Grabs the delay of the NewsQuery.
   *
   * @return delay integer
   */
  public int getDelay() {
    return delay;
  }
}
