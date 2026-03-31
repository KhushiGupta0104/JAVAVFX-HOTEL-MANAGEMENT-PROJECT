package com.hotel.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServiceRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum ServiceType {
        HOUSEKEEPING("🧹 Housekeeping"),
        LAUNDRY("👕 Laundry"),
        ROOM_SERVICE("🍽 Room Service"),
        SPA("💆 Spa & Wellness"),
        EXTRA_TOWELS("🛁 Extra Towels/Amenities"),
        MAINTENANCE("🔧 Maintenance");

        private final String label;
        ServiceType(String label) { this.label = label; }
        public String getLabel() { return label; }
        @Override public String toString() { return label; }
    }

    public enum Priority {
        LOW("Low"), NORMAL("Normal"), URGENT("Urgent");
        private final String label;
        Priority(String label) { this.label = label; }
        @Override public String toString() { return label; }
    }

    public enum Status {
        PENDING("⏳ Pending"),
        IN_PROGRESS("🔄 In Progress"),
        COMPLETED("✅ Completed");

        private final String label;
        Status(String label) { this.label = label; }
        public String getLabel() { return label; }
        @Override public String toString() { return label; }
    }

    private static int counter = 1;

    private final int           requestId;
    private final int           roomNumber;
    private final ServiceType   serviceType;
    private final Priority      priority;
    private final String        notes;
    private Status              status;
    private final LocalDateTime requestedAt;

    public ServiceRequest(int roomNumber, ServiceType serviceType, Priority priority, String notes) {
        this.requestId   = counter++;
        this.roomNumber  = roomNumber;
        this.serviceType = serviceType;
        this.priority    = priority;
        this.notes       = notes;
        this.status      = Status.PENDING;
        this.requestedAt = LocalDateTime.now();
    }

    public int         getRequestId()   { return requestId; }
    public int         getRoomNumber()  { return roomNumber; }
    public ServiceType getServiceType() { return serviceType; }
    public Priority    getPriority()    { return priority; }
    public String      getNotes()       { return notes; }
    public Status      getStatus()      { return status; }
    public void        setStatus(Status s) { this.status = s; }

    public String getRequestedAtFormatted() {
        return requestedAt.format(DateTimeFormatter.ofPattern("dd MMM, hh:mm a"));
    }

    @Override
    public String toString() {
        return "#" + requestId + " | Room " + roomNumber + " | " + serviceType.getLabel();
    }
}
