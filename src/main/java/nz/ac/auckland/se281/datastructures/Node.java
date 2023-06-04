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
   * @param data data input for the node.
   */
  public Node(T data) {
    this.data = data;
  }

  /**
   * The method returns the data of the node.
   *
   * @return the data of the node.
   */
  public T getData() {
    return data;
  }

  /**
   * Sets the data of the node.
   *
   * @param data data that is to be set.
   */
  public void setData(T data) {
    this.data = data;
  }

  /**
   * The method returns the next node.
   *
   * @return next node.
   */
  public Node<T> getNext() {
    return next;
  }

  /**
   * This method will set the next node for this current node.
   *
   * @param next next node to be set as.
   */
  public void setNext(Node<T> next) {
    this.next = next;
  }
}
