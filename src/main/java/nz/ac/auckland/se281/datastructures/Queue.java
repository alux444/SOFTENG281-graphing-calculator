package nz.ac.auckland.se281.datastructures;

import java.util.ArrayList;
import java.util.List;

public class Queue<T> {

  private List<Edge<T>> queueList;

  public Queue() {
    this.queueList = new ArrayList<Edge<T>>();
  }

  public void enqueue(Edge<T> data) {
    queueList.add(data);
  }

  public Edge<T> dequeue() {
    Edge<T> removed = queueList.get(0);
    queueList.remove(0);
    return removed;
  }

  public Edge<T> peek() {
    return queueList.get(0);
  }

  public boolean isEmpty() {
    if (queueList.size() == 0) {
      return true;
    }
    return false;
  }

  public List<Edge<T>> returnQueue() {
    return queueList;
  }
}
