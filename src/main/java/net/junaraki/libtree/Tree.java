package net.junaraki.libtree;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * An interface for a general-purpose tree and a node.
 * 
 * @author Jun Araki
 * 
 * @param <E>
 *          the type of elements in this tree
 */
public interface Tree<E> extends Collection<E> {

  /**
   * Returns {@code true} if the given object is equal to this tree.
   * 
   * @param obj
   *          an object to be compared with this tree
   * @return {@code true} if the given object is equal to this tree; {@code false} otherwise
   */
  public boolean equals(Object obj);

  /**
   * Returns the hash code value for this tree.
   * 
   * @return the hash code value for this tree
   */
  public int hashCode();

  /**
   * Finds a node of the first occurrence of the given element in this tree.
   * 
   * @param element
   *          an element to be found in this tree, if present
   * @return a node of the first occurrence of the given element in this tree
   */
  public Tree<E> find(E element);

  /**
   * Finds a child of the first occurrence of the given element in this node.
   * 
   * @param element
   *          an element to be found in the children of this node, if present
   * @return a child of the first occurrence of the given element in this node
   */
  public Tree<E> findChild(E element);

  /**
   * Returns the depth of this tree. The depth of a tree is defined as the maximum depth of a node
   * in the tree.
   * 
   * @return the depth of this tree
   */
  public int depth();

  /**
   * Returns the depth of the given node. The depth of a node is defined as the number edges on the
   * path from the node to this node (as the root). Returns -1 if this node is not an ancestor of
   * the given node.
   * 
   * @param node
   *          a node whose depth will be returned
   * @return the depth of the given node
   */
  public int depth(Tree<E> node);

  /**
   * Returns the parent of this node.
   * 
   * @return the parent of this node
   */
  public Tree<E> getParent();

  /**
   * Returns {@code true} if this node has a parent.
   * 
   * @return {@code true} if this node has a parent; {@code false} otherwise
   */
  public boolean hasParent();

  /**
   * Returns {@code true} if this node has a parent with the given element.
   * 
   * @param element
   *          an element whose presence in the parent of this node will be tested
   * @return {@code true} if this node has a parent with the given element; {@code false} otherwise
   */
  public boolean hasParent(E element);

  /**
   * Returns {@code true} if this node has the given parent.
   * 
   * @param parent
   *          a parent whose presence in this node will be tested
   * @return {@code true} if this node has the given parent; {@code false} otherwise
   */
  public boolean hasParent(Tree<E> parent);

  /**
   * Returns the degree of this tree. The degree of a tree is defined as the maximum degree of a
   * node in the tree.
   * 
   * @return the degree of this tree
   */
  public int degree();

  /**
   * Returns the degree of the given node. The degree of a node is defined as the number of children
   * of the node.
   * 
   * @param node
   *          a node whose degree will be returned
   * @return the degree of the given node
   */
  public int degree(Tree<E> node);

  /**
   * Returns the number of children of this node.
   * 
   * @return the number of children of this node
   */
  public int numChildren();

  /**
   * Returns {@code true} if this node has a child.
   * 
   * @return {@code true} if this node has a child; {@code false} otherwise
   */
  public boolean hasChild();

  /**
   * Returns {@code true} if this node has a child with the given element.
   * 
   * @param element
   *          an element whose presence in the children of this node will be tested
   * @return {@code true} if this node has a child with the given element; {@code false} otherwise
   */
  public boolean hasChild(E element);

  /**
   * Adds a child with the given element to this node.
   * 
   * @param element
   *          an element that will be added to the children of this node
   */
  public void addChild(E element);

  /**
   * Returns a collection of elements of siblings of this node.
   * 
   * @return a collection of elements of siblings of this node
   */
  public Collection<E> getSiblingElements();

  /**
   * Returns the number of siblings of this node.
   * 
   * @return the number of siblings of this node
   */
  public int numSiblings();

  /**
   * Returns the root of this tree.
   * 
   * @return the root of this tree
   */
  public Tree<E> getRoot();

  /**
   * Returns {@code true} if this node is the root.
   * 
   * @return {@code true} if this node is the root; {@code false} otherwise
   */
  public boolean isRoot();

  /**
   * Returns a list of elements of ancestors of this node, ordered from the root to the parent of
   * this node.
   * 
   * @return a list of elements of ancestors of this node, ordered from the root to the parent of
   *         this node
   */
  public List<E> getAncestorElements();

  /**
   * Removes this node.
   */
  public void remove();

  /**
   * Removes a child with the given element from this node. This operation removes the child and its
   * all descendants.
   * 
   * @param element
   *          an element to be removed from the children of this node
   */
  public void removeChild(E element);

  /**
   * Removes all children from this node.
   */
  public void removeChildren();

  /**
   * Removes children with one of the given elements from this node. This operation removes the
   * children and their all descendants.
   * 
   * @param element
   *          a collection of elements to be removed from the children of this node
   */
  public void removeChildren(Collection<E> element);

  /**
   * Returns {@code true} if this node has an ancestor with the given element.
   * 
   * @param element
   *          an element whose presence in the ancestors of this node will be tested
   * @return {@code true} if this node has the given ancestor; {@code false} otherwise
   */
  public boolean hasAncestor(E element);

  /**
   * Returns {@code true} if this node has the given ancestor.
   * 
   * @param ancestor
   *          an ancestor whose presence for this node will be tested
   * @return {@code true} if this node has the given ancestor; {@code false} otherwise
   */
  public boolean hasAncestor(Tree<E> ancestor);

  /**
   * Returns {@code true} if this node has a descendant with the given element.
   * 
   * @param element
   *          an element whose presence in the descendants of this node will be tested
   * @return {@code true} if this node has the given descendant; {@code false} otherwise
   */
  public boolean hasDescendant(E element);

  /**
   * Returns {@code true} if this node has the given descendant.
   * 
   * @param descendant
   *          a descendant whose presence for this node will be tested
   * @return {@code true} if this node has the given descendant; {@code false} otherwise
   */
  public boolean hasDescendant(Tree<E> descendant);

  /**
   * Returns a list of all elements of this tree.
   * 
   * @return a list of all elements of this tree
   */
  public List<E> getElements();

  /**
   * Returns a list of all nodes of this tree.
   * 
   * @return a list of all nodes of this tree
   */
  public List<Tree<E>> getNodes();

  /**
   * Returns {@code true} if this node is a leaf.
   * 
   * @return {@code true} if this node is a leaf; {@code false} otherwise
   */
  public boolean isLeaf();

  /**
   * Returns the number of leaves in this tree.
   * 
   * @return the number of leaves in this tree
   */
  public int numLeaves();

  /**
   * Returns the element of this node.
   * 
   * @return the element of this node
   */
  public E getElement();

  /**
   * Sets the given element to this node.
   * 
   * @param element
   *          an element that will be set to this node
   */
  public void setElement(E element);

  /**
   * Returns {@code true} if this node has an element.
   * 
   * @return {@code true} if this node has an element; {@code false} otherwise
   */
  public boolean hasElement();

  /**
   * Returns {@code true} if this node has the given element.
   * 
   * @param element
   *          an element whose presence in this node will be tested
   * @return {@code true} if this node has the given element; {@code false} otherwise
   */
  public boolean hasElement(E element);

  /**
   * Returns {@code true} if this node is labeled.
   * 
   * @return {@code true} if this node is labeled; {@code false} otherwise
   */
  public boolean isLabeled();

  /**
   * Returns {@code true} if this tree is a labeled tree. A labeled tree is defined as a tree such
   * that each node has a symbol assigned from a fixed finite alphabet.
   * 
   * @param alphabet
   *          a set of elements to be compared with elements in this tree
   * @return {@code true} if this tree is a labeled tree; {@code false} otherwise
   */
  public boolean isLabeledTree(Set<E> alphabet);

  /**
   * Returns the yield of this tree as a list of elements of leaves.
   * 
   * @return the yield of this tree
   */
  public List<E> yield();

  /**
   * Returns {@code true} if this tree is balanced. A balanced tree is a tree such that all leaf
   * nodes of the tree are at the same depth.
   * 
   * @return {@code true} if this tree is balanced; {@code false} otherwise
   */
  public boolean isBalanced();

}
