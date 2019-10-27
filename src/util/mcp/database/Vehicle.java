package util.mcp.database;

public class Vehicle {

    private String licencePlate;
    private String vehicleType;

    public Vehicle() {

    }

    public Vehicle(String plate, String type) {
        this.licencePlate = plate;
        setVehicleType(type);
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getVehicleTypeAsNumber() {
        switch (vehicleType) {
            case "Standard vehicle":
                vehicleType = "1";
                break;
            case "Higher vehicle":
                vehicleType = "2";
                break;
            case "Longer vehicle":
                vehicleType = "3";
                break;
            case "Coach":
                vehicleType = "4";
                break;
            case "Motorbike":
                vehicleType = "5";
                break;
            case "N/A":
                vehicleType = "N/A";
                break;
        }
        return vehicleType;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    private void setVehicleType(String type) {
        switch(type) {
            case "1":
                vehicleType = "Standard vehicle";
                break;
            case "2":
                vehicleType = "Higher vehicle";
                break;
            case "3":
                vehicleType = "Longer vehicle";
                break;
            case "4":
                vehicleType = "Coach";
                break;
            case "5":
                vehicleType = "Motorbike";
                break;
            case "N/A":
                vehicleType = "N/A";
                break;
            default:
                System.err.println("Something went wrong. Cannot assign vehicle");
        }
    }

    @Override
    public String toString() {
        String car;
        if (licencePlate != null) {
            car = "LicencePlate: " + licencePlate + ", Type: " + vehicleType;
        } else {
            car = "licencePlate: N/A Type: N/A";
        }
        return car;
    }
}