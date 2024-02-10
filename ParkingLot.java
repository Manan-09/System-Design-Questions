import java.time.LocalDateTime;
import java.util.*;

// Enum to represent the types of parking spots
enum ParkingSpotType {
    REGULAR,
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
class ParkingSpot {
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
    private LocalDateTime entryTime;
    private LocalDateTime exitTime; // Added exit time
    private ParkingSpot parkingSpot;

    public ParkingTicket(Vehicle vehicle, LocalDateTime entryTime, ParkingSpot parkingSpot) {
        this.vehicle = vehicle;
        this.entryTime = entryTime;
        this.exitTime = null; // Initialize exit time as null
        this.parkingSpot = parkingSpot;
    }

    // Getter and setter for exit time
    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    // Other getters remain the same
    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }
}

// Class to represent the parking lot
class ParkingLot {
    private Map<ParkingSpotType, List<ParkingSpot>> spotsMap; 
    private Map<Vehicle, ParkingTicket> parkedVehicles;
    private List<ParkingTicket> parkingTickets;

    public ParkingLot() {
        spotsMap = new HashMap<>();
        parkedVehicles = new HashMap<>();
        parkingTickets = new ArrayList<>();
    }

    // Method to add a parking spot to the parking lot
    public void addParkingSpot(ParkingSpot spot) {
        spotsMap.computeIfAbsent(spot.type, k -> new ArrayList<>()).add(spot);
    }

    // Method to assign a parking spot to a vehicle
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
        ParkingTicket ticket = new ParkingTicket(vehicle, LocalDateTime.now(), spot);
        parkedVehicles.put(vehicle, ticket);
        parkingTickets.add(ticket);
        System.out.println("Vehicle parked successfully in spot number " + spot.spotNumber);
        printParkingTicket(ticket); // Print the ticket
        return ticket;
    }

    // Method to unpark a vehicle from a parking spot
    public synchronized void unparkVehicle(Vehicle vehicle) {
        ParkingTicket ticket = parkedVehicles.get(vehicle);
        if (ticket == null) {
            throw new ParkingException("Vehicle not found or not parked");
        }
        ticket.setExitTime(LocalDateTime.now());
        ParkingSpot spot = ticket.getParkingSpot();
        spot.parkedVehicle = null;
        spot.available = true;
        parkedVehicles.remove(vehicle);
        System.out.println("Vehicle " + vehicle.registrationNumber + " unparked from spot number: " + spot.spotNumber);
    }

    // Method to print the parking ticket
    private void printParkingTicket(ParkingTicket ticket) {
        System.out.println("Parking Ticket:");
        System.out.println("Vehicle Registration Number: " + ticket.getVehicle().getRegistrationNumber());
        System.out.println("Entry Time: " + ticket.getEntryTime());
        System.out.println("Exit Time: " + ticket.getExitTime());
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

    // Method to find an available spot of a given type
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

    // Method to check if a vehicle is already parked
    private boolean vehicleAlreadyParked(Vehicle vehicle) {
        return parkedVehicles.containsKey(vehicle);
    }
}

// Main class to test the parking lot system
public class Main {
    public static void main(String[] args) {
        ParkingLot parkingLot = new ParkingLot(); // Create a parking lot
        Scanner scanner = new Scanner(System.in);

        // Add parking spots
        parkingLot.addParkingSpot(new ParkingSpot("1", ParkingSpotType.REGULAR));
        parkingLot.addParkingSpot(new ParkingSpot("2", ParkingSpotType.TWO_WHEELER));
        parkingLot.addParkingSpot(new ParkingSpot("3", ParkingSpotType.BUS));

        boolean running = true;
        while (running) {
            System.out.println("");
            System.out.println("Choose an action:");
            System.out.println("1. Park a vehicle");
            System.out.println("2. Unpark a vehicle");
            System.out.println("3. View information about all parked vehicles");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter vehicle registration number: ");
                    String registrationNumber = scanner.nextLine();
                    System.out.print("Enter parking spot type (REGULAR, TWO_WHEELER, BUS): ");
                    String spotTypeStr = scanner.nextLine();
                    ParkingSpotType spotType = ParkingSpotType.valueOf(spotTypeStr.toUpperCase());
                    Vehicle vehicle = new Vehicle(registrationNumber);
                    try {
                        ParkingTicket ticket = parkingLot.assignParkingSpot(vehicle, spotType);
                    } catch (ParkingException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.print("Enter vehicle registration number to unpark: ");
                    String regNumber = scanner.nextLine();
                    Vehicle unparkVehicle = new Vehicle(regNumber);
                    try {
                        parkingLot.unparkVehicle(unparkVehicle);
                    } catch (ParkingException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 3:
                    parkingLot.printParkedVehiclesInfo();
                    break;
                case 4:
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
