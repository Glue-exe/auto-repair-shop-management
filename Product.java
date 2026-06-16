public class Product extends Item {
    private String itemId; 
    private String brand; 
    private String carType; 

    public Product(String name, double price, String ItemId, String brand, String carType) {
        super(name, price);
        this.itemId = ItemId;
        this.brand = brand;
        this.carType = carType;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getItemId() {
        return itemId;
    }

    public String getBrand() {
        return brand;
    }

    public String getCarType() {
        return carType;
    }
}
