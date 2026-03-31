package com.hotel.model;

public class StandardRoom extends Room {

    private static final long serialVersionUID = 1L;

    public StandardRoom(int roomNumber) {
        super(roomNumber, 2000, "Standard");
    }

    @Override
    public double calculatePrice(double occupancyRate, boolean isWeekend) {
        double price = basePrice;
        if (occupancyRate > 0.7) price = price * 1.2;
        if (isWeekend)           price = price * 1.1;
        return price;
    }
}
