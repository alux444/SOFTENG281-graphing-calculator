package nz.ac.auckland.se281.datastructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
   * @param verticies a set of the graphs verticies.
   * @param edges a set of the graphs edges.
   */
  public Graph(Set<T> verticies, Set<Edge<T>> edges) {
    this.verticies = verticies;
    this.edges = edges;
  }

  /**
   * Finds the roots of the graph. Does this by checking for equivalence first, and then returning
   * the minimum values of the equivalence classes of classes that have more than 1 node.
   *
   * @return set of roots.
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
    Set<T> result = new LinkedHashSet<>();
    // put into a tree map to sort by integer type.
    TreeMap<Integer, T> intList = new TreeMap<>();

    for (T root : roots) {
      intList.put(Integer.parseInt(root.toString()), root);
    }

    for (int key : intList.keySet()) {
      result.add(intList.get(key));
    }

    return result;
  }

  /**
   * Checks reflexivity by going through every vertex and ensuring there is a self loop. returns a
   * boolean result.
   *
   * @return boolean of reflexivity.
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
   *
   * @return boolean of symmetry.
   */
  public boolean isSymmetric() {

    // check for a vertice
    for (T vertice : verticies) {
      for (Edge<T> edge : edges) {
        // check all edges with specific vertice
        if (edge.getSource().equals(vertice)) {
          boolean symmetry = false;
          for (Edge<T> otherEdge : edges) {
            // go through all edges and look for another edge which has the source of our first
            // edges destination (i.e the first edge is going into this edge)
            if (otherEdge.getSource().equals(edge.getDestination())) {
              // now if there exists an edge which returns back to the vertice, there is symmetry.
              if (otherEdge.getDestination().equals(edge.getSource())) {
                symmetry = true;
              }
            }
          }
          if (!symmetry) {
            return false;
          }
        }
      }
    }

    return true;
  }

  /**
   * finds the transitivity of a graph by going through all edges where xRy and there exists a yRz.
   * It then searches for a xRz to confirm transitivity, otherwise return a false if this is ever
   * broken. (i.e for every edge that goes A to B which exists a B to C, ensure that there is always
   * an edge A to C) returns a boolean result.
   *
   * @return boolean of transitivity.
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
   *
   * @return boolean of antisymmetry.
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
   *
   * @return boolean of equivalence.
   */
  public boolean isEquivalence() {
    // if the graph is reflexive, symmetric and transitive, it is equivalent.
    return (isReflexive() && isSymmetric() && isTransitive());
  }

  /**
   * Checks the equivalence class for a specific vertex by finding every edge that leads from or to
   * the vertex returns a set of all the vertices which lead to or from the vertex (aka the
   * equivalence class).
   *
   * @param vertex vertex of equivalence class.
   * @return set of equivalence class for the vertex.
   */
  public Set<T> getEquivalenceClass(T vertex) {
    Set<T> equivalenceClasses = new HashSet<T>();

    // only check equivalence class if the graph is an equivalence realtion
    if (isEquivalence()) {
      List<T> vertexList = new ArrayList<>();
      // Iterate through every edge
      for (Edge<T> edge : edges) {
        // // If the destination of the edge is the vertex, add the source to the equivalence class
        if (edge.getDestination().equals(vertex)) {
          vertexList.add(edge.getSource());
        }
        // If the source of the edge is the vertex, add the destination to the equivalence class
        // (ie. edge fulfills (v1,v2))
        if (edge.getSource().equals(vertex)) {
          vertexList.add(edge.getDestination());
        }

        Collections.sort(vertexList);
        for (T currentVertex : vertexList) {
          equivalenceClasses.add(currentVertex);
        }
      }
    }

    return equivalenceClasses;
  }

  /**
   * goes through an iterative breadth first search, returning a list of the order of traversal of
   * the graph.
   *
   * @return list of order of search.
   */
  public List<T> iterativeBreadthFirstSearch() {
    Set<T> roots = getRoots();
    Queue<T> queue = new Queue<T>();
    Set<T> visited = new HashSet<T>();
    List<T> result = new ArrayList<T>();

    // first visit the roots
    for (T root : roots) {
      // Mark the root as visited
      visited.add(root);
      // Add the root to the result list
      result.add(root);
    }

    // now visit every child of the roots.
    for (T root : roots) {

      // create new treemap
      TreeMap<Integer, Edge<T>> rootsChildren = new TreeMap<>();

      // Find all edges with the root as the source and enqueue them
      for (Edge<T> edge : edges) {
        if (edge.getSource().equals(root)) {
          rootsChildren.put(Integer.parseInt(edge.getDestination().toString()), edge);
        }
      }

      // queue the children
      for (int key : rootsChildren.keySet()) {
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
          TreeMap<Integer, Edge<T>> children = new TreeMap<>();

          // find all edges with the same source as this edges destinations
          for (Edge<T> edge : edges) {
            if (edge.getSource().equals(currentVertex)) {
              children.put(Integer.parseInt(edge.getDestination().toString()), edge);
            }
          }

          // queue each edge.
          for (int key : children.keySet()) {
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
   *
   * @return list of order of search.
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
        TreeMap<Integer, Edge<T>> currentChildren = new TreeMap<>(Comparator.reverseOrder());

        for (Edge<T> edge : edges) {
          // look for edges with the same source
          if (edge.getSource().equals(currentVertex)) {
            // look for edges that do not have a vertex we have already visited
            if (!visited.contains(edge.getDestination())) {
              // add the edge to the list
              currentChildren.put(Integer.parseInt(edge.getDestination().toString()), edge);
            }
          }
        }

        if (!currentChildren.isEmpty()) {
          // push the destination into the stack.
          for (int key : currentChildren.keySet()) {
            stack.push(currentChildren.get(key).getDestination());
          }
        }
      }
    }
    return result;
  }

  /**
   * goes through an recursive breadth first search, returning a list of the order of traversal of
   * the graph.
   *
   * @return list of order of search.
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
      TreeMap<Integer, Edge<T>> rootsChildren = new TreeMap<>();

      // Find all edges with the root as the source and enqueue them
      for (Edge<T> edge : edges) {
        if (edge.getSource().equals(root)) {
          rootsChildren.put(Integer.parseInt(edge.getDestination().toString()), edge);
        }
      }

      // queue the children
      for (int key : rootsChildren.keySet()) {
        queue.enqueue(rootsChildren.get(key));
      }
    }

    // running recursive function
    List<T> finalResult = recursiveFunctionBreadthFirst(queue, visited, result);

    return finalResult;
  }

  /**
   * a recursive helper function for the recursive breath search.
   *
   * @param queue queue type of input queue.
   * @param visited set of visited vertices
   * @param result list of previous results.
   * @return list of order of search.
   */
  private List<T> recursiveFunctionBreadthFirst(Queue<T> queue, Set<T> visited, List<T> result) {
    if (queue.isEmpty()) {
      // base case of if the queue is empty, return the list of results.
      return result;
    } else {
      // dequeue current edge and vertex.
      Edge<T> currentEdge = queue.dequeue();
      T currentVertex = currentEdge.getDestination();
      // check if the current vertex has already been visited
      if (!visited.contains(currentVertex)) {
        // mark the current vertex as visited
        visited.add(currentVertex);
        // add the current vertex to the result list
        result.add(currentVertex);

        // create a tree map to automatically sort by the destination values.
        TreeMap<Integer, Edge<T>> children = new TreeMap<>();

        // find all edges with the same source as this edges destinations
        for (Edge<T> edge : edges) {
          if (edge.getSource().equals(currentVertex)) {
            children.put(Integer.parseInt(edge.getDestination().toString()), edge);
          }
        }

        // queue each edge.
        for (int key : children.keySet()) {
          queue.enqueue(children.get(key));
        }
      }
    }

    // recursively call function again.
    return recursiveFunctionBreadthFirst(queue, visited, result);
  }

  /**
   * goes through an recursive depth first search, returning a list of the order of traversal of the
   * graph.
   *
   * @return list of order of search.
   */
  public List<T> recursiveDepthFirstSearch() {
    // intiialise variables for recursive search
    Set<T> roots = getRoots();
    List<T> listOfRoots = new ArrayList<>();

    // put sets into a list
    for (T root : roots) {
      listOfRoots.add(root);
    }

    Stack<T> stack = new Stack<T>();
    Set<T> visited = new HashSet<T>();
    List<T> result = new ArrayList<T>();

    for (int i = 0; i < listOfRoots.size(); i++) {
      // add to stack in reverse order of roots.
      stack.push(listOfRoots.get(listOfRoots.size() - i - 1));
    }

    List<T> finalResult = recursiveFunctionDepthFirst(stack, visited, result);

    return finalResult;
  }

  /**
   * a recursive helper function for the recursive depth search.
   *
   * @param queue queue type of input queue.
   * @param visited set of visited vertices
   * @param result list of previous results.
   * @return list of order of search.
   */
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
      TreeMap<Integer, Edge<T>> currentChildren = new TreeMap<>(Comparator.reverseOrder());

      for (Edge<T> edge : edges) {
        // look for edges with the same source
        if (edge.getSource().equals(currentVertex)) {
          // look for edges that do not have a vertex we have already visited
          if (!visited.contains(edge.getDestination())) {
            // add the edge to the list
            currentChildren.put(Integer.parseInt(edge.getDestination().toString()), edge);
          }
        }
      }

      if (!currentChildren.isEmpty()) {
        // push the destination into the stack.
        for (int key : currentChildren.keySet()) {
          stack.push(currentChildren.get(key).getDestination());
        }
      }
      return recursiveFunctionDepthFirst(stack, visited, result);
    }
  }
}
