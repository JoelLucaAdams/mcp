package util.mcp.database;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import zones.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The database class is in charge of loading in the actual car park and assigning each space to a ArrayList of
 * different zones
 * @author Joel Adams (joa38)
 * @version 1.0
 */
public class Database {

    private ZoneCost zoneCost;
    private Scanner scan;
    private Attendants attendants;
    private ArrayList<Zone> spaces;
    private String[] zones = {"zone1", "zone2", "zone3", "zone4", "zone5"};

    /**
     * Similar to the Admin constructor it calls objects from the Application class
     * @param zoneCost loads in the zone cost file
     * @param attendant loads in the attendants file
     * @param scan calls a scanner
     */
    public Database(ZoneCost zoneCost, Attendants attendant, Scanner scan) {
        spaces = new ArrayList<>();
        this.scan = scan;
        this.zoneCost = zoneCost;
        this.attendants = attendant;
    }

    /**
     * This function loads in the file database.json and using an external library json.simple. It parses in the data
     * from the file and assigns each new Json Object to a zone and space which is then added to an ArrayList
     */
    public void load() {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("database.json"));

            JSONObject jsonObject = (JSONObject) obj;

            for (String z : zones) {

                JSONArray zone = (JSONArray) jsonObject.get(z);
                int s = 0;
                while (s < 20) {
                    JSONObject space = (JSONObject) zone.get(s);

                    String spaceID = (String) space.get("spaceID");
                    Boolean spaceOccupied = (Boolean) space.get("spaceOccupied");
                    String date = (String) space.get("date");
                    String vehicleType = (String) space.get("vehicleType");
                    String numberPlate = (String) space.get("numberPlate");
                    String receipt = (String) space.get("receipt");

                    Zone a = null;
                    switch (z) {
                        case "zone1":
                            a = new Zone1(spaceID, spaceOccupied, date, numberPlate, vehicleType, receipt);
                            break;
                        case "zone2":
                            a = new Zone2(spaceID, spaceOccupied, date, numberPlate, vehicleType, receipt);
                            break;
                        case "zone3":
                            a = new Zone3(spaceID, spaceOccupied, date, numberPlate, vehicleType, receipt);
                            break;
                        case "zone4":
                            a = new Zone4(spaceID, spaceOccupied, date, numberPlate, vehicleType, receipt);
                            break;
                        case "zone5":
                            a = new Zone5(spaceID, spaceOccupied, date, numberPlate, vehicleType, receipt);
                            break;
                        default:
                            System.err.println("File must be corrupt");
                            break;
                    }
                    spaces.add(a);
                    s++;
                }
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function works in the complete opposite way to the load function. It creates 5 Json Arrays which are
     * responsible for each zone in the car park, it then iterates over the ArrayList of zones and spaces and assigns
     * them to a json object which adds it to the corresponding zone.
     */
    public void save() {
        JSONObject file = new JSONObject();

        JSONArray zone1 = new JSONArray();
        JSONArray zone2 = new JSONArray();
        JSONArray zone3 = new JSONArray();
        JSONArray zone4 = new JSONArray();
        JSONArray zone5 = new JSONArray();

        for (Zone s: spaces) {
            JSONObject object = new JSONObject();

            object.put("spaceID", s.getParkingSpace());
            object.put("spaceOccupied", s.isSpaceOccupied());
            object.put("date", s.getTimeReserved());
            object.put("vehicleType", s.getVehicleType());
            object.put("numberPlate", s.getLicencePlate());
            object.put("receipt", s.getParkingReceipt());

            switch (s.getZone()) {
                case "1":
                    zone1.add(object);
                    break;
                case "2":
                    zone2.add(object);
                    break;
                case "3":
                    zone3.add(object);
                    break;
                case "4":
                    zone4.add(object);
                    break;
                case "5":
                    zone5.add(object);
                    break;
            }
        }
        file.put("zone1", zone1);
        file.put("zone2", zone2);
        file.put("zone3", zone3);
        file.put("zone4", zone4);
        file.put("zone5", zone5);

        try (FileWriter f = new FileWriter("database.json")) {

            f.write(file.toJSONString());
            f.flush();

        } catch (IOException io) {
            io.printStackTrace();
        }
    }


    /**
     * This function simply iterates over the spaces in the car park and checks for a free space in the correct zone
     * depending on the vehicle type
     * @param type is the type of vehicle being added
     * @param plate is the number plate of said vehicle
     */
    public void searchForSpace(String type, String plate) {
        try {
            for (Zone s : spaces) {
                if (s.checkVehicles(type) && !s.isSpaceOccupied()) {
                        setSpaces(s, plate, type);
                    System.out.println("Please park your vehicle in space: " + s.getParkingSpace() +
                            " in zone: " + s.getZone());
                    System.out.println("Your parking receipt is " + s.getParkingReceipt());
                        attendants.setAttendant(s.getParkingSpace(), s.getZone());
                        break;
                }
            }
        } catch(InputMismatchException e){
            e.printStackTrace();
        }
    }

    /**
     * This function is only accessed by the attendants and checks if the space they have inputted is free otherwise
     * it tells them that the space isn't free
     * @param type is the vehicle's type
     * @param plate is the vehicle's plate
     * @param zone is the zone the attendant has input
     * @param space is the space the attendant has input
     */
    public void inputSpace(String type, String plate, String zone, String space) {
        boolean freeSpace = false;
        for (Zone s : spaces) {
            if (s.getZone().equals(zone) && s.getParkingSpace().equals(space) && s.checkVehicles(type) && !s.isSpaceOccupied()) {
                setSpaces(s, plate, type);
                System.out.println("The vehicle has been assigned to this space");
                System.out.println("The parking receipt is " + s.getParkingReceipt());
                freeSpace = true;
                break;
            }
        }
        if (!freeSpace) {
            System.out.println("Sorry but this space is not free or the incorrect zone is used");
        }
    }

    /**
     * This function sets the vehicle in the space and also puts in the current time in the correct SimpleDateFormat
     * @param s is the zone/space of the vehicle
     * @param plate is the vehicle's number plate
     * @param type is the vehicle's type
     */
    private void setSpaces(Zone s, String plate, String type) {
        s.setSpaceOccupied(true);
        s.setVehicle(new Vehicle(plate, type));
        s.setParkingReceipt(parkingReceipt());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        s.setTimeReserved(sdf.format(calendar.getTime()));
    }

    /**
     * The function is called when the user wishes to exit the car park and they need to enter their corresponding
     * receipt
     * @param receipt is a number in String format
     */
    public boolean checkParkingReceipt(String receipt) {
        boolean isReceiptCorrect = false;
            for (Zone s : spaces) {
                if (s.getParkingReceipt().equals(receipt)) {
                    System.out.println("Please collect your vehicle from zone: " + s.getZone() + " in space " +
                            s.getParkingSpace());
                    s.setSpaceOccupied(false);
                    s.setParkingReceipt("N/A");
                    s.setVehicle(new Vehicle("N/A", "N/A"));
                    long time = s.getTimeInMillis();
                    s.setTimeReserved("N/A");
                    Calendar calendar = Calendar.getInstance();
                    long currentTime = calendar.getTimeInMillis();
                    long difference = currentTime - time;
                    String zone = "Zone" + s.getZone();
                    float cost = zoneCost.getTimeCost(difference, zone);
                    System.out.println("If you're disabled press D otherwise press any other button");
                    String answer = scan.next().toUpperCase();
                    if (answer.equals("D")) {
                        cost = cost / 2;
                    }
                    if (answer.equals("D") && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        System.out.println("");
                    } else {
                        System.out.println("Please pay " + cost + " units");
                        boolean paid = false;
                        while (!paid) {
                            String payment = scan.next();
                            cost = zoneCost.pay(payment, cost);
                            if (cost == 0) {
                                paid = true;
                            }
                        }
                    }
                    attendants.makeAttendantFree(s.getParkingSpace(), s.getZone());
                    isReceiptCorrect = true;
                    break;
                }
            }
            return isReceiptCorrect;
    }

    /**
     * This function checks all the current vehicle receipts and adds 1 to them
     * @return String with the next available parking receipt
     */
    private String parkingReceipt() {
        String newReceipt = "";
        int numOfReceipts = 0;
        for (Zone s: spaces) {
            if (!s.getParkingReceipt().equals("N/A")) {
                numOfReceipts ++;
            }
        }
        newReceipt = String.valueOf(numOfReceipts + 1);
        return newReceipt;
    }

    /**
     * The toString method creates a new StringBuilder and prints off the spaces that are currently taken in the car
     * park
     * @return String with all occupied spaces in car park
     */
    @Override
    public String toString() {
        String result = "Spaces occupied in car park: \n";
        StringBuilder sb = new StringBuilder();
        for(Zone d: spaces) {
            if (d.isSpaceOccupied()) {
                sb.append("Zone: ").append(d.getZone()).append(" space: ").append(d.getParkingSpace()).append("\n");
            }
        }
        result += sb;
        return result;
    }

    /**
     * This method also uses a StringBuilder like toString but it displays every space in the car park and their
     * corresponding information
     * @return String of all the spaces in the car park
     */
    public String printCarPark() {
        String result = "Car Park: \n";
        StringBuilder sb = new StringBuilder();
        for(Zone d: spaces) {
            sb.append(d).append("\n");
        }
        result += sb;
        return result;
    }
}