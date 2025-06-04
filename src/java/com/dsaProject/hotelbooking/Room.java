package com.dsaProject.hotelbooking;

public class Room {
    private String roomNumber;
    private String roomType;
    private int price;
    private double rating;
    private String status;
    
    public Room(String roomNumber, String roomType, int price, double rating, String status) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        this.rating = rating;
        this.status = status;
    }
    
    public String getRoomNumber() { 
        return roomNumber; 
    }
    public String getRoomType() { 
        return roomType; 
    }
    public int getPrice() { 
        return price; 
    }
    public double getRating() { 
        return rating; 
    }
    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }
    
    @Override
    public String toString() {
        return String.format("%s | %s | $%d | Rating: %.1f | %s",
            roomNumber, roomType, price, rating, status);
    }
}