package ch.zhaw.ads;

import java.util.LinkedList;
import java.util.Queue;

public class TreeTraversal<T extends Comparable<T>> implements Traversal<T> {
    private final TreeNode<T> root;

    public TreeTraversal(TreeNode<T> root) {
        this.root = root;
    }

    public void inorder(Visitor<T> vis) {
        inorderNode(vis, root);
    }

    private void inorderNode(Visitor<T> vis, TreeNode<T> node) {
        if (node != null) {
            inorderNode(vis, node.left);
            vis.visit(node.getValue());
            inorderNode(vis, node.right);
        }
    }

    public void preorder(Visitor<T> vis) {
        preorderNode(vis, root);
    }

    private void preorderNode(Visitor<T> vis, TreeNode<T> node) {
        if (node != null) {
            vis.visit(node.getValue());
            preorderNode(vis, node.left);
            preorderNode(vis, node.right);
        }
    }

    public void postorder(Visitor<T> vis) {
        postorderNode(vis, root);
    }

    private void postorderNode(Visitor<T> vis, TreeNode<T> node) {
        if (node != null) {
            postorderNode(vis, node.left);
            postorderNode(vis, node.right);
            vis.visit(node.getValue());
        }
    }

    @Override
    public void levelorder(Visitor<T> vistor) {
        levelorderNode(vistor, root);
    }

    private void levelorderNode(Visitor<T> vis, TreeNode<T> node) {
        Queue<TreeNode<T>> q = new LinkedList<>();
        if (node != null) {
            q.add(node);
        }
        while (!q.isEmpty()) {
            node = q.poll();
            vis.visit(node.getValue());
            if (node.left != null) {q.add(node.left);}
            if (node.right != null) {q.add(node.right);}
        }
    }

    @Override
    public void interval(T min, T max, Visitor<T> vistor) {
        intervalNode(vistor, root, min, max);

    }

    public void intervalNode(Visitor<T> vistor, TreeNode<T> node, T min, T max) {
        if (node != null) {
            if (node.getValue().compareTo(min) >= 0 && node.getValue().compareTo(max) <= 0) {
                vistor.visit(node.getValue());
            }
            if (node.left != null  && !(node.getValue().compareTo(min) < 0 && node.left.getValue().compareTo(min) < 0)){
                intervalNode(vistor, node.left, min, max);
            }

            if (node.right != null  && !(node.getValue().compareTo(max) > 0 && node.right.getValue().compareTo(max) > 0)){
                intervalNode(vistor, node.right, min, max);
            }
        }
    }
}
