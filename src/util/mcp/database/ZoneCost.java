package util.mcp.database;

import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

/**
 * This class is in charge of dealing with the different cost of the zones in the car park
 * @author Joel Adams (joa38)
 * @version 1.0
 */
public class ZoneCost {

    private Properties pro;
    private String zone;
    private float cost;
    private ArrayList<ZoneCost> zones;

    public ZoneCost() {
        pro = new Properties();
        zones = new ArrayList<>();
    }

    /**
     * This is private so that a new zone cannot be created without calling the admin class
     * @param zone the name of the new zone
     * @param cost the cost of this new zone
     */
    private ZoneCost(String zone, float cost) {
        this.zone = zone;
        this.cost = cost;
        new ZoneCost();
    }

    /**
     * This method takes a long which would be the current time in milliseconds and the zone, it then checks the cost
     * of the zone and multiplies it by the time in hours
     * @param time is the current time in milliseconds
     * @param zone is the corresponding zone
     * @return a float which consists of the cost of the zone
     */
    float getTimeCost(long time, String zone) {
        float cost = getZoneCost(zone);
        cost = cost * getTimeInHours(time);
        return cost;
    }

    public float getCost() {
        return cost;
    }

    public String getZone() {
        return zone;
    }

    /**
     * This method iterates through an Arraylist of the current available zones and checks if their name matches up
     * to String and gets the cost of the zone
     * @param zone is the current cost of the zone that the user is trying to find
     * @return a float of the cost of this zone per hour
     */
    private float getZoneCost(String zone) {
        float costOfZone = 0.0f;
        for (ZoneCost z : zones) {
            if (zone.equals(z.getZone())) {
                costOfZone = z.getCost();
            }
        }
        return costOfZone;
    }

    /**
     * changes the time in milliseconds to hours so that it can be easily multiplied by the cost of the zone per hour
     * @param ms is the current time in milliseconds
     * @return a float of the current time in hours
     */
    private float getTimeInHours(long ms) {
        double time;
        time = ms * 2.7777778 * Math.pow(10, -7);
        return correctDecimalCost(time);
    }

    /**
     * converts a double to a float with 2 decimal places
     * @param cost is of type double
     * @return a float of the correct value with the right amount of decimal places
     */
    private float correctDecimalCost(double cost) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return Float.parseFloat(df.format(cost));
    }

    /**
     * This method is used to pay and also checks if they input the correct amount of money and returns the correct
     * amount of change
     * @param inputMoney is a String with the money they input
     * @param cost is a float of the money that the user has to pay
     * @return float with money they still need to pay
     */
    float pay(String inputMoney, float cost) {
        float input;
        double change = 0.0f;
        input = Float.parseFloat(inputMoney) - cost;
        if (input > 0) {
            System.out.println("Payment successful with " + correctDecimalCost(input) + " units change");
        } else if (input < 0) {
            System.out.println("Payment not successful you still need to pay " + correctDecimalCost(Math.abs(input)) +
                    " units");
            change = Math.abs(input);
        } else {
            System.out.println("Payment successful");
        }
        return correctDecimalCost(change);
    }

    /**
     * Loads in the .properties file and adds them to an ArrayList
     */
    public void load() {
        InputStream in = null;

        try {
            in = new FileInputStream("cost.properties"); //determines the file to load in called "cost.properties"
            pro.load(in);

            Enumeration<?> e = pro.propertyNames();
            while (e.hasMoreElements()) { //iterates through all the elements in the .properties file
                String zone = (String) e.nextElement();
                float cost = Float.parseFloat(pro.getProperty(zone));
                ZoneCost newZone = new ZoneCost(zone, cost);
                zones.add(newZone); //adds each new zone to an array list of zones
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     *This is the replacement for the save method as the you can only change 1 zone at a time and that automatically
     * saves and loads the file again
     * @param key is the zone they want to change. It must be a zone that is already created e.g. Zone1
     * @param units is the amount of units they want to change it to
     */
    public void changeZone(String key, String units) {
        OutputStream out = null;

        if (key.equals("Zone1") || key.equals("Zone2") || key.equals("Zone3") || key.equals("Zone4")
                || key.equals("Zone5")) { //checks if the key String is equal to the name of one of the keys in the properties file
            try {
                out = new FileOutputStream("cost.properties");

                pro.setProperty(key, units);
                pro.store(out, null);
                zones.clear();
                load();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } else {
            System.err.println("Please enter a valid zone");
        }
    }

    @Override
    public String toString() {
        String s = zone + ", costs "+ cost;
        return s;
    }

    public void printZoneCost() {
        StringBuilder sb = new StringBuilder();
        for (ZoneCost z: zones) {
            sb.append(z).append("\n");
        }
        System.out.println("Zones: \n" + sb);
    }
}