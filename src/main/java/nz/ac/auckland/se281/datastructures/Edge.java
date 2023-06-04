package nz.ac.auckland.se281.datastructures;

/**
 * An edge in a graph that connects two verticies.
 *
 * <p>You must NOT change the signature of the constructor of this class.
 *
 * @param <T> The type of each vertex.
 */
public class Edge<T> {
  private T source;
  private T destination;

  /**
   * The constructor for the edge type.
   *
   * @param <T> The source vertex, and destination vertex.
   */
  public Edge(T source, T destination) {
    this.source = source;
    this.destination = destination;
  }

  /**
   * The function returns the source vertex of the graph.
   *
   * @return source of vertex.
   */
  public T getSource() {
    return source;
  }

  /**
   * This method returns the destinatiion vertex of the graph.
   *
   * @return source of destination.
   */
  public T getDestination() {
    return destination;
  }
}
