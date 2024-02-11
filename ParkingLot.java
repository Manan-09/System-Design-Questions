import java.util.*;

// Enum to represent the types of parking spots
enum ParkingSpotType {
    CAR,
    TWO_WHEELER,
    BUS
}

// Custom exception for parking lot related errors
class ParkingException extends RuntimeException {
    public ParkingException(String message) {
        super(message);
    }
}

// Class to represent a parking spot
abstract class ParkingSpot {
    String spotNumber;
    boolean available;
    Vehicle parkedVehicle;
    ParkingSpotType type;

    public ParkingSpot(String spotNumber, ParkingSpotType type) {
        this.spotNumber = spotNumber;
        available = true;
        parkedVehicle = null;
        this.type = type;
    }

    public String getSpotNumber() {
        return spotNumber;
    }

    public boolean isAvailable() {
        return available;
    }

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }

    public ParkingSpotType getType() {
        return type;
    }

    public abstract double calculateParkingFees(long entry, long exitTime);
}

class CarSpot extends ParkingSpot {

    public CarSpot(String spotNumber) {
        super(spotNumber, ParkingSpotType.CAR);
    }

    @Override
    public double calculateParkingFees(long entry, long exitTime) {
        return 100.0;
    }
}

class TwoWheelerSpot extends ParkingSpot {

    public TwoWheelerSpot(String spotNumber) {
        super(spotNumber, ParkingSpotType.TWO_WHEELER);
    }

    @Override
    public double calculateParkingFees(long entry, long exitTime) {
        return 30.0;
    }
}

class BusSpot extends ParkingSpot {

    public BusSpot(String spotNumber) {
        super(spotNumber, ParkingSpotType.BUS);
    }

    @Override
    public double calculateParkingFees(long entry, long exitTime) {
        return 200.0;
    }
}

// Class to represent a vehicle
class Vehicle {
    String registrationNumber;

    public Vehicle(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }
}

// Class to represent a parking ticket
class ParkingTicket {
    private Vehicle vehicle;
    private Long entryTime;
    private Long exitTime; // Added exit time
    private ParkingSpot parkingSpot;
    private double parkingFees;

    public ParkingTicket(Vehicle vehicle, Long entryTime, ParkingSpot parkingSpot) {
        this.vehicle = vehicle;
        this.entryTime = entryTime;
        this.exitTime = null; // Initialize exit time as null
        this.parkingSpot = parkingSpot;
    }

    // Getter and setter for exit time
    public Long getExitTime() {
        return exitTime;
    }

    public void setExitTime(Long exitTime) {
        this.exitTime = exitTime;
    }

    // Other getters remain the same
    public Vehicle getVehicle() {
        return vehicle;
    }

    public Long getEntryTime() {
        return entryTime;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public double getParkingFees() {
        return parkingFees;
    }

    public void setParkingFees(double fees) {
        this.parkingFees = fees;
    }

}


// Class to represent the parking lot
class ParkingLot {
    private Map<ParkingSpotType, List<ParkingSpot>> spotsMap;
    private Map<String, ParkingTicket> parkedVehicles;
    private List<ParkingTicket> parkingTickets;

    public ParkingLot() {
        spotsMap = new HashMap<>();
        parkedVehicles = new HashMap<>();
        parkingTickets = new ArrayList();
    }

    public void addParkingSpot(ParkingSpot spot) {
        spotsMap.computeIfAbsent(spot.type, k -> new ArrayList<>()).add(spot);
    }

    public synchronized ParkingTicket assignParkingSpot(Vehicle vehicle, ParkingSpotType spotType) {
        ParkingSpot spot = findAvailableSpot(spotType);
        if (spot == null) {
            throw new ParkingException("No available spots found for type: " + spotType);
        }

        if (vehicleAlreadyParked(vehicle)) {
            throw new ParkingException("Vehicle is already parked");
        }

        spot.parkedVehicle = vehicle;
        spot.available = false;
        ParkingTicket ticket = new ParkingTicket(vehicle, System.currentTimeMillis(), spot);
        parkedVehicles.put(vehicle.getRegistrationNumber(), ticket);
        parkingTickets.add(ticket);
        System.out.println("Vehicle parked successfully in spot number " + spot.spotNumber);
        printParkingTicket(ticket); // Print the ticket
        return ticket;
    }

