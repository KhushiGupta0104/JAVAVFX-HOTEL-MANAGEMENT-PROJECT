package com.hotel.util;

import com.hotel.model.Booking;
import com.hotel.model.Room;
import com.hotel.model.ServiceRequest;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    private static final String DATA_DIR          = System.getProperty("user.home") + File.separator + ".fortunehotel";
    private static final String ROOMS_FILE        = DATA_DIR + File.separator + "rooms.dat";
    private static final String BOOKINGS_FILE     = DATA_DIR + File.separator + "bookings.dat";
    private static final String SERVICES_FILE     = DATA_DIR + File.separator + "services.dat";

    private static final String OLD_ROOMS_FILE    = "rooms.dat";
    private static final String OLD_BOOKINGS_FILE = "bookings.dat";

    static {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) dir.mkdirs();
        migrateIfNeeded(OLD_ROOMS_FILE,    ROOMS_FILE);
        migrateIfNeeded(OLD_BOOKINGS_FILE, BOOKINGS_FILE);
    }

    private static void migrateIfNeeded(String oldPath, String newPath) {
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        if (oldFile.exists() && !newFile.exists()) {
            try {
                Files.move(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("[FileUtil] Migrated: " + oldPath + " -> " + newPath);
            } catch (IOException e) {
                System.err.println("[FileUtil] Migration failed: " + e.getMessage());
            }
        }
    }

    // ── Rooms ────────────────────────────────────────────────────────────

    public static void saveRooms(List<Room> rooms) {
        save(ROOMS_FILE, rooms);
    }

    @SuppressWarnings("unchecked")
    public static List<Room> loadRooms() {
        return (List<Room>) load(ROOMS_FILE);
    }

    // ── Bookings ─────────────────────────────────────────────────────────

    public static void saveBookings(List<Booking> bookings) {
        save(BOOKINGS_FILE, bookings);
    }

    @SuppressWarnings("unchecked")
    public static List<Booking> loadBookings() {
        return (List<Booking>) load(BOOKINGS_FILE);
    }

    // ── Services ─────────────────────────────────────────────────────────

    public static void saveServices(List<ServiceRequest> services) {
        save(SERVICES_FILE, services);
    }

    @SuppressWarnings("unchecked")
    public static List<ServiceRequest> loadServices() {
        return (List<ServiceRequest>) load(SERVICES_FILE);
    }

    // ── Generic save/load ────────────────────────────────────────────────

    private static void save(String path, List<?> list) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            out.writeObject(new ArrayList<>(list));
        } catch (IOException e) {
            System.err.println("[FileUtil] ERROR saving " + path + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static List<?> load(String path) {
        File f = new File(path);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            return (List<?>) in.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (InvalidClassException e) {
            backupCorrupted(path);
            System.err.println("[FileUtil] " + path + " incompatible — backed up, starting fresh.");
            return new ArrayList<>();
        } catch (Exception e) {
            backupCorrupted(path);
            System.err.println("[FileUtil] Failed to load " + path + ": " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private static void backupCorrupted(String path) {
        File original = new File(path);
        File backup   = new File(path + ".bak");
        if (original.exists()) {
            if (backup.exists()) backup.delete();
            original.renameTo(backup);
        }
    }

    public static String getDataDir() { return DATA_DIR; }
}
