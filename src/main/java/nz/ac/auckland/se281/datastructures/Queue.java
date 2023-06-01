package nz.ac.auckland.se281.datastructures;

import java.util.ArrayList;
import java.util.List;

/** Queue data structure implementing FIFO policy. */
public class Queue<T> {

  private List<Edge<T>> queueList;

  /** Constructor for the queue. */
  public Queue() {
    this.queueList = new ArrayList<Edge<T>>();
  }

  /**
   * Adds an edge to the back of the queue.
   *
   * @param Edge<T> edge to be added.
   */
  public void enqueue(Edge<T> data) {
    queueList.add(data);
  }

  /**
   * Dequeues the first added item from the queue and returns it.
   *
   * @return removed edge.
   */
  public Edge<T> dequeue() {
    Edge<T> removed = queueList.get(0);
    queueList.remove(0);
    return removed;
  }

  /**
   * Returns the first added item from the queue without removing it.
   *
   * @return edge at front of queue.
   */
  public Edge<T> peek() {
    return queueList.get(0);
  }

  /**
   * Returns a boolean true if the queue is currently empty, otherwise a false.
   *
   * @return boolean of is empty.
   */
  public boolean isEmpty() {
    if (queueList.size() == 0) {
      return true;
    }
    return false;
  }

  /**
   * Returns the current queue in a list form.
   *
   * @return list of the current queue.
   */
  public List<Edge<T>> returnQueue() {
    return queueList;
  }
}
