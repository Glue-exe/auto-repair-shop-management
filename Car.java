public class Car {
    private String licensePlate;
    private String brand;
    private String carModel; 
    private Customer owner; // Reference to the customer who owns this car

    public Car(String licensePlate, String brand, String carModel, Customer owner) {
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.carModel = carModel;
        this.owner = owner;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public void setOwner(Customer owner) {
        this.owner = owner;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getBrand() {
        return brand;
    }

    public String getCarModel() {
        return carModel;
    }

    public Customer getOwner() {
        return owner;
    }
}
