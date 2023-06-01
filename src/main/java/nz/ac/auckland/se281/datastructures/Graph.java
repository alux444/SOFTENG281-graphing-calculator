package nz.ac.auckland.se281.datastructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 * A graph that is composed of a set of verticies and edges.
 *
 * <p>You must NOT change the signature of the existing methods or constructor of this class.
 *
 * @param <T> The type of each vertex, that have a total ordering.
 */
public class Graph<T extends Comparable<T>> {
  private Set<T> verticies;
  private Set<Edge<T>> edges;

  /**
   * the constructor for the graph.
   *
   * @param Set<T> a set of the graphs verticies.
   * @param Set<T> a set of the graphs edges.
   */
  public Graph(Set<T> verticies, Set<Edge<T>> edges) {
    this.verticies = verticies;
    this.edges = edges;
  }

  /**
   * Finds the roots of the graph. Does this by checking for equivalence first, and then returning
   * the minimum values of the equivalence classes of classes that have more than 1 node.
   */
  public Set<T> getRoots() {
    // creates a new set for roots
    List<T> roots = new ArrayList<T>();
    // go through each vertice

    // only check equivalence class if the graph is an equivalence realtion
    if (isEquivalence()) {
      for (T vertex : verticies) {
        // go through each vertex and add the minimum value for each equivalence relation
        Set<T> equivalenceClass = getEquivalenceClass(vertex);
        // if there are more than 1 node in the clas, add to the roots.
        if (equivalenceClass.size() > 1) {
          roots.add(Collections.min(equivalenceClass));
        }
      }
    }
    // if the relation isnt equivalence, then just look for an indegree of 0 and outdegree of > 0
    for (T vertice : verticies) {
      int inDegree = 0;
      int outDegree = 0;

      for (Edge<T> edge : edges) {
        // if the edge destination is equal to the vertice, and source ISNT, i.e it isnt a self
        // loop, increase the in degree.
        if (edge.getDestination().equals(vertice) && !edge.getSource().equals(vertice)) {
          inDegree++;
        }
        // otherwise, if the source is the vertice, increase the out degree.
        if (edge.getSource().equals(vertice)) {
          outDegree++;
        }
      }
      // add the verticle if the in degree is 0 and out degree is > 0
      if (inDegree == 0 && outDegree > 0) {
        roots.add(vertice);
      }
    }

    // sort the roots and convert to a set
    Set<T> result = new HashSet<>();
    Collections.sort(roots);
    for (T root : roots) {
      result.add(root);
    }

    return result;
  }

  /**
   * Checks reflexivity by going through every vertex and ensuring there is a self loop. returns a
   * boolean result.
   */
  public boolean isReflexive() {
    // check if the vertices is the same as the roots. if all verticles are roots, the graph is
    // reflexing
    // creates a new set for roots
    Set<T> selfLoops = new HashSet<T>();
    for (Edge<T> edge : edges) {
      // if an edge has the same source and destination (and so is a root), add it to the set.
      if (edge.getSource().equals(edge.getDestination())) {
        selfLoops.add(edge.getSource());
      }
    }
    // compare verticles with all self loops
    return (verticies.equals(selfLoops));
  }

  /**
   * Checks for symmetry by ensuring that for every edge where xRy, there is also a yRx (as in, an
   * edge going from A to B AND B to A) returns a boolean based off result.
   */
  public boolean isSymmetric() {

    // check for a vertice
    for (T vertice : verticies) {
      boolean symmetry = false;
      for (Edge<T> edge : edges) {
        // check all edges with specific vertice
        if (edge.getSource().equals(vertice)) {
          for (Edge<T> otherEdge : edges) {
            // go through all edges and look for another edge which has the source of our first
            // edges destination (i.e the first edge is going into this edge)
            if (otherEdge.getSource().equals(edge.getDestination())) {
              // now if there exists an edge which returns back to the vertice, there is symmetry.
              if (otherEdge.getDestination().equals(vertice)) {
                symmetry = true;
              }
            }
          }
        }
      }
      if (!symmetry) {
        return false;
      }
    }

    return true;
  }

  /**
   * finds the transitivity of a graph by going through all edges where xRy and there exists a yRz.
   * It then searches for a xRz to confirm transitivity, otherwise return a false if this is ever
   * broken. (i.e for every edge that goes A to B which exists a B to C, ensure that there is always
   * an edge A to C) returns a boolean result.
   */
  public boolean isTransitive() {
    // go through all edges.
    for (Edge<T> edge : edges) {
      // set the 'x'. assuming for this edge, we have xRy
      T x = edge.getSource();
      for (Edge<T> secondEdge : edges) {
        T z = secondEdge.getDestination();
        // go through all edges again to find an edge which has a source the same as the first edges
        // destination (i.e the first is incoming to the second) thereby fulfilling yRz
        if (edge.getDestination().equals(secondEdge.getSource())) {
          boolean transitivity = false;

          // go through all edges and look for an edge which fulfills xRz.
          for (Edge<T> validationEdge : edges) {
            if (validationEdge.getSource().equals(x) && validationEdge.getDestination().equals(z)) {
              transitivity = true;
              break;
            }
          }

          // if there is no edge which fulfills xRz if xRy and yRz, the graph isnt transitive.
          if (!transitivity) {
            return false;
          }
        }
      }
    }
    return true;
  }

