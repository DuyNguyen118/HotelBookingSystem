package com.dsaProject.dataStructures;

import com.dsaProject.hotelbooking.Room;
import java.util.List;

public class RoomSortService {
    
    public static void sortByRoomNumber(List<Room> rooms, boolean ascending) {
        if (rooms == null || rooms.size() <= 1) return;
        mergeSort(rooms, 0, rooms.size() - 1, (r1, r2) -> {
            int compare = r1.getRoomNumber().compareTo(r2.getRoomNumber());
            return ascending ? compare : -compare;
        });
    }
    
    public static void sortByPrice(List<Room> rooms, boolean ascending) {
        if (rooms == null || rooms.size() <= 1) return;
        mergeSort(rooms, 0, rooms.size() - 1, (r1, r2) -> 
            ascending ? Integer.compare(r1.getPrice(), r2.getPrice()) 
                     : Integer.compare(r2.getPrice(), r1.getPrice())
        );
    }
    
    public static void sortByRating(List<Room> rooms, boolean descending) {
        if (rooms == null || rooms.size() <= 1) return;
        mergeSort(rooms, 0, rooms.size() - 1, (r1, r2) -> 
            descending ? Double.compare(r2.getRating(), r1.getRating()) 
                      : Double.compare(r1.getRating(), r2.getRating())
        );
    }
    
    @FunctionalInterface
    private interface RoomComparator {
        int compare(Room r1, Room r2);
    }
    
    private static void mergeSort(List<Room> rooms, int left, int right, RoomComparator comparator) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            
            mergeSort(rooms, left, mid, comparator);
            mergeSort(rooms, mid + 1, right, comparator);
            
            merge(rooms, left, mid, right, comparator);
        }
    }
    
    private static void merge(List<Room> rooms, int left, int mid, int right, RoomComparator comparator) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        
        Room[] leftArray = new Room[n1];
        Room[] rightArray = new Room[n2];
        
        for (int i = 0; i < n1; i++) {
            leftArray[i] = rooms.get(left + i);
        }
        for (int j = 0; j < n2; j++) {
            rightArray[j] = rooms.get(mid + 1 + j);
        }
        
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (comparator.compare(leftArray[i], rightArray[j]) <= 0) {
                rooms.set(k, leftArray[i]);
                i++;
            } else {
                rooms.set(k, rightArray[j]);
                j++;
            }
            k++;
        }
        
        while (i < n1) {
            rooms.set(k, leftArray[i]);
            i++;
            k++;
        }
        
        while (j < n2) {
            rooms.set(k, rightArray[j]);
            j++;
            k++;
        }
    }
}
