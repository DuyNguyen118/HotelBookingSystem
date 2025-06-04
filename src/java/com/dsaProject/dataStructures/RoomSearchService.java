package com.dsaProject.dataStructures;

import com.dsaProject.hotelbooking.Room;
import java.util.ArrayList;
import java.util.List;

public class RoomSearchService {
    
    public static List<Room> searchRooms(List<Room> rooms, String type, String status, 
                                       int minPrice, int maxPrice) {
        List<Room> results = new ArrayList<>();

        for (Room room : rooms) {
            boolean typeMatches = type == null || 
                                room.getRoomType().toLowerCase().contains(type.toLowerCase());
            boolean statusMatches = status == null || 
                                  room.getStatus().equalsIgnoreCase(status);
            boolean priceInRange = (minPrice == -1 || room.getPrice() >= minPrice) &&
                                 (maxPrice == -1 || room.getPrice() <= maxPrice);

            if (typeMatches && statusMatches && priceInRange) {
                results.add(room);
            }
        }

        return results;
    }
}