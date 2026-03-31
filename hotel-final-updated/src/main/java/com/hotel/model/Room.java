package com.hotel.model;

import java.io.Serializable;

public abstract class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    protected int roomNumber;
    protected double basePrice;
    protected boolean isAvailable;
    protected String type;

    public Room(int roomNumber, double basePrice, String type) {
        this.roomNumber  = roomNumber;
        this.basePrice   = basePrice;
        this.isAvailable = true;
        this.type        = type;
    }

    public abstract double calculatePrice(double occupancyRate, boolean isWeekend);

    public int getRoomNumber()          { return roomNumber; }
    public boolean isAvailable()        { return isAvailable; }
    public void setAvailable(boolean s) { this.isAvailable = s; }
    public double getBasePrice()        { return basePrice; }
    public double getPricePerDay()      { return basePrice; }
    public String getType()             { return type; }

    @Override
    public String toString() {
        String availability = isAvailable ? "Available" : "Booked";
        return "Room " + roomNumber + " (" + type + ") - ₹" + basePrice + "/day - " + availability;
    }
}
