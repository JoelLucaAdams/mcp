package zones;

/**
 *This class extends Zone and is in charge of the second zone of the car park
 * @author Joel Adams (joa38)
 * @version 1.0
 */
public class Zone2 extends Zone {

    public Zone2() {
        super();
    }

    public Zone2(String space, boolean occupied, String reserved, String plate, String type, String receipt) {
        super(space, occupied, reserved, plate, type, receipt);
    }

    /**
     * @return 2 as it is the second zone
     */
    @Override
    public String getZone() {
        return "2";
    }

    /**
     * This checks which vehicles can park here
     * @param vehicleType is the type of vehicle's
     * @return 3
     */
    @Override
    public boolean checkVehicles(String vehicleType) {
        return (vehicleType.equals("3"));
    }
}
