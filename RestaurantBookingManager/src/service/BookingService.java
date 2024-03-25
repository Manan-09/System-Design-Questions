package service;

import exception.InvalidRequestException;
import model.Booking;
import model.Restaurant;
import model.Schedule;
import repository.BookingRepo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BookingService {

    private static BookingService instance;

    private BookingRepo bookingRepo;
    private RestaurantService restaurantService;
    private ConcurrentHashMap<String, Map<LocalDate, Lock>> lockForRestaurantAndDate;

    private BookingService() {
        this.bookingRepo = BookingRepo.getInstance();
        this.restaurantService = RestaurantService.getInstance();
        lockForRestaurantAndDate = new ConcurrentHashMap<>();
    }

    public static BookingService getInstance() {
        if (instance == null) {
            instance = new BookingService();
        }
        return instance;
    }

    public Lock acquireLockForRestaurantAndDate(String restaurantId, LocalDate date) {
        Map<LocalDate, Lock> dateLocks = lockForRestaurantAndDate.computeIfAbsent(restaurantId, k -> new ConcurrentHashMap<>());
        Lock lock = dateLocks.computeIfAbsent(date, k -> new ReentrantLock());
        lock.lock();
        return lock;
    }

    public Booking createBooking(Booking booking) {
        Restaurant restaurant = restaurantService.findRestaurantById(booking.getRestaurant());
        isWithinRestaurantSchedule(booking, restaurant);
        Lock lock = acquireLockForRestaurantAndDate(restaurant.getId(), booking.getStartTime().toLocalDate());
        try {
            List<Booking> existingBookings = bookingRepo.findBookingsByRestaurantAndDate(booking.getRestaurant(),
                    booking.getStartTime().toLocalDate());
            if (countConflictingBookings(booking, existingBookings) >= restaurant.getNumOfTables()) {
                throw new InvalidRequestException("No restaurant table available for requested time");
            }
            bookingRepo.saveBooking(booking);
            return booking;
        } catch (Exception exception) {
            throw exception;
        } finally {
            lock.unlock();
        }
    }

    private boolean isWithinRestaurantSchedule(Booking booking, Restaurant restaurant) {
        DayOfWeek bookingDayOfWeek = booking.getStartTime().getDayOfWeek();
        for (Schedule schedule : restaurant.getSchedule()) {
            if (schedule.getWeekDay() == bookingDayOfWeek) {
                if (!booking.getStartTime().toLocalTime().isBefore(schedule.getStartTime()) &&
                    !booking.getEndTime().toLocalTime().isAfter(schedule.getEndTime())) {
                    return true;
                }
            }
        }
        throw new InvalidRequestException("Requested booking time is not within restaurant schedule");
    }

    private int countConflictingBookings(Booking newBooking, List<Booking> existingBookings) {
        int conflicts = 0;
        for (Booking existingBooking : existingBookings) {
            if (newBooking.getEndTime().isAfter(existingBooking.getStartTime()) &&
                newBooking.getStartTime().isBefore(existingBooking.getEndTime())) {
                conflicts++;
            }
        }
        return conflicts;
    }
}