package net.junaraki.libtree;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements a binary tree. A binary tree is an ordered tree in which each node has at
 * most two children, referred to as the left child and the right child.
 * 
 * @author Jun Araki
 * 
 * @param <E>
 *          the type of elements in this tree
 */
public class BinaryTree<E> extends OrderedTree<E> {

  private static final int LEFT_CHILD_INDEX = 0;

  private static final int RIGHT_CHILD_INDEX = 1;

  /**
   * Public constructor.
   */
  public BinaryTree() {
    super();
  }

  /**
   * Public constructor.
   * 
   * @param element
   *          an element of this node
   */
  public BinaryTree(E element) {
    super(element);
  }

  /**
   * Finds a node of the first occurrence of the given element in this tree.
   * 
   * @param element
   *          an element to be found in this tree, if present
   * @return a node of the first occurrence of the given element in this tree
   */
  @Override
  public BinaryTree<E> find(E element) {
    for (BinaryTree<E> node : getBinaryTreeNodes()) {
      if (node.getElement().equals(element)) {
        return node;
      }
    }
    return null;
  }

  /**
   * Finds a child of the first occurrence of the given element in this node.
   * 
   * @param element
   *          an element to be found in the children of this node, if present
   * @return a child of the first occurrence of the given element in this node
   */
  @Override
  public BinaryTree<E> findChild(E element) {
    if (hasLeftChild()) {
      BinaryTree<E> leftChild = getLeftChild();
      if (leftChild.getElement().equals(element)) {
        return leftChild;
      }
    }

    if (hasRightChild()) {
      BinaryTree<E> rightChild = getRightChild();
      if (rightChild.getElement().equals(element)) {
        return rightChild;
      }
    }

    return null;
  }

  /**
   * Returns the parent of this node.
   * 
   * @return the parent of this node
   */
  @Override
  public BinaryTree<E> getParent() {
    if (parent == null) {
      return null;
    }
    if (!(parent instanceof BinaryTree<?>)) {
      throw new RuntimeException("Found a node that is not of the type BinaryTree.");
    }

    return (BinaryTree<E>) parent;
  }

  /**
   * Sets the given parent to this node.
   * 
   * @param parent
   *          a parent that will be set to this node
   */
  public void setParent(BinaryTree<E> parent) {
    if (hasParent()) {
      getParent().removeChild(this);
    }
    this.parent = parent;
    addChild(parent, this);
  }

  /**
   * Returns the child of this node at the given index.
   * 
   * @param index
   *          an index of the element to return
   * @return the child of this node at the given index
   */
  @Override
  public BinaryTree<E> getChildAt(int index) {
    if (!hasChild() || index < 0 || index >= 2) {
      throw new IndexOutOfBoundsException(
              String.format("Index: %d, Size: %d", index, numChildren()));
    }
    if (children.get(index) == null) {
      return null;
    }
    if (!(children.get(index) instanceof BinaryTree<?>)) {
      throw new RuntimeException("Found a node that is not of the type BinaryTree.");
    }

    return (BinaryTree<E>) children.get(index);
  }

  /**
   * Returns the left child of this node.
   * 
   * @return the left child of this node
   */
  public BinaryTree<E> getLeftChild() {
    if (numChildren() == 0) {
      return null;
    }

    return getChildAt(LEFT_CHILD_INDEX);
  }

  /**
   * Returns the deepest left leaf node of this tree.
   * 
   * @return the deepest left leaf node of this tree
   */
  public BinaryTree<E> getDeepestLeftLeaf() {
    BinaryTree<E> deepestLeftLeaf = this;
    while (deepestLeftLeaf.hasLeftChild()) {
      deepestLeftLeaf = deepestLeftLeaf.getLeftChild();
    }
    return deepestLeftLeaf;
  }

