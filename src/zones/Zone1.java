package zones;

/**
 *This class extends Zone and is in charge of the first zone of the car park
 * @author Joel Adams (joa38)
 * @version 1.0
 */
public class Zone1 extends Zone {

    public Zone1() {
        super();
    }

    public Zone1(String space, boolean occupied, String reserved, String plate, String type, String receipt) {
        super(space, occupied, reserved, plate, type, receipt);
    }

    /**
     * @return 1 as it is the first zone
     */
    @Override
    public String getZone() {
        return "1";
    }

    /**
     * This checks which vehicles can park here
     * @param vehicleType is the type of vehicle's
     * @return 1 or 2
     */
    @Override
    public boolean checkVehicles(String vehicleType) {
        return (vehicleType.equals("1") || vehicleType.equals("2"));
    }
}
