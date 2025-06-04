package com.dsaProject.dataStructures;

public class ServiceQueue {
    private Node front;
    private Node rear;
    private String serviceType;
    private int size;
    
    public ServiceQueue(String serviceType) {
        this.serviceType = serviceType;
        this.front = null;
        this.rear = null;
        this.size = 0;
    }
    
    // Add a new service booking to the queue
    public void addBooking(ServiceBooking booking) {
        if (!booking.getServiceType().equals(serviceType)) {
            return;
        }
        
        Node newNode = new Node(booking);
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }
    
    // Process the next booking in the queue
    public ServiceBooking processNext() {
        if (front == null) {
            return null;
        }
        
        ServiceBooking booking = front.data;
        front = front.next;
        
        if (front == null) {
            rear = null;
        }
        
        size--;
        return booking;
    }
    
    // View the next booking without removing it
    public ServiceBooking viewNext() {
        if (front == null) {
            return null;
        }
        return front.data;
    }
    
    // Get all bookings in the queue as an array
    public ServiceBooking[] getAllBookings() {
        ServiceBooking[] bookings = new ServiceBooking[size];
        Node current = front;
        int index = 0;
        
        while (current != null) {
            bookings[index++] = current.data;
            current = current.next;
        }
        
        return bookings;
    }
    
    // Check if the queue is empty
    public boolean isEmpty() {
        return front == null;
    }
    
    // Get the size of the queue
    public int size() {
        return size;
    }
    
    public String getServiceType() {
        return serviceType;
    }
    
    // Remove a specific booking from the queue
    public boolean removeBooking(ServiceBooking booking) {
        if (front == null || booking == null) {
            System.out.println("Queue is empty or booking is null");
            return false;
        }

        System.out.println("Trying to remove booking: " + booking.getCustomerName() + 
                         " Room: " + booking.getRoomNumber() + 
                         " Service: " + booking.getServiceType());
        
        System.out.println("Current queue (" + serviceType + "):");
        Node current = front;
        int pos = 1;
        while (current != null) {
            ServiceBooking b = current.data;
            System.out.println(pos++ + ". " + b.getCustomerName() + 
                            " Room: " + b.getRoomNumber() + 
                            " Service: " + b.getServiceType());
            current = current.next;
        }
        
        // Reset current to front for the actual removal logic
        current = front;
        
        // If the booking is at the front
        if (booking.equals(front.data)) {
            System.out.println("Found booking at front, removing...");
            front = front.next;
            if (front == null) {
                rear = null;
            }
            size--;
            return true;
        }

        // Search for the booking in the rest of the queue
        while (current.next != null && !booking.equals(current.next.data)) {
            current = current.next;
        }

        // If booking was found, remove it
        if (current.next != null) {
            System.out.println("Found booking in the middle/end, removing...");
            current.next = current.next.next;
            if (current.next == null) {
                rear = current;
            }
            size--;
            return true;
        }

        System.out.println("Booking not found in the queue");
        return false; // Booking not found
    }
}