package com.hotel.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Booking implements Serializable {

    private static final long serialVersionUID = 2L;

    private String    bookingId;
    private Customer  customer;
    private Room      room;
    private int       days;
    private LocalDate bookingDate;
    private double    totalBill;
    private boolean   cancelled;
    private LocalDate cancelledDate;

    public Booking(Customer customer, Room room, int days) {
        this.bookingId   = "BK" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        this.customer    = customer;
        this.room        = room;
        this.days        = days;
        this.bookingDate = LocalDate.now();
        this.totalBill   = calculateBill();
        this.cancelled   = false;
    }

    public double calculateBill() {
        double subtotal = room.getPricePerDay() * days;
        double tax      = subtotal * 0.18;
        return subtotal + tax;
    }

    public double getSubtotal()        { return room.getPricePerDay() * days; }
    public double getTax()             { return getSubtotal() * 0.18; }
    public Customer getCustomer()      { return customer; }
    public Room getRoom()              { return room; }
    public int getDays()               { return days; }
    public LocalDate getBookingDate()  { return bookingDate; }
    public double getTotalBill()       { return getSubtotal() + getTax(); }
    public void setRoom(Room room)     { this.room = room; }
    public String getBookingId()       { return bookingId != null ? bookingId : "N/A"; }
    public boolean isCancelled()       { return cancelled; }
    public LocalDate getCancelledDate(){ return cancelledDate; }

    public void cancel() {
        this.cancelled     = true;
        this.cancelledDate = LocalDate.now();
    }

    public String getBookingDateFormatted() {
        if (bookingDate == null) return "N/A";
        return bookingDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    public String getStatusDisplay() {
        return cancelled ? "❌ Cancelled" : "✅ Active";
    }

    @Override
    public String toString() {
        return customer.getName() + " | Room " + room.getRoomNumber()
                + " | " + days + " days | ₹" + String.format("%.2f", totalBill);
    }
}
