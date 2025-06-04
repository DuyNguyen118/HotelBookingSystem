package com.dsaProject.dataStructures;

import java.util.ArrayList;
import java.util.List;

public class ServiceBookingStack {
    private Node top;
    private int size;
    
    public ServiceBookingStack() {
        top = null;
        size = 0;
    }
    
    public void push(ServiceBooking item) {
        Node newNode = new Node(item);
        if (top == null) {
            top = newNode;
        } else {
            newNode.next = top;
            top = newNode;
        }
        size++;
    }
    
    public ServiceBooking pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        ServiceBooking item = top.data;
        top = top.next;
        size--;
        return item;
    }
    
    public ServiceBooking peek() {
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
    
    public List<ServiceBooking> toList() {
        List<ServiceBooking> list = new ArrayList<>();
        Node current = top;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }
}