    public synchronized void unparkVehicle(String vehicleRegistrationNumber) {
        ParkingTicket ticket = parkedVehicles.get(vehicleRegistrationNumber);
        if (ticket == null) {
            throw new ParkingException("Vehicle not found or not parked");
        }
        ticket.setExitTime(System.currentTimeMillis());
        ParkingSpot spot = ticket.getParkingSpot();
        double parkingFees = spot.calculateParkingFees(ticket.getEntryTime(), ticket.getExitTime());
        ticket.setParkingFees(parkingFees);
        spot.parkedVehicle = null;
        spot.available = true;
        parkedVehicles.remove(vehicleRegistrationNumber);
        System.out.println("Vehicle " + vehicleRegistrationNumber + " unparked from spot number: " + spot.spotNumber + "with parking fees: " + ticket.getParkingFees());
    }

    // Method to print the parking ticket
    private void printParkingTicket(ParkingTicket ticket) {
        System.out.println("Parking Ticket:");
        System.out.println("Vehicle Registration Number: " + ticket.getVehicle().getRegistrationNumber());
        System.out.println("Entry Time: " + ticket.getEntryTime());
        System.out.println("Spot Number: " + ticket.getParkingSpot().spotNumber);
    }

    // Method to provide information about all parked vehicles
    public void printParkedVehiclesInfo() {
        System.out.println("Parked Vehicles Information:");
        for (ParkingTicket ticket : parkedVehicles.values()) {
            Vehicle vehicle = ticket.getVehicle();
            ParkingSpot spot = ticket.getParkingSpot();
            System.out.println("Vehicle Registration Number: " + vehicle.getRegistrationNumber());
            System.out.println("Spot Number: " + spot.spotNumber);
            System.out.println("Entry Time: " + ticket.getEntryTime());
            System.out.println("-----------------------");
        }
    }

    private ParkingSpot findAvailableSpot(ParkingSpotType spotType) {
        List<ParkingSpot> spotsOfType = spotsMap.get(spotType);
        if (spotsOfType == null) {
            return null;
        }
        for (ParkingSpot spot : spotsOfType) {
            if (spot.available) {
                return spot;
            }
        }
        return null;
    }

    private boolean vehicleAlreadyParked(Vehicle vehicle) {
        return parkedVehicles.containsKey(vehicle);
    }
}

public class Main {
    public static void main(String[] args) {
        ParkingLot parkingLot = new ParkingLot(); // Create a parking lot
        Scanner scanner = new Scanner(System.in);

        // Add parking spots
        parkingLot.addParkingSpot(new CarSpot("C1"));
        parkingLot.addParkingSpot(new CarSpot("C2"));
        parkingLot.addParkingSpot(new TwoWheelerSpot("T1"));
        parkingLot.addParkingSpot(new TwoWheelerSpot("T2"));
        parkingLot.addParkingSpot(new TwoWheelerSpot("T3"));
        parkingLot.addParkingSpot(new BusSpot("B1"));


        boolean running = true;
        while (running) {
            System.out.println("Choose an action:");
            System.out.println("1. Park a vehicle");
            System.out.println("2. Unpark a vehicle");
            System.out.println("3. View information about all parked vehicles");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter vehicle registration number: ");
                    String registrationNumber = scanner.nextLine();
                    System.out.print("Enter parking spot type (CAR, TWO_WHEELER, BUS): ");
                    String spotTypeStr = scanner.nextLine();
                    ParkingSpotType spotType = ParkingSpotType.valueOf(spotTypeStr.toUpperCase());
                    Vehicle vehicle = new Vehicle(registrationNumber);
                    try {
                        ParkingTicket ticket = parkingLot.assignParkingSpot(vehicle, spotType);
                    } catch (ParkingException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case "2":
                    System.out.print("Enter vehicle registration number to unpark: ");
                    String regNumber = scanner.nextLine();
                    try {
                        parkingLot.unparkVehicle(regNumber);
                    } catch (ParkingException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case "3":
                    parkingLot.printParkedVehiclesInfo();
                    break;
                case "4":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        }

        System.out.println("Program exited.");
        scanner.close();
    }
}


