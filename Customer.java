public class Customer extends Person {
    private String customerId;

    public Customer(String customerId, String name, String phone, String address) {
        super(name, phone, address);
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }
}
