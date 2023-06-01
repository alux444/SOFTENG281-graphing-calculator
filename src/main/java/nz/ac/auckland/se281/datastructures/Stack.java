package nz.ac.auckland.se281.datastructures;

/** Data structure implementing LIFO policy. */
public class Stack<T> {
  private Node<T> head;
  private int size;

  /** Constructor for the stack structure */
  public Stack() {
    this.head = null;
    this.size = 0;
  }

  /**
   * Pushes data into the top of the stack
   *
   * @param T data to be pushed in.
   */
  public void push(T data) {
    Node<T> tempNode = new Node<T>(data);
    tempNode.setNext(this.head);
    this.head = tempNode;
    size++;
  }

  /** Pops the top item from the stack and returns it. */
  public T pop() {
    if (head == null) {
      return null;
    }
    T popped = head.getData();
    this.head = head.getNext();
    size--;
    return popped;
  }

  /** Peeks at the first item of the stack, returning it without removing it. */
  public T peek() {
    return head.getData();
  }

  /** Returns the integer size of the stack. */
  public int size() {
    return size;
  }

  /** Returns a boolean true if the stack is currently empty, otherwise false. */
  public boolean isEmpty() {
    return (size == 0);
  }
}
