package com.dsaProject.dataStructures;

import java.util.ArrayList;
import java.util.List;

public class RoomBookingStack {
    private Node top;
    private int size;
    
    private class Node {
        RoomBooking data;
        Node next;
        
        public Node(RoomBooking data) {
            this.data = data;
            this.next = null;
        }
    }
    
    public RoomBookingStack() {
        top = null;
        size = 0;
    }
    
    public void push(RoomBooking item) {
        Node newNode = new Node(item);
        if (top == null) {
            top = newNode;
        } else {
            newNode.next = top;
            top = newNode;
        }
        size++;
    }
    
    public RoomBooking pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        RoomBooking item = top.data;
        top = top.next;
        size--;
        return item;
    }
    
    public RoomBooking peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return top.data;
    }
    
    public boolean isEmpty() {
        return top == null;
    }
    
    public int size() {
        return size;
    }
    
    public List<RoomBooking> toList() {
        List<RoomBooking> list = new ArrayList<>();
        Node current = top;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }
}