  /**
   * Sets the left child of this child with the given element.
   * 
   * @param element
   *          an element to be set to the left child
   */
  public void setLeftChild(E element) {
    if (hasLeftChild()) {
      // This node has the left child.
      getLeftChild().setElement(element);
      return;
    }

    // At this point, we know that this node has no left child.
    BinaryTree<E> child = new BinaryTree<E>(element);
    if (numChildren() > 0) {
      // This node has the right child (but does not have the left child).
      setChildAt(LEFT_CHILD_INDEX, child);
    } else {
      // This node has no child.
      addChild(child);
    }
  }

  /**
   * Sets the given left child to this node.
   * 
   * @param child
   *          a child node to be set as the left child
   */
  public void setLeftChild(BinaryTree<E> child) {
    if (numChildren() > 0) {
      // This node has either the left child or the right child.
      setChildAt(LEFT_CHILD_INDEX, child);
    } else {
      // This node has no child.
      addChild(child);
    }
  }

  /**
   * Returns the right child of this node.
   * 
   * @return the right child of this node
   */
  public BinaryTree<E> getRightChild() {
    if (numChildren() == 0) {
      return null;
    }
    if (children.size() < 2) {
      return null;
    }

    return getChildAt(RIGHT_CHILD_INDEX);
  }

  /**
   * Returns the deepest right leaf node of this tree.
   * 
   * @return the deepest right leaf node of this tree
   */
  public BinaryTree<E> getDeepestRightLeaf() {
    // If there is no right node, the deepest right leaf is this node
    // itself.
    BinaryTree<E> deepestRightLeaf = this;

    while (deepestRightLeaf.hasRightChild()) {
      deepestRightLeaf = deepestRightLeaf.getRightChild();
    }

    return deepestRightLeaf;
  }

  /**
   * Sets the right child of this node with the given element.
   * 
   * @param element
   *          an element to be set to the right child
   */
  public void setRightChild(E element) {
    if (hasRightChild()) {
      // This node has the right child.
      getRightChild().setElement(element);
      return;
    }

    // At this point, we know that this node has no right child.
    BinaryTree<E> child = new BinaryTree<E>(element);
    if (numChildren() > 0) {
      // This node has the left child (but does not have the right child).
      addChild(child);
    } else {
      // This node has no child.
      initChildren();
      children.add(null); // For the left child
      children.add(child); // For the right child
      child.setParent(this, false);
    }
  }

  /**
   * Sets the given right child to this node.
   * 
   * @param child
   *          a child node to be set as the right child
   */
  public void setRightChild(BinaryTree<E> child) {
    if (hasRightChild()) {
      // This node has the right child.
      setChildAt(RIGHT_CHILD_INDEX, child);
      return;
    }

    // At this point, we know that this node has no right child.
    if (numChildren() > 0) {
      // This node has the left child (but does not have the right child).
      addChild(child);
    } else {
      // This node has no child.
      initChildren();
      children.add(null); // For the left child
      children.add(child); // For the right child
      child.setParent(this, false);
    }
  }

  /**
   * Returns the number of children of this node.
   * 
   * @return the number of children of this node
   */
  @Override
  public int numChildren() {
    int numChildren = 0;
    if (hasLeftChild()) {
      numChildren++;
    }
    if (hasRightChild()) {
      numChildren++;
    }

    return numChildren;
  }

  /**
   * Returns {@code true} if this node has a left child.
   * 
   * @return {@code true} if this node has a left child; {@code false} otherwise
   */
  public boolean hasLeftChild() {
    if (children == null || children.isEmpty()) {
      return false;
    }
    if (children.size() < 1) {
      return false;
    }

    return (children.get(LEFT_CHILD_INDEX) != null);
  }

  /**
   * Returns {@code true} if this node has a left child with the given element.
   * 
   * @param element
   *          an element whose presence in the left child of this node will be tested
   * @return {@code true} if this node has a left child with the given element; {@code false}
   *         otherwise
   */
  public boolean hasLeftChild(E element) {
    if (!hasLeftChild()) {
      return false;
    }

    if (getLeftChild().getElement().equals(element)) {
      return true;
    }

    return false;
  }