  /**
   * Checks for antisymmetry by going through all xRy and yRx, in every case x = y, otherwise the
   * graph is not antisymmetric (i.e for all edges where A to B and B to A, this MUST mean that B is
   * A) returns a boolean result.
   */
  public boolean isAntiSymmetric() {
    // go through edges
    for (Edge<T> edge : edges) {
      for (Edge<T> nestedEdge : edges) {
        // if the destination of this edge = the source of the other edge
        if (edge.getDestination().equals(nestedEdge.getSource())) {
          // and if the destination of the other edge = source of this (i.e xRy and yRx)
          if (nestedEdge.getDestination().equals(edge.getSource())) {
            // if y =/= x, the graph isnt antisymmetric
            if (!edge.getDestination().equals(edge.getSource())) {
              return false;
            }
          }
        }
      }
    }

    return true;
  }

  /**
   * checks for equivalence by chceking if the graph is reflexive, symmetric and transitive. it must
   * be all 3. returns a boolean result.
   */
  public boolean isEquivalence() {
    // if the graph is reflexive, symmetric and transitive, it is equivalent.
    return (isReflexive() && isSymmetric() && isTransitive());
  }

  /**
   * Checks the equivalence class for a specific vertex by finding every edge that leads from or to
   * the vertex returns a set of all the vertices which lead to or from the vertex (aka the
   * equivalence class).
   */
  public Set<T> getEquivalenceClass(T vertex) {
    Set<T> equivalenceClasses = new HashSet<T>();

    // only check equivalence class if the graph is an equivalence realtion
    if (isEquivalence()) {
      // Iterate through every edge
      for (Edge<T> edge : edges) {
        // // If the destination of the edge is the vertex, add the source to the equivalence class
        if (edge.getDestination().equals(vertex)) {
          equivalenceClasses.add(edge.getSource());
        }
        // If the source of the edge is the vertex, add the destination to the equivalence class
        // (ie. edge fulfills (v1,v2))
        if (edge.getSource().equals(vertex)) {
          equivalenceClasses.add(edge.getDestination());
        }
      }
    }

    return equivalenceClasses;
  }

  /**
   * goes through an iterative breadth first search, returning a list of the order of traversal of
   * the graph.
   */
  public List<T> iterativeBreadthFirstSearch() {
    Set<T> roots = getRoots();
    Queue<T> queue = new Queue<T>();
    Set<T> visited = new HashSet<T>();
    List<T> result = new ArrayList<T>();

    for (T root : roots) {
      // Mark the root as visited
      visited.add(root);
      // Add the root to the result list
      result.add(root);

      // create new treemap
      TreeMap<T, Edge<T>> rootsChildren = new TreeMap<>();

      // Find all edges with the root as the source and enqueue them
      for (Edge<T> edge : edges) {
        if (edge.getSource().equals(root)) {
          rootsChildren.put(edge.getDestination(), edge);
        }
      }

      // queue the children
      for (T key : rootsChildren.keySet()) {
        queue.enqueue(rootsChildren.get(key));
      }

      // Perform BFS
      while (!queue.isEmpty()) {
        Edge<T> currentEdge = queue.dequeue();
        T currentVertex = currentEdge.getDestination();
        // check if the current vertex has already been visited
        if (!visited.contains(currentVertex)) {
          // mark the current vertex as visited
          visited.add(currentVertex);
          // add the current vertex to the result list
          result.add(currentVertex);

          // create a tree map to automatically sort by the destination values.
          TreeMap<T, Edge<T>> children = new TreeMap<>();

          // find all edges with the same source as this edges destinations
          for (Edge<T> edge : edges) {
            if (edge.getSource().equals(currentVertex)) {
              children.put(edge.getDestination(), edge);
            }
          }

          // queue each edge.
          for (T key : children.keySet()) {
            queue.enqueue(children.get(key));
          }
        }
      }
    }

    return result;
  }

