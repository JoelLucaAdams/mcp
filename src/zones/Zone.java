package zones;

import util.mcp.database.Vehicle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * The Zone class is an abstract class that has 5 subclasses which are in charge of each one of the zones in the car
 * park
 * @author Joel Adams (joa38)
 * @version 1.0
 */
public abstract class Zone {

    private String parkingSpace;
    private boolean spaceOccupied;
    private Calendar timeReserved;
    private String noTimeReserved;
    private String parkingReceipt;
    private Vehicle vehicle;

    Zone() {
        vehicle = new Vehicle();
    }

    /**
     * The constructor creates a new space in the zone. Each subclass calls this constructor.
     */
    Zone(String space, boolean occupied, String reserved, String plate, String type, String receipt) {
        this();
        this.parkingSpace = space;
        this.spaceOccupied = occupied;
        setTimeReserved(reserved);
        vehicle = new Vehicle(plate, type);
        this.parkingReceipt = receipt;
    }

    /**
     * As this is abstract each subclass has this function.
     * @return the zone number e.g. Zone1 returns "1"
     */
    public abstract String getZone();

    /**
     * This method checks the different types of vehicle's allowed in each zone
     * @param vehicleType is the type of vehicle's
     * @return true if the vehicleType is the same as the allowed vehicles in the zone
     */
    public abstract boolean checkVehicles(String vehicleType);

    public final String getParkingSpace() {
        return parkingSpace;
    }

    public final boolean isSpaceOccupied() {
        return spaceOccupied;
    }

    /**
     * If no time is reserved then it uses a SimpleDateFormat and returns the reserved time otherwise it returns N/A
     * @return the reserved time of the vehicle in a String
     */
    public final String getTimeReserved() {
        String time;
        if (noTimeReserved != null) {
            time = noTimeReserved;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            time = sdf.format(timeReserved.getTime());
        }
        return time;
    }

    /**
     * Converts the time to milliseconds
     * @return the time in a long or 0 if the space is N/A
     */
    public final long getTimeInMillis() {
        long time;
        if (noTimeReserved != null) {
            time = 0;
        } else {
            time = timeReserved.getTimeInMillis();
        }
        return time;
    }

    public final String getParkingReceipt() {
        return parkingReceipt;
    }

    public final String getVehicleType() {
        return vehicle.getVehicleTypeAsNumber();
    }

    public final String getLicencePlate() {
        return vehicle.getLicencePlate();
    }

    public final void setSpaceOccupied(boolean spaceOccupied) {
        this.spaceOccupied = spaceOccupied;
    }

    /**
     * Sets the time reserved for the vehicle and assigns it to either noTimeReserved if the timeReserved is N/A
     * or it parses in the time reserved using SimpleDateFormat
     * @param timeReserved is the time reserved of the vehicle in the space
     */
    public final void setTimeReserved(String timeReserved) {
        try {
            noTimeReserved = null;
            if (timeReserved.equals("N/A")) {
                this.noTimeReserved = timeReserved;
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date dateDate = sdf.parse(timeReserved);
                Calendar date = Calendar.getInstance();
                date.setTime(dateDate);
                this.timeReserved = date;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public final void setParkingReceipt(String parkingReceipt) {
        this.parkingReceipt = parkingReceipt;
    }

    public final void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public String toString() {
        String space = "Zone: " + getZone() +
                " Space: " + parkingSpace +
                " Occupied: " + spaceOccupied +
                " TimeReserved: " + getTimeReserved() +
                " Vehicle: " + vehicle.toString() +
                " Receipt: " + parkingReceipt;
        return space;
    }


}
