package net.junaraki.libtree;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class LibtreeTester extends TestCase {

  /**
   * Public constructor.
   * 
   * @param name
   */
  public LibtreeTester(String name) {
    super(name);
  }

  /**
   * Tests ordered trees.
   */
  public void testOrderedTrees() {
    // Test case 1
    // 1
    // |-- 2
    // |   |-- 4
    // |   |   `-- 5
    // |   |-- 6
    // |   |-- 11
    // |   `-- 12
    // `-- 3
    //     `-- 7
    OrderedTree<Integer> oTree1 = new OrderedTree<Integer>(1);
    oTree1.add(2);
    oTree1.addChild(3);
    OrderedTree<Integer> oNode2 = oTree1.getChildAt(0);
    oNode2.add(4);
    OrderedTree<Integer> oNode4 = oNode2.getChildAt(0);
    oNode4.add(5);
    oNode2.add(6);
    oNode2.add(11);
    oNode2.add(12);
    OrderedTree<Integer> oNode3 = oTree1.getChildAt(1);
    oNode3.add(7);
    oNode3.add(8);
    OrderedTree<Integer> oNode8 = oNode3.getChildAt(1);
    oNode8.add(9);
    oNode8.add(10);
    oNode3.removeChild(8);
    OrderedTree<Integer> oNode5 = oNode4.getChildAt(0);
    BinaryTree<Integer> bTree1 = oTree1.binarize();
    List<Integer> valuesPreOrder = oTree1.traversePreOrder();
    List<Integer> valuesPostOrder = oTree1.traversePostOrder();
    List<Integer> valuesLevelOrder = oTree1.traverseLevelOrder();

    // Output the string representation of the tree
    System.out.println("Ordered tree 1:");
    System.out.println(oTree1);

    // Output the string representation of the binarized tree
    System.out.println("Binarized ordered tree 1:");
    System.out.println(bTree1);

    // Test general methods
    assertTrue(oTree1.size() == 9);
    assertTrue(oTree1.depth() == 4);
    assertTrue(oTree1.depth(oTree1) == 0);
    assertTrue(oTree1.depth(oNode3) == 1);
    assertTrue(oTree1.depth(oNode5) == 3);
    assertTrue(oTree1.degree() == 4);
    assertTrue(oNode3.degree() == 1);
    assertTrue(oNode2.numChildren() == 4);

    assertTrue(oTree1.hasChild());
    assertFalse(oTree1.hasParent());
    assertTrue(oNode2.hasChild());
    assertTrue(oNode2.hasChild(4));
    assertFalse(oNode2.hasChild(5));
    assertTrue(oNode2.hasChild(oNode4));
    assertFalse(oNode2.hasChild(oNode5));
    assertTrue(oNode2.hasParent());
    assertTrue(oNode2.hasParent(oTree1));
    assertTrue(oTree1.numLeaves() == 5);
    assertTrue(oNode2.numSiblings() == 1);
    assertTrue(oNode5.numSiblings() == 0);
    assertTrue(oNode5.getParent().getElement() == 4);
    assertTrue(oNode5.getAncestors().size() == 3);
    assertFalse(oNode5.hasChild());
    assertFalse(oNode5.hasChild(3));
    assertFalse(oNode5.hasChild(oNode2));
    assertTrue(oNode5.isLeaf());
    assertFalse(oNode2.isLeaf());
    assertTrue(oTree1.isLabeled());
    assertFalse(oTree1.isBalanced());
    assertTrue(oTree1.subsumes(oNode3));
    assertFalse(oTree1.subsumes(oNode8));
    assertTrue(oNode4.equals(oTree1.find(4)));
    assertTrue(oTree1.findChild(4) == null);
    assertTrue(oNode5.hasAncestor(2));
    assertTrue(oNode5.hasAncestor(oNode2));
    assertFalse(oNode5.hasAncestor(3));
    assertFalse(oNode5.hasAncestor(oNode3));
    assertFalse(oNode5.hasAncestor(5));
    assertFalse(oNode5.hasAncestor(oNode5));
    assertTrue(oNode2.hasDescendant(5));
    assertTrue(oNode2.hasDescendant(oNode5));
    assertFalse(oNode3.hasDescendant(5));
    assertFalse(oNode3.hasDescendant(oNode5));
    assertFalse(oNode2.hasDescendant(2));
    assertFalse(oNode2.hasDescendant(oNode2));
    List<Integer> yield = oTree1.yield();
    assertTrue(yield.size() == 5);
    assertTrue(yield.get(0) == 5);
    assertTrue(yield.get(1) == 6);
    assertTrue(yield.get(2) == 11);
    assertTrue(yield.get(3) == 12);
    assertTrue(yield.get(4) == 7);

    assertTrue(bTree1.size() == oTree1.size());
    assertTrue(bTree1.depth() == 5);
    assertTrue(bTree1.depth(bTree1) == 0);
    assertTrue(bTree1.degree() == 2);
    assertTrue(bTree1.numChildren() == 2);
    yield = bTree1.yield();
    assertTrue(yield.size() == 3);
    assertTrue(yield.get(0) == 5);
    assertTrue(yield.get(1) == 12);
    assertTrue(yield.get(2) == 7);

    // Test traversal methods
    assertTrue(valuesPreOrder.size() == 9);
    assertTrue(valuesPreOrder.get(0) == 1);
    assertTrue(valuesPreOrder.get(1) == 2);
    assertTrue(valuesPreOrder.get(2) == 4);
    assertTrue(valuesPreOrder.get(3) == 5);
    assertTrue(valuesPreOrder.get(4) == 6);
    assertTrue(valuesPreOrder.get(5) == 11);
    assertTrue(valuesPreOrder.get(6) == 12);
    assertTrue(valuesPreOrder.get(7) == 3);
    assertTrue(valuesPreOrder.get(8) == 7);
    assertTrue(valuesPostOrder.size() == 9);
    assertTrue(valuesPostOrder.get(0) == 5);
    assertTrue(valuesPostOrder.get(1) == 4);
    assertTrue(valuesPostOrder.get(2) == 6);
    assertTrue(valuesPostOrder.get(3) == 11);
    assertTrue(valuesPostOrder.get(4) == 12);
    assertTrue(valuesPostOrder.get(5) == 2);
    assertTrue(valuesPostOrder.get(6) == 7);
    assertTrue(valuesPostOrder.get(7) == 3);
    assertTrue(valuesPostOrder.get(8) == 1);
    assertTrue(valuesLevelOrder.size() == 9);
    assertTrue(valuesLevelOrder.get(0) == 1);
    assertTrue(valuesLevelOrder.get(1) == 2);
    assertTrue(valuesLevelOrder.get(2) == 3);
    assertTrue(valuesLevelOrder.get(3) == 4);
    assertTrue(valuesLevelOrder.get(4) == 6);
    assertTrue(valuesLevelOrder.get(5) == 11);
    assertTrue(valuesLevelOrder.get(6) == 12);
    assertTrue(valuesLevelOrder.get(7) == 7);
    assertTrue(valuesLevelOrder.get(8) == 5);

    // Test case 2
    // a
    // |-- b
    // |   |-- d
    // |   |   `-- e
    // |   `-- f
    // `-- c
    //     `-- g
    OrderedTree<String> oTree2 = new OrderedTree<String>("a");
    oTree2.add("b");
    oTree2.addChild("c");
    OrderedTree<String> oNodeB = oTree2.getChildAt(0);
    oNodeB.add("d");
    OrderedTree<String> oNodeD = oNodeB.getChildAt(0);
    oNodeD.add("e");
    oNodeB.add("f");
    OrderedTree<String> oNodeC = oTree2.getChildAt(1);
    oNodeC.add("g");
    oNodeC.add("h");
    OrderedTree<String> oNodeH = oNodeC.getChildAt(1);
    oNodeH.add("i");
    oNodeH.add("j");
    oNodeC.removeChildAt(1);
    oNodeB.add("k");
    OrderedTree<String> oNodeK = oNodeB.getChildAt(2);
    oNodeK.remove();
    OrderedTree<String> oNodeE = oNodeD.getChildAt(0);

    // Output the string representation of the tree
    System.out.println("Ordered tree 2:");
    System.out.println(oTree2);

    // Test general methods
    assertTrue(oTree2.size() == 7);
    assertTrue(oTree2.depth() == 4);
    assertTrue(oTree2.depth(oTree2) == 0);
    assertTrue(oTree2.depth(oNodeC) == 1);
    assertTrue(oTree2.depth(oNodeE) == 3);
    assertTrue(oTree2.degree() == 2);
    assertTrue(oNodeC.degree() == 1);
    assertTrue(oNodeB.numChildren() == 2);

    assertTrue(oTree2.hasChild());
    assertFalse(oTree2.hasParent());
    assertTrue(oNodeB.hasChild());
    assertTrue(oNodeB.hasChild("d"));
    assertFalse(oNodeB.hasChild("e"));
    assertTrue(oNodeB.hasChild(oNodeD));
    assertFalse(oNodeB.hasChild(oNodeE));
    assertTrue(oNodeB.hasParent());
    assertTrue(oNodeB.hasParent(oTree2));
    assertTrue(oTree2.numLeaves() == 3);
    assertTrue(oNodeB.numSiblings() == 1);
    assertTrue(oNodeE.numSiblings() == 0);
    assertTrue(oNodeE.getParent().getElement().equals("d"));
    assertTrue(oNodeE.getAncestors().size() == 3);
    assertFalse(oNodeE.hasChild());
    assertFalse(oNodeE.hasChild("c"));
    assertFalse(oNodeE.hasChild(oNodeB));
    assertTrue(oNodeE.isLeaf());
    assertFalse(oNodeB.isLeaf());
    assertTrue(oTree2.isLabeled());
    assertFalse(oTree2.isBalanced());
    assertTrue(oTree2.subsumes(oNodeC));
    assertFalse(oTree2.subsumes(oNodeH));
    assertTrue(oNodeD.equals(oTree2.find("d")));
    assertTrue(oTree2.findChild("d") == null);
    assertTrue(oNodeE.hasAncestor("b"));
    assertTrue(oNodeE.hasAncestor(oNodeB));
    assertFalse(oNodeE.hasAncestor("c"));
    assertFalse(oNodeE.hasAncestor(oNodeC));
    assertTrue(oNodeB.hasDescendant("e"));
    assertTrue(oNodeB.hasDescendant(oNodeE));
    assertFalse(oNodeC.hasDescendant("e"));
    assertFalse(oNodeC.hasDescendant(oNodeE));
    assertTrue(oNodeE.getRoot().equals(oTree2));
    assertTrue(oNodeE.getRoot().getElement().equals("a"));
    assertTrue(oTree2.findChild("d") == null);
    assertTrue(oNodeB.numChildren() == 2);
    assertTrue(oTree2.isFirstChild(oNodeB));
    assertFalse(oTree2.isLastChild(oNodeB));
    assertTrue(oTree2.isLastChild(oNodeC));
    assertFalse(oTree2.isFirstChild(oNodeC));
    List<String> yield2 = oTree2.yield();
    assertTrue(yield2.size() == 3);
    assertTrue(yield2.get(0).equals("e"));
    assertTrue(yield2.get(1).equals("f"));
    assertTrue(yield2.get(2).equals("g"));
  }

  /**
   * Tests unordered trees.
   */
  public void testUnorderedTrees() {
    // Test case 1
    // 1
    // |-- 2
    // |   |-- 4
    // |   |   `-- 5
    // |   `-- 6
    // `-- 3
    //     `-- 7
    UnorderedTree<Integer> uTree = new UnorderedTree<Integer>(1);
    uTree.add(2);
    uTree.addChild(3);
    UnorderedTree<Integer> uNode2 = uTree.findChild(2);
    uNode2.add(4);
    UnorderedTree<Integer> uNode4 = uNode2.findChild(4);
    uNode4.add(5);
    uNode2.add(6);
    UnorderedTree<Integer> uNode3 = uTree.findChild(3);
    uNode3.add(7);
    uNode3.add(8);
    UnorderedTree<Integer> uNode8 = uNode3.findChild(8);
    uNode8.add(9);
    uNode8.add(10);
    uNode3.removeChild(8);
    UnorderedTree<Integer> uNode5 = uNode4.findChild(5);

    // Output the string representation of the tree
    System.out.println("Unordered tree:");
    System.out.println(uTree);

    // Test general methods
    assertTrue(uTree.size() == 7);
    assertTrue(uTree.depth() == 4);
    assertTrue(uTree.depth(uTree) == 0);
    assertTrue(uTree.depth(uNode3) == 1);
    assertTrue(uTree.depth(uNode5) == 3);
    assertTrue(uTree.degree() == 2);
    assertTrue(uNode3.degree() == 1);
    assertTrue(uNode2.numChildren() == 2);

    assertTrue(uTree.hasChild());
    assertFalse(uTree.hasParent());
    assertTrue(uNode2.hasChild());
    assertTrue(uNode2.hasChild(4));
    assertFalse(uNode2.hasChild(5));
    assertTrue(uNode2.hasChild(uNode4));
    assertFalse(uNode2.hasChild(uNode5));
    assertTrue(uNode2.hasParent());
    assertTrue(uNode2.hasParent(uTree));
    assertTrue(uTree.numLeaves() == 3);
    assertTrue(uNode2.numSiblings() == 1);
    assertTrue(uNode5.numSiblings() == 0);
    assertTrue(uNode5.getParent().getElement() == 4);
    assertTrue(uNode5.getAncestors().size() == 3);
    assertFalse(uNode5.hasChild());
    assertFalse(uNode5.hasChild(3));
    assertFalse(uNode5.hasChild(uNode2));
    assertTrue(uNode5.isLeaf());
    assertFalse(uNode2.isLeaf());
    assertTrue(uTree.isLabeled());
    assertFalse(uTree.isBalanced());
    assertTrue(uNode4.equals(uTree.find(4)));
    assertTrue(uTree.findChild(4) == null);
    assertTrue(uNode5.hasAncestor(2));
    assertTrue(uNode5.hasAncestor(uNode2));
    assertFalse(uNode5.hasAncestor(3));
    assertFalse(uNode5.hasAncestor(uNode3));
    assertTrue(uNode2.hasDescendant(5));
    assertTrue(uNode2.hasDescendant(uNode5));
    assertFalse(uNode3.hasDescendant(5));
    assertFalse(uNode3.hasDescendant(uNode5));
  }

  /**
   * Tests binary trees.
   */
  public void testBinaryTrees() {
    // Test case 1
    // 1
    // |-- 2
    // |   |-- 4
    // |   |   `-- 5
    // |   `-- 6
    // `-- 3
    //     `-- 7
    BinaryTree<Integer> bTree1 = new BinaryTree<Integer>(1);
    bTree1.add(2);
    bTree1.addChild(3);
    BinaryTree<Integer> bNode2 = bTree1.getChildAt(0);
    bNode2.add(4);
    BinaryTree<Integer> bNode4 = bNode2.getLeftChild();
    bNode4.add(5);
    bNode2.add(6);
    BinaryTree<Integer> bNode3 = bTree1.findChild(3);
    bNode3.add(7);
    bNode3.add(8);
    BinaryTree<Integer> bNode8 = bNode3.getRightChild();
    bNode8.add(9);
    bNode8.add(10);
    bNode3.removeChild(8);
    BinaryTree<Integer> bNode5 = bNode4.getLeftChild();
    bNode4.setRightChild(11);
    bNode4.removeRightChild();
    BinaryTree<Integer> bNode7 = bNode3.getLeftChild();
    bNode7.setRightChild(12);
    bNode7.removeLeftChild();

    // Output the string representation of the tree
    System.out.println("Binary tree 1:");
    System.out.println(bTree1);

    // Test general methods
    assertTrue(bTree1.size() == 8);
    assertTrue(bTree1.depth() == 4);
    assertTrue(bTree1.depth(bTree1) == 0);
    assertTrue(bTree1.depth(bNode3) == 1);
    assertTrue(bTree1.depth(bNode5) == 3);
    assertTrue(bTree1.degree() == 2);
    assertTrue(bNode3.degree() == 1);
    assertTrue(bNode2.numChildren() == 2);

    assertTrue(bTree1.hasChild());
    assertFalse(bTree1.hasParent());
    assertTrue(bNode2.hasChild());
    assertTrue(bNode2.hasChild(4));
    assertTrue(bNode2.hasLeftChild(4));
    assertFalse(bNode2.hasRightChild(4));
    assertFalse(bNode2.hasChild(5));
    assertTrue(bNode2.hasChild(bNode4));
    assertFalse(bNode2.hasChild(bNode5));
    assertTrue(bNode2.hasParent());
    assertTrue(bNode2.hasParent(bTree1));
    assertTrue(bTree1.numLeaves() == 3);
    assertTrue(bNode2.numSiblings() == 1);
    assertTrue(bNode5.numSiblings() == 0);
    assertTrue(bNode5.getParent().getElement() == 4);
    assertTrue(bNode5.getAncestors().size() == 3);
    assertFalse(bNode5.hasChild());
    assertFalse(bNode5.hasChild(3));
    assertFalse(bNode5.hasChild(bNode2));
    assertTrue(bNode5.isLeaf());
    assertFalse(bNode2.isLeaf());
    assertTrue(bTree1.isLabeled());
    assertFalse(bTree1.isBalanced());
    assertTrue(bTree1.subsumes(bNode3));
    assertFalse(bTree1.subsumes(bNode8));
    assertTrue(bNode4.equals(bTree1.find(4)));
    assertTrue(bTree1.findChild(4) == null);
    assertTrue(bNode5.hasAncestor(2));
    assertTrue(bNode5.hasAncestor(bNode2));
    assertFalse(bNode5.hasAncestor(3));
    assertFalse(bNode5.hasAncestor(bNode3));
    assertTrue(bNode2.hasDescendant(5));
    assertTrue(bNode2.hasDescendant(bNode5));
    assertFalse(bNode3.hasDescendant(5));
    assertFalse(bNode3.hasDescendant(bNode5));
    assertTrue(bTree1.getDeepestLeftLeaf().getElement() == 5);
    assertTrue(bTree1.getDeepestRightLeaf().getElement() == 3);

    // Test case 2
    // a
    // |-- b
    // |   |-- h
    // |   `-- d
    // |       |-- e
    // |       `-- g
    // `-- c
    //     |-- i
    //     `-- j
    BinaryTree<String> bTree2 = new BinaryTree<String>("a");
    bTree2.add("b");
    bTree2.addChild("c");
    BinaryTree<String> bTree3 = new BinaryTree<String>("d");
    bTree3.add("e");
    bTree3.addChild("f");
    bTree2.getLeftChild().setRightChild(bTree3);
    BinaryTree<String> bTree4 = new BinaryTree<String>("g");
    bTree3.setRightChild(bTree4);
    BinaryTree<String> bTree5 = new BinaryTree<String>("h");
    bTree2.getLeftChild().setLeftChild(bTree5);
    BinaryTree<String> bTree6 = new BinaryTree<String>("j");
    bTree2.getRightChild().setLeftChild("i");
    bTree2.getRightChild().setRightChild(bTree6);

    // Output the string representation of the tree
    System.out.println("Binary tree 2:");
    System.out.println(bTree2);

    // Test general methods
    assertTrue(bTree2.size() == 9);
    assertTrue(bTree2.degree() == 2);
    assertTrue(bTree2.numSiblings() == 0);
    assertFalse(bTree2.getLeftChild().getLeftChild() == null);
    assertTrue(bTree2.getLeftChild().getLeftChild().getElement().equals("h"));
    assertTrue(bTree3.numSiblings() == 1);
    assertTrue(bTree3.degree(bTree2.getLeftChild()) == 2);
    assertTrue(bTree3.getParent().getElement().equals("b"));
    assertTrue(bTree3.getRightChild().getElement().equals("g"));
    assertTrue(bTree2.getRightChild().getLeftChild().getElement().equals("i"));
    assertTrue(bTree2.getRightChild().getRightChild().getElement().equals("j"));
    assertTrue(bTree2.getDeepestLeftLeaf().getElement().equals("h"));
    assertTrue(bTree2.getDeepestRightLeaf().getElement().equals("j"));
  }

  public static Test suite() {
    TestSuite suite = new TestSuite("Junit tests for an ordered and unordered tree.");

    suite.addTest(new LibtreeTester("testOrderedTrees"));
    suite.addTest(new LibtreeTester("testUnorderedTrees"));
    suite.addTest(new LibtreeTester("testBinaryTrees"));

    return suite;
  }

}
