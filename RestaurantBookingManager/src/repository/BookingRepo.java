package repository;

import model.Booking;

import java.time.LocalDate;
import java.util.*;

public class BookingRepo {

    private static BookingRepo instance;

    private Map<String, Booking> bookingById = new HashMap<>();
    private Map<String, Map<LocalDate, List<Booking>>> bookingByRestaurantAndDate = new HashMap<>();
    private Map<String, List<Booking>> bookingByUser = new HashMap<>();

    private BookingRepo() {
    }

    public static BookingRepo getInstance() {
        if (instance == null) {
            instance = new BookingRepo();
        }
        return instance;
    }

    public void saveBooking(Booking booking) {
        bookingById.put(booking.getId(), booking);
        String restaurantId = booking.getRestaurant();
        LocalDate date = booking.getStartTime().toLocalDate();
        Map<LocalDate, List<Booking>> restaurantBookings = bookingByRestaurantAndDate.getOrDefault(restaurantId, new HashMap<>());
        List<Booking> bookingsOnDate = restaurantBookings.getOrDefault(date, new ArrayList<>());
        bookingsOnDate.add(booking);
        restaurantBookings.put(date, bookingsOnDate);
        bookingByRestaurantAndDate.put(restaurantId, restaurantBookings);
        String userId = booking.getUser();
        List<Booking> userBookings = bookingByUser.getOrDefault(userId, new ArrayList<>());
        userBookings.add(booking);
        bookingByUser.put(userId, userBookings);
    }

    public Booking findBookingById(String id) {
        return bookingById.get(id);
    }

    public List<Booking> findBookingsByRestaurantAndDate(String restaurantId, LocalDate date) {
        Map<LocalDate, List<Booking>> restaurantBookings = bookingByRestaurantAndDate.getOrDefault(restaurantId, Collections.emptyMap());
        return restaurantBookings.getOrDefault(date, Collections.emptyList());
    }

    public List<Booking> findBookingsByUser(String userId) {
        return bookingByUser.getOrDefault(userId, Collections.emptyList());
    }
}