  /**
   * goes through an iterative depth first search, returning a list of the order of traversal of the
   * graph.
   */
  public List<T> iterativeDepthFirstSearch() {
    Set<T> roots = getRoots();
    Stack<T> stack = new Stack<T>();
    Set<T> visited = new HashSet<T>();
    List<T> result = new ArrayList<T>();

    for (T root : roots) {
      // add roots to results, visited and the stack
      visited.add(root);
      result.add(root);
      stack.push(root);

      while (!stack.isEmpty()) {
        // pop the current stack
        T currentVertex = stack.pop();

        // add the vertex on the stack to results and visited if we havent already
        if (!visited.contains(currentVertex)) {
          visited.add(currentVertex);
          result.add(currentVertex);
        }

        // create a tree map that compares in reverse order.
        TreeMap<T, Edge<T>> currentChildren = new TreeMap<>(Comparator.reverseOrder());

        for (Edge<T> edge : edges) {
          // look for edges with the same source
          if (edge.getSource().equals(currentVertex)) {
            // look for edges that do not have a vertex we have already visited
            if (!visited.contains(edge.getDestination())) {
              // add the edge to the list
              currentChildren.put(edge.getDestination(), edge);
            }
          }
        }

        if (!currentChildren.isEmpty()) {
          // push the destination into the stack.
          for (T destination : currentChildren.keySet()) {
            stack.push(destination);
          }
        }
      }
    }
    return result;
  }

  /**
   * goes through an recursive breadth first search, returning a list of the order of traversal of
   * the graph.
   */
  public List<T> recursiveBreadthFirstSearch() {
    // set initial values for recursive search
    Set<T> roots = getRoots();
    Queue<T> queue = new Queue<>();
    Set<T> visited = new HashSet<>();
    List<T> result = new ArrayList<>();

    for (T root : roots) {
      visited.add(root);
      result.add(root);

      // create new treemap
      TreeMap<T, Edge<T>> rootsChildren = new TreeMap<>();

      // Find all edges with the root as the source and enqueue them
      for (Edge<T> edge : edges) {
        if (edge.getSource().equals(root)) {
          rootsChildren.put(edge.getDestination(), edge);
        }
      }

      // queue the children
      for (T key : rootsChildren.keySet()) {
        queue.enqueue(rootsChildren.get(key));
      }
    }

    // running recursive function
    List<T> finalResult = recursiveFunctionBreadthFirst(queue, visited, result);

    for (T t : finalResult) {
      System.out.println(t);
    }

    return finalResult;
  }

  /** a recursive helper function for the recursive breath search. */
  private List<T> recursiveFunctionBreadthFirst(Queue<T> queue, Set<T> visited, List<T> result) {
    if (queue.isEmpty()) {
      // base case of if the queue is empty, return the list of results.
      return result;
    } else {
      // dequeue and get the current edge and its vertex
      Edge<T> currentEdge = queue.dequeue();
      T vertex = currentEdge.getDestination();

      // if we have not visited this node before,
      if (!visited.contains(vertex)) {
        visited.add(vertex);
        result.add(vertex);

        // create a tree map to automatically sort by the destination values.
        TreeMap<T, Edge<T>> children = new TreeMap<>();

        // find all edges with the same source as this edges destinations
        for (Edge<T> edge : edges) {
          if (edge.getSource().equals(vertex)) {
            children.put(edge.getDestination(), edge);
          }
        }

        // queue each edge.
        for (T key : children.keySet()) {
          queue.enqueue(children.get(key));
        }
      }

      // recursively call function again.
      return recursiveFunctionBreadthFirst(queue, visited, result);
    }
  }

  /**
   * goes through an recursive depth first search, returning a list of the order of traversal of the
   * graph.
   */
  public List<T> recursiveDepthFirstSearch() {
    // intiialise variables for recursive search
    Set<T> roots = getRoots();
    Stack<T> stack = new Stack<T>();
    Set<T> visited = new HashSet<T>();
    List<T> result = new ArrayList<T>();

    for (T root : roots) {
      // add roots to results, visited and the stack
      visited.add(root);
      result.add(root);
      stack.push(root);
    }

    List<T> finalResult = recursiveFunctionDepthFirst(stack, visited, result);

    return finalResult;
  }

  /** a recursive helper function for the recursive depth search. */
  private List<T> recursiveFunctionDepthFirst(Stack<T> stack, Set<T> visited, List<T> result) {
    if (stack.isEmpty()) {
      return result;
    } else {
      // pop the current stack
      T currentVertex = stack.pop();

      // add the vertex on the stack to results and visited if we havent already
      if (!visited.contains(currentVertex)) {
        visited.add(currentVertex);
        result.add(currentVertex);
      }

      // create a tree map that compares in reverse order.
      TreeMap<T, Edge<T>> currentChildren = new TreeMap<>(Comparator.reverseOrder());

      for (Edge<T> edge : edges) {
        // look for edges with the same source
        if (edge.getSource().equals(currentVertex)) {
          // look for edges that do not have a vertex we have already visited
          if (!visited.contains(edge.getDestination())) {
            // add the edge to the list
            currentChildren.put(edge.getDestination(), edge);
          }
        }
      }

      if (!currentChildren.isEmpty()) {
        // push the destination into the stack.
        for (T destination : currentChildren.keySet()) {
          stack.push(destination);
        }
      }
      return recursiveFunctionDepthFirst(stack, visited, result);
    }
  }
}
