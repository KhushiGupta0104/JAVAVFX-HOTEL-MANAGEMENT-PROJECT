package com.hotel.model;

public class DeluxeRoom extends Room {

    private static final long serialVersionUID = 1L;

    public DeluxeRoom(int roomNumber) {
        super(roomNumber, 4000, "Deluxe");
    }

    @Override
    public double calculatePrice(double occupancyRate, boolean isWeekend) {
        double price = basePrice + (occupancyRate * 1000);
        if (isWeekend) price = price + 500;
        return price;
    }
}
