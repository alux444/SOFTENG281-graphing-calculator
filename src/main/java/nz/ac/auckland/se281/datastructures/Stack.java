package nz.ac.auckland.se281.datastructures;

public class Stack<T> {
  private Node<T> head;
  private int size;

  public Stack() {
    this.head = null;
    this.size = 0;
  }

  public void push(Edge<T> data) {
    Node<T> tempNode = new Node<T>(data);
    tempNode.setNext(this.head);
    this.head = tempNode;
    size++;
  }

  public Edge<T> pop() {
    if (head == null) {
      return null;
    }
    Edge<T> popped = head.getData();
    this.head = head.getNext();
    return popped;
  }

  public Edge<T> peek() {
    return head.getData();
  }

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return (size == 0);
  }
}
