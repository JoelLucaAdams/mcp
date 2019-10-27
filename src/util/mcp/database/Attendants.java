package util.mcp.database;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;

public class Attendants {

    private String name;
    private boolean attendantOccupied;
    private String space;
    private String zone;
    private ArrayList<Attendants> attendants;

    public Attendants() {
        attendants = new ArrayList<>();
    }

    public Attendants(String name) {
        this.name = name;
        this.attendantOccupied = false;
        this.space = "N/A";
        this.zone = "N/A";
    }

    public Attendants(String name, boolean attendantOccupied, String space, String zone) {
        this.name = name;
        this.attendantOccupied = attendantOccupied;
        this.space = space;
        this.zone = zone;
    }

    public boolean isAttendantOccupied() {
        return attendantOccupied;
    }

    public void setAttendantOccupied(boolean attendantOccupied) {
        this.attendantOccupied = attendantOccupied;
    }

    public String getName() {
        return name;
    }

    public String getSpace() {
        return space;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public void addAttendant(String name) {
        Attendants at = new Attendants(name);
        attendants.add(at);
    }

    public String callAttendant() {
        String attendant = "Sorry but there are no free attendants";
        for (Attendants at : attendants) {
            if (!at.isAttendantOccupied()) {
                at.setAttendantOccupied(true);
                attendant = "You are being served by attendant: " + at.getName();
                break;
            }
        }
        return attendant;
    }

    public void setAttendant(String space, String zone) {
        for (Attendants at : attendants) {
            if (at.isAttendantOccupied() && at.getSpace().equals("N/A") && at.getZone().equals("N/A")) {
                at.setSpace(space);
                at.setZone(zone);
                break;
            }
        }
    }

    public void makeAttendantFree(String space, String zone) {
        for (Attendants at : attendants) {
            if (at.getZone().equals(zone) && at.getSpace().equals(space)) {
                System.out.println("The attendant " + at.getName() + " is returning the vehicle to the customer");
                at.setZone("N/A");
                at.setAttendantOccupied(false);
                at.setSpace("N/A");
                break;
            }
        }
    }

    public void removeAttendant(String name) {
        for (Attendants at : attendants) {
            if (at.getName().equals(name)) {
                attendants.remove(at);
                System.out.println("Removed attendant " + name);
                break;
            }
        }
    }

    public void load() {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("attendants.json"));

            JSONObject jsonObject = (JSONObject) obj;

                JSONArray attendant = (JSONArray) jsonObject.get("attendants");
            for (Object o : attendant) {
                JSONObject at = (JSONObject) o;
                String name = (String) at.get("name");
                Boolean occupied = (Boolean) at.get("occupied");
                String space = (String) at.get("space");
                String zone = (String) at.get("zone");
                attendants.add(new Attendants(name, occupied, space, zone));
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        JSONObject file = new JSONObject();
        JSONArray employees = new JSONArray();

        for (Attendants at: attendants) {
            JSONObject object = new JSONObject();
            object.put("name", at.getName());
            object.put("occupied", at.isAttendantOccupied());
            object.put("space", at.getSpace());
            object.put("zone", at.getZone());

            employees.add(object);
        }
        file.put("attendants", employees);

        try (FileWriter f = new FileWriter("attendants.json")) {

            f.write(file.toJSONString());
            f.flush();

        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void printAttendantsAdmin() {
        String a = "Attendants: \n";
        StringBuilder data = new StringBuilder();
        for (Attendants at: attendants) {
            data.append(at).append("\n");
        }
        a += data;
        System.out.println(a);
    }

    public void printAttendants() {
        String a = "Attendants: \n";
        StringBuilder data = new StringBuilder();
        for (Attendants at: attendants) {
            data.append(at.getName()).append("\n");
        }
        a += data;
        System.out.println(a);
    }

    @Override
    public String toString() {
        return "name: " + name + " occupied: " + attendantOccupied + " space: " + space + " zone: " + zone;
    }
}
