package model;

import helper.IDGenerator;

import java.util.List;

public class Restaurant {
    String id;
    String city;
    String area;
    String name;
    Double costOfTwo;
    List<Schedule> schedule;
    List<String> cuisines;
    int numOfTables;

    public Restaurant(String id, String city, String area, String name, Double costOfTwo, List<Schedule> schedule,
                      List<String> cuisines, int numOfTables) {
        this.id = id;
        this.city = city;
        this.area = area;
        this.name = name;
        this.costOfTwo = costOfTwo;
        this.schedule = schedule;
        this.cuisines = cuisines;
        this.numOfTables = numOfTables;
    }

    public String getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getArea() {
        return area;
    }

    public String getName() {
        return name;
    }

    public Double getCostOfTwo() {
        return costOfTwo;
    }

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public List<String> getCuisines() {
        return cuisines;
    }

    public int getNumOfTables() {
        return numOfTables;
    }

    @Override
    public String toString() {
        return "Restaurant{" + "id='" + id + '\'' + ", city='" + city + '\'' + ", area='" + area + '\'' + ", name='" +
               name + '\'' + ", costOfTwo=" + costOfTwo + ", schedule=" + schedule + ", cuisines=" + cuisines +
               ", numOfTables=" + numOfTables + '}';
    }
}
