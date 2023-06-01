package nz.ac.auckland.se281.datastructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

  public Graph(Set<T> verticies, Set<Edge<T>> edges) {
    this.verticies = verticies;
    this.edges = edges;
  }

  public Set<T> getRoots() {
    // creates a new set for roots
    Set<T> roots = new HashSet<T>();
    // go through each vertice

    // only check equivalence class if the graph is an equivalence realtion
    if (isEquivalence()) {
      for (T vertex : verticies) {
        // go through each vertex and add the minimum value for each equivalence relation
        roots.add(Collections.min(getEquivalenceClass(vertex)));
      }
    } else {
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
    }
    return roots;
  }

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

  public boolean isTransitive() {
    // go through all edges.
    for (Edge<T> edge : edges) {
      // set the 'x' and 'y' values. assuming for this edge, we have xRy
      T x = edge.getSource();
      T y = edge.getDestination();
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

  public boolean isEquivalence() {
    // if the graph is reflexive, symmetric and transitive, it is equivalent.
    return (isReflexive() && isSymmetric() && isTransitive());
  }

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

  public List<T> iterativeBreadthFirstSearch() {
    Set<T> roots = getRoots();
    Queue<T> queue = new Queue<T>();
    Set<T> visited = new HashSet<T>();
    List<T> result = new ArrayList<T>();

    for (T root : roots) {
      // Check if the root has already been visited
      if (!visited.contains(root)) {
        // Mark the root as visited
        visited.add(root);
        // Add the root to the result list
        result.add(root);

        List<Edge<T>> rootsChildren = new ArrayList<>();

        // Find all edges with the root as the source and enqueue them
        for (Edge<T> edge : edges) {
          if (edge.getSource().equals(root)) {
            rootsChildren.add(edge);
          }
        }

        // sort the children before queueing
        Collections.sort(rootsChildren, Comparator.comparing(edge -> edge.getDestination()));

        // queue the children
        for (Edge<T> edge : rootsChildren) {
          queue.enqueue(edge);
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

            // create list for all edges of this particular queue.
            List<Edge<T>> currentChildren = new ArrayList<>();

            // find all edges with the current vertex as the source and enqueue them
            for (Edge<T> edge : edges) {
              if (edge.getSource().equals(currentVertex)) {
                currentChildren.add(edge);
              }
            }

            // sort the list
            Collections.sort(currentChildren, Comparator.comparing(edge -> edge.getDestination()));

            for (Edge<T> edge : currentChildren) {
              queue.enqueue(edge);
            }
          }
        }
      }
    }

    return result;
  }

  public List<T> iterativeDepthFirstSearch() {
    // TODO: Task 2.
    throw new UnsupportedOperationException();
  }

  public List<T> recursiveBreadthFirstSearch() {
    // TODO: Task 3.
    throw new UnsupportedOperationException();
  }

  public List<T> recursiveDepthFirstSearch() {
    // TODO: Task 3.
    throw new UnsupportedOperationException();
  }
}
