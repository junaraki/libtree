package net.junaraki.libtree;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * This class provides a skeletal implementation of the tree interface. Without making a sharp
 * distinction between an entire tree and an individual node, the class supports various methods to
 * work on an entire tree or on a particular node. In order to make this class as general as
 * possible, it only provides methods that return a value of a primitive type such as {@code int} or
 * {@code boolean}, not instances of the {@code Type} type. Note that child nodes may have duplicate
 * elements.
 * 
 * @author Jun Araki
 * @param <E>
 *          the type of elements in this tree
 */
public abstract class AbstractTree<E> extends AbstractCollection<E> implements Tree<E> {

  public static final String PAREN_START = "(";

  public static final String PAREN_END = ")";

  /** An elemental value of this node as known as a label */
  protected E element;

  /**
   * Public constructor.
   */
  public AbstractTree() {
    this.element = null;
  }

  /**
   * Public constructor.
   * 
   * @param element
   *          an element of this node
   */
  public AbstractTree(E element) {
    this.element = element;
  }

  /**
   * A subclass for iterator implementation.
   */
  private class TreeIterator implements Iterator<E> {
    /** A list of nodes in this tree */
    private List<Tree<E>> nodeList;

    protected TreeIterator(Tree<E> node) {
      nodeList = node.getNodes();
    }

    @Override
    public boolean hasNext() {
      return (!nodeList.isEmpty());
    }

