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
   * @param source the source vertex
   * @param destination the destination vertex.
   */
  public Edge(T source, T destination) {
    this.source = source;
    this.destination = destination;
  }

  /**
   * This method will return the destination vertex of the edge.
   *
   * @return destination of vertex.
   */
  public T getDestination() {
    return destination;
  }

  /**
   * The function returns the source vertex of the edge.
   *
   * @return source of vertex.
   */
  public T getSource() {
    return source;
  }
}
