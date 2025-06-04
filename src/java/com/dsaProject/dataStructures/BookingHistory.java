package com.dsaProject.dataStructures;

import java.util.ArrayList;
import java.util.List;

import com.dsaProject.hotelbooking.Room;

public class BookingHistory {
    private static BookingHistory instance;
    private final ServiceBookingStack serviceBookings;
    private final RoomBookingStack roomBookings;
    private final RoomDataStore roomDataStore;

    // Update constructor to accept RoomDataStore
    private BookingHistory(RoomDataStore roomDataStore) {
        this.roomDataStore = roomDataStore;
        this.serviceBookings = new ServiceBookingStack();
        this.roomBookings = new RoomBookingStack();
    }

    // Update getInstance to require RoomDataStore
    public static BookingHistory getInstance(RoomDataStore roomDataStore) {
        if (instance == null) {
            instance = new BookingHistory(roomDataStore);
        }
        return instance;
    }
    
    // Service Booking Methods
    public void addServiceBooking(ServiceBooking booking) {
        serviceBookings.push(booking);
    }
    
    public ServiceBooking undoLastServiceBooking() {
        if (!serviceBookings.isEmpty()) {
            return serviceBookings.pop();
        }
        return null;
    }
    
    public List<ServiceBooking> getServiceHistory() {
        return serviceBookings.toList();
    }
    
    // Room Booking Methods
    public void addRoomBooking(RoomBooking booking) {
        roomBookings.push(booking);
    }
    
    public RoomBooking undoLastRoomBooking() {
        if (!roomBookings.isEmpty()) {
            return roomBookings.pop();
        }
        return null;
    }
    
    public List<RoomBooking> getRoomHistory() {
        return roomBookings.toList();
    }
    
    // Generate combined bill for a room and its services
    public String generateBill(String roomNumber) {
        StringBuilder bill = new StringBuilder();
        bill.append("\n=== HOTEL GRAND VIEW ===\n");
        bill.append("\n--- Booking Receipt ---\n");
        
        // Find room booking
        RoomBooking roomBooking = findRoomBooking(roomNumber);
        if (roomBooking == null) {
            return "No booking found for room " + roomNumber;
        }
        
        // Get the room to access its actual price
        Room room = null;
        for (Room r : roomDataStore.getRoomList()) {
            if (r.getRoomNumber().equals(roomNumber)) {
                room = r;
                break;
            }
        }
        
        if (room == null) {
            return "Room details not found for " + roomNumber;
        }
        
        // Room booking details
        bill.append("\nRoom Booking:\n");
        bill.append("------------\n");
        bill.append(String.format("Room: %s (%s)\n", room.getRoomNumber(), room.getRoomType()));
        bill.append(String.format("Guest: %s\n", roomBooking.getCustomerName()));
        bill.append(String.format("Check-in: %s\n", roomBooking.getCheckInDate()));
        bill.append(String.format("Check-out: %s\n", roomBooking.getCheckOutDate()));
        bill.append(String.format("Rate per night: $%d\n", room.getPrice()));
        
        // Calculate room charge using actual room price
        long nights = roomBooking.getCheckInDate().until(roomBooking.getCheckOutDate()).getDays();
        double roomCharge = nights * room.getPrice();
        
        // Find service bookings
        List<ServiceBooking> serviceBookings = findServiceBookings(roomNumber);
        
        // Service booking details
        if (!serviceBookings.isEmpty()) {
            bill.append("\nServices:\n");
            bill.append("----------\n");
            
            for (ServiceBooking booking : serviceBookings) {
                String serviceType = booking.getServiceType();
                double servicePrice = serviceType.equals("Spa") ? 50.0 : 30.0;
                bill.append(String.format("%s at $%.2f\n", 
                    serviceType, servicePrice));
            }
        }
        
        // Calculate total service charge
        double serviceCharge = serviceBookings.stream()
            .mapToDouble(s -> s.getServiceType().equals("Spa") ? 50.0 : 30.0)
            .sum();
        
        double total = roomCharge + serviceCharge;
        
        // Add totals
        bill.append("\nCharges:\n");
        bill.append("--------\n");
        bill.append(String.format("Room Charge (%d nights @ $%d): $%.2f\n", 
            nights, room.getPrice(), roomCharge));
            
        if (serviceCharge > 0) {
            bill.append(String.format("Service Charges: $%.2f\n", serviceCharge));
        }
        
        bill.append("-------------------\n");
        bill.append(String.format("TOTAL: $%.2f\n", total));
        bill.append("===================\n");
        bill.append("Thank you for choosing Hotel Grand View!\n");
        
        return bill.toString();
    }
    
    private RoomBooking findRoomBooking(String roomNumber) {
        for (RoomBooking booking : roomBookings.toList()) {
            if (booking.getRoomNumber().equals(roomNumber)) {
                return booking;
            }
        }
        return null;
    }
    
    private List<ServiceBooking> findServiceBookings(String roomNumber) {
        List<ServiceBooking> result = new ArrayList<>();
        for (ServiceBooking booking : serviceBookings.toList()) {
            if (booking.getRoomNumber().equals(roomNumber)) {
                result.add(booking);
            }
        }
        return result;
    }
}
