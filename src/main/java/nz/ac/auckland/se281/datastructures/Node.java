package nz.ac.auckland.se281.datastructures;

public class Node<T> {
  private Edge<T> data;
  private Node<T> next;

  public Node(Edge<T> data) {
    this.data = data;
  }

  public Edge<T> getData() {
    return data;
  }

  public void setData(Edge<T> data) {
    this.data = data;
  }

  public Node<T> getNext() {
    return next;
  }

  public void setNext(Node<T> next) {
    this.next = next;
  }
}
