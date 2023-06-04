package nz.ac.auckland.se281.datastructures;

import java.util.ArrayList;
import java.util.List;

/** Queue data structure implementing FIFO policy. */
public class Queue<T> {

  private List<Edge<T>> queueList;
  private int front;
  private int end;

  /** Constructor for the queue. */
  public Queue() {
    this.queueList = new ArrayList<Edge<T>>();
    this.front = 0;
    this.end = 0;
  }

  /**
   * Adds an edge to the back of the queue.
   *
   * @param data edge to be added.
   */
  public void enqueue(Edge<T> data) {
    queueList.add(data);
    this.end++;
  }

  /**
   * Dequeues the first added item from the queue and returns it.
   *
   * @return removed edge.
   */
  public Edge<T> dequeue() {
    Edge<T> removed = queueList.get(front);
    this.front++;

    return removed;
  }

  /**
   * The method returns the first added item from the queue without removing it.
   *
   * @return edge at front of queue.
   */
  public Edge<T> peek() {
    return queueList.get(this.front);
  }

  /**
   * The method returns a boolean true if the queue is currently empty, otherwise a false.
   *
   * @return boolean of is empty.
   */
  public boolean isEmpty() {
    if (queueList.size() - this.front == 0) {
      return true;
    }
    return false;
  }

  /**
   * The method returns the current queue in a list form.
   *
   * @return list of the current queue.
   */
  public List<Edge<T>> returnQueue() {
    return queueList.subList(front, end);
  }
}
