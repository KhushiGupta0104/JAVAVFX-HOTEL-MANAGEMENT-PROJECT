package com.hotel.model;

public class LuxuryRoom extends Room {

    private static final long serialVersionUID = 1L;

    public LuxuryRoom(int roomNumber) {
        super(roomNumber, 7000, "Luxury");
    }

    @Override
    public double calculatePrice(double occupancyRate, boolean isWeekend) {
        double price = basePrice * (1 + occupancyRate);
        if (isWeekend) price = price + 1500;
        return price;
    }
}
