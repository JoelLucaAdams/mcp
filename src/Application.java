import util.mcp.database.Attendants;
import util.mcp.database.Database;
import util.mcp.database.ZoneCost;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *This is the main class where the program runs
 * @author Joel Adams (joa38)
 * @version 1.0
 */
public class Application {

    private Scanner scan;
    private Admin admin;
    private ZoneCost zoneCost;
    private Attendants attendants;
    private Database database;

    /**
     * The constructor loads; The database.json file (multi story car park), the attendants.json file
     * (the car park attendants) and the cost.properties file (the different zone costs)
     */
    private Application() {
        scan = new Scanner(System.in);
        zoneCost = new ZoneCost();
        attendants = new Attendants();
        attendants.load();
        zoneCost.load();
        database = new Database(zoneCost, attendants, scan);
        database.load();
        admin = new Admin(zoneCost, database, attendants, scan);
    }

    private void printMenu() {
        System.out.println("1 - register vehicle");
        System.out.println("2 - collect vehicle");
        System.out.println("3 - show car park");
        System.out.println("4 - Admin");
        System.out.println("Q - quit");
    }

    private void runMenu() {
        String answer;
        do {
            printMenu();
            System.out.println("Welcome! Please enter an option:");
            answer = scan.nextLine().toUpperCase();
            switch(answer) {
                case "1":
                    registerVehicle();
                    break;
                case "2":
                    collectVehicle();
                    break;
                case "3":
                    printAll();
                    break;
                case "4":
                    admin();
                    break;
                case "Q":
                    System.out.println("Exiting program...");
                    attendants.save();
                    database.save();
                    break;
                default:
                    System.err.println("Invalid option requested");
                    break;
            }
        } while (!(answer.equals("Q")));
    }

    private void registerVehicle() {
            System.out.println("Welcome!");
            System.out.println("Please enter the vehicles' licence plate number: ");
            String licencePlate = scan.nextLine();

            printVehicleType();
            System.out.println("Please enter your vehicle type: (1-5)");
            String vehicleType = scan.nextLine();
            if (!(vehicleType.equals("1") || vehicleType.equals("2") || vehicleType.equals("3") ||
                    vehicleType.equals("4") || vehicleType.equals("5"))) {
                System.err.println("error Please try again and use a actual vehicle type");
                registerVehicle();
            }

            System.out.println("Do you want an attendant to park the vehicle? (Y/N)");
            String answer = scan.nextLine().toUpperCase();
            if (answer.equals("Y") && !vehicleType.equals("4") && !vehicleType.equals("5")) {
                System.out.println(attendants.callAttendant());
                attendantParking(vehicleType, licencePlate);
            } else if (answer.equals("N")) {
                database.searchForSpace(vehicleType, licencePlate);
            } else {
            System.out.println("Sorry the attendants can't park Motorbikes or Coaches");
            System.out.println();
            registerVehicle();
        }
    }

    private void collectVehicle() {
        System.out.println("Please enter your parking receipt: ");
        String receipt = scan.next();
        if (database.checkParkingReceipt(receipt)) {
            System.out.println("Please exit the car park in the next 15 mins");
            leaveCarPark();
        } else {
            System.err.println("Please enter a valid receipt number and try again");
            collectVehicle();
        }
    }

    private void printAll() {
        zoneCost.printZoneCost();
        attendants.printAttendants();
        System.out.println(database.toString());
    }



    private void admin(){
        System.out.println("Please enter the password credentials: ");
        String password = scan.nextLine();
        if (password.equals("1234Admin")) {
            admin.runAdmin();
        } else {
            System.err.println("Invalid password");
        }
    }

    private void leaveCarPark() {
        long timeBeforeLeaving = System.currentTimeMillis();
        System.out.println("Please enter Y when you've left the car park");
        scan.next();
        long timeAfterLeaving = System.currentTimeMillis();
        timeBeforeLeaving = timeBeforeLeaving + (15 * 60000);
        if (timeBeforeLeaving > timeAfterLeaving) {
            System.out.println("You are free to exit the car park.");
        } else if (timeBeforeLeaving < timeAfterLeaving) {
            System.out.println("Please seek further assistance as you took more than 15 mins to leave the garage");
        } else {
            System.out.println("You are free to leave the car park.");
        }
    }

    private void printVehicleType() {
        System.out.println("1 = Standard sized vehicles - (up to 2 metres in height and less than 5 " +
                "metres in length: cars and small vans)");
        System.out.println("2 = Higher vehicles - (over 2 metres in height but less then 3 metres in " +
                "height and less than 5 metres in length: tall short wheel-based vans)");
        System.out.println("3 = Longer vehicles - (less then 3 metres in height and between 5.1 and 6 " +
                "metres in length: long wheel-based vans)");
        System.out.println("4 = Coaches - (of any height up to 15 metres in length)");
        System.out.println("5 = Motorbikes - (any size)");
    }

    private void attendantParking(String type, String plate) {
        System.out.println("Find a free space using SYSTEM (S) or ROAM (R) for a free space? ");
        String answer = scan.nextLine().toUpperCase();
        if (answer.equals("S")) {
            database.searchForSpace(type, plate);
        } else if (answer.equals("R")) {
            System.out.println("Enter the vehicle's zone and the space on separate lines:");
            System.out.print("Zone: ");
            String zone = scan.nextLine();
            System.out.print("Space: ");
            String space = scan.nextLine();
            attendants.setAttendant(space, zone);
            database.inputSpace(type, plate, zone, space);
        } else {
            System.err.println("Please enter a valid option");
            attendantParking(type, plate);
        }
    }



    public static void main(String[] args) {
        Application app = new Application();
        app.runMenu();
    }
}
