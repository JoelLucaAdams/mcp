import java.util.Scanner;

import util.mcp.database.Attendants;
import util.mcp.database.Database;
import util.mcp.database.ZoneCost;

/**
 * The admin class allows the admin to change information about the attendants and also change the different zone cost
 * @author Joel Adams (joa38)
 * @version 1.0
 */
public class Admin {

    private Scanner scan;
    private Attendants attendant;
    private ZoneCost zoneCost;
    private Database database;

    /**
     * The admin constructor has these parameters so that it can load in the file without any problems occuring
     * @param zoneCost allows for the loading of the zone cost file
     * @param database allows for the loading of the database file
     * @param attendant allows for the loading of the attendants file
     * @param scan loads a scanner without creating a new instance of it
     */
    Admin (ZoneCost zoneCost, Database database, Attendants attendant, Scanner scan) {
        this.scan = scan;
        this.attendant = attendant;
        this.zoneCost = zoneCost;
        this.database = database;
    }

    /**
     * This is a do while loop with a nested switch statement that runs the different options of the admin class
     */
    public void runAdmin() {
        String choice;
        do {
            System.out.println("Welcome Admin.");
            printAdminMenu();
            choice = scan.nextLine().toUpperCase();
            switch(choice) {
                case "1":
                    addAttendant();
                    break;
                case "2":
                    removeAttendant();
                    break;
                case "3":
                    displayAttendants();
                    break;
                case "4":
                    displayCarPark();
                    break;
                case "5":
                    changeZone();
                    break;
                case "Q":
                    attendant.save();
                    System.out.println("Exiting admin section...\n");
                    break;
                default:
                    System.err.println("Invalid option selected, please try again");
                    break;
            }
        } while(!(choice.equals("Q")));
    }
    private void printAdminMenu() {
        System.out.println("1 - Add attendant");
        System.out.println("2 - Remove attendant");
        System.out.println("3 - Display attendants");
        System.out.println("4 - Display car park");
        System.out.println("5 - Change the cost of a zone");
        System.out.println("Q - quit");
    }

    private void addAttendant() {
        System.out.println("Attendant name: ");
        String name = scan.nextLine();
        attendant.addAttendant(name);
    }

    private void removeAttendant() {
        displayAttendants();
        System.out.println("Which attendant do you want to remove?");
        String name = scan.nextLine();
        attendant.removeAttendant(name);
    }

    private void displayAttendants() {
        attendant.printAttendantsAdmin();
    }

    private void displayCarPark() {
        zoneCost.printZoneCost();
        attendant.printAttendantsAdmin();
        System.out.println(database.printCarPark());
    }

    private void changeZone() {
        System.out.println("Here are the current zones");
        zoneCost.printZoneCost();
        System.out.println("Which zone do you want to change (Zone1-Zone5)");
        String zone = scan.nextLine();
        System.out.println("What do you want to change the cost to?");
        String cost = scan.nextLine();
        zoneCost.changeZone(zone, cost);
        zoneCost.printZoneCost();
    }

}
