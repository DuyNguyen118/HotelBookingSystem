package com.dsaProject.dataStructures;

public class Node {
    ServiceBooking data;
    Node next;
    
    public Node(ServiceBooking data) {
        this.data = data;
        this.next = null;
    }
}