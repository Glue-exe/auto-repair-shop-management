public class Service extends Item {
    private String itemId;

    public Service(String itemId, String name, double price) {
        super(name, price);
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }
}
