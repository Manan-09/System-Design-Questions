package repository;

import model.Restaurant;

import java.util.*;

public class RestaurantRepo {

    private static RestaurantRepo instance;

    private Map<String, Restaurant> restaurantById = new HashMap<>();
    private Map<String, List<Restaurant>> restaurantByCity = new HashMap<>();
    private Map<String, List<Restaurant>> restaurantByArea = new HashMap<>();
    private Map<String, List<Restaurant>> restaurantByCuisine = new HashMap<>();
    private Map<Double, List<Restaurant>> restaurantByCostOfTwo = new HashMap<>();

    private RestaurantRepo() {
    }

    public static RestaurantRepo getInstance() {
        if (instance == null) {
            instance = new RestaurantRepo();
        }
        return instance;
    }

    public void saveRestaurant(Restaurant restaurant) {
        restaurantById.put(restaurant.getId(), restaurant);
        restaurantByCity.computeIfAbsent(restaurant.getCity(), k -> new ArrayList<>()).add(restaurant);
        restaurantByArea.computeIfAbsent(restaurant.getArea(), k -> new ArrayList<>()).add(restaurant);
        for (String cuisine : restaurant.getCuisines()) {
            restaurantByCuisine.computeIfAbsent(cuisine, k -> new ArrayList<>()).add(restaurant);
        }
        restaurantByCostOfTwo.computeIfAbsent(restaurant.getCostOfTwo(), k -> new ArrayList<>()).add(restaurant);
    }

    public Restaurant findRestaurantById(String id) {
        return restaurantById.get(id);
    }

    public List<Restaurant> searchRestaurants(String criteria, Object value) {
        switch (criteria.toLowerCase()) {
            case "city":
                return restaurantByCity.getOrDefault((String) value, Collections.emptyList());
            case "area":
                return restaurantByArea.getOrDefault((String) value, Collections.emptyList());
            case "cuisine":
                return restaurantByCuisine.getOrDefault((String) value, Collections.emptyList());
            case "costoftwo":
                return restaurantByCostOfTwo.getOrDefault((Double) value, Collections.emptyList());
            default:
                return Collections.emptyList();
        }
    }
}
