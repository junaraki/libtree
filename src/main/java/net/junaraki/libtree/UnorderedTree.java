package net.junaraki.libtree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

/**
 * This class implements an unordered tree. An unordered tree is a tree in which there is no
 * ordering specified for children of each node.
 * 
 * @author Jun Araki
 * 
 * @param <E>
 *          the type of elements in this tree
 */
public class UnorderedTree<E> extends AbstractTree<E> {

  /** The parent of this node */
  protected UnorderedTree<E> parent;

  /** A set of children (allowing duplicates) of this node */
  protected Multiset<UnorderedTree<E>> children;

  /**
   * Public constructor.
   */
  public UnorderedTree() {
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
  public UnorderedTree(E element) {
    super(element);
    this.parent = null;
    this.children = null;
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
    Stack<UnorderedTree<E>> stack = new Stack<UnorderedTree<E>>();
    Set<UnorderedTree<E>> visited = new HashSet<UnorderedTree<E>>();

    stack.push(this);
    visited.add(this);

    while (!stack.isEmpty()) {
      UnorderedTree<E> currNode = stack.pop();
      if (currNode == null) {
        continue;
      }

      int depth = this.depth(currNode);
      if (depth > 0) {
        // Indents of the current node.
        List<String> indents = new ArrayList<String>();

        Multiset<UnorderedTree<E>> siblings;
        boolean siblingNotPrinted;
        UnorderedTree<E> node = currNode;
        for (int i = 0; i < depth; i++) {
          siblings = node.getSiblings();

          siblingNotPrinted = false;
          for (Tree<E> sibling : siblings) {
            if (stack.contains(sibling)) {
              // Some siblings have not been printed yet.
              siblingNotPrinted = true;
              break;
            }
          }

          if (siblingNotPrinted) {
            if (i == 0) {
              indents.add("|-- ");
            } else {
              indents.add("|   ");
            }
          } else {
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

      for (UnorderedTree<E> child : currNode.getChildren()) {
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
  private void toNewick(UnorderedTree<E> node, StringBuilder buf, String startMark, String endMark) {
    buf.append(startMark);
    buf.append(node.getElement().toString());
    if (node.hasChild()) {
      for (UnorderedTree<E> child : node.getChildren()) {
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
  public UnorderedTree<E> find(E element) {
    for (UnorderedTree<E> node : getUnorderedTreeNodes()) {
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
  public UnorderedTree<E> findChild(E element) {
    for (UnorderedTree<E> child : getChildren()) {
      if (child.getElement().equals(element)) {
        return child;
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
  public UnorderedTree<E> getParent() {
    return parent;
  }

  /**
   * Sets the given parent to this node.
   * 
   * @param parent
   *          a parent that will be set to this node
   */
  public void setParent(UnorderedTree<E> parent) {
    if (hasParent()) {
      getParent().removeChild(this);
    }
    this.parent = parent;
    addChild(parent, this);
  }

  /**
   * Returns a set of children of this node.
   * 
   * @return a set of children of this node
   */
  public Multiset<UnorderedTree<E>> getChildren() {
    return children;
  }

  /**
   * Returns the number of children of this node.
   * 
   * @return the number of children of this node
   */
  @Override
  public int numChildren() {
    if (children == null) {
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

    for (UnorderedTree<E> child : getChildren()) {
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
  public boolean hasChild(UnorderedTree<E> child) {
    if (!hasChild()) {
      return false;
    }

    return (children.contains(child));
  }

  /**
   * Initializes children of this node.
   */
  private void initChildren() {
    this.children = HashMultiset.create();
  }

  /**
   * Adds the given child to this node.
   * 
   * @param child
   *          a child that will be added to this node
   */
  public void add(UnorderedTree<E> child) {
    addChild(child);
  }

  /**
   * Adds the given child to this node.
   * 
   * @param child
   *          a child that will be added to this node
   */
  public void addChild(UnorderedTree<E> child) {
    if (child.hasParent(this)) {
      // Simply add the given child, and that's it.
      addChild(this, child);
      return;
    }

    // Set this node to a parent of the given child, assuming that the
    // method
    // setParent(UnorderedTree<E>) adds the given child as a child of this
    // node.
    child.setParent(this);
  }

  /**
   * This is an internal method to add the given child to the given parent.
   * 
   * @param parent
   *          a parent that will be added to this node
   * @param child
   *          a child that will be added to this node
   */
  private void addChild(UnorderedTree<E> parent, UnorderedTree<E> child) {
    if (!parent.hasChild()) {
      parent.initChildren();
    }
    parent.getChildren().add(child);
  }

  /**
   * Adds a child with the given element to this node.
   * 
   * @param element
   *          an element that will be added to the children of this node
   */
  @Override
  public void addChild(E element) {
    addChild(new UnorderedTree<E>(element));
  }

  /**
   * Returns a list of all nodes of the type {@code UnorderedTree<E>} in this tree.
   * 
   * @return a list of all nodes of the type {@code UnorderedTree<E>} in this tree
   */
  public List<UnorderedTree<E>> getUnorderedTreeNodes() {
    List<UnorderedTree<E>> nodes = new ArrayList<UnorderedTree<E>>();

    // Depth-first search implementation
    Stack<UnorderedTree<E>> stack = new Stack<UnorderedTree<E>>();
    Set<UnorderedTree<E>> visited = new HashSet<UnorderedTree<E>>();

    stack.push(this);
    visited.add(this);

    while (!stack.isEmpty()) {
      UnorderedTree<E> currNode = stack.pop();
      if (currNode == null) {
        continue;
      }

      nodes.add(currNode);

      if (!currNode.hasChild()) {
        continue;
      }

      for (UnorderedTree<E> child : currNode.getChildren()) {
        if (child == null) {
          continue;
        }

        if (!visited.contains(child)) {
          stack.push(child);
          visited.add(child);
        }
      }
    }

    return nodes;
  }

  /**
   * Returns a list of all nodes of the type {@code Tree<E>} in this tree.
   * 
   * @return a list of all nodes of the type {@code Tree<E>} in this tree
   */
  @Override
  public List<Tree<E>> getNodes() {
    List<Tree<E>> nodes = new ArrayList<Tree<E>>();
    for (UnorderedTree<E> node : getUnorderedTreeNodes()) {
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
    List<E> list = new ArrayList<E>();

    // Depth-first search implementation
    Stack<UnorderedTree<E>> stack = new Stack<UnorderedTree<E>>();
    Set<UnorderedTree<E>> visited = new HashSet<UnorderedTree<E>>();

    stack.push(this);
    visited.add(this);

    while (!stack.isEmpty()) {
      UnorderedTree<E> currNode = stack.pop();
      if (currNode == null) {
        continue;
      }

      list.add(currNode.getElement());

      if (!currNode.hasChild()) {
        continue;
      }

      for (UnorderedTree<E> child : currNode.getChildren()) {
        if (child == null) {
          continue;
        }

        if (!visited.contains(child)) {
          stack.push(child);
          visited.add(child);
        }
      }
    }

    return list;
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
   * Removes the given child.
   * 
   * @param child
   *          a node to be removed from the children of this node
   */
  public void removeChild(UnorderedTree<E> child) {
    if (children != null) {
      children.remove(child);
    }
  }

  /**
   * Removes a child with the given element from this node.
   * 
   * @param element
   *          a node to be removed from the children of this node
   */
  @Override
  public void removeChild(E element) {
    Multiset<UnorderedTree<E>> newChildren = HashMultiset.create();
    for (UnorderedTree<E> child : children) {
      if (child.getElement().equals(element)) {
        continue;
      }
      newChildren.add(child);
    }
    children = newChildren;
  }

  @Override
  public void removeChildren() {
    children = null;
  }

  /**
   * Returns a list of siblings of this node.
   * 
   * @return a list of siblings of this node
   */
  public Multiset<UnorderedTree<E>> getSiblings() {
    Multiset<UnorderedTree<E>> siblings = HashMultiset.create();
    if (!hasParent()) {
      return siblings;
    }

    for (UnorderedTree<E> child : getParent().getChildren()) {
      if (child.equals(this)) {
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
  public Multiset<E> getSiblingElements() {
    Multiset<E> siblingElements = HashMultiset.create();
    for (UnorderedTree<E> node : getSiblings()) {
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
  public UnorderedTree<E> getRoot() {
    if (!hasParent()) {
      // This node is the root.
      return this;
    }

    UnorderedTree<E> node = getParent();
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
  public List<UnorderedTree<E>> getAncestors() {
    List<UnorderedTree<E>> ancestors = new ArrayList<UnorderedTree<E>>();
    UnorderedTree<E> node = getParent();
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
    for (UnorderedTree<E> node : getAncestors()) {
      ancestorElements.add(node.getElement());
    }
    return ancestorElements;
  }

  /**
   * Returns a list of subtrees of this tree.
   * 
   * @return a list of subtrees of this tree
   */
  public List<UnorderedTree<E>> getSubtrees() {
    List<UnorderedTree<E>> subtreeList = getUnorderedTreeNodes();
    subtreeList.remove(this);
    return subtreeList;
  }

  /**
   * Returns a list of leaves in this tree in a natural order.
   * 
   * @return a list of leaves in this tree
   */
  public List<UnorderedTree<E>> getLeaves() {
    List<UnorderedTree<E>> leaves = new ArrayList<UnorderedTree<E>>();
    for (UnorderedTree<E> node : getUnorderedTreeNodes()) {
      if (node.isLeaf()) {
        leaves.add(node);
      }
    }
    return leaves;
  }

}
