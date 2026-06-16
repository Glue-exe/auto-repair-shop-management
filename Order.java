import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private String orderNo;
    private Customer billedTo; // Customer to pay (ลูกค้าที่รับบริการ)
    private Car targetCar; // Car being repaired (รถที่นำมาซ่อม)
    private Mechanic mechanic; // Mechanic assigned
    private LocalDate inputDate;
    private String status;
    private List<Item> items = new ArrayList<>(); // Products and labor fees (รายการสินค้าและค่าบริการ)
    private String paymentStatus;

    public Order(String orderNo, Customer billedTo, Car targetCar, Mechanic mechanic, LocalDate inputDate, String status, String paymentStatus) {
        this.orderNo = orderNo;
        this.billedTo = billedTo;
        this.targetCar = targetCar;
        this.mechanic = mechanic;
        this.inputDate = inputDate;
        this.status = status;
        this.paymentStatus = paymentStatus;
    }

    // Calculation for total amount (คำนวณยอดเงินรวม)
    public double calculateTotal() {
        double total = 0;
        for (Item i : items) {
            total += i.getPrice();
        }
        return total;
    }

public void printBill() {
        double total = calculateTotal();
        System.out.println("\n========================================");
        System.out.println("            RECEIPT / ORDER             ");
        System.out.println("========================================");
        System.out.println("Order No    : " + orderNo);
        System.out.println("Date        : " + inputDate);
        System.out.println("Mechanic    : " + mechanic.getName());
        System.out.println("----------------------------------------");
        System.out.println("Customer ID : " + billedTo.getCustomerId());
        System.out.println("Name        : " + billedTo.getName());
        System.out.println("License     : " + targetCar.getLicensePlate() + " (" + targetCar.getBrand() + ")");
        System.out.println("----------------------------------------");
        System.out.println("Items:");
        for (Item item : items) {
            System.out.printf(" - %-20s %8.2f THB\n", item.getName(), item.getPrice());
        }
        System.out.println("----------------------------------------");
        System.out.println("Total Amount: " + total + " THB");
        System.out.println("Status      : " + status); // สถานะการซ่อม
        System.out.println("Payment     : " + paymentStatus);
        System.out.println("========================================\n");
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public Customer getBilledTo() {
        return billedTo;
    }

    public Car getTargetCar() {
        return targetCar;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public LocalDate getInputDate() {
        return inputDate;
    }

    public String getStatus() {
        return status;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setPaymentStatus(String paymentStatus) { 
        this.paymentStatus = paymentStatus; 
    }

    public String getPaymentStatus() { 
        return paymentStatus; 
    }
}
