import java.util.EnumMap;
import java.util.List;
import java.util.Map;

enum SeatType {
    GOLD, SILVER, RECLINER
}

enum BookingStatus {
    PENDING, CONFIRMED, CANCELLED
}

class Movie {
    private String name;
    private String genre;
    private int duration; // in minutes

    // Getters and Setters
}

class User {
    private String username;
    private String email;

    // Getters and Setters
}

class Theater {
    private String name;
    private List<Screen> screens;

    // Getters and Setters
}

class Screen {
    private int number;
    private List<Show> shows;

    // Getters and Setters
}

class Show {
    private Movie movie;
    private Screen screen;
    private String time;
    private Map<SeatType, Double> seatPrices;


    // Method to get price based on seat type
    public double getPrice(SeatType type) {
        return seatPrices.getOrDefault(type, 0.0);
    }
}

class Seat {
    private int row;
    private int number;
    private boolean booked;
    private SeatType type;

    // Getters and Setters
}

class PaymentInfo {
    private String paymentMethod;
    private double totalPrice;
    // Additional payment-related fields can be added as needed

    // Getters and Setters
}

class Booking {
    private User user;
    private Show show;
    private List<Seat> seats;
    private PaymentInfo paymentInfo;
    private BookingStatus status;
    private double totalPrice;

    // Getters and Setters
}

public class BookMyShow {
    private List<Movie> movies;
    private List<User> users;
    private List<Theater> theaters;
}
