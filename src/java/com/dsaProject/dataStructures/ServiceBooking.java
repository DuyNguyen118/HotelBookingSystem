package com.dsaProject.dataStructures;

public class ServiceBooking {
    private String customerName;
    private String serviceType;
    private String roomNumber;
    private boolean processed;
    
    public ServiceBooking(String customerName, String roomNumber, String serviceType) {
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.serviceType = serviceType;
        this.processed = false;
    }
    
    // Getters and Setters
    public String getCustomerName() {
        return customerName;
    }
    
    public String getServiceType() {
        return serviceType;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }
    
    public boolean isProcessed() {
        return processed;
    }
    
    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
    
    public String[] toTableRow() {
        return new String[]{
            this.roomNumber,
            this.customerName,
            this.serviceType,
            this.processed ? "Processed" : "Pending"
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        ServiceBooking that = (ServiceBooking) o;
        
        // Compare customerName
        if (customerName == null) {
            if (that.customerName != null) return false;
        } else if (!customerName.equals(that.customerName)) {
            return false;
        }
        
        // Compare serviceType
        if (serviceType == null) {
            if (that.serviceType != null) return false;
        } else if (!serviceType.equals(that.serviceType)) {
            return false;
        }
        
        // Compare roomNumber
        if (roomNumber == null) {
            if (that.roomNumber != null) return false;
        } else if (!roomNumber.equals(that.roomNumber)) {
            return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        int result = customerName != null ? customerName.hashCode() : 0;
        result = 31 * result + (serviceType != null ? serviceType.hashCode() : 0);
        result = 31 * result + (roomNumber != null ? roomNumber.hashCode() : 0);
        return result;
    }
}