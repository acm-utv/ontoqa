package com.acmutv.ontoqa.treePrinter;

public class TreePrinterTest {
	private static Node<String> test1() {
        Node<String> root = new Node<String>("a");
        Node<String> n11 = new Node<String>("b");
        Node<String> n12 = new Node<String>("c");
        Node<String> n21 = new Node<String>("d");
        Node<String> n22 = new Node<String>("e");
        Node<String> n23 = new Node<String>("f");
        Node<String> n24 = new Node<String>("g");
        Node<String> n31 = new Node<String>("h");
        Node<String> n32 = new Node<String>("i");
        Node<String> n33 = new Node<String>("l");
        Node<String> n34 = new Node<String>("m");
        Node<String> n35 = new Node<String>("n");
        Node<String> n36 = new Node<String>("o");
        Node<String> n37 = new Node<String>("p");
        Node<String> n38 = new Node<String>("q");

        root.left = n11;
        root.right = n12;

        n11.left = n21;
        n11.right = n22;
        n12.left = n23;
        n12.right = n24;

        n21.left = n31;
        n21.right = n32;
        n22.left = n33;
        n22.right = n34;
        n23.left = n35;
        n23.right = n36;
        n24.left = n37;
        n24.right = n38;

        return root;
    }

    private static Node<Integer> test2() {
        Node<Integer> root = new Node<Integer>(2);
        Node<Integer> n11 = new Node<Integer>(7);
        Node<Integer> n12 = new Node<Integer>(5);
        Node<Integer> n21 = new Node<Integer>(2);
        Node<Integer> n22 = new Node<Integer>(6);
        Node<Integer> n23 = new Node<Integer>(9);
        Node<Integer> n31 = new Node<Integer>(5);
        Node<Integer> n32 = new Node<Integer>(8);
        Node<Integer> n33 = new Node<Integer>(4);

        root.left = n11;
        root.right = n12;

        n11.left = n21;
        n11.right = n22;

        n12.right = n23;
        n22.left = n31;
        n22.right = n32;

        n23.left = n33;

        return root;
    }

    public static void main(String[] args) {

        TreePrinter.printNode(test1());
        TreePrinter.printNode(test2());

    }
}
