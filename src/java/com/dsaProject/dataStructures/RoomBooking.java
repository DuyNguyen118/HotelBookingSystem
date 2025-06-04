package com.dsaProject.dataStructures;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RoomBooking {
    private String roomNumber;
    private String customerName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double roomRate;
    private boolean isCancelled;

    public RoomBooking(String roomNumber, String customerName, LocalDate checkInDate, 
                     LocalDate checkOutDate, double roomRate) {
        this.roomNumber = roomNumber;
        this.customerName = customerName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomRate = roomRate;
        this.isCancelled = false;
    }

    // Getters and Setters
    public String getRoomNumber() {
        return roomNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public double getRoomRate() {
        return roomRate;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        this.isCancelled = true;
    }


    // Calculate total charge for the booking
    public double getTotalCharge() {
        if (isCancelled) return 0.0;
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return nights * roomRate;
    }

    @Override
    public String toString() {
        return String.format("Room %s - %s (%s to %s) - $%.2f",
                roomNumber, customerName, checkInDate, checkOutDate, getTotalCharge());
    }
}
