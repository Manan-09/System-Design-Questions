import model.Booking;
import model.Restaurant;
import model.Schedule;
import model.User;
import service.BookingService;
import service.RestaurantService;
import service.UserService;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        BookingService bookingService = BookingService.getInstance();
        RestaurantService restaurantService = RestaurantService.getInstance();
        UserService userService = new UserService();

        List<Schedule> scheduleList = new ArrayList<>();
        scheduleList.add(new Schedule(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(18, 0)));
        scheduleList.add(new Schedule(DayOfWeek.TUESDAY, LocalTime.of(9, 0), LocalTime.of(18, 0)));
        scheduleList.add(new Schedule(DayOfWeek.WEDNESDAY, LocalTime.of(9, 0), LocalTime.of(18, 0)));
        scheduleList.add(new Schedule(DayOfWeek.THURSDAY, LocalTime.of(9, 0), LocalTime.of(18, 0)));
        scheduleList.add(new Schedule(DayOfWeek.FRIDAY, LocalTime.of(9, 0), LocalTime.of(18, 0)));
        scheduleList.add(new Schedule(DayOfWeek.SATURDAY, LocalTime.of(9, 0), LocalTime.of(18, 0)));
        scheduleList.add(new Schedule(DayOfWeek.SUNDAY, LocalTime.of(9, 0), LocalTime.of(18, 0)));

        List<String> cuisinesList = new ArrayList<>();
        cuisinesList.add("Italian");
        cuisinesList.add("Chinese");
        cuisinesList.add("Mexican");

        Restaurant restaurant = new Restaurant("1","New York", "Manhattan", "Italian Delight",
                50.0, scheduleList, cuisinesList, 1);
        Restaurant restaurant2 = new Restaurant("2","New York", "Brooklyn", "Pizza Delight",
                100.0, scheduleList, cuisinesList, 1);
        System.out.println(restaurantService.saveRestaurant(restaurant).toString());
        System.out.println(restaurantService.saveRestaurant(restaurant2).toString());

        User user = new User("1","Manan", "43432");
        System.out.println(userService.saveUser(user).toString());


        Booking booking1 = new Booking( "1","1", "1",
                LocalDateTime.of(2024, 3, 27, 4, 0),
                LocalDateTime.of(2024, 3, 27, 13, 0));

        Booking booking2 = new Booking( "2","1", "1",
                LocalDateTime.of(2024, 3, 28, 10, 0),
                LocalDateTime.of(2024, 3, 28, 12, 0));

        Booking booking3 = new Booking("3","1", "1",
                LocalDateTime.of(2024, 3, 28, 10, 0),
                LocalDateTime.of(2024, 3, 28, 12, 0));

        try {
            System.out.println(bookingService.createBooking(booking1).toString());
        } catch (Exception e) {
            System.out.println("Error creating booking 1: " + e.getMessage());
        }

        try {
            System.out.println(bookingService.createBooking(booking2).toString());
        } catch (Exception e) {
            System.out.println("Error creating booking 2: " + e.getMessage());
        }

        try {
            System.out.println(bookingService.createBooking(booking3).toString());
        } catch (Exception e) {
            System.out.println("Error creating booking 3: " + e.getMessage());
        }

    }
}