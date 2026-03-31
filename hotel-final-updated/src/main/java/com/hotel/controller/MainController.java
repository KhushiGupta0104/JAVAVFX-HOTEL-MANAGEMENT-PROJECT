package com.hotel.controller;

import java.util.Optional;

import com.hotel.model.Booking;
import com.hotel.model.Room;
import com.hotel.model.ServiceRequest;
import com.hotel.model.ServiceRequest.Status;
import com.hotel.service.HotelService;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainController {

    @FXML private Label totalRoomsLabel;
    @FXML private Label availableLabel;
    @FXML private Label bookedLabel;
    @FXML private Label totalBookingsLabel;
    @FXML private Label revenueLabel;

    @FXML private TableView<Room>           roomTable;
    @FXML private TableColumn<Room, String> colRoomNo;
    @FXML private TableColumn<Room, String> colType;
    @FXML private TableColumn<Room, String> colPrice;
    @FXML private TableColumn<Room, String> colStatus;
    @FXML private ComboBox<String>          roomTypeCombo;

    @FXML private ComboBox<String>          availFilterCombo;
    @FXML private TextField                 availSearchField;
    @FXML private TableView<Room>           availTable;
    @FXML private TableColumn<Room, String> colAvailNo;
    @FXML private TableColumn<Room, String> colAvailType;
    @FXML private TableColumn<Room, String> colAvailPrice;
    @FXML private TableColumn<Room, String> colAvailStatus;
    @FXML private Label                     availSummaryLabel;

    @FXML private TextField      nameField;
    @FXML private TextField      phoneField;
    @FXML private TextField      daysField;
    @FXML private ComboBox<Room> roomCombo;
    @FXML private Label          bookingStatusLabel;
    @FXML private Label          bookingIdLabel;

    @FXML private TableView<Booking>           billTable;
    @FXML private TableColumn<Booking, String> colBillId;
    @FXML private TableColumn<Booking, String> colBillCustomer;
    @FXML private TableColumn<Booking, String> colBillRoom;
    @FXML private TableColumn<Booking, String> colBillDays;
    @FXML private TableColumn<Booking, String> colBillDate;
    @FXML private TableColumn<Booking, String> colBillSubtotal;
    @FXML private TableColumn<Booking, String> colBillTax;
    @FXML private TableColumn<Booking, String> colBillTotal;
    @FXML private TableColumn<Booking, String> colBillStatus;
    @FXML private Label                        billTotalRevenueLabel;

    @FXML private VBox   receiptPanel;
    @FXML private Label  rcptId;
    @FXML private Label  rcptDate;
    @FXML private Label  rcptCustomer;
    @FXML private Label  rcptPhone;
    @FXML private Label  rcptRoom;
    @FXML private Label  rcptType;
    @FXML private Label  rcptDays;
    @FXML private Label  rcptPrice;
    @FXML private Label  rcptSubtotal;
    @FXML private Label  rcptTax;
    @FXML private Label  rcptTotal;
    @FXML private Label  rcptStatus;

    @FXML private ComboBox<Booking> cancelCombo;
    @FXML private Label             cancelPreviewLabel;
    @FXML private Label             cancelStatusLabel;
    @FXML private VBox              cancelPreviewBox;

    @FXML private ComboBox<Room> checkoutCombo;
    @FXML private Label          checkoutStatusLabel;

    @FXML private ComboBox<Integer>                    serviceRoomCombo;
    @FXML private ComboBox<ServiceRequest.ServiceType> serviceTypeCombo;
    @FXML private ComboBox<ServiceRequest.Priority>    servicePriorityCombo;
    @FXML private TextField                            serviceNotesField;
    @FXML private Label                                serviceStatusLabel;
    @FXML private VBox                                 serviceRequestsContainer;
    @FXML private Label                                noServicesLabel;
    @FXML private Label                                svcPendingCount;
    @FXML private Label                                svcProgressCount;
    @FXML private Label                                svcDoneCount;
    @FXML private Label                                pendingServicesLabel;

    @FXML private TextField      reviewNameField;
    @FXML private ComboBox<String> reviewRoomTypeCombo;
    @FXML private ComboBox<String> reviewRatingCombo;
    @FXML private TextArea        reviewTextField;
    @FXML private Label           reviewStatusLabel;
    @FXML private VBox            reviewsContainer;
    @FXML private Label           noReviewsLabel;

    @FXML private TabPane    mainTabPane;
    @FXML private BorderPane rootPane;
    @FXML private Button     themeBtn;
    @FXML private Button     navDashboard;
    @FXML private Button     navRooms;
    @FXML private Button     navAvailability;
    @FXML private Button     navBooking;
    @FXML private Button     navBilling;
    @FXML private Button     navCheckout;
    @FXML private Button     navCancellation;
    @FXML private Button     navReviews;
    @FXML private Button     navServices;

    private boolean isDarkMode = true;
    private Button  activeNav;

    private final HotelService service = com.hotel.MainApp.SERVICE;

    @FXML private void showDashboard()    { selectTab(0, navDashboard); }
    @FXML private void showRooms()        { selectTab(1, navRooms); }
    @FXML private void showAvailability() { selectTab(2, navAvailability); refreshAvailabilityTable(); }
    @FXML private void showBooking()      { selectTab(3, navBooking); }
    @FXML private void showBilling()      { selectTab(4, navBilling); refreshBillingTotal(); }
    @FXML private void showCheckout()     { selectTab(5, navCheckout); }
    @FXML private void showCancellation() { selectTab(6, navCancellation); refreshCancelCombo(); }
    @FXML private void showReviews()      { selectTab(7, navReviews); }
    @FXML private void showServices()     { selectTab(8, navServices); refreshServicesUI(); }

    @FXML
    private void handleLogout() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Logout");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to logout?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Stage stage = (Stage) rootPane.getScene().getWindow();
                    Parent login = FXMLLoader.load(getClass().getResource("/ui/login.fxml"));
                    stage.setScene(new Scene(login));
                    stage.setTitle("Fortune Hotel — Login");
                } catch (Exception e) { e.printStackTrace(); }
            }
        });
    }

    private void selectTab(int index, Button btn) {
        mainTabPane.getSelectionModel().select(index);
        if (activeNav != null) activeNav.getStyleClass().remove("nav-btn-active");
        btn.getStyleClass().add("nav-btn-active");
        activeNav = btn;
    }

    @FXML
    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        if (isDarkMode) {
            rootPane.getStyleClass().remove("light");
        } else {
            if (!rootPane.getStyleClass().contains("light"))
                rootPane.getStyleClass().add("light");
        }
        themeBtn.setText(isDarkMode ? "☀  Light Mode" : "🌙  Dark Mode");
    }

    @FXML
    public void initialize() {
        activeNav = navDashboard;

        colRoomNo.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getRoomNumber())));
        colType.setCellValueFactory(d   -> new SimpleStringProperty(d.getValue().getType()));
        colPrice.setCellValueFactory(d  -> new SimpleStringProperty("₹" + d.getValue().getPricePerDay()));
        colStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().isAvailable() ? "✅ Available" : "❌ Booked"));
        roomTable.setItems(service.getRooms());
        roomTypeCombo.getItems().addAll("Standard", "Deluxe", "Luxury");
        roomTypeCombo.setValue("Standard");

        colAvailNo.setCellValueFactory(d    -> new SimpleStringProperty(String.valueOf(d.getValue().getRoomNumber())));
        colAvailType.setCellValueFactory(d  -> new SimpleStringProperty(d.getValue().getType()));
        colAvailPrice.setCellValueFactory(d -> new SimpleStringProperty("₹" + d.getValue().getPricePerDay() + "/night"));
        colAvailStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().isAvailable() ? "✅ Available" : "❌ Booked"));
        availFilterCombo.getItems().addAll("All Rooms", "Available Only", "Booked Only");
        availFilterCombo.setValue("All Rooms");
        availFilterCombo.setOnAction(e -> refreshAvailabilityTable());
        refreshAvailabilityTable();

        roomCombo.setItems(service.getRooms());
        checkoutCombo.setItems(service.getRooms());

        colBillId.setCellValueFactory(d       -> new SimpleStringProperty(d.getValue().getBookingId()));
        colBillCustomer.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCustomer().getName()));
        colBillRoom.setCellValueFactory(d     -> new SimpleStringProperty("Room " + d.getValue().getRoom().getRoomNumber() + " (" + d.getValue().getRoom().getType() + ")"));
        colBillDays.setCellValueFactory(d     -> new SimpleStringProperty(String.valueOf(d.getValue().getDays())));
        colBillDate.setCellValueFactory(d     -> new SimpleStringProperty(d.getValue().getBookingDateFormatted()));
        colBillSubtotal.setCellValueFactory(d -> new SimpleStringProperty("₹" + String.format("%.2f", d.getValue().getSubtotal())));
        colBillTax.setCellValueFactory(d      -> new SimpleStringProperty("₹" + String.format("%.2f", d.getValue().getTax())));
        colBillTotal.setCellValueFactory(d    -> new SimpleStringProperty("₹" + String.format("%.2f", d.getValue().getTotalBill())));
        colBillStatus.setCellValueFactory(d   -> new SimpleStringProperty(d.getValue().getStatusDisplay()));
        billTable.setItems(service.getBookings());

        billTable.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) showReceipt(selected);
        });
        if (receiptPanel != null) receiptPanel.setVisible(false);

        refreshCancelCombo();

        if (serviceTypeCombo != null) {
            serviceTypeCombo.getItems().addAll(ServiceRequest.ServiceType.values());
            serviceTypeCombo.setValue(ServiceRequest.ServiceType.HOUSEKEEPING);
        }
        if (servicePriorityCombo != null) {
            servicePriorityCombo.getItems().addAll(ServiceRequest.Priority.values());
            servicePriorityCombo.setValue(ServiceRequest.Priority.NORMAL);
        }
        refreshServiceRoomCombo();

        if (reviewRoomTypeCombo != null) {
            reviewRoomTypeCombo.getItems().addAll("Standard", "Deluxe", "Luxury");
            reviewRoomTypeCombo.setValue("Standard");
        }
        if (reviewRatingCombo != null) {
            reviewRatingCombo.getItems().addAll("⭐ 1 Star — Poor", "⭐⭐ 2 Stars — Fair", "⭐⭐⭐ 3 Stars — Good",
                    "⭐⭐⭐⭐ 4 Stars — Very Good", "⭐⭐⭐⭐⭐ 5 Stars — Excellent");
            reviewRatingCombo.setValue("⭐⭐⭐⭐⭐ 5 Stars — Excellent");
        }

        refreshDashboard();
        refreshServicesUI();
        refreshBillingTotal();
    }

    private void refreshAvailabilityTable() {
        String filter = availFilterCombo != null ? availFilterCombo.getValue() : "All Rooms";
        String search = availSearchField != null ? availSearchField.getText().trim().toLowerCase() : "";

        javafx.collections.ObservableList<Room> filtered = javafx.collections.FXCollections.observableArrayList();
        for (Room r : service.getRooms()) {
            boolean matchesFilter = "All Rooms".equals(filter)
                    || ("Available Only".equals(filter) && r.isAvailable())
                    || ("Booked Only".equals(filter) && !r.isAvailable());
            boolean matchesSearch = search.isEmpty()
                    || String.valueOf(r.getRoomNumber()).contains(search)
                    || r.getType().toLowerCase().contains(search);
            if (matchesFilter && matchesSearch) filtered.add(r);
        }

        if (availTable != null) availTable.setItems(filtered);

        long avail  = service.getRooms().stream().filter(Room::isAvailable).count();
        long booked = service.getRooms().stream().filter(r -> !r.isAvailable()).count();
        if (availSummaryLabel != null)
            availSummaryLabel.setText("Total: " + service.getRooms().size() + "   ✅ Available: " + avail + "   ❌ Booked: " + booked);
    }

    @FXML
    private void handleAvailSearch() {
        refreshAvailabilityTable();
    }

    @FXML
    private void handleAddRoom() {
        String type = roomTypeCombo.getValue();
        service.addRoom(type);
        roomTable.refresh();
        refreshDashboard();
        refreshAvailabilityTable();
        showSuccessPopup("Room Added", "New " + type + " room has been added successfully!");
    }

    @FXML
    private void handleBooking() {
        String name  = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        Room   room  = roomCombo.getValue();
        int    days;
        try {
            days = Integer.parseInt(daysField.getText().trim());
        } catch (NumberFormatException e) {
            setStatus(bookingStatusLabel, "⚠  Please enter a valid number of days.", false);
            return;
        }
        String result = service.bookRoom(name, phone, room, days);
        if (result.startsWith("SUCCESS:")) {
            String[] parts = result.split(":");
            double total   = Double.parseDouble(parts[1]);
            String bkId    = parts.length > 2 ? parts[2] : "N/A";

            roomTable.refresh();
            roomCombo.getSelectionModel().clearSelection();
            nameField.clear(); phoneField.clear(); daysField.clear();
            refreshDashboard();
            refreshAvailabilityTable();
            refreshCancelCombo();
            refreshBillingTotal();

            setStatus(bookingStatusLabel, "✅ Booking confirmed! Total (incl. 18% GST): ₹" + String.format("%.2f", total), true);
            if (bookingIdLabel != null) {
                bookingIdLabel.setText("Booking ID: " + bkId);
                bookingIdLabel.setVisible(true);
            }
            showSuccessPopup("Booking Confirmed!", 
                "Booking ID: " + bkId + "\nRoom: " + (room != null ? room.getRoomNumber() : "N/A") +
                "\nTotal Bill: ₹" + String.format("%.2f", total));
        } else {
            setStatus(bookingStatusLabel, "❌ " + result, false);
        }
    }

    // ── Cancellation ──────────────────────────────────────────────────────
    private void refreshCancelCombo() {
        if (cancelCombo == null) return;
        cancelCombo.getItems().clear();
        for (Booking b : service.getBookings()) {
            if (!b.isCancelled()) cancelCombo.getItems().add(b);
        }
        if (cancelPreviewBox != null) cancelPreviewBox.setVisible(false);
    }

    @FXML
    private void handleCancelPreview() {
        if (cancelCombo == null) return;
        Booking selected = cancelCombo.getValue();
        if (selected == null) {
            if (cancelPreviewBox != null) cancelPreviewBox.setVisible(false);
            return;
        }
        if (cancelPreviewLabel != null) {
            cancelPreviewLabel.setText(
                "Booking ID: " + selected.getBookingId() + "\n" +
                "Guest: " + selected.getCustomer().getName() + " (" + selected.getCustomer().getPhone() + ")\n" +
                "Room: " + selected.getRoom().getRoomNumber() + " — " + selected.getRoom().getType() + "\n" +
                "Duration: " + selected.getDays() + " nights\n" +
                "Amount: ₹" + String.format("%.2f", selected.getTotalBill()) + " (incl. GST)\n" +
                "Booked on: " + selected.getBookingDateFormatted()
            );
        }
        if (cancelPreviewBox != null) cancelPreviewBox.setVisible(true);
    }

    @FXML
    private void handleCancelBooking() {
        if (cancelCombo == null) return;
        Booking selected = cancelCombo.getValue();
        if (selected == null) {
            setStatus(cancelStatusLabel, "⚠  Please select a booking to cancel.", false);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Cancellation");
        confirm.setHeaderText("Cancel Booking " + selected.getBookingId() + "?");
        confirm.setContentText(
            "Guest: " + selected.getCustomer().getName() + "\n" +
            "Room: " + selected.getRoom().getRoomNumber() + " (" + selected.getRoom().getType() + ")\n" +
            "This action cannot be undone."
        );
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String res = service.cancelBooking(selected.getBookingId());
            if (res.startsWith("SUCCESS:")) {
                roomTable.refresh();
                refreshDashboard();
                refreshAvailabilityTable();
                refreshCancelCombo();
                billTable.refresh();
                refreshBillingTotal();
                setStatus(cancelStatusLabel, "✅ " + res.substring(8), true);
            } else {
                setStatus(cancelStatusLabel, "❌ " + res, false);
            }
        }
    }

    // ── Billing Receipt ───────────────────────────────────────────────────
    private void showReceipt(Booking b) {
        if (receiptPanel == null) return;
        receiptPanel.setVisible(true);
        if (rcptId       != null) rcptId.setText(b.getBookingId());
        if (rcptDate     != null) rcptDate.setText(b.getBookingDateFormatted());
        if (rcptCustomer != null) rcptCustomer.setText(b.getCustomer().getName());
        if (rcptPhone    != null) rcptPhone.setText(b.getCustomer().getPhone());
        if (rcptRoom     != null) rcptRoom.setText("Room " + b.getRoom().getRoomNumber());
        if (rcptType     != null) rcptType.setText(b.getRoom().getType());
        if (rcptDays     != null) rcptDays.setText(b.getDays() + " Night(s)");
        if (rcptPrice    != null) rcptPrice.setText("₹" + b.getRoom().getPricePerDay() + " / night");
        if (rcptSubtotal != null) rcptSubtotal.setText("₹" + String.format("%.2f", b.getSubtotal()));
        if (rcptTax      != null) rcptTax.setText("₹" + String.format("%.2f", b.getTax()));
        if (rcptTotal    != null) rcptTotal.setText("₹" + String.format("%.2f", b.getTotalBill()));
        if (rcptStatus   != null) rcptStatus.setText(b.getStatusDisplay());
    }

    @FXML
    private void handleCloseReceipt() {
        if (receiptPanel != null) receiptPanel.setVisible(false);
        billTable.getSelectionModel().clearSelection();
    }

    private void refreshBillingTotal() {
        if (billTotalRevenueLabel != null) {
            billTotalRevenueLabel.setText("Total Revenue: ₹" + String.format("%.2f", service.getTotalRevenue()));
        }
        billTable.refresh();
    }

    // ── Checkout ──────────────────────────────────────────────────────────
    @FXML
    private void handleCheckout() {
        Room   room   = checkoutCombo.getValue();
        String result = service.checkoutRoom(room);
        roomTable.refresh();
        checkoutCombo.getSelectionModel().clearSelection();
        refreshDashboard();
        refreshAvailabilityTable();
        boolean ok = result.contains("successfully");
        setStatus(checkoutStatusLabel, (ok ? "✅ " : "❌ ") + result, ok);
        if (ok) showSuccessPopup("Checkout Complete", result);
    }

    // ── Services ──────────────────────────────────────────────────────────
    @FXML
    private void handleServiceRequest() {
        Integer roomNo = serviceRoomCombo != null ? serviceRoomCombo.getValue() : null;
        ServiceRequest.ServiceType svcType  = serviceTypeCombo != null ? serviceTypeCombo.getValue() : null;
        ServiceRequest.Priority    priority = servicePriorityCombo != null ? servicePriorityCombo.getValue() : ServiceRequest.Priority.NORMAL;
        String notes = serviceNotesField != null ? serviceNotesField.getText().trim() : "";

        if (roomNo == null) {
            if (serviceStatusLabel != null) setStatus(serviceStatusLabel, "⚠  Please select a room number.", false);
            return;
        }
        String result = service.requestService(roomNo, svcType, priority, notes);
        if (result.startsWith("SUCCESS:")) {
            if (serviceNotesField != null) serviceNotesField.clear();
            refreshServicesUI();
            refreshDashboard();
            setStatus(serviceStatusLabel, "✅ Service request submitted! (ID: " + result.split(":")[1] + ")", true);
        } else {
            setStatus(serviceStatusLabel, "❌ " + result, false);
        }
    }

    private void refreshServiceRoomCombo() {
        if (serviceRoomCombo == null) return;
        serviceRoomCombo.getItems().clear();
        for (Room r : service.getRooms()) serviceRoomCombo.getItems().add(r.getRoomNumber());
        if (!serviceRoomCombo.getItems().isEmpty()) serviceRoomCombo.setValue(serviceRoomCombo.getItems().get(0));
    }

    private void refreshServicesUI() {
        if (serviceRequestsContainer == null) return;
        serviceRequestsContainer.getChildren().clear();
        long pending = 0, inProgress = 0, done = 0;

        for (ServiceRequest req : service.getServices()) {
            if (req.getStatus() == Status.PENDING)          pending++;
            else if (req.getStatus() == Status.IN_PROGRESS) inProgress++;
            else                                             done++;

            VBox card = new VBox(8);
            card.getStyleClass().add("service-card");
            card.setStyle("-fx-padding: 14 16 14 16;");

            HBox topRow = new HBox(10);
            topRow.setAlignment(Pos.CENTER_LEFT);
            Label typeLabel = new Label(req.getServiceType().getLabel());
            typeLabel.getStyleClass().add("service-type");

            Label badge = new Label(req.getStatus().getLabel());
            switch (req.getStatus()) {
                case PENDING     -> badge.getStyleClass().add("badge-pending");
                case IN_PROGRESS -> badge.getStyleClass().add("badge-progress");
                case COMPLETED   -> badge.getStyleClass().add("badge-done");
            }

            String priorityClass = switch (req.getPriority()) {
                case URGENT -> "priority-urgent";
                case NORMAL -> "priority-normal";
                case LOW    -> "priority-low";
            };
            Label priorityLabel = new Label("● " + req.getPriority());
            priorityLabel.getStyleClass().add(priorityClass);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            topRow.getChildren().addAll(typeLabel, badge, spacer, priorityLabel);

            Label meta = new Label("Room " + req.getRoomNumber() + "  ·  " + req.getRequestedAtFormatted() + "  ·  #" + req.getRequestId());
            meta.getStyleClass().add("service-meta");

            VBox bottom = new VBox(6);
            if (!req.getNotes().isBlank()) {
                Label notes = new Label("📝 " + req.getNotes());
                notes.getStyleClass().add("service-notes");
                notes.setWrapText(true);
                bottom.getChildren().add(notes);
            }

            if (req.getStatus() != Status.COMPLETED) {
                HBox actions = new HBox(8);
                if (req.getStatus() == Status.PENDING) {
                    Button startBtn = new Button("🔄  Mark In Progress");
                    startBtn.getStyleClass().add("btn-status-progress");
                    startBtn.setOnAction(e -> { service.updateServiceStatus(req, Status.IN_PROGRESS); refreshServicesUI(); refreshDashboard(); });
                    actions.getChildren().add(startBtn);
                }
                Button doneBtn = new Button("✅  Mark Completed");
                doneBtn.getStyleClass().add("btn-status-done");
                doneBtn.setOnAction(e -> { service.updateServiceStatus(req, Status.COMPLETED); refreshServicesUI(); refreshDashboard(); });
                actions.getChildren().add(doneBtn);
                bottom.getChildren().add(actions);
            }

            card.getChildren().addAll(topRow, meta, bottom);
            serviceRequestsContainer.getChildren().add(card);
        }

        if (svcPendingCount  != null) svcPendingCount.setText(String.valueOf(pending));
        if (svcProgressCount != null) svcProgressCount.setText(String.valueOf(inProgress));
        if (svcDoneCount     != null) svcDoneCount.setText(String.valueOf(done));
        if (noServicesLabel  != null) noServicesLabel.setVisible(service.getServices().isEmpty());
        refreshServiceRoomCombo();
    }

    // ── Reviews ───────────────────────────────────────────────────────────
    @FXML
    private void handleSubmitReview() {
        String guestName = reviewNameField != null ? reviewNameField.getText().trim() : "";
        String roomType  = reviewRoomTypeCombo != null ? reviewRoomTypeCombo.getValue() : "Standard";
        String rating    = reviewRatingCombo  != null ? reviewRatingCombo.getValue()    : "5 Stars";
        String text      = reviewTextField    != null ? reviewTextField.getText().trim() : "";

        if (guestName.isEmpty() || text.isEmpty()) {
            if (reviewStatusLabel != null) setStatus(reviewStatusLabel, "⚠  Please fill in your name and review.", false);
            return;
        }

        VBox card = new VBox(6);
        card.getStyleClass().add("review-card");

        HBox header = new HBox(10);
        Label nameLabel   = new Label(guestName);  nameLabel.getStyleClass().add("review-guest-name");
        Label ratingLabel = new Label(rating);       ratingLabel.getStyleClass().add("review-rating");
        header.getChildren().addAll(nameLabel, ratingLabel);

        Label typeLabel    = new Label(roomType + " Room"); typeLabel.getStyleClass().add("review-room-type");
        Label reviewContent = new Label(text); reviewContent.getStyleClass().add("review-text"); reviewContent.setWrapText(true);
        card.getChildren().addAll(header, typeLabel, reviewContent);

        if (reviewsContainer != null) {
            reviewsContainer.getChildren().add(0, card);
            if (noReviewsLabel != null) noReviewsLabel.setVisible(false);
        }
        if (reviewNameField  != null) reviewNameField.clear();
        if (reviewTextField  != null) reviewTextField.clear();
        if (reviewStatusLabel != null) setStatus(reviewStatusLabel, "✅ Thank you for your review!", true);
    }

    // ── Helpers ───────────────────────────────────────────────────────────
    private void refreshDashboard() {
        totalRoomsLabel.setText(String.valueOf(service.getTotalRooms()));
        availableLabel.setText(String.valueOf(service.getAvailableRooms()));
        bookedLabel.setText(String.valueOf(service.getBookedRooms()));
        totalBookingsLabel.setText(String.valueOf(service.getTotalBookings()));
        revenueLabel.setText("₹" + String.format("%.2f", service.getTotalRevenue()));
        if (pendingServicesLabel != null) pendingServicesLabel.setText(String.valueOf(service.getPendingServicesCount()));
    }

    private void setStatus(Label label, String text, boolean success) {
        if (label == null) return;
        label.setText(text);
        label.setStyle(success
                ? "-fx-text-fill: #7ab870; -fx-font-weight: bold;"
                : "-fx-text-fill: #c47850; -fx-font-weight: bold;");
        label.setVisible(true);
    }

    private void showSuccessPopup(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
