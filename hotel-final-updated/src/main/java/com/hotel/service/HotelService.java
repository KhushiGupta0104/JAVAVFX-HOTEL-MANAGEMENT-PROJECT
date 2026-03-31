package com.hotel.service;

import java.util.List;
import java.util.stream.Collectors;

import com.hotel.model.Booking;
import com.hotel.model.Customer;
import com.hotel.model.DeluxeRoom;
import com.hotel.model.LuxuryRoom;
import com.hotel.model.Room;
import com.hotel.model.ServiceRequest;
import com.hotel.model.ServiceRequest.Status;
import com.hotel.model.StandardRoom;
import com.hotel.util.FileUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HotelService {

    private ObservableList<Room>           rooms    = FXCollections.observableArrayList();
    private ObservableList<Booking>        bookings = FXCollections.observableArrayList();
    private ObservableList<ServiceRequest> services = FXCollections.observableArrayList();

    public HotelService() {
        List<Room> savedRooms = FileUtil.loadRooms();
        if (savedRooms != null) rooms.addAll(savedRooms);

        List<Booking> savedBookings = FileUtil.loadBookings();
        if (savedBookings != null) {
            for (Booking b : savedBookings) {
                for (Room r : rooms) {
                    if (r.getRoomNumber() == b.getRoom().getRoomNumber()) {
                        b.setRoom(r);
                        break;
                    }
                }
            }
            bookings.addAll(savedBookings);
        }

        List<ServiceRequest> savedServices = FileUtil.loadServices();
        if (savedServices != null) services.addAll(savedServices);
    }


    public void addRoom(String type) {
        int maxNo  = rooms.stream().mapToInt(Room::getRoomNumber).max().orElse(100);
        int roomNo = maxNo + 1;
        Room room;
        switch (type) {
            case "Deluxe"  -> room = new DeluxeRoom(roomNo);
            case "Luxury"  -> room = new LuxuryRoom(roomNo);
            default        -> room = new StandardRoom(roomNo);
        }
        rooms.add(room);
        FileUtil.saveRooms(rooms);
    }

    public ObservableList<Room> getRooms() { return rooms; }

    public List<Room> getRoomsByAvailability(boolean available) {
        return rooms.stream()
                    .filter(r -> r.isAvailable() == available)
                    .collect(Collectors.toList());
    }

    public Room findRoom(int roomNumber) {
        return rooms.stream()
                    .filter(r -> r.getRoomNumber() == roomNumber)
                    .findFirst().orElse(null);
    }


    public String bookRoom(String customerName, String phone, Room room, int days) {
        if (room == null)           return "Please select a room.";
        if (!room.isAvailable())    return "Room " + room.getRoomNumber() + " is already booked!";
        if (customerName.isBlank()) return "Please enter customer name.";
        if (phone.isBlank())        return "Please enter phone number.";
        if (days <= 0)              return "Days must be greater than 0.";

        Customer customer = new Customer(customerName, phone);
        Booking  booking  = new Booking(customer, room, days);
        room.setAvailable(false);
        bookings.add(booking);
        FileUtil.saveRooms(rooms);
        FileUtil.saveBookings(bookings);
        return "SUCCESS:" + booking.getTotalBill() + ":" + booking.getBookingId();
    }

    public String cancelBooking(String bookingId) {
        if (bookingId == null || bookingId.isBlank()) return "Please select a booking to cancel.";
        for (Booking b : bookings) {
            if (b.getBookingId().equals(bookingId)) {
                if (b.isCancelled()) return "Booking " + bookingId + " is already cancelled.";
                b.cancel();
                b.getRoom().setAvailable(true);
                FileUtil.saveRooms(rooms);
                FileUtil.saveBookings(bookings);
                return "SUCCESS:Booking " + bookingId + " cancelled. Room " + b.getRoom().getRoomNumber() + " is now available.";
            }
        }
        return "Booking not found.";
    }

    public String checkoutRoom(Room room) {
        if (room == null)       return "Please select a room.";
        if (room.isAvailable()) return "Room " + room.getRoomNumber() + " is not currently booked.";
        room.setAvailable(true);
        FileUtil.saveRooms(rooms);
        FileUtil.saveBookings(bookings);
        return "Room " + room.getRoomNumber() + " checked out successfully!";
    }

    public ObservableList<Booking> getBookings() { return bookings; }

    public Booking getActiveBookingForRoom(int roomNumber) {
        return bookings.stream()
                       .filter(b -> !b.isCancelled() && b.getRoom().getRoomNumber() == roomNumber)
                       .findFirst().orElse(null);
    }


    public String requestService(int roomNumber, ServiceRequest.ServiceType type,
                                  ServiceRequest.Priority priority, String notes) {
        if (type == null) return "Please select a service type.";
        ServiceRequest req = new ServiceRequest(roomNumber, type, priority, notes);
        services.add(req);
        FileUtil.saveServices(services);
        return "SUCCESS:" + req.getRequestId();
    }

    public String updateServiceStatus(ServiceRequest req, Status newStatus) {
        if (req == null) return "Please select a request.";
        req.setStatus(newStatus);
        FileUtil.saveServices(services);
        return "Request #" + req.getRequestId() + " updated to " + newStatus;
    }

    public ObservableList<ServiceRequest> getServices() { return services; }

    public long getPendingServicesCount() {
        return services.stream().filter(s -> s.getStatus() == Status.PENDING).count();
    }


    public int    getTotalRooms()    { return rooms.size(); }
    public int    getTotalBookings() { return (int) bookings.stream().filter(b -> !b.isCancelled()).count(); }

    public int getAvailableRooms() {
        return (int) rooms.stream().filter(Room::isAvailable).count();
    }
    public int getBookedRooms() {
        return (int) rooms.stream().filter(r -> !r.isAvailable()).count();
    }
    public double getTotalRevenue() {
        return bookings.stream().filter(b -> !b.isCancelled()).mapToDouble(Booking::getTotalBill).sum();
    }


    public void saveAll() {
        FileUtil.saveRooms(rooms);
        FileUtil.saveBookings(bookings);
        FileUtil.saveServices(services);
    }
}
