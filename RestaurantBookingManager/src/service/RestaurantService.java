package service;

import model.Restaurant;
import repository.RestaurantRepo;

import java.util.Collections;
import java.util.List;

import java.util.List;

public class RestaurantService {

    private static RestaurantService instance;
    private RestaurantRepo restaurantRepo;

    private RestaurantService() {
        this.restaurantRepo = RestaurantRepo.getInstance();
    }

    public static synchronized RestaurantService getInstance() {
        if (instance == null) {
            instance = new RestaurantService();
        }
        return instance;
    }

    public Restaurant saveRestaurant(Restaurant restaurant) {
        restaurantRepo.saveRestaurant(restaurant);
        return  restaurant;
    }

    public Restaurant findRestaurantById(String id) {
        return restaurantRepo.findRestaurantById(id);
    }

    public List<Restaurant> searchRestaurants(String criteria, Object value) {
        return restaurantRepo.searchRestaurants(criteria, value);
    }
}

