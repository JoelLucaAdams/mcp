package zones;

/**
 *This class extends Zone and is in charge of the fifth zone of the car park
 * @author Joel Adams (joa38)
 * @version 1.0
 */
public class Zone5 extends Zone {

    public Zone5() {
        super();
    }

    public Zone5(String space, boolean occupied, String reserved, String plate, String type, String receipt) {
        super(space, occupied, reserved, plate, type, receipt);
    }

    /**
     * @return 5 as it is the fifth zone
     */
    @Override
    public String getZone() {
        return "5";
    }

    /**
     * This checks which vehicles can park here
     * @param vehicleType is the type of vehicle's
     * @return 5
     */
    @Override
    public boolean checkVehicles(String vehicleType) {
        return (vehicleType.equals("5"));
    }
}
