package net.junaraki.libtree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

/**
 * This class implements an ordered tree. An ordered tree is a tree in which an ordering is
 * specified for children of each node.
 * 
 * @author Jun Araki
 * 
 * @param <E>
 *          the type of elements in this tree
 */
public class OrderedTree<E> extends AbstractTree<E> {

  /** The parent of this node */
  protected OrderedTree<E> parent;

  /** A list of children of this nodes */
  protected List<OrderedTree<E>> children;

  /**
   * Traversal types; pre-order, in-order, and post-order are depth-first search (DFS), and
   * level-order is breadth-first search (BFS). The in-order traversal should be defined in a binary
   * tree.
   */
  protected enum TraversalType {
    PRE_ORDER, POST_ORDER, IN_ORDER, LEVEL_ORDER
  };

  /**
   * Public constructor.
   */
  public OrderedTree() {
    super();
    this.parent = null;
    this.children = null;
  }

  /**
   * Public constructor.
   * 
   * @param element
   *          an element of this node
   */
  public OrderedTree(E element) {
    super(element);
    this.parent = null;
    this.children = null;
  }

  /**
   * Returns {@code true} if this tree is equal to the given object. Two ordered trees are equal if
   * they have the same elements in the same tree structure.
   * 
   * @param obj
   *          an object to be compared with this tree
   * @return {@code true} if this tree is equal to the given object; {@code false} otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof OrderedTree)) {
      return false;
    }

    OrderedTree<?> thatTree = (OrderedTree<?>) obj;
    if (isEmpty() && thatTree.isEmpty()) {
      // Two empty trees.
      return true;
    }

    // If two ordered trees are equal, then current nodes have the same element, and their child
    // nodes are also equal.
    if (!getElement().equals(thatTree.getElement())) {
      return false;
    }

    if (numChildren() != thatTree.numChildren()) {
      return false;
    }
    for (int i = 0; i < numChildren(); i++) {
      OrderedTree<E> child = getChildAt(i);
      if (!child.equals(thatTree.getChildAt(i))) {
        return false;
      }
    }

    return true;
  }

  /**
   * Returns the hash code value for this tree. Two trees must have the same hash code if they are
   * equal; we hash elements of all nodes to generate the hash code.
   * 
   * @return the hash code value for this tree
   */
  @Override
  public int hashCode() {
    int hashCode = 17;
    for (E element : getElements()) {
      hashCode = 31 * hashCode + element.hashCode();
    }
    return hashCode;
  }

  /**
   * Returns a string representation of this tree in the format of the UNIX 'tree' command.
   * 
   * @return a string representation of this tree in the format of the UNIX 'tree' command
   */
  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder();

    // Depth-first search implementation
    Stack<OrderedTree<E>> stack = new Stack<OrderedTree<E>>();
    List<OrderedTree<E>> visited = new ArrayList<OrderedTree<E>>();

    stack.push(this);
    visited.add(this);

    while (!stack.isEmpty()) {
      OrderedTree<E> currNode = stack.pop();
      if (currNode == null) {
        continue;
      }

      int depth = this.depth(currNode);
      if (depth > 0) {
        // Indents of the current node.
        List<String> indents = new ArrayList<String>();

        List<OrderedTree<E>> siblings;
        boolean siblingNotPrinted;
        OrderedTree<E> node = currNode;
        for (int i = 0; i < depth; i++) {
          siblings = node.getSiblings();

          siblingNotPrinted = false;
          for (Tree<E> sibling : siblings) {
            if (stack.contains(sibling)) {
              siblingNotPrinted = true;
              break;
            }
          }

          if (siblingNotPrinted) {
            // There are some siblings that have not been printed yet.
            if (i == 0) {
              indents.add("|-- ");
            } else {
              indents.add("|   ");
            }
          } else {
            // All siblings have already been printed.
            if (i == 0) {
              indents.add("`-- ");
            } else {
              indents.add("    ");
            }
          }

          node = node.getParent();
        }
        // Collect the indent by making 'indents' reverse
        for (int i = indents.size() - 1; i >= 0; i--) {
          buf.append(indents.get(i));
        }
      }
      buf.append(currNode.getElement());
      buf.append("\n");

      if (!currNode.hasChild()) {
        continue;
      }

      // In the case of ordered trees, we want to reverse the order of children for traversing in
      // order to preserve the order in output since a stack is LIFO.
      List<OrderedTree<E>> currChildren = currNode.getChildren();
      for (int i = currChildren.size() - 1; i >= 0; i--) {
        OrderedTree<E> child = currChildren.get(i);
        if (child == null) {
          continue;
        }

        if (!visited.contains(child)) {
          stack.push(child);
          visited.add(child);
        }
      }
    }