    @Override
    public E next() {
      if (nodeList.isEmpty()) {
        throw new NoSuchElementException();
      }

      Tree<E> node = nodeList.get(0);
      nodeList.remove(node);

      return node.getElement();
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * Returns the iterator of this tree.
   * 
   * @return the iterator of this tree
   */
  @Override
  public Iterator<E> iterator() {
    return new TreeIterator(this);
  }

  /**
   * Returns the number of nodes in this tree.
   * 
   * @return the number of nodes in this tree
   */
  @Override
  public int size() {
    return getNodes().size();
  }

  /**
   * Returns the depth of this tree. The depth of a tree is defined as the maximum depth of a node
   * in the tree.
   * 
   * @return the depth of this tree
   */
  @Override
  public int depth() {
    if (isLeaf()) {
      return 0;
    }

    int maxDepth = 0;
    for (Tree<E> node : getNodes()) {
      int depth = depth(node);
      if (depth > maxDepth) {
        maxDepth = depth;
      }
    }

    return maxDepth + 1;
  }

  /**
   * Returns the depth of the given node. The depth of a node is defined as the number edges on the
   * path from the node to this node (as a root node). Returns -1 if this node is not an ancestor of
   * the given node.
   * 
   * @param node
   *          a node whose depth will be returned
   * @return the depth of the given node
   */
  public int depth(Tree<E> node) {
    if (node == null) {
      throw new IllegalArgumentException("The node from which depth will be checked is not given.");
    }

    int depth = 0;
    while (node != null) {
      if (node.equals(this)) {
        return depth;
      }
      depth++;
      node = node.getParent();
    }

    return -1;
  }

  /**
   * Returns {@code true} if this node has a parent.
   * 
   * @return {@code true} if this node has a parent; {@code false} otherwise
   */
  @Override
  public boolean hasParent() {
    if (getParent() == null) {
      return false;
    }
    return true;
  }

  /**
   * Returns {@code true} if this node has a parent with the given element.
   * 
   * @param element
   *          an element whose presence in the parent of this node will be tested
   * @return {@code true} if this node has a parent with the given element; {@code false} otherwise
   */
  @Override
  public boolean hasParent(E element) {
    if (hasParent()) {
      return getParent().getElement().equals(element);
    }
    return false;
  }

  /**
   * Returns {@code true} if this node has the given parent.
   * 
   * @param parent
   *          a parent whose presence in this node will be tested
   * @return {@code true} if this node has the given parent; {@code false} otherwise
   */
  @Override
  public boolean hasParent(Tree<E> parent) {
    if (hasParent()) {
      return (getParent().equals(parent));
    }
    return (parent == null);
  }

  /**
   * Returns {@code true} if this node is the root.
   * 
   * @return {@code true} if this node is the root; {@code false} otherwise
   */
  @Override
  public boolean isRoot() {
    return !hasParent();
  }

  /**
   * Returns the degree of this tree. The degree of a tree is defined as the maximum degree of a
   * node in the tree.
   * 
   * @return the degree of this tree
   */
  @Override
  public int degree() {
    int maxDegree = 0;
    for (Tree<E> node : getNodes()) {
      int degree = degree(node);
      if (degree > maxDegree) {
        maxDegree = degree;
      }
    }

    return maxDegree;
  }

  /**
   * Returns the degree of the given node. The degree of a node is defined as the number of children
   * of the node.
   * 
   * @param node
   *          a node whose degree will be returned
   * @return the degree of the given node
   */
  @Override
  public int degree(Tree<E> node) {
    if (node == null) {
      throw new IllegalArgumentException("The node is not specified.");
    }

    return node.numChildren();
  }

  /**
   * Returns {@code true} if this node has a child.
   * 
   * @return {@code true} if this node has a child; {@code false} otherwise
   */
  @Override
  public boolean hasChild() {
    return (numChildren() > 0);
  }

  /**
   * Adds a child with the given element.
   * 
   * @param e
   *          an element that will be added to the children of this node
   * @return {@code true} if this collection changed as a result of the call
   */
  @Override
  public boolean add(E e) {
    addChild(e);
    return true;
  }

  /**
   * Removes children with one of the given elements from this node. This operation removes the
   * children and their all descendants.
   * 
   * @param elements
   *          a collection of elements to be removed from the children of this node
   */
  @Override
  public void removeChildren(Collection<E> elements) {
    for (E element : elements) {
      removeChild(element);
    }
  }

  /**
   * Returns {@code true} if this node has an ancestor with the given element.
   * 
   * @param element
   *          an element whose presence in the ancestors of this node will be tested
   * @return {@code true} if this node has the given ancestor; {@code false} otherwise
   */
  public boolean hasAncestor(E element) {
    Tree<E> node = getParent();
    while (node != null) {
      if (node.getElement().equals(element)) {
        return true;
      }
      node = node.getParent();
    }
    return false;
  }

  /**
   * Returns {@code true} if this node has the given ancestor.
   * 
   * @param ancestor
   *          an ancestor whose presence for this node will be tested
   * @return {@code true} if this node has the given ancestor; {@code false} otherwise
   */
  @Override
  public boolean hasAncestor(Tree<E> ancestor) {
    Tree<E> node = getParent();
    while (node != null) {
      if (node.equals(ancestor)) {
        return true;
      }
      node = node.getParent();
    }
    return false;
  }

  /**
   * Returns {@code true} if this node has a descendant with the given element.
   * 
   * @param element
   *          an element whose presence in the descendants of this node will be tested
   * @return {@code true} if this node has the given descendant; {@code false} otherwise
   */
  @Override
  public boolean hasDescendant(E element) {
    for (Tree<E> node : getNodes()) {
      if (node.equals(this)) {
        // Descendants do not contain this node.
        continue;
      }
      if (node.getElement().equals(element)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns {@code true} if this node has the given descendant.
   * 
   * @param descendant
   *          a descendant whose presence for this node will be tested
   * @return {@code true} if this node has the given descendant; {@code false} otherwise
   */
  @Override
  public boolean hasDescendant(Tree<E> descendant) {
    for (Tree<E> node : getNodes()) {
      if (node.equals(this)) {
        // Descendants do not contain this node.
        continue;
      }
      if (node.equals(descendant)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns {@code true} if this node is a leaf.
   * 
   * @return {@code true} if this node is a leaf; {@code false} otherwise
   */
  @Override
  public boolean isLeaf() {
    return (numChildren() == 0);
  }

  /**
   * Returns a list of leaves.
   * 
   * @return a list of leaves
   */
  private List<Tree<E>> getLeaves() {
    List<Tree<E>> leaves = new ArrayList<Tree<E>>();
    for (Tree<E> node : getNodes()) {
      if (node.isLeaf()) {
        leaves.add(node);
      }
    }
    return leaves;
  }

  /**
   * Returns the number of leaves of this tree.
   * 
   * @return the number of leaves of this tree
   */
  @Override
  public int numLeaves() {
    return getLeaves().size();
  }

  /**
   * Returns the element of this node.
   * 
   * @return the element of this node
   */
  @Override
  public E getElement() {
    return element;
  }

  /**
   * Sets the given element to this node.
   * 
   * @param element
   *          an element that will be set to this node
   */
  @Override
  public void setElement(E element) {
    this.element = element;
  }

  /**
   * Returns {@code true} if this node has an element.
   * 
   * @return {@code true} if this node has an element; {@code false} otherwise
   */
  @Override
  public boolean hasElement() {
    return (element != null);
  }

  /**
   * Returns {@code true} if this node has the given element.
   * 
   * @param element
   *          an element whose presence in this node will be tested
   * @return {@code true} if this node has the given element; {@code false} otherwise
   */
  @Override
  public boolean hasElement(E element) {
    return element.equals(this.element);
  }

  /**
   * Returns {@code true} if this node is labeled.
   * 
   * @return {@code true} if this node is labeled; {@code false} otherwise
   */
  @Override
  public boolean isLabeled() {
    return hasElement();
  }

  /**
   * Returns {@code true} if this tree is a labeled tree. A labeled tree is defined as a tree such
   * that each node has a symbol assigned from a fixed finite alphabet.
   * 
   * @param alphabet
   *          a set of elements to be compared with elements in this tree
   * @return {@code true} if this tree is a labeled tree; {@code false} otherwise
   */
  public boolean isLabeledTree(Set<E> alphabet) {
    if (alphabet == null || alphabet.isEmpty()) {
      throw new IllegalArgumentException("The given alphabet has no elements.");
    }

    for (Tree<E> node : getNodes()) {
      if (node == null) {
        return false;
      }
      if (!alphabet.contains(node.getElement())) {
        return false;
      }
    }

    return true;
  }

  /**
   * Returns the yield of this tree as a list of elements of leaves.
   * 
   * @return the yield of this tree
   */
  @Override
  public List<E> yield() {
    List<E> leafElements = new ArrayList<E>();
    for (Tree<E> node : getNodes()) {
      if (node.isLeaf()) {
        leafElements.add(node.getElement());
      }
    }
    return leafElements;
  }

  /**
   * Returns a list of leaf elements in this tree in a natural order.
   * 
   * @return a list of leaf elements in this tree
   */
  public List<E> getLeafElements() {
    return yield();
  }

  /**
   * Returns {@code true} if this tree is balanced. A balanced tree is a tree such that all leaves
   * of the tree are at the same depth.
   * 
   * @return {@code true} if this tree is balanced; {@code false} otherwise
   */
  public boolean isBalanced() {
    List<Tree<E>> leaves = getLeaves();
    int numLeaves = leaves.size();
    if (numLeaves == 0) {
      throw new RuntimeException("Invalid tree without any leaves");
    }
    if (numLeaves == 1) {
      return true;
    }

    int depth = depth(leaves.get(0));
    for (int i = 1; i < numLeaves; i++) {
      if (depth(leaves.get(i)) != depth) {
        // A tree is not balanced when a leaf node's depth is different from
        // other node's depth.
        return false;
      }
    }

    return true;
  }

}
