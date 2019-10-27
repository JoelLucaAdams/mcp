package zones;

/**
 *This class extends Zone and is in charge of the third zone of the car park
 * @author Joel Adams (joa38)
 * @version 1.0
 */
public class Zone3 extends Zone {

    public Zone3() {
        super();
    }

    public Zone3(String space, boolean occupied, String reserved, String plate, String type, String receipt) {
        super(space, occupied, reserved, plate, type, receipt);
    }

    /**
     * @return 3 as it is the third zone
     */
    @Override
    public String getZone() {
        return "3";
    }

    /**
     * This checks which vehicles can park here
     * @param vehicleType is the type of vehicle's
     * @return 4
     */
    @Override
    public boolean checkVehicles(String vehicleType) {
        return (vehicleType.equals("4"));
    }
}
