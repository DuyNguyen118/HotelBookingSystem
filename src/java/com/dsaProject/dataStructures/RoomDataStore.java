package com.dsaProject.dataStructures;

import java.io.*;
import java.util.*;

import com.dsaProject.hotelbooking.Room;

public class RoomDataStore {
    private List<Room> roomList;

    public RoomDataStore(String filePath) {
        roomList = new ArrayList<>();
        loadRoomsFromFile(filePath);
    }

    private void loadRoomsFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String roomNumber = parts[0].trim();
                    String roomType = parts[1].trim();
                    int price = Integer.parseInt(parts[2].trim());
                    double rating = Double.parseDouble(parts[3].trim());
                    String status = parts[4].trim();

                    Room room = new Room(roomNumber, roomType, price, rating, status);
                    roomList.add(room);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading room data: " + e.getMessage());
        }
    }

    public List<Room> getRoomList() {
        return roomList;
    }
    
    /**
     * Books a room by its room number if it's available.
     * @param roomNumber The room number to book
     * @return true if the room was successfully booked, false otherwise
     */
    public boolean bookRoom(String roomNumber) {
        for (Room room : roomList) {
            if (room.getRoomNumber().equals(roomNumber)) {
                if ("Available".equalsIgnoreCase(room.getStatus())) {
                    room.setStatus("Booked");
                    return true;
                }
                return false; // Room exists but is not available
            }
        }
        return false; // Room not found
    }
}
