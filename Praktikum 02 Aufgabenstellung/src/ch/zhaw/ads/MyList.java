package ch.zhaw.ads;

import java.util.AbstractList;

public class MyList extends AbstractList {
    ListNode head, tail;

    class ListNode {
        Object data;
        ListNode next, prev;
        ListNode (Object o) {
            data = o;
        }
    }

    public MyList() {
        setup();
    }

    private void setup() {
        head = new ListNode("head");
        tail = new ListNode("tail");
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public Object get(int index) {
        int i = 0;
        ListNode node = head.next;

        while (0 < index && i < index) {
            if(node.next != null) {
                node = node.next;
                i++;
            } else  {
                return null;
            }
        }
        return node.data;
    }

    @Override
    public int size() {
        int size = 0;
        ListNode node = head.next;
        while (node.next != null) {
            size ++;
            node = node.next;
        }
        return size;
    }

    public boolean add (Object o) {
        ListNode newNode = new ListNode(o);
        ListNode lastNode = tail.prev;

        newNode.prev = lastNode;
        newNode.next = tail;

        lastNode.next = newNode;
        tail.prev = newNode;

        return true;
    }

    public boolean add (Object o, int pos) {
        ListNode newNode = new ListNode(o);
        int i = 0;
        ListNode node = head.next;
        while (node.next != null) {
            if (i >= pos) {
                newNode.prev = node.prev;
                newNode.next = node;
                node.prev.next = newNode;
                node.prev = newNode;
                return true;
            } else {
                node = node.next;
                i++;
            }
        }
        add(o);
        return true;
    }

    public boolean remove(Object o) {
        ListNode node = head.next;
        ListNode lastnode = head;
        while (node.next != null) {
            if (node.data == o) {
                lastnode.next = node.next;
                node.next.prev = lastnode;
                break;
            } else {
                lastnode = node;
                node = node.next;
            }
        }
        return true;
    }

    public Object remove(int index) {
        int i = 0;
        ListNode node = head.next;
        ListNode lastnode = head;
        Object nodeData = null;
        while (node.next != null) {
            if (i == index) {
                nodeData = node.data;

                lastnode.next = node.next;
                node.next.prev = lastnode;
            } else {
                lastnode = node;
                node = node.next;
                i++;
            }
        }
        return nodeData;
    }

    public void clear() {
        setup();
    }

}