    return buf.toString();
  }

  /**
   * Returns a string representation of this tree in the Newick standard format.
   * 
   * @return a string representation of this tree in the Newick standard format
   */
  public String toNewick() {
    return toNewick(PAREN_START, PAREN_END);
  }

  /**
   * Returns a string representation of this tree in the Newick standard format with the given
   * starting and ending mark.
   * 
   * @param startMark
   *          a string to start with a string presentation of a node
   * @param endMark
   *          a string to end with a string presentation of a node
   * @return a string representation of this tree in the Newick standard format
   */
  public String toNewick(String startMark, String endMark) {
    StringBuilder buf = new StringBuilder();

    toNewick(this, buf, startMark, endMark);

    return buf.toString();
  }

  /**
   * This is an internal method to recursively return a string representation of this tree in the
   * Newick standard format with the given starting and ending mark.
   * 
   * @param node
   *          a node that will be represented as a string
   * @param buf
   *          a buffer that will store the string presentation of the node
   * @param startMark
   *          a string to start with a string presentation of a node
   * @param endMark
   *          a string to end with a string presentation of a node
   */
  private void toNewick(OrderedTree<E> node, StringBuilder buf, String startMark, String endMark) {
    buf.append(startMark);
    buf.append(node.getElement().toString());
    if (node.hasChild()) {
      for (OrderedTree<E> child : node.getChildren()) {
        if (child == null) {
          continue;
        }

        toNewick(child, buf, startMark, endMark);
      }
    }
    buf.append(endMark);
  }

  /**
   * Finds a node of the first occurrence of the given element in this tree.
   * 
   * @param element
   *          an element to be found in this tree, if present
   * @return a node of the first occurrence of the given element in this tree
   */
  @Override
  public OrderedTree<E> find(E element) {
    for (OrderedTree<E> node : getOrderedTreeNodes()) {
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
  public OrderedTree<E> findChild(E element) {
    if (this.hasChild()) {
      for (OrderedTree<E> child : getChildren()) {
        if (child.getElement().equals(element)) {
          return child;
        }
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
  public OrderedTree<E> getParent() {
    return parent;
  }

  /**
   * Sets the given parent to this node.
   * 
   * @param parent
   *          a parent that will be set to this node
   */
  public void setParent(OrderedTree<E> parent) {
    setParent(parent, true);
  }

  /**
   * Sets the given parent to this node while specifying whether to add this node as a child of the
   * given parent.
   * 
   * @param parent
   *          a parent that will be set to this node
   * @param addChild
   *          whether to add this node as a child of the given parent
   */
  protected void setParent(OrderedTree<E> parent, boolean addChild) {
    if (hasParent()) {
      getParent().removeChild(this);
    }
    this.parent = parent;
    if (addChild) {
      addChild(parent, this);
    }
  }

  /**
   * Returns the child of this node at the given index.
   * 
   * @param index
   *          an index of the element to return
   * @return the child of this node at the given index
   */
  public OrderedTree<E> getChildAt(int index) {
    if (!hasChild() || index < 0 || index >= numChildren()) {
      throw new IndexOutOfBoundsException(
              String.format("Index: %d, Size: %d", index, numChildren()));
    }
    return children.get(index);
  }

  /**
   * Returns a list of children of this node.
   * 
   * @return a list of children of this node
   */
  public List<OrderedTree<E>> getChildren() {
    return children;
  }

  /**
   * Returns the number of children of this node.
   * 
   * @return the number of children of this node
   */
  @Override
  public int numChildren() {
    if (children == null || children.isEmpty()) {
      return 0;
    }
    return children.size();
  }

  /**
   * Returns {@code true} if this node has a child with the given element.
   * 
   * @param element
   *          an element whose presence in the children of this node will be tested
   * @return {@code true} if this node has a child with the given element; {@code false} otherwise
   */
  @Override
  public boolean hasChild(E element) {
    if (!hasChild()) {
      return false;
    }

    for (OrderedTree<E> child : getChildren()) {
      if (child.hasElement(element)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns {@code true} if this node has the given child.
   * 
   * @param child
   *          a node whose presence in the children of this node will be tested
   * @return {@code true} if this node has the given child; {@code false} otherwise
   */
  public boolean hasChild(OrderedTree<E> child) {
    if (!hasChild()) {
      return false;
    }

    return (children.contains(child));
  }

  /**
   * Returns the first child of this node.
   * 
   * @return the first child of this node
   */
  public OrderedTree<E> getFirstChild() {
    return getChildAt(0);
  }

  /**
   * Returns {@code true} if the first child of this node is the given node.
   * 
   * @param node
   *          a node whose presence as the first child will be tested
   * @return {@code true} if the first child of this node is the given node; {@code false} otherwise
   */
  public boolean isFirstChild(OrderedTree<E> node) {
    return (getFirstChild().equals(node));
  }

  /**
   * Returns the last child of this node.
   * 
   * @return the last child of this node
   */
  public OrderedTree<E> getLastChild() {
    return getChildAt(numChildren() - 1);
  }

  /**
   * Returns {@code true} if the last child of this node is the given node.
   * 
   * @param node
   *          a node whose presence as the last child will be tested
   * @return {@code true} if the last child of this node is the given node; {@code false} otherwise
   */
  public boolean isLastChild(OrderedTree<E> node) {
    return (getLastChild().equals(node));
  }

  /**
   * Initializes children of this node.
   */
  protected void initChildren() {
    this.children = new ArrayList<OrderedTree<E>>();
  }

  /**
   * Sets the given child to this node at the given index.
   * 
   * @param index
   *          an index of the element to set
   * @param child
   *          a child that will be set to this node
   */
  public void setChildAt(int index, OrderedTree<E> child) {
    if (!hasChild() || index < 0 || index >= numChildren()) {
      throw new IndexOutOfBoundsException(
              String.format("Index: %d, Size: %d", index, numChildren()));
    }

    children.set(index, child);
    child.setParent(this, false);
  }

  /**
   * Adds the given child to this node.
   * 
   * @param child
   *          a child that will be added to this node
   */
  public void add(OrderedTree<E> child) {
    addChild(child);
  }

  /**
   * Adds the given child to this node.
   * 
   * @param child
   *          a child that will be added to this node
   */
  public void addChild(OrderedTree<E> child) {
    if (child.hasParent(this)) {
      // Simply add the given child, and that's it.
      addChild(this, child);
      return;
    }

    // Set this node to a parent of the given child, assuming that the method
    // setParent(OrderedTree<E>) adds the given child as a child of this node.
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
    addChild(new OrderedTree<E>(element));
  }

  /**
   * This is an internal method to add the given child to the given parent.
   * 
   * @param parent
   *          a parent that will be added to this node
   * @param child
   *          a child that will be added to this node
   */
  private void addChild(OrderedTree<E> parent, OrderedTree<E> child) {
    if (!parent.hasChild()) {
      parent.initChildren();
    }
    parent.getChildren().add(child);
  }

  /**
   * Returns a list of siblings of this node.
   * 
   * @return a list of siblings of this node
   */
  public List<OrderedTree<E>> getSiblings() {
    List<OrderedTree<E>> siblings = new ArrayList<OrderedTree<E>>();
    if (!hasParent()) {
      return siblings;
    }

    for (OrderedTree<E> child : getParent().getChildren()) {
      if (child == null || child.equals(this)) {
        continue;
      }
      siblings.add(child);
    }

    return siblings;
  }

  /**
   * Returns a list of elements of siblings of this node.
   * 
   * @return a list of elements of siblings of this node
   */
  @Override
  public List<E> getSiblingElements() {
    List<E> siblingElements = new ArrayList<E>();
    for (OrderedTree<E> node : getSiblings()) {
      siblingElements.add(node.getElement());
    }
    return siblingElements;
  }

  /**
   * Returns the number of siblings of this node.
   * 
   * @return the number of siblings of this node
   */
  @Override
  public int numSiblings() {
    return getSiblings().size();
  }

  /**
   * Returns the root of this tree.
   * 
   * @return the root of this tree.
   */
  @Override
  public OrderedTree<E> getRoot() {
    if (!hasParent()) {
      // This node is the root.
      return this;
    }

    OrderedTree<E> node = getParent();
    while (node.hasParent()) {
      node = node.getParent();
    }
    return node;
  }

  /**
   * Returns a list of ancestors of this node, ordered from the root to the parent of this node.
   * 
   * @return a list of ancestors of this node, ordered from the root to the parent of this node
   */
  public List<OrderedTree<E>> getAncestors() {
    List<OrderedTree<E>> ancestors = new ArrayList<OrderedTree<E>>();
    OrderedTree<E> node = getParent();
    while (node != null) {
      ancestors.add(node);
      node = node.getParent();
    }

    Collections.reverse(ancestors);
    return ancestors; // The top ancestor is the root.
  }

  /**
   * Returns a list of elements of ancestors of this node, ordered from the root to the parent of
   * this node.
   * 
   * @return a list of elements of ancestors of this node, ordered from the root to the parent of
   *         this node
   */
  @Override
  public List<E> getAncestorElements() {
    List<E> ancestorElements = new ArrayList<E>();
    for (OrderedTree<E> node : getAncestors()) {
      ancestorElements.add(node.getElement());
    }
    return ancestorElements;
  }

  /**
   * Returns a list of all nodes of the type {@code OrderedTree<E>} in this tree.
   * 
   * @return a list of all nodes of the type {@code OrderedTree<E>} in this tree
   */
  public List<OrderedTree<E>> getOrderedTreeNodes() {
    return traverseNodesPreOrder();
  }

  /**
   * Returns a list of all nodes of the type {@code Tree<E>} in this tree.
   * 
   * @return a list of all nodes of the type {@code Tree<E>} in this tree
   */
  @Override
  public List<Tree<E>> getNodes() {
    List<Tree<E>> nodes = new ArrayList<Tree<E>>();
    for (OrderedTree<E> node : getOrderedTreeNodes()) {
      nodes.add(node);
    }

    return nodes;
  }

  /**
   * Returns a list of all elements in this tree.
   * 
   * @return a list of all elements in this tree
   */
  @Override
  public List<E> getElements() {
    return traverse(TraversalType.LEVEL_ORDER); // Use BFS.
  }

  /**
   * Traverses this tree, and returns a list of found elements in this tree.
   * 
   * @return a list of elements of nodes traversed with breadth-first search (BFS)
   */
  public List<E> traverse() {
    return traverse(TraversalType.LEVEL_ORDER); // Use BFS.
  }

  /**
   * Traverses this tree in the given traversal method, and returns a list of found elements.
   * 
   * @param traversalType
   * @return a list of elements of nodes traversed in the given traversal method
   */
  private List<E> traverse(TraversalType traversalType) {
    List<E> list = new ArrayList<E>();
    for (OrderedTree<E> node : traverseNodes(traversalType)) {
      list.add(node.getElement());
    }

    return list;
  }

  /**
   * Traverses this tree, and returns a list of found nodes in this tree.
   * 
   * @return a list of nodes traversed with breadth-first search (BFS)
   */
  public List<OrderedTree<E>> traverseNodes() {
    return traverseNodes(TraversalType.LEVEL_ORDER); // Use BFS.
  }

  /**
   * Traverses this tree in the given traversal method, and returns a list of found nodes.
   * 
   * @param traversalType
   * @return a list of nodes traversed in the given traversal method
   */
  private List<OrderedTree<E>> traverseNodes(TraversalType traversalType) {
    List<OrderedTree<E>> list = new ArrayList<OrderedTree<E>>();
    switch (traversalType) {
      case PRE_ORDER:
        traverseNodesPreOrder(this, list);
        break;
      case POST_ORDER:
        traverseNodesPostOrder(this, list);
        break;
      case LEVEL_ORDER:
        traverseNodesLevelOrder(this, list);
        break;
      default:
        // The default traversal is BFS.
        traverseNodesLevelOrder(this, list);
    }

    return list;
  }

  /**
   * Traverses this tree in the pre-order manner, which is one way of depth-first search (DFS), and
   * returns a list of found elements.
   * 
   * @return a list of elements of nodes traversed in the pre-order manner
   */
  public List<E> traversePreOrder() {
    return traverse(TraversalType.PRE_ORDER);
  }

  /**
   * Traverses this tree in the pre-order manner, which is one way of depth-first search (DFS), and
   * returns a list of found nodes.
   * 
   * @return a list of nodes traversed in the pre-order manner
   */
  public List<OrderedTree<E>> traverseNodesPreOrder() {
    return traverseNodes(TraversalType.PRE_ORDER);
  }

  /**
   * This is an internal method to recursively traverse the given node in the pre-order manner,
   * adding it to the given list.
   * 
   * @param node
   *          a node to be traversed
   * @param list
   *          a list of nodes that have been traversed so far
   */
  private void traverseNodesPreOrder(OrderedTree<E> node, List<OrderedTree<E>> list) {
    if (node == null) {
      return;
    }

    list.add(node);
    if (node.hasChild()) {
      for (OrderedTree<E> child : node.getChildren()) {
        traverseNodesPreOrder(child, list);
      }
    }
  }

  /**
   * Traverses this tree in the post-order manner, which is one way of depth-first search (DFS), and
   * returns a list of found elements.
   * 
   * @return a list of elements of nodes traversed in the post-order manner
   */
  public List<E> traversePostOrder() {
    return traverse(TraversalType.POST_ORDER);
  }

  /**
   * Traverses this tree in the post-order manner, which is one way of depth-first search (DFS), and
   * returns a list of found nodes.
   * 
   * @return a list of nodes traversed in the post-order manner
   */
  public List<OrderedTree<E>> traverseNodesPostOrder() {
    return traverseNodes(TraversalType.POST_ORDER);
  }

  /**
   * This is an internal method to recursively traverse the given node in the post-order manner,
   * adding it to the given list.
   * 
   * @param node
   *          a node to be traversed
   * @param list
   *          a list of nodes that have been traversed so far
   */
  private void traverseNodesPostOrder(OrderedTree<E> node, List<OrderedTree<E>> list) {
    if (node == null) {
      return;
    }

    if (node.hasChild()) {
      for (OrderedTree<E> child : node.getChildren()) {
        traverseNodesPostOrder(child, list);
      }
    }
    list.add(node);
  }

  /**
   * Traverses this tree in the level-order manner, which is breadth-first search (BFS), and returns
   * a list of found elements.
   * 
   * @return a list of elements of nodes traversed in the level-order manner
   */
  public List<E> traverseLevelOrder() {
    return traverse(TraversalType.LEVEL_ORDER);
  }

  /**
   * Traverses this tree in the level-order manner, which is breadth-first search (BFS), and returns
   * a list of found nodes.
   * 
   * @return a list of nodes traversed in the level-order manner
   */
  public List<OrderedTree<E>> traverseNodesLevelOrder() {
    return traverseNodes(TraversalType.LEVEL_ORDER);
  }

  /**
   * This is an internal method to recursively traverse the given node in the level-order manner,
   * adding it to the given list.
   * 
   * @param node
   *          a node to be traversed
   * @param list
   *          a list of nodes that have been traversed so far
   */
  private void traverseNodesLevelOrder(OrderedTree<E> node, List<OrderedTree<E>> list) {
    // Breadth-first search implementation
    Queue<OrderedTree<E>> queue = new LinkedList<OrderedTree<E>>();
    List<OrderedTree<E>> visited = new ArrayList<OrderedTree<E>>();

    queue.add(this);
    visited.add(this);

    while (!queue.isEmpty()) {
      OrderedTree<E> currNode = queue.remove();
      if (currNode == null) {
        continue;
      }

      list.add(currNode);

      if (!currNode.hasChild()) {
        continue;
      }

      for (OrderedTree<E> child : currNode.getChildren()) {
        if (child == null) {
          continue;
        }

        if (!visited.contains(child)) {
          queue.add(child);
          visited.add(child);
        }
      }
    }
  }

  /**
   * Removes this node.
   */
  @Override
  public void remove() {
    if (!isRoot()) {
      getParent().removeChild(this);
    } else {
      setElement(null);
      removeChildren();
    }
  }

  /**
   * Removes the given child from this node. This operation removes all nodes under the child.
   * 
   * @param child
   *          a node to be removed from the children of this node
   */
  public void removeChild(OrderedTree<E> child) {
    if (children != null) {
      children.remove(child);
    }
  }

  /**
   * Removes a child with the given element from this node. This operation removes the child and its
   * all descendants.
   * 
   * @param element
   *          an element to be removed from the children of this node
   */
  @Override
  public void removeChild(E element) {
    List<OrderedTree<E>> newChildren = new ArrayList<OrderedTree<E>>();
    for (OrderedTree<E> child : children) {
      if (child.getElement().equals(element)) {
        continue;
      }
      newChildren.add(child);
    }
    children = newChildren;
  }

  /**
   * Removes a child at the given index from this node. This operation removes the child and its all
   * descendants.
   * 
   * @param index
   *          an index where a child will be removed from this node
   */
  public void removeChildAt(int index) {
    if (!hasChild() || index < 0 || index >= numChildren()) {
      throw new IndexOutOfBoundsException(
              String.format("Index: %d, Size: %d", index, numChildren()));
    }
    children.remove(index);
  }

  @Override
  public void removeChildren() {
    children = null;
  }

  /**
   * Returns a list of subtrees of this tree.
   * 
   * @return a list of subtrees of this tree
   */
  public List<OrderedTree<E>> getSubtrees() {
    List<OrderedTree<E>> subtreeList = getOrderedTreeNodes();
    subtreeList.remove(this);
    return subtreeList;
  }

  /**
   * Returns a list of leaves in this tree in a natural order.
   * 
   * @return a list of leaves in this tree
   */
  public List<OrderedTree<E>> getLeaves() {
    List<OrderedTree<E>> leaves = new ArrayList<OrderedTree<E>>();
    for (OrderedTree<E> node : getOrderedTreeNodes()) {
      if (node.isLeaf()) {
        leaves.add(node);
      }
    }
    return leaves;
  }

  /**
   * Returns {@code true} if the given tree matches this tree or a subtree of the tree.
   * 
   * @param tree
   *          a tree to compared with this tree
   * @return {@code true} if the given tree matches this tree or a subtree of the tree;
   *         {@code false} otherwise
   */
  public boolean subsumes(OrderedTree<E> tree) {
    for (OrderedTree<E> subtree : getOrderedTreeNodes()) {
      if (subtree.equals(tree)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Converts this ordered tree to a binary tree. This method constructs a left-child, right-sibling
   * (LC-RS) binary tree for this ordered tree, guaranteeing a one-to-one mapping between an ordered
   * tree and a binary tree.
   * 
   * @return a binary tree converted from this tree
   */
  public BinaryTree<E> binarize() {
    BinaryTree<E> bTree = new BinaryTree<E>();

    //BiMap<OrderedTree<E>, BinaryTree<E>> nodeMap = HashBiMap.create();
    Map<OrderedTree<E>, BinaryTree<E>> nodeMap = new HashMap<OrderedTree<E>, BinaryTree<E>>();
    for (OrderedTree<E> node : traverseNodesPreOrder()) {
      E element = node.getElement();
      if (node.equals(this)) {
        // In the case of the root
        bTree.setElement(element);
        nodeMap.put(node, bTree);
        continue;
      }

      OrderedTree<E> oParent = node.getParent();
      if (!nodeMap.containsKey(oParent)) {
        throw new RuntimeException("Unexpected error in binarizing an ordered tree.");
      }
      BinaryTree<E> bParent = nodeMap.get(oParent);

      if (oParent.isFirstChild(node)) {
        bParent.setLeftChild(element);
        nodeMap.put(node, bParent.getLeftChild());
      } else {
        BinaryTree<E> deepestRightLeaf = bParent.getDeepestRightLeaf();
        deepestRightLeaf.setRightChild(element);
        nodeMap.put(node, deepestRightLeaf.getRightChild());
      }
    }

    return bTree;
  }

}