  /**
   * Returns {@code true} if this node has the given left child.
   * 
   * @param child
   *          a node whose presence in the left child of this node will be tested
   * @return {@code true} if this node has the given left child; {@code false} otherwise
   */
  public boolean hasLeftChild(BinaryTree<E> child) {
    if (getLeftChild().equals(child)) {
      return true;
    }

    return false;
  }

  /**
   * Returns {@code true} if this node has a right child.
   * 
   * @return {@code true} if this node has a right child; {@code false} otherwise
   */
  public boolean hasRightChild() {
    if (children == null || children.isEmpty()) {
      return false;
    }
    if (children.size() < 2) {
      return false;
    }

    return (children.get(RIGHT_CHILD_INDEX) != null);
  }

  /**
   * Returns {@code true} if this node has a right child with the given element.
   * 
   * @param element
   *          an element whose presence in the right child of this node will be tested
   * @return {@code true} if this node has a right child with the given element; {@code false}
   *         otherwise
   */
  public boolean hasRightChild(E element) {
    if (!hasRightChild()) {
      return false;
    }

    if (getRightChild().getElement().equals(element)) {
      return true;
    }

    return false;
  }

  /**
   * Returns {@code true} if this node has the given right child.
   * 
   * @param child
   *          a node whose presence in the right child of this node will be tested
   * @return {@code true} if this node has the given right child; {@code false} otherwise
   */
  public boolean hasRightChild(BinaryTree<E> child) {
    if (getRightChild().equals(child)) {
      return true;
    }

    return false;
  }

  /**
   * Adds the given child to this node.
   * 
   * @param child
   *          a child that will be added to this node
   */
  public void add(BinaryTree<E> child) {
    addChild(child);
  }

  /**
   * Adds the given child to this node.
   * 
   * @param child
   *          a child that will be added to this node
   */
  public void addChild(BinaryTree<E> child) {
    if (child.hasParent(this)) {
      // Simply add the given child, and that's it.
      addChild(this, child);
      return;
    }

    // Set this node to a parent of the given child, assuming that the
    // method
    // setParent(OrderedTree<E>) adds the given child as a child of this
    // node.
    child.setParent(this);
  }

  /**
   * Adds a child with the given element to this node.
   * 
   * @param element
   *          an element that will be added to the children of this node
   */
  @Override
  public void addChild(E element) {
    addChild(new BinaryTree<E>(element));
  }

  /**
   * This is an internal method to add the given child to the given parent.
   * 
   * @param parent
   *          a parent that will be added to this node
   * @param child
   *          a child that will be added to this node
   */
  private void addChild(BinaryTree<E> parent, BinaryTree<E> child) {
    if (!parent.hasChild()) {
      parent.initChildren();
    } else if (parent.numChildren() >= 2) {
      throw new RuntimeException("A binary tree node cannot add three or more children.");
    }

    parent.getChildren().add(child);
  }

  /**
   * Returns a list of all nodes of the type {@code BinaryTree<E>} in this tree.
   * 
   * @return a list of all nodes of the type {@code BinaryTree<E>} in this tree
   */
  public List<BinaryTree<E>> getBinaryTreeNodes() {
    List<BinaryTree<E>> nodes = new ArrayList<BinaryTree<E>>();
    for (OrderedTree<E> node : traverseNodesPreOrder()) {
      if (!(node instanceof BinaryTree<?>)) {
        throw new RuntimeException("Found a node that is not of the type BinaryTree.");
      }
      nodes.add((BinaryTree<E>) node);
    }

    return nodes;
  }

  /**
   * Removes the left child of this node.
   */
  public void removeLeftChild() {
    if (!hasLeftChild()) {
      // Do nothing.
      return;
    }

    children.set(LEFT_CHILD_INDEX, null);
  }

  /**
   * Removes the right child of this node.
   */
  public void removeRightChild() {
    if (!hasRightChild()) {
      // Do nothing.
      return;
    }

    children.set(RIGHT_CHILD_INDEX, null);
  }

}
