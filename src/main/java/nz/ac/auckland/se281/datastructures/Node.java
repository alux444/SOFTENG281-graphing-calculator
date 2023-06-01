package nz.ac.auckland.se281.datastructures;

/**
 * A node used for implementing the Queue and Stack data structure. It has data stored and points to
 * the next node.
 */
public class Node<T> {
  private T data;
  private Node<T> next;

  /**
   * The constructor for the node.
   *
   * @param <T> data input for the node.
   */
  public Node(T data) {
    this.data = data;
  }

  /**
   * returns the data of the node.
   *
   * @return the data of the node.
   */
  public T getData() {
    return data;
  }

  /**
   * sets the data of the node.
   *
   * @param <T> data that is to be set.
   */
  public void setData(T data) {
    this.data = data;
  }

  /**
   * Returns the next node.
   *
   * @return next node.
   */
  public Node<T> getNext() {
    return next;
  }

  /**
   * Sets the next node.
   *
   * @param Node<T> The next node.
   */
  public void setNext(Node<T> next) {
    this.next = next;
  }
}
