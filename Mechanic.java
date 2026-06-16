import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Mechanic extends Person {
    private String mechanicId;
    private String expertLevel; 

    // Lists to store system data
    private List<Customer> customerList = new ArrayList<>();
    private List<Car> carList = new ArrayList<>();
    private List<Product> productList = new ArrayList<>();
    private List<Service> serviceList = new ArrayList<>();
    private List<Order> orderList = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public Mechanic(String name, String phone, String address, String mechanicId, String expertLevel) {
        super(name, phone, address);
        this.mechanicId = mechanicId;
        this.expertLevel = expertLevel;
    }

    public void saveData() {
        JsonStorageManager.saveCustomers(this.customerList);
        JsonStorageManager.saveCars(this.carList);
        JsonStorageManager.saveProducts(this.productList);
        JsonStorageManager.saveServices(this.serviceList);
        JsonStorageManager.saveOrders(this.orderList);
    }

    public void loadData() {
        customerList.clear();
        carList.clear();
        productList.clear();
        serviceList.clear();
        orderList.clear();

        JsonStorageManager.loadCustomers(this.customerList);
        JsonStorageManager.loadCars(this.carList, this.customerList);
        JsonStorageManager.loadProducts(this.productList);
        JsonStorageManager.loadServices(this.serviceList);
        JsonStorageManager.loadOrders(this.orderList, this.customerList, this.carList, this);
    }

    public void register() {
        System.out.println("===== Register =====");

        // check user in system.
        System.out.println("--- Customers ---");
        if (customerList.isEmpty()) {
            System.out.println("  No customers in system");
        } else {
            for (int i = 0; i < customerList.size(); i++) {
                Customer c = customerList.get(i);
                System.out.println("  ID: " + c.getCustomerId() + " | Name: " + c.getName());
            }
        }

        // id
        String customerId = "";
        while (true) {
            System.out.printf("customerId (0 to cancel): ");
            customerId = scanner.nextLine().trim();

            if (customerId.equals("0")) {
                System.out.println("Return to main menu\n");
                return;
            }

            // check id Duplicate
            boolean isDuplicate = false;
            for (Customer c : customerList) {
                if (c.getCustomerId().equals(customerId)) {
                    isDuplicate = true;
                    break;
                }
            }

            // if true
            if (isDuplicate) {
                System.out.println("Register failed, id already in system");
            } else if (!customerId.matches("^[0-9]+$")) {
                System.out.println("Please enter numbers.");
            } else {
                break;
            }
        }

        // name
        String name = "";
        while (true) {
            System.out.printf("Name: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Please enter your name.");
            } else if (!name.matches("^[a-zA-Z\\u0E00-\\u0E7F\\s\\-\\.\\']+$")) {
                System.out.println("Please enter only text.");
            } else if (name.length() < 4) {
                System.out.println("Please enter more than 4 characters.");
            } else {
                break;
            }
        }

        // phone
        String phone = "";
        while (true) {
            System.out.print("Phone: ");
            phone = scanner.nextLine().trim();

            if (phone.isEmpty()) {
                System.out.println("Please enter your phone number.");
            } else if (!phone.matches("^[0-9]{9,10}$")) {
                System.out.println("Please enter numbers 9-10 digits.");
            } else {
                break;
            }
        }

        // address
        String address = "";
        while (true) {
            System.out.print("Address: ");
            address = scanner.nextLine().trim();

            if (address.isEmpty()) {
                System.out.println("Please enter your address.");
            } else if (!address.matches("^[a-zA-Z0-9\\u0E00-\\u0E7F/\\s\\-.,()]+$")) {
                System.out.println("Please enter valid text.");
            } else if (address.length() < 4) {
                System.out.println("Please enter more than 4 characters.");
            } else {
                break;
            }
        }

        // licensePlate
        String licensePlate = "";
        while (true) {
            System.out.printf("licensePlate: ");
            licensePlate = scanner.nextLine().trim().toUpperCase();

            if (licensePlate.isEmpty()) {
                System.out.println("Please enter your licensePlate.");
            } else if (!licensePlate.matches("^[a-zA-Z0-9\\u0E00-\\u0E7F\\s]+$")) {
                System.out.println("Please enter only text and number.");
            } else if (licensePlate.length() < 3) {
                System.out.println("Please enter more than 3 characters.");
            } else {
                break;
            }
        }

        // brand
        String brand = "";
        String carModel = "";
        while (true) {

            while (true) {
                System.out.println("\nSelect Car Brand:");
                System.out.println("[1] Toyota");
                System.out.println("[2] Isuzu");
                System.out.println("[3] Honda");
                System.out.println("[4] Other / Unknown");
                System.out.printf("Select Car Brand (1-4): ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        brand = "Toyota";
                        break;
                    case "2":
                        brand = "Isuzu";
                        break;
                    case "3":
                        brand = "Honda";
                        break;
                    case "4":
                        brand = "";
                        break;
                    default:
                        System.out.println("\nInvalid choice. Please enter 1-4.");
                        continue;
                }
                break;
            }

            // Return to select car brand.
            boolean isGoBack = false;
            if (brand.equals("")) {
                while (true) {
                    System.out.println("[0] Return to Select Car Brand.");
                    System.out.print("Brand: ");
                    brand = scanner.nextLine().trim().toUpperCase();

                    if (brand.equals("0")) {
                        isGoBack = true;
                        break;
                    }
                    if (brand.isEmpty()) {
                        System.out.println("Please enter your brand.");
                    } else if (!brand.matches("^[a-zA-Z0-9/\\s-.]+$")) {
                        System.out.println("Please enter valid text or numbers.");
                    } else if (brand.length() <= 1) {
                        System.out.println("Please enter more than 1 character.");
                    } else {
                        break;
                    }
                }

                // if != false (case 4)
                if (!isGoBack) {
                    while (true) {
                        System.out.print("carModel: ");
                        carModel = scanner.nextLine().trim();
                        if (carModel.isEmpty()) {
                            System.out.println("Please enter your car model.");
                        } else if (!carModel.matches("^[a-zA-Z0-9/\\s-.]+$")) {
                            System.out.println("Please enter valid text or numbers.");
                        } else if (carModel.length() <= 1) {
                            System.out.println("Please enter more than 1 character.");
                        } else {
                            break;
                        }
                    }
                }
            }

            // Car Model
            if (brand.equalsIgnoreCase("Toyota") || brand.equalsIgnoreCase("Isuzu") || brand.equalsIgnoreCase("Honda")) {
                while (true) {
                    System.out.println("\nSelect Car Model for " + brand + ":");
                    if (brand.equalsIgnoreCase("Toyota")) {
                        System.out.println("[1] Hilux Revo");
                        System.out.println("[2] Yaris ATIV");
                        System.out.println("[3] Yaris Cross");
                        System.out.println("[0] Return to select car brand.");
                    } else if (brand.equalsIgnoreCase("Isuzu")) {
                        System.out.println("[1] D-Max");
                        System.out.println("[2] MU-X");
                        System.out.println("[0] Return to select car brand.");
                    } else if (brand.equalsIgnoreCase("Honda")) {
                        System.out.println("[1] HR-V");
                        System.out.println("[2] City Hatchback");
                        System.out.println("[3] City Sedan");
                        System.out.println("[0] Return to select car brand.");
                    }

                    System.out.printf("Select carModel: ");
                    String modelChoice = scanner.nextLine();

                    if (modelChoice.equals("0")) {
                        isGoBack = true;
                        break;
                    }

                    if (brand.equalsIgnoreCase("Toyota")) {
                        switch (modelChoice) {
                            case "1":
                                carModel = "Hilux Revo";
                                break;
                            case "2":
                                carModel = "Yaris ATIV";
                                break;
                            case "3":
                                carModel = "Yaris Cross";
                                break;
                            default:
                                System.out.println("\nInvalid choice. Please enter 1-3");
                                continue;
                        }
                    } else if (brand.equalsIgnoreCase("Isuzu")) {
                        switch (modelChoice) {
                            case "1":
                                carModel = "D-Max";
                                break;
                            case "2":
                                carModel = "MU-X";
                                break;
                            default:
                                System.out.println("\nInvalid choice. Please enter 1-2");
                                continue;
                        }
                    } else if (brand.equalsIgnoreCase("Honda")) {
                        switch (modelChoice) {
                            case "1":
                                carModel = "HR-V";
                                break;
                            case "2":
                                carModel = "City Hatchback";
                                break;
                            case "3":
                                carModel = "City Sedan";
                                break;
                            default:
                                System.out.println("\nInvalid choice. Please enter 1-3");
                                continue;
                        }
                    }
                    break;
                }
            }
            if (isGoBack) {
                System.out.println("Return to select car brand.");
                continue;
            } else {
                break;
            }
        }

        Customer newCustomer = new Customer(customerId, name, phone, address);
        Car newCar = new Car(licensePlate, brand, carModel, newCustomer);

        customerList.add(newCustomer);
        carList.add(newCar);

        saveData();

        System.out.println("\nprint check");
        System.out.println("name: " + name);
        System.out.println("phone: " + phone);
        System.out.println("address: " + address);
        System.out.println("licensePlate: " + licensePlate);
        System.out.println("brand: " + brand);
        System.out.println("carModel: " + carModel);

        System.out.println("\nRegister success\n");
    }

    public void editInformation() {
        System.out.println("===== Edit Information =====");

        // check user in system (customerList).
        if (customerList.isEmpty()) {
            System.out.println("No customers in system.\n");
            return;
        }

        // loop print customerList in system.
        System.out.println("--- Customer List ---");
        for (int i = 0; i < customerList.size(); i++) {
            Customer c = customerList.get(i);
            System.out.println("ID: " + c.getCustomerId() + " | Name: " + c.getName());
        }

        System.out.print("\nEnter Customer ID to edit/delete (0 to cancel): ");
        String searchId = scanner.nextLine().trim();

        if (searchId.equals("0")) {
            return;
        }

        // if input match customerList get to c.
        Customer foundCustomer = null;
        for (int i = 0; i < customerList.size(); i++) {
            Customer c = customerList.get(i);
            if (c.getCustomerId().equals(searchId)) {
                foundCustomer = c;
                break;
            }
        }

        // not found Customer in system.
        if (foundCustomer == null) {
            System.out.println("Customer ID " + searchId + " not found.\n");
            return;
        }

        while (true) {
            System.out.println("\nEditing Customer: " + foundCustomer.getName());
            System.out.println("[1] Edit Name (Current: " + foundCustomer.getName() + ")");
            System.out.println("[2] Edit Phone (Current: " + foundCustomer.getPhone() + ")");
            System.out.println("[3] Edit Address (Current: " + foundCustomer.getAddress() + ")");
            System.out.println("[4] Delete Customer");
            System.out.println("[0] Finish & Go Back");
            System.out.print("Select an option (0-4): ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("0")) {
                break;
            }

            switch (choice) {
                case "1":
                    String newName = "";
                    while (true) {
                        System.out.print("Enter New Name: ");
                        newName = scanner.nextLine().trim();
                        if (newName.isEmpty()) {
                            System.out.println("Please enter your name.");
                            // ban input duplicate.
                        } else if (newName.equalsIgnoreCase(foundCustomer.getName())) {
                            System.out.println("Duplicate values cannot be change.");
                        } else if (!newName.matches("^[a-zA-Z\\u0E00-\\u0E7F\\s\\-\\.\\']+$")) {
                            System.out.println("Please enter only text.");
                        } else if (newName.length() < 4) {
                            System.out.println("Please enter more than 4 characters.");
                        } else {
                            foundCustomer.setName(newName);
                            System.out.println("Name update successful!");
                            break;
                        }
                    }
                    break;

                case "2":
                    String newPhone = "";
                    while (true) {
                        System.out.print("Enter New Phone: ");
                        newPhone = scanner.nextLine().trim();
                        if (newPhone.trim().isEmpty()) {
                            System.out.println("Please enter your phone.");
                            // ban input duplicate.
                        } else if (newPhone.equalsIgnoreCase(foundCustomer.getPhone())) {
                            System.out.println("Duplicate values cannot be change.");
                        } else if (!newPhone.matches("^[0-9]{9,10}$")) {
                            System.out.println("Please enter numbers 9-10 digits.");
                        } else {
                            foundCustomer.setPhone(newPhone);
                            System.out.println("Phone update successful");
                            break;
                        }
                    }
                    break;

                case "3":
                    String newAddress = "";
                    while (true) {
                        System.out.print("Enter New Address: ");
                        newAddress = scanner.nextLine().trim();
                        if (newAddress.trim().isEmpty()) {
                            System.out.println("Please enter your address.");
                            // ban input duplicate.
                        } else if (newAddress.equalsIgnoreCase(foundCustomer.getAddress())) {
                            System.out.println("Duplicate values cannot be change.");
                        } else if (!newAddress.matches("^[a-zA-Z0-9\\u0E00-\\u0E7F/\\s\\-.,()]+$")) {
                            System.out.println("Please enter valid text.");
                        } else if (newAddress.length() < 4) {
                            System.out.println("Less than 4 characters");
                        } else {
                            foundCustomer.setAddress(newAddress);
                            System.out.println("Address update successful");
                            break;
                        }
                    }
                    break;

                case "4":
                    System.out.print("Are you sure? (y/n): ");
                    String confirm = scanner.nextLine().trim();

                    if (confirm.equalsIgnoreCase("y")) {

                        // delete car related user. ลบผู้ใช้ที่เกี่ยวข้องกับรถยนต์
                        for (int i = carList.size() - 1; i >= 0; i--) {
                            Car car = carList.get(i);
                            if (car.getOwner().getCustomerId().equals(foundCustomer.getCustomerId())) {
                                carList.remove(i);
                            }
                        }

                        customerList.remove(foundCustomer);

                        System.out.println("Customer [" + foundCustomer.getName() + "] delete successful.");
                        saveData();
                        return;
                    } else {
                        System.out.println("Delete cancel.");
                    }
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        saveData();
        System.out.println("Exit edit menu\n");
    }

    public void memberList() {
        System.out.println("\n===== Member List =====");

        // check user in system.
        if (customerList.isEmpty()) {
            System.out.println("No members in system.");
            return;
        }

        // print user in system.
        for (Customer c : customerList) {
            System.out.println("Id: " + c.getCustomerId() + " | Name: " + c.getName() + " | Phone: " + c.getPhone());
        }

        System.out.printf("Select id at need(select 0 back to menu): ");
        String searchId = scanner.nextLine().trim();

        if (searchId.equals("0")) {
            System.out.println("Return to main menu\n");
            return;
        }

        // if input = customerList print.
        boolean isFound = false;
        for (Customer c : customerList) {
            if (c.getCustomerId().equals(searchId)) {

                System.out.println("\n===== Full Information =====");
                System.out.println("Customer ID  : " + c.getCustomerId());
                System.out.println("Name         : " + c.getName());
                System.out.println("Phone        : " + c.getPhone());
                System.out.println("Address      : " + c.getAddress());

                System.out.println("\n===== Car Details =====");

                boolean hasCar = false;
                for (Car car : carList) {
                    if (car.getOwner().getCustomerId().equals(searchId)) {
                        System.out.println("- License Plate: " + car.getLicensePlate() + " | Brand: " + car.getBrand() + " | Model: " + car.getCarModel());
                        hasCar = true;
                    }
                }

                if (!hasCar) {
                    System.out.println("No cars register for this customer.");
                }

                System.out.println("\n");
                isFound = true;
                break;
            }
        }

        if (!isFound) {
            System.out.println("id: " + searchId + " not found in system.");
        }
    }

    public void addCar() {
        System.out.println("===== Add Car =====");

        // check user in system.
        if (customerList.isEmpty()) {
            System.out.println("No customers in system.\n");
            return;
        }

        System.out.println("----- Customer List -----");
        for (Customer c : customerList) {
            System.out.println("ID: " + c.getCustomerId() + " | Name: " + c.getName());
        }

        System.out.print("\nEnter Customer ID to add a car (0 to cancel): ");
        String searchId = scanner.nextLine().trim();

        if (searchId.equals("0")) {
            System.out.println("Return to menu.\n");
            return;
        }

        // if input match customerList get to c.
        Customer foundCustomer = null;
        for (Customer c : customerList) {
            if (c.getCustomerId().equals(searchId)) {
                foundCustomer = c;
                break;
            }
        }

        // if not found customer print.
        if (foundCustomer == null) {
            System.out.println("Customer ID " + searchId + " not found.\n");
            return;
        }

        System.out.println("\nAdd car for: " + foundCustomer.getName());

        String licensePlate = "";
        while (true) {
            System.out.printf("licensePlate: ");
            licensePlate = scanner.nextLine().trim().toUpperCase();

            if (licensePlate.isEmpty()) {
                System.out.println("Please enter your licensePlate.");
            } else if (!licensePlate.matches("^[a-zA-Z0-9\\s]+$")) {
                System.out.println("Please enter only text and number.");
            } else if (licensePlate.length() < 3) {
                System.out.println("Less than 3 characters");
            } else {
                break;
            }
        }

        String brand = "";
        String carModel = "";
        while (true) {
            while (true) {
                System.out.println("\nSelect Car Brand:");
                System.out.println("[1] Toyota");
                System.out.println("[2] Isuzu");
                System.out.println("[3] Honda");
                System.out.println("[4] Other / Unknown");
                System.out.printf("Select Car Brand (1-4): ");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        brand = "Toyota";
                        break;
                    case "2":
                        brand = "Isuzu";
                        break;
                    case "3":
                        brand = "Honda";
                        break;
                    case "4":
                        brand = "";
                        break;
                    default:
                        System.out.println("\nInvalid choice. Please enter 1-4.");
                        continue;
                }
                break;
            }

            boolean isGoBack = false;
            if (brand.equals("")) {
                while (true) {
                    System.out.println("[0] Return to Select Car Brand.");
                    System.out.print("Brand: ");
                    brand = scanner.nextLine().trim().toUpperCase();

                    if (brand.equals("0")) {
                        isGoBack = true;
                        break;
                    }
                    if (brand.isEmpty()) {
                        System.out.println("Please enter your brand.");
                    } else if (!brand.matches("^[a-zA-Z0-9/\\s-.]+$")) {
                        System.out.println("Please enter valid text or numbers.");
                    } else if (brand.length() <= 1) {
                        System.out.println("Please enter more than 1 character.");
                    } else {
                        break;
                    }
                }

                if (!isGoBack) {
                    while (true) {
                        System.out.print("carModel: ");
                        carModel = scanner.nextLine().trim();
                        if (carModel.isEmpty()) {
                            System.out.println("Please enter your car model.");
                        } else if (!carModel.matches("^[a-zA-Z0-9/\\s-.]+$")) {
                            System.out.println("Please enter valid text or numbers.");
                        } else if (carModel.length() <= 1) {
                            System.out.println("Please enter more than 1 character.");
                        } else {
                            break;
                        }
                    }
                }
            }

            if (brand.equalsIgnoreCase("Toyota") || brand.equalsIgnoreCase("Isuzu") || brand.equalsIgnoreCase("Honda")) {
                while (true) {
                    System.out.println("\nSelect Car Model for " + brand + ":");
                    if (brand.equalsIgnoreCase("Toyota")) {
                        System.out.println("[1] Hilux Revo");
                        System.out.println("[2] Yaris ATIV");
                        System.out.println("[3] Yaris Cross");
                        System.out.println("[0] Return to select car brand.");
                    } else if (brand.equalsIgnoreCase("Isuzu")) {
                        System.out.println("[1] D-Max");
                        System.out.println("[2] MU-X");
                        System.out.println("[0] Return to select car brand.");
                    } else if (brand.equalsIgnoreCase("Honda")) {
                        System.out.println("[1] HR-V");
                        System.out.println("[2] City Hatchback");
                        System.out.println("[3] City Sedan");
                        System.out.println("[0] Return to select car brand.");
                    }

                    System.out.printf("Select carModel: ");
                    String modelChoice = scanner.nextLine();

                    if (modelChoice.equals("0")) {
                        isGoBack = true;
                        break;
                    }

                    if (brand.equalsIgnoreCase("Toyota")) {
                        switch (modelChoice) {
                            case "1":
                                carModel = "Hilux Revo";
                                break;
                            case "2":
                                carModel = "Yaris ATIV";
                                break;
                            case "3":
                                carModel = "Yaris Cross";
                                break;
                            default:
                                System.out.println("\nInvalid choice. Please enter 1-3");
                                continue;
                        }
                    } else if (brand.equalsIgnoreCase("Isuzu")) {
                        switch (modelChoice) {
                            case "1":
                                carModel = "D-Max";
                                break;
                            case "2":
                                carModel = "MU-X";
                                break;
                            default:
                                System.out.println("\nInvalid choice. Please enter 1-2");
                                continue;
                        }
                    } else if (brand.equalsIgnoreCase("Honda")) {
                        switch (modelChoice) {
                            case "1":
                                carModel = "HR-V";
                                break;
                            case "2":
                                carModel = "City Hatchback";
                                break;
                            case "3":
                                carModel = "City Sedan";
                                break;
                            default:
                                System.out.println("\nInvalid choice. Please enter 1-3");
                                continue;
                        }
                    }
                    break;
                }
            }

            if (isGoBack) {
                System.out.println("Return to select car brand.");
                continue;
            } else {
                break;
            }
        }

        Car newCar = new Car(licensePlate, brand, carModel, foundCustomer);
        carList.add(newCar);

        saveData();

        System.out.println("\nSuccessfully added " + brand + " " + carModel + " for " + foundCustomer.getName() + "!\n");
    }

    public void editCar() {
        System.out.println("===== Edit Car =====");

        // เช็คว่ามีลูกค้าในระบบหรือไม่ ถ้าไม่มีให้จบการทำงาน
        if (customerList.isEmpty()) {
            System.out.println("No customers in system.\n");
            return;
        }

        // แสดงรายชื่อลูกค้าและป้ายทะเบียนรถทุกคันที่มีในระบบ
        System.out.println("--- Customers & Cars ---");
        for (Customer c : customerList) {
            System.out.print("ID: " + c.getCustomerId() + " | Name: " + c.getName() + " | Cars: ");

            boolean hasCar = false;

            // ค้นหารถที่เป็นของลูกค้าคนนี้
            for (Car car : carList) {
                if (car.getOwner().getCustomerId().equals(c.getCustomerId())) {
                    System.out.print("[" + car.getLicensePlate() + "] ");
                    hasCar = true;
                }
            }

            // if hasCar = false;
            if (!hasCar) {
                System.out.print("(No cars)");
            }
            System.out.println();
        }

        // รับรหัสลูกค้าที่ต้องการแก้ไขข้อมูลรถ
        System.out.print("\nEnter Customer ID to edit car (0 to cancel): ");
        String searchId = scanner.nextLine().trim();

        if (searchId.equals("0")) {
            System.out.println("Return to main menu\n");
            return;
        }

        // ค้นหา Object ลูกค้าจาก ID ที่กรอกมา
        Customer targetCustomer = null;
        for (Customer c : customerList) {
            if (c.getCustomerId().equals(searchId)) {
                targetCustomer = c;
                break;
            }
        }

        // if car not found.
        if (targetCustomer == null) {
            System.out.println("Customer ID " + searchId + " not found.\n");
            return;
        }

        // ดึงรถทุกคันของลูกค้ารายนี้มาเก็บไว้ใน List ชั่วคราว (ownedCars)
        List<Car> ownedCars = new ArrayList<>();
        for (Car car : carList) {
            if (car.getOwner().getCustomerId().equals(searchId)) {
                ownedCars.add(car);
            }
        }

        // if customer has no car. (ถ้าลูกค้าคนนี้ไม่มีรถเลย ให้จบการทำงาน)
        if (ownedCars.isEmpty()) {
            System.out.println("This customer have no register cars.\n");
            return;
        }

        // select car to edit. (กระบวนการเลือกรถที่จะแก้ไข)
        Car carToEdit = null;
        if (ownedCars.size() == 1) {
            // ถ้ามีคันเดียว เลือกรถคันนั้นให้อัตโนมัติ
            carToEdit = ownedCars.get(0);
        } else {
            // ถ้ามีหลายคัน แสดงเมนูให้พิมพ์ตัวเลขเลือกรถ
            System.out.println("\nFound " + ownedCars.size() + " cars for " + targetCustomer.getName() + ":");
            for (int i = 0; i < ownedCars.size(); i++) {
                Car car = ownedCars.get(i);
                System.out.println("[" + (i + 1) + "] Plate: " + car.getLicensePlate() + " (" + car.getBrand() + ")");
            }
            System.out.print("Select car (1-" + ownedCars.size() + "): ");
            try {
                int index = Integer.parseInt(scanner.nextLine()) - 1;
                carToEdit = ownedCars.get(index);
            } catch (Exception e) {
                System.out.println("Invalid selection.");
                return;
            }
        }
        System.out.println("\nEditing Car: " + carToEdit.getLicensePlate());

        // ลูปเมนูรับค่ายี่ห้อรถและรุ่นรถใหม่
        String brand = "";
        String carModel = "";
        while (true) {
            while (true) {
                System.out.println("\nSelect Car Brand:");
                System.out.println("[1] Toyota");
                System.out.println("[2] Isuzu");
                System.out.println("[3] Honda");
                System.out.println("[4] Other / Unknown");
                System.out.printf("Select Car Brand (1-4): ");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        brand = "Toyota";
                        break;
                    case "2":
                        brand = "Isuzu";
                        break;
                    case "3":
                        brand = "Honda";
                        break;
                    case "4":
                        brand = "";
                        break;
                    default:
                        System.out.println("\nInvalid choice. Please enter 1-4.");
                        continue;
                }
                break;
            }

            boolean isGoBack = false;

            // input custom brand if select 'Other'. (กรณีเลือก Other ให้พิมพ์ยี่ห้อและรุ่นเอง)
            if (brand.equals("")) {
                while (true) {
                    System.out.println("[0] Return to Select Car Brand.");
                    System.out.print("Brand: ");
                    brand = scanner.nextLine().trim().toUpperCase();

                    if (brand.equals("0")) {
                        isGoBack = true;
                        break;
                    }
                    if (brand.isEmpty()) {
                        System.out.println("Please enter your brand.");
                    } else if (!brand.matches("^[a-zA-Z0-9/\\s-.]+$")) {
                        System.out.println("Please enter valid text or numbers.");
                    } else if (brand.length() <= 1) {
                        System.out.println("Please enter more than 1 character.");
                    } else {
                        break;
                    }
                }

                if (!isGoBack) {
                    while (true) {
                        System.out.print("carModel: ");
                        carModel = scanner.nextLine().trim();
                        if (carModel.isEmpty()) {
                            System.out.println("Please enter your car model.");
                        } else if (!carModel.matches("^[a-zA-Z0-9/\\s-.]+$")) {
                            System.out.println("Please enter valid text or numbers.");
                        } else if (carModel.length() <= 1) {
                            System.out.println("Please enter more than 1 character.");
                        } else {
                            break;
                        }
                    }
                }
            }

            // กรณีเลือกยี่ห้อหลัก ให้แสดงรุ่นรถย่อย
            if (brand.equalsIgnoreCase("Toyota") || brand.equalsIgnoreCase("Isuzu") || brand.equalsIgnoreCase("Honda")) {
                while (true) {
                    System.out.println("\nSelect Car Model for " + brand + ":");
                    if (brand.equalsIgnoreCase("Toyota")) {
                        System.out.println("[1] Hilux Revo");
                        System.out.println("[2] Yaris ATIV");
                        System.out.println("[3] Yaris Cross");
                        System.out.println("[0] Return to select car brand.");
                    } else if (brand.equalsIgnoreCase("Isuzu")) {
                        System.out.println("[1] D-Max");
                        System.out.println("[2] MU-X");
                        System.out.println("[0] Return to select car brand.");
                    } else if (brand.equalsIgnoreCase("Honda")) {
                        System.out.println("[1] HR-V");
                        System.out.println("[2] City Hatchback");
                        System.out.println("[3] City Sedan");
                        System.out.println("[0] Return to select car brand.");
                    }

                    System.out.printf("Select carModel: ");
                    String modelChoice = scanner.nextLine();

                    if (modelChoice.equals("0")) {
                        isGoBack = true;
                        break;
                    }

                    if (brand.equalsIgnoreCase("Toyota")) {
                        switch (modelChoice) {
                            case "1":
                                carModel = "Hilux Revo";
                                break;
                            case "2":
                                carModel = "Yaris ATIV";
                                break;
                            case "3":
                                carModel = "Yaris Cross";
                                break;
                            default:
                                System.out.println("\nInvalid choice. Please enter 1-3");
                                continue;
                        }
                    } else if (brand.equalsIgnoreCase("Isuzu")) {
                        switch (modelChoice) {
                            case "1":
                                carModel = "D-Max";
                                break;
                            case "2":
                                carModel = "MU-X";
                                break;
                            default:
                                System.out.println("\nInvalid choice. Please enter 1-2");
                                continue;
                        }
                    } else if (brand.equalsIgnoreCase("Honda")) {
                        switch (modelChoice) {
                            case "1":
                                carModel = "HR-V";
                                break;
                            case "2":
                                carModel = "City Hatchback";
                                break;
                            case "3":
                                carModel = "City Sedan";
                                break;
                            default:
                                System.out.println("\nInvalid choice. Please enter 1-3");
                                continue;
                        }
                    }
                    break;
                }
            }

            // check if user wants to go back.
            if (isGoBack) {
                System.out.println("Return to select car brand.");
                continue;
            } else {
                break;
            }

        }

        carToEdit.setBrand(brand);
        carToEdit.setCarModel(carModel);
        saveData();
        System.out.println("Car update successful!\n");
    }

    public void deleteCar() {
        System.out.println("===== Delete Car =====");

        // check if any cars exist in the system. (เช็คว่ามีรถในระบบให้ลบหรือไม่)
        if (carList.isEmpty()) {
            System.out.println("No cars in system to delete.\n");
            return;
        }

        // print all customers and their cars. (แสดงรายชื่อลูกค้าและรถทั้งหมด)
        System.out.println("--- Customer & Register Cars ---");
        for (Customer c : customerList) {
            System.out.print("ID: " + c.getCustomerId() + " | Name: " + c.getName() + " | Cars: ");

            boolean hasCar = false;

            for (Car car : carList) {
                if (car.getOwner().getCustomerId().equals(c.getCustomerId())) {
                    System.out.print("[" + car.getLicensePlate() + "] ");
                    hasCar = true;
                }
            }

            if (!hasCar) {
                System.out.print("No cars");
            }
            System.out.println();
        }

        // รับรหัสลูกค้าที่ต้องการลบรถ
        System.out.print("\nEnter Customer ID to delete car (0 to cancel): ");
        String searchId = scanner.nextLine().trim();

        if (searchId.equals("0")) {
            System.out.println("Return to main menu\n");
            return;
        }

        // find all cars owned by this customer. (ค้นหารถทุกคันของลูกค้าคนนี้มาเก็บไว้)
        List<Car> ownedCars = new ArrayList<>();
        for (Car car : carList) {
            if (car.getOwner().getCustomerId().equals(searchId)) {
                ownedCars.add(car);
            }
        }

        // if no cars found for this customer. (ถ้าลูกค้าคนนี้ไม่มีรถเลย)
        if (ownedCars.isEmpty()) {
            System.out.println("No cars register.\n");
            return;
        }

        // select car to delete. (กระบวนการเลือกรถที่จะลบ)
        Car carToDelete = null;
        if (ownedCars.size() == 1) {
            // auto-select if only 1 car.
            carToDelete = ownedCars.get(0);
            System.out.println("Found 1 car: " + carToDelete.getLicensePlate());
        } else {
            // prompt user to select if multiple cars. (ถ้ามีหลายคัน แสดงเมนูให้พิมพ์เลือก)
            System.out.println("\nFound " + ownedCars.size() + " cars. Which one to delete?");
            for (int i = 0; i < ownedCars.size(); i++) {
                System.out.println("[" + (i + 1) + "] Plate: " + ownedCars.get(i).getLicensePlate());
            }

            while (true) {
                System.out.print("Select (1-" + ownedCars.size() + " or 0 to cancel): ");
                String input = scanner.nextLine().trim();

                if (input.equals("0")) {
                    return;
                }

                // validate input. (ดักจับ Error กันระบบพังถ้าพิมพ์มั่ว และเช็คว่าเลขอยู่ในช่วงที่กำหนดไหม)
                try {
                    int index = Integer.parseInt(input) - 1;
                    if (index >= 0 && index < ownedCars.size()) {
                        carToDelete = ownedCars.get(index);
                        break;
                    } else {
                        System.out.println(
                                "Invalid selection. Please enter a number between 1 and " + ownedCars.size() + ".");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }
        }

        // confirm and delete.
        System.out.print("Are you sure you want to delete car [" + carToDelete.getLicensePlate() + "]? (y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            carList.remove(carToDelete);
            saveData();
            System.out.println("Car [" + carToDelete.getLicensePlate() + "] Delete successful.\n");
        } else {
            System.out.println("Delete cancel.\n");
        }
    }

    public void openOrder() {
        System.out.println("===== Manage Order =====");

        // display all current orders. (แสดงรายการออเดอร์ทั้งหมดในระบบ)
        if (orderList.isEmpty()) {
            System.out.println("  No orders in system.");
        } else {
            for (int i = 0; i < orderList.size(); i++) {
                Order o = orderList.get(i);
                
                // Safety check: เผื่อบิลเก่าใน JSON ไม่มีสถานะ Payment ส่ Unpaid อัตโนมัติ
                if (o.getPaymentStatus() == null) {
                    o.setPaymentStatus("Unpaid");
                }

                System.out.println("  Order No: " + o.getOrderNo() +
                        " | Customer: " + o.getBilledTo().getName() +
                        " | Car: " + o.getTargetCar().getLicensePlate() +
                        " | Status: " + o.getStatus() +
                        " | Payment: " + o.getPaymentStatus());
            }
        }

        // รับเลขที่บิล
        String orderNo = "";
        while (true) {
            System.out.print("Enter Order No (0 to cancel): ");
            orderNo = scanner.nextLine().trim();

            if (orderNo.equals("0")) {
                return;
            }

            if (orderNo.isEmpty()) {
                System.out.println("Order No cannot be empty.");
            } else if (!orderNo.matches("^[0-9]+$")) {
                System.out.println("Please enter only numbers.");
            } else {
                break;
            }
        }

        // search for existing order. (ตรวจสอบว่าเลขบิลนี้มีอยู่ในระบบแล้วหรือยัง)
        Order currentOrder = null;
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getOrderNo().equals(orderNo)) {
                currentOrder = orderList.get(i);
                break;
            }
        }

        // กรณีเป็น บิลใหม่ (หาไม่เจอในระบบ)
        if (currentOrder == null) {
            System.out.println("\n----- Create new order -----");
            LocalDate today = LocalDate.now();
            System.out.println("Date: " + today);
            System.out.println("Mechanic: " + this.getName());

            // check if system has customers.
            if (customerList.isEmpty()) {
                System.out.println("No customers in system to create an order.\n");
                return;
            }

            // print all customers and cars.
            System.out.println("\n--- Customer & Cars ---");
            for (int i = 0; i < customerList.size(); i++) {
                Customer c = customerList.get(i);
                System.out.print("ID: " + c.getCustomerId() + " | Name: " + c.getName() + " | Cars: ");

                boolean hasCar = false;
                for (int j = 0; j < carList.size(); j++) {
                    Car car = carList.get(j);
                    if (car.getOwner().getCustomerId().equals(c.getCustomerId())) {
                        System.out.print("[" + car.getLicensePlate() + "] ");
                        hasCar = true;
                    }
                }
                if (!hasCar)
                    System.out.print("No cars");
                System.out.println();
            }

            // input customer ID.
            Customer targetCustomer = null;
            String searchId = "";
            while (true) {
                System.out.print("\nEnter Customer ID (0 to cancel): ");
                searchId = scanner.nextLine().trim();

                if (searchId.equals("0"))
                    return;
                if (searchId.isEmpty()) {
                    System.out.println("Customer ID cannot be empty.");
                    continue;
                }

                for (int i = 0; i < customerList.size(); i++) {
                    if (customerList.get(i).getCustomerId().equals(searchId)) {
                        targetCustomer = customerList.get(i);
                        break;
                    }
                }

                if (targetCustomer == null) {
                    System.out.println("Customer ID not found. Please try again.");
                } else {
                    break;
                }
            }

            // find all cars owned by this customer.
            List<Car> ownedCars = new ArrayList<>();
            for (int i = 0; i < carList.size(); i++) {
                Car car = carList.get(i);
                if (car.getOwner().getCustomerId().equals(searchId)) {
                    ownedCars.add(car);
                }
            }

            if (ownedCars.isEmpty()) {
                System.out.println("This customer has no register cars.\n");
                return;
            }

            // select target car.
            Car targetCar = null;
            if (ownedCars.size() == 1) {
                targetCar = ownedCars.get(0);
                System.out.println("Select car: " + targetCar.getLicensePlate());
            } else {
                while (true) {
                    System.out.println("\nSelect car for order:");
                    for (int i = 0; i < ownedCars.size(); i++) {
                        System.out.println("[" + (i + 1) + "] Plate: " + ownedCars.get(i).getLicensePlate() + " ("
                                + ownedCars.get(i).getBrand() + ")");
                    }
                    System.out.print("Select (1-" + ownedCars.size() + " or 0 to cancel): ");
                    String carChoice = scanner.nextLine().trim();

                    if (carChoice.equals("0")) {
                        return;
                    }

                    try {
                        int carIdx = Integer.parseInt(carChoice) - 1;
                        if (carIdx >= 0 && carIdx < ownedCars.size()) {
                            targetCar = ownedCars.get(carIdx);
                            break;
                        } else {
                            System.out.println("Please enter a number between 1 and " + ownedCars.size() + ".");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input. Please enter numbers only.");
                    }
                }
            }
            
            // สร้างบิลใหม่ โดยตั้งสถานะเริ่มต้นเป็น New และ Unpaid
            currentOrder = new Order(orderNo, targetCustomer, targetCar, this, today, "New", "Unpaid");
            orderList.add(currentOrder);
            System.out.println("Order " + orderNo + " create successful.");
            saveData();

        // manage existing order. (กรณีเป็น บิลเดิม)
        } else {
            System.out.println("\n[ Manage Exist Order: " + currentOrder.getOrderNo() + " ]");
            System.out.println("Date: " + currentOrder.getInputDate());
            System.out.println("Customer: " + currentOrder.getBilledTo().getName());
            System.out.println("Car: " + currentOrder.getTargetCar().getLicensePlate());
            
            // Safety check สำหรับบิลเก่าที่อาจจะไม่มีสถานะ Payment
            if (currentOrder.getPaymentStatus() == null) {
                currentOrder.setPaymentStatus("Unpaid");
            }
        }

        // update order status loop. (ลูปเมนูหลักสำหรับจัดการบิล และเปลี่ยนสถานะ)
        while (true) {
            System.out.println("\n--- Update Order Status ---");
            System.out.println("Current Status: [" + currentOrder.getStatus() + "]");
            System.out.println("Payment Status: [" + currentOrder.getPaymentStatus() + "]");
            System.out.println("[1] Waiting for parts");
            System.out.println("[2] Repair in progress");
            System.out.println("[3] Repair completed");
            
            // เมนู 4 และ 5 โชว์เมื่อ ซ่อมเสร็จแล้ว เท่านั้น
            if (currentOrder.getStatus().equals("Repair completed")) {
                System.out.println("[4] Edit Payment Status");
                System.out.println("[5] Print Receipt");
            }
            
            System.out.println("[0] Save & Exit to Main Menu");
            System.out.print("Select Option: ");
            String statusChoice = scanner.nextLine().trim();

            if (statusChoice.equals("0")) {
                System.out.println("Exit Order Menu\n");
                break;

            } else if (statusChoice.equals("1")) {
                currentOrder.setStatus("Waiting for parts");
                currentOrder.setPaymentStatus("Unpaid");
                System.out.println("Status updated to 'Waiting for parts'. Payment reset to 'Unpaid'.");

            } else if (statusChoice.equals("2")) {
                currentOrder.setStatus("Repair in progress");
                currentOrder.setPaymentStatus("Unpaid"); 
                System.out.println("Status updated to 'Repair in progress'. Payment reset to 'Unpaid'.");
                manageOrderItems(currentOrder); // เข้าไปจัดการเพิ่ม/ลดอะไหล่

            } else if (statusChoice.equals("3")) {
                // ถ้าเพิ่งเปลี่ยนเป็นซ่อมเสร็จครั้งแรก ให้บังคับ Payment เป็น Unpaid
                if (!currentOrder.getStatus().equals("Repair completed")) {
                    currentOrder.setStatus("Repair completed");
                    currentOrder.setPaymentStatus("Unpaid");
                    System.out.println("Status updated to 'Repair completed'. Payment set to 'Unpaid'.");
                } else {
                    System.out.println("Order is already marked as 'Repair completed'.");
                }

            } else if (statusChoice.equals("4") && currentOrder.getStatus().equals("Repair completed")) {
                // เมนูย่อยสำหรับแก้ไขการจ่ายเงิน (เข้าได้เฉพาะตอนรถซ่อมเสร็จแล้ว)
                while (true) {
                    System.out.println("\n--- Edit Payment Status ---");
                    System.out.println("[1] Unpaid (ยังไม่ชำระเงิน)");
                    System.out.println("[2] Paid (ชำระเงินแล้ว)");
                    System.out.println("[0] Cancel");
                    System.out.print("Select (0-2): ");
                    String payChoice = scanner.nextLine().trim();

                    if (payChoice.equals("1")) {
                        currentOrder.setPaymentStatus("Unpaid");
                        System.out.println("Payment status set to 'Unpaid'.");
                        break;
                    } else if (payChoice.equals("2")) {
                        currentOrder.setPaymentStatus("Paid");
                        System.out.println("Payment status set to 'Paid'.");
                        break;
                    } else if (payChoice.equals("0")) {
                        break;
                    } else {
                        System.out.println("Invalid choice. Please select 0-2.");
                    }
                }
                
            } else if (statusChoice.equals("5") && currentOrder.getStatus().equals("Repair completed")) {
                currentOrder.printBill();
                
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
        saveData(); // บันทึกข้อมูลเมื่อออกจากหน้าเมนู
    }

    private void manageOrderItems(Order order) {  
        while (true) {
            System.out.println("\n--- Manage Items for order: " + order.getOrderNo() + " ---");

            // ดึงรายการของที่อยู่ในบิลซ่อมนี้ออกมาดู (ทั้งอะไหล่ บริการ และค่าแรง)
            List<Item> currentItems = order.getItems();

            // ส่วนที่ 1: แสดงรายการและราคาทั้งหมดที่ถูกใส่บิลไปแล้ว
            System.out.println("Current Items in Bill:");
            if (currentItems.isEmpty()) {
                System.out.println("  (Empty)");
            } else {
                for (int i = 0; i < currentItems.size(); i++) {
                    Item item = currentItems.get(i);
                    System.out.println("  [" + (i + 1) + "] " + item.getName() + " : " + item.getPrice() + " THB");
                }
            }

            // ส่วนที่ 2: แสดงเมนูให้ช่างเลือกเพิ่ม/ลดรายการ
            System.out.println("\n[1] Add Product");
            System.out.println("[2] Add Service (From List)");
            System.out.println("[3] Remove Item");
            System.out.println("[4] Add Labor Fee");
            System.out.println("[0] Back to Status Menu");
            System.out.print("Select: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("0")) {
                break;
            }

            // MENU 1: Add Product (เพิ่มสินค้า/อะไหล่ลงบิล)
            if (choice.equals("1")) {
                // เช็คก่อนว่าอู่มีอะไหล่ให้เลือกไหม
                if (productList.isEmpty()) {
                    System.out.println("No products available in stock.");
                    continue;
                }

                // 1. ดึงข้อมูลยี่ห้อและรุ่นรถจากบิลปัจจุบัน
                Car currentCar = order.getTargetCar();
                String carBrand = currentCar.getBrand().toUpperCase();
                String model = currentCar.getCarModel();

                // 2. วิเคราะห์ประเภทรถเพื่อใช้กรองอะไหล่ (Eco, SUV, Pickup)
                String currentCarType = "ALL";
                if (model.equalsIgnoreCase("Hilux Revo") || model.equalsIgnoreCase("D-Max") || model.equalsIgnoreCase("MU-X")) {
                    currentCarType = "Pickup";
                } else if (model.equalsIgnoreCase("Yaris ATIV") || model.equalsIgnoreCase("City Hatchback") || model.equalsIgnoreCase("City Sedan")) {
                    currentCarType = "Eco";
                } else if (model.equalsIgnoreCase("Yaris Cross") || model.equalsIgnoreCase("HR-V")) {
                    currentCarType = "SUV";
                }

                // 3. ร่อนตะแกรงกรองอะไหล่ให้ตรง ยี่ห้อ และ ประเภทรถ
                List<Product> matchingProducts = new ArrayList<>();
                for (Product p : productList) {
                    String pBrand = p.getBrand().toUpperCase();
                    String pType = p.getCarType();

                    // ยี่ห้อตรงกัน หรือ เป็นอะไหล่รวม (OTHER/ว่างเปล่า)
                    boolean isBrandMatch = pBrand.equals(carBrand) || pBrand.equals("OTHER") || pBrand.isEmpty();
                    // ประเภทรุ่นรถตรงกัน หรือ เป็นอะไหล่รวม (ALL)
                    boolean isTypeMatch = pType.equals(currentCarType) || pType.equals("ALL");

                    // ต้องตรงทั้ง 2 เงื่อนไขถึงจะเอามาใส่ตะแกรง (matchingProducts)
                    if (isBrandMatch && isTypeMatch) {
                        matchingProducts.add(p);
                    }
                }

                // ถ้ากรองแล้วไม่เหลืออะไหล่เลย ให้เด้งกลับ
                if (matchingProducts.isEmpty()) {
                    System.out.println("No products matching car brand [" + carBrand + "] and type [" + currentCarType
                            + "] available in stock.");
                    continue;
                }

                System.out.println("--- Select Product for [" + carBrand + " - " + currentCarType + "] ---");

                // ปริ้นท์รายการอะไหล่ที่ผ่านการกรองแล้วให้ช่างเลือก
                for (int i = 0; i < matchingProducts.size(); i++) {
                    Product p = matchingProducts.get(i);
                    System.out.println(
                            "[" + (i + 1) + "] " + p.getItemId() + " " + p.getName() + " (" + p.getPrice() + " THB)");
                }
                System.out.print("Select (1-" + matchingProducts.size() + "): ");

                // ดักจับ Error รับตัวเลขที่เลือก แปลงเป็น Index แล้วยัดลงบิล
                try {
                    int pIdx = Integer.parseInt(scanner.nextLine()) - 1;
                    if (pIdx >= 0 && pIdx < matchingProducts.size()) { // เช็คว่าเลขที่พิมพ์มา อยู่ในตัวเลือกไหม
                        currentItems.add(matchingProducts.get(pIdx));
                        System.out.println("Added to bill.");
                    } else {
                        System.out.println("Invalid selection. Number out of range.");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid selection. Please enter number only.");
                }

                // MENU 2: Add Service (เพิ่มค่าบริการแบบมีใน List)
            } else if (choice.equals("2")) {
                if (serviceList.isEmpty()) {
                    // เช็คว่ามีบริการในระบบไหม
                    System.out.println("No services available.");
                    continue;
                }
                System.out.println("--- Select Service ---");

                // แสดงรายการบริการทั้งหมด
                for (int i = 0; i < serviceList.size(); i++) {
                    Service s = serviceList.get(i);
                    System.out.println("[" + (i + 1) + "] " + s.getName() + " (" + s.getPrice() + " THB)");
                }
                System.out.print("Select (1-" + serviceList.size() + "): ");

                // ดักจับ Error รับตัวเลข แปลงเป็น Index แล้วยัดลงบิล
                try {
                    int sIdx = Integer.parseInt(scanner.nextLine()) - 1;
                    if (sIdx >= 0 && sIdx < serviceList.size()) {
                        currentItems.add(serviceList.get(sIdx));
                        System.out.println("Added to bill.");
                    } else {
                        System.out.println("Invalid selection. Number out of range.");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid selection. Please enter number only.");
                }

            // MENU 3: Remove Item (ลบรายการที่คีย์ผิดออกจากบิล)
            } else if (choice.equals("3")) {
                if (currentItems.isEmpty()) {
                    System.out.println("Bill is empty. Nothing to remove.");
                    continue;
                }
                System.out.print("Enter item number to remove (1-" + currentItems.size() + "): ");

                // ดักจับ Error รับเลขรายการ แล้วเตะออกจาก List ของบิล
                try {
                    int rIdx = Integer.parseInt(scanner.nextLine()) - 1;
                    if (rIdx >= 0 && rIdx < currentItems.size()) {
                        Item removedItem = currentItems.remove(rIdx);
                        System.out.println("Removed [" + removedItem.getName() + "] from bill.");
                    } else {
                        System.out.println("Invalid selection. Number out of range.");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid selection. Please enter number only.");
                }

            // MENU 4: Add Labor Fee (เพิ่มค่าแรงช่างแบบกำหนดเอง)
            } else if (choice.equals("4")) {
                double customFee = 0;
                while (true) {
                    System.out.print("Enter Custom Labor Fee (THB): ");
                    String feeInput = scanner.nextLine().trim();

                    if (feeInput.isEmpty()) {
                        System.out.println("Fee cannot be empty.");
                        continue;
                    }

                    if (!feeInput.matches("^[0-9]+(\\.[0-9]+)?$")) {
                        System.out.println("Please enter numbers only.");
                        continue;
                    }

                    try {
                        customFee = Double.parseDouble(feeInput);
                        if (customFee <= 0) {
                            System.out.println("Fee must be greater than 0.");
                        } else {
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input. Please enter numbers only.");
                    }
                }

                currentItems.add(new Item("Custom Labor Fee", customFee));
                System.out.println("Custom Labor Fee added to bill.");

            } else {
                System.out.println("Invalid choice. Please select 0-4.");
            }
        }
    }

    public void manageProducts() {
        while (true) {
            System.out.println("\n===== Product Management =====");

            // DISPLAY INVENTORY (แสดงรายการสินค้าทั้งหมดในสต๊อกแยกตามยี่ห้อ)
            String[] mainBrands = { "TOYOTA", "ISUZU", "HONDA", "OTHER" };

            for (String b : mainBrands) {
                System.out.println("--- " + b + " ---");
                boolean found = false;

                for (int i = 0; i < productList.size(); i++) {
                    Product p = productList.get(i);
                    String pBrand = p.getBrand().toUpperCase();

                    // ถ้ายี่ห้อเป็น OTHER ให้ดึงของที่ไม่ใช่ 3 ยี่ห้อหลักมาแสดง
                    if (b.equals("OTHER")) {
                        if (!pBrand.equals("TOYOTA") && !pBrand.equals("HONDA") && !pBrand.equals("ISUZU")) {
                            System.out.println("  ID: " + p.getItemId() + " | [" + p.getCarType() + "] " + p.getName()
                                    + " - " + p.getPrice() + " THB");
                            found = true;
                        }
                    // ถ้ายี่ห้อตรงกับหัวข้อพอดี ก็ปริ้นท์โชว์ได้เลย
                    } else if (pBrand.equals(b)) {
                        System.out.println("  ID: " + p.getItemId() + " | [" + p.getCarType() + "] " + p.getName()
                                + " - " + p.getPrice() + " THB");
                        found = true;
                    }
                }
                if (!found)
                    System.out.println("  (No items)");
            }

            System.out.println("\n[1] Add Product");
            System.out.println("[2] Edit Product");
            System.out.println("[3] Delete Product");
            System.out.println("[0] Back to Menu");
            System.out.print("Select: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("0")) {
                break;
            }

            // MENU 1: Add Product (เพิ่มสินค้าใหม่)
            if (choice.equals("1")) {
                String id = "";
                // ลูปที่ 1: รับและตรวจสอบรหัสสินค้า (Item ID)
                while (true) {
                    System.out.print("Item ID (0 to cancel): ");
                    id = scanner.nextLine().trim().toUpperCase();
                    
                    if (id.equals("0")) {
                        break;
                    }

                    if (id.isEmpty() || id.equals("-1")) {
                        System.out.println("Invalid ID.");
                    } else if (id.length() < 2 || id.startsWith("-")) {
                        System.out.println("ID must be at least 2 characters and cannot start with a hyphen (-).");
                    } else if (!id.matches("^[A-Z0-9-]+$")) {
                        System.out.println("Please enter valid ID (letters, numbers, hyphens).");
                    } else {
                        // เช็คว่ารหัสนี้ถูกใช้ไปแล้วหรือยัง (ป้องกัน ID ซ้ำ)
                        boolean isDuplicate = false;
                        for (Product p : productList) {
                            if (p.getItemId().equals(id)) {
                                isDuplicate = true;
                                break;
                            }
                        }
                        if (isDuplicate) {
                            System.out.println("This Item ID already exists.");
                        } else {
                            break;
                        }
                    }
                }
                
                if (id.equals("0")) {
                    continue; // กลับไปเมนู Product Management
                } 

                // ลูปที่ 2: รับและตรวจสอบชื่อสินค้า (Name)
                String name = "";
                while (true) {
                    System.out.print("Name: ");
                    name = scanner.nextLine().trim();
                    if (name.isEmpty() || name.equals("0") || name.equals("-1")) {
                        System.out.println("Name cannot be empty, 0, or -1.");
                    } else if (name.length() < 2) {
                        System.out.println("Name must be at least 2 characters.");
                    } else if (!name.matches("^[a-zA-Z0-9\\s\\-\\.\\'\\+]+$")) {
                        System.out.println("Please enter valid text (letters, numbers, spaces, -, ., ', +).");
                    } else {
                        break;
                    }
                }

                // ลูปที่ 3: รับและตรวจสอบราคาสินค้า (Price)
                double price = 0;
                while (true) {
                    System.out.print("Price: ");
                    String priceInput = scanner.nextLine().trim();
                    if (priceInput.isEmpty()) {
                        System.out.println("Price cannot be empty.");
                        continue;
                    }
                    try {
                        price = Double.parseDouble(priceInput);
                        if (price <= 0) {
                            System.out.println("Price must be greater than 0.");
                        } else {
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid Price. Please enter numbers only.");
                    }
                }

                // ลูปที่ 4: รับและตรวจสอบยี่ห้อ (Brand)
                String brand = "";
                while (true) {
                    System.out.print("Car Brand/Model: ");
                    brand = scanner.nextLine().trim().toUpperCase();
                    if (brand.isEmpty() || brand.equals("0") || brand.equals("-1")) {
                        System.out.println("Brand cannot be empty, 0, or -1.");
                    } else if (brand.length() < 2) {
                        System.out.println("Brand must be at least 2 characters.");
                    } else if (!brand.matches("^[a-zA-Z0-9\\s\\-\\.]+$")) {
                        System.out.println("Please enter valid text.");
                    } else {
                        break;
                    }
                }

                // ลูปที่ 5: เลือกว่าใช้กับรถกลุ่มไหน (Car Type)
                String carType = "";
                while (true) {
                    System.out.println("Select Car Type:");
                    System.out.println("[1] Pickup (PPV)");
                    System.out.println("[2] Eco (Eco Car)");
                    System.out.println("[3] SUV (B-SUV)");
                    System.out.println("[4] ALL");
                    System.out.print("Select (1-4): ");
                    String typeChoice = scanner.nextLine().trim();

                    if (typeChoice.equals("1")) {
                        carType = "Pickup";
                        break;
                    } else if (typeChoice.equals("2")) {
                        carType = "Eco";
                        break;
                    } else if (typeChoice.equals("3")) {
                        carType = "SUV";
                        break;
                    } else if (typeChoice.equals("4")) {
                        carType = "ALL";
                        break;
                    } else {
                        System.out.println("Invalid choice. Please select 1-4.");
                    }
                }

                Product newProduct = new Product(name, price, id, brand, carType);
                productList.add(newProduct);

                System.out.println("Add successful.");
                saveData();

            // MENU 2: Edit Product (แก้ไขข้อมูลสินค้า แบบเลือกหัวข้อ)
            } else if (choice.equals("2")) {
                System.out.print("Enter Item ID to Edit (0 to cancel): ");
                String id = scanner.nextLine().trim().toUpperCase();

                if (id.equals("0")) {
                    continue; // ยกเลิกการค้นหา
                }

                // ค้นหาสินค้าจาก ID ที่พิมพ์มา
                Product foundProduct = null;
                for (int i = 0; i < productList.size(); i++) {
                    Product p = productList.get(i);
                    if (p.getItemId().equals(id)) {
                        foundProduct = p; // เจอเป้าหมาย
                        break;
                    }
                }

                if (foundProduct == null) {
                    System.out.println("Item ID not found.");
                    continue;
                }

                // ลูปเมนูย่อยสำหรับการ Edit
                while (true) {
                    System.out.println("\nEditing Product: [" + foundProduct.getItemId() + "] " + foundProduct.getName());
                    System.out.println("[1] Edit Name     (Current: " + foundProduct.getName() + ")");
                    System.out.println("[2] Edit Price    (Current: " + foundProduct.getPrice() + " THB)");
                    System.out.println("[3] Edit Brand    (Current: " + foundProduct.getBrand() + ")");
                    System.out.println("[4] Edit Car Type (Current: " + foundProduct.getCarType() + ")");
                    System.out.println("[0] Finish & Go Back");
                    System.out.print("Select an option (0-4): ");
                    String editChoice = scanner.nextLine().trim();

                    if (editChoice.equals("0")) {
                        System.out.println("Exit edit menu.");
                        break; // ออกจากเมนูแก้ไข กลับไปหน้า Product หลัก
                    }

                    switch (editChoice) {
                        // แก้ไขชื่อ (Name)
                        case "1":
                            while (true) {
                                System.out.print("Enter New Name: ");
                                String newName = scanner.nextLine().trim();
                                if (newName.isEmpty() || newName.equals("0") || newName.equals("-1")) {
                                    System.out.println("Name cannot be empty, 0, or -1.");
                                } else if (newName.length() < 2) {
                                    System.out.println("Name must be at least 2 characters.");
                                } else if (newName.equalsIgnoreCase(foundProduct.getName())) {
                                    System.out.println("Duplicate values cannot be changed.");
                                } else if (!newName.matches("^[a-zA-Z0-9\\s\\-\\.\\'\\+]+$")) {
                                    System.out.println("Please enter valid text.");
                                } else {
                                    foundProduct.setName(newName);
                                    System.out.println("Name update successful!");
                                    saveData();
                                    break;
                                }
                            }
                            break;

                        // แก้ไขราคา (Price)
                        case "2":
                            while (true) {
                                System.out.print("Enter New Price: ");
                                String priceInput = scanner.nextLine().trim();
                                if (priceInput.isEmpty()) {
                                    System.out.println("Price cannot be empty.");
                                    continue;
                                }
                                try {
                                    double newPrice = Double.parseDouble(priceInput);
                                    if (newPrice <= 0) {
                                        System.out.println("Price must be greater than 0.");
                                    } else if (newPrice == foundProduct.getPrice()) {
                                        System.out.println("Duplicate values cannot be changed.");
                                    } else {
                                        foundProduct.setPrice(newPrice);
                                        System.out.println("Price update successful!");
                                        saveData();
                                        break;
                                    }
                                } catch (Exception e) {
                                    System.out.println("Invalid Price. Please enter numbers only.");
                                }
                            }
                            break;

                        // แก้ไขยี่ห้อที่รองรับ (Brand)
                        case "3":
                            while (true) {
                                System.out.print("Enter New Car Brand/Model: ");
                                String newBrand = scanner.nextLine().trim().toUpperCase();
                                if (newBrand.isEmpty() || newBrand.equals("0") || newBrand.equals("-1")) {
                                    System.out.println("Brand cannot be empty, 0, or -1.");
                                } else if (newBrand.length() < 2) {
                                    System.out.println("Brand must be at least 2 characters.");
                                } else if (newBrand.equals(foundProduct.getBrand())) {
                                    System.out.println("Duplicate values cannot be changed.");
                                } else if (!newBrand.matches("^[a-zA-Z0-9\\s\\-\\.]+$")) {
                                    System.out.println("Please enter valid text.");
                                } else {
                                    foundProduct.setBrand(newBrand);
                                    System.out.println("Brand update successful!");
                                    saveData();
                                    break;
                                }
                            }
                            break;

                        // แก้ไขกลุ่มรถ (Car Type)
                        case "4":
                            while (true) {
                                System.out.println("Select New Car Type:");
                                System.out.println("[1] Pickup (PPV)");
                                System.out.println("[2] Eco (Eco Car)");
                                System.out.println("[3] SUV (B-SUV)");
                                System.out.println("[4] ALL");
                                System.out.print("Select (1-4): ");
                                String typeChoice = scanner.nextLine().trim();

                                String newType = "";
                                if (typeChoice.equals("1")) newType = "Pickup";
                                else if (typeChoice.equals("2")) newType = "Eco";
                                else if (typeChoice.equals("3")) newType = "SUV";
                                else if (typeChoice.equals("4")) newType = "ALL";
                                else {
                                    System.out.println("Invalid choice. Please select 1-4.");
                                    continue;
                                }

                                if (newType.equals(foundProduct.getCarType())) {
                                    System.out.println("Duplicate values cannot be changed.");
                                    break;
                                } else {
                                    foundProduct.setCarType(newType);
                                    System.out.println("Car Type update successful!");
                                    saveData();
                                    break;
                                }
                            }
                            break;

                        default:
                            System.out.println("Invalid option. Please enter 0-4.");
                            break;
                    }
                }

            // MENU 3: Delete Product (ลบสินค้าทิ้ง)
            } else if (choice.equals("3")) {
                System.out.print("Enter Item ID to Delete (0 to cancel): ");
                String id = scanner.nextLine().trim().toUpperCase();

                if (id.equals("0")) {
                    continue; // กด 0 เพื่อยกเลิก
                }

                // ค้นหาสินค้าจาก ID
                Product targetProduct = null;
                for (int i = 0; i < productList.size(); i++) {
                    Product p = productList.get(i);
                    if (p.getItemId().equals(id)) {
                        targetProduct = p;
                        break;
                    }
                }

                //Confirmation prompt
                if (targetProduct != null) {
                    System.out.print("Are you sure you want to delete [" + targetProduct.getName() + "]? (y/n): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                        productList.remove(targetProduct);
                        System.out.println("Delete successful.");
                        saveData();
                    } else {
                        System.out.println("Delete canceled.");
                    }
                } else {
                    System.out.println("Item ID not found.");
                }
            } else {
                System.out.println("Invalid choice. Please select 0-3.");
            }
        }
    }

    public void manageServices() {
        while (true) {
            System.out.println("\n===== Service Management =====");

            // DISPLAY SERVICES (แสดงรายการบริการทั้งหมดที่มีในระบบ)
            if (serviceList.isEmpty()) {
                System.out.println("No services available.");
            } else {
                for (int i = 0; i < serviceList.size(); i++) {
                    Service s = serviceList.get(i);
                    System.out.println("  ID: " + s.getItemId() + " | " + s.getName() + " - " + s.getPrice() + " THB");
                }
            }

            System.out.println("\n[1] Add Service");
            System.out.println("[2] Edit Service");
            System.out.println("[3] Delete Service");
            System.out.println("[0] Back to Menu");
            System.out.print("Select: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("0")) {
                break; // กลับไปที่เมนูหลักของช่าง
            }

            // MENU 1: Add Service (เพิ่มบริการใหม่)
            if (choice.equals("1")) {
                String id = "";
                while (true) {
                    // ลูปที่ 1: รับและตรวจสอบรหัสบริการ
                    System.out.print("Service ID (e.g., SRV-01): ");
                    id = scanner.nextLine().trim().toUpperCase();
                    if (id.isEmpty()) {
                        System.out.println("Service ID cannot be empty.");
                    } else if (!id.matches("^[A-Z0-9-]+$")) {
                        System.out.println("Please enter valid ID (letters, numbers, hyphens).");
                    } else {

                        // check for duplicate ID. (เช็คว่า ID นี้ซ้ำกับที่มีอยู่แล้วหรือเปล่า)
                        boolean isDuplicate = false;
                        for (Service s : serviceList) {
                            if (s.getItemId().equals(id)) {
                                isDuplicate = true;
                                break;
                            }
                        }
                        if (isDuplicate) {
                            System.out.println("This Service ID already exists.");
                        } else {
                            break; // ID ผ่านฉลุย ไม่ซ้ำ
                        }
                    }
                }

                // ลูปที่ 2: รับและตรวจสอบชื่อบริการ
                String name = "";
                while (true) {
                    System.out.print("Service Name: ");
                    name = scanner.nextLine().trim();
                    if (name.isEmpty()) {
                        System.out.println("Service Name cannot be empty.");
                    } else if (!name.matches("^[a-zA-Z0-9\\s\\-\\.\\'\\+]+$")) {
                        System.out.println("Please enter valid text.");
                    } else {
                        break;
                    }
                }

                // ลูปที่ 3: รับและตรวจสอบราคาค่าบริการ
                double price = 0;
                while (true) {
                    System.out.print("Price: ");
                    String priceInput = scanner.nextLine().trim();
                    if (priceInput.isEmpty()) {
                        System.out.println("Price cannot be empty.");
                        continue;
                    }
                    try {
                        price = Double.parseDouble(priceInput);
                        if (price <= 0) {
                            System.out.println("Price must be greater than 0.");
                        } else {
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid Price. Please enter numbers only.");
                    }
                }

                serviceList.add(new Service(id, name, price));

                System.out.println("Service add successful.");
                saveData();

            // MENU 2: Edit Service (แก้ไขข้อมูลบริการที่มีอยู่)
            } else if (choice.equals("2")) {
                System.out.print("Enter Service ID to Edit: ");
                String id = scanner.nextLine().trim().toUpperCase();

                boolean isFound = false;

                // find service by ID. (ค้นหาบริการจาก ID ที่ป้อนเข้ามา)
                for (int i = 0; i < serviceList.size(); i++) {
                    Service s = serviceList.get(i);
                    if (s.getItemId().equals(id)) {

                        // update Name. (ลูปให้กรอกชื่อใหม่)
                        while (true) {
                            System.out.print("New Name (Current: " + s.getName() + "): ");
                            String newName = scanner.nextLine().trim();
                            if (newName.isEmpty()) {
                                System.out.println("Name cannot be empty.");
                            } else if (!newName.matches("^[a-zA-Z0-9\\s\\-\\.\\'\\+]+$")) {
                                System.out.println("Please enter valid text.");
                            } else {
                                s.setName(newName);
                                break;
                            }
                        }

                        // update Price. (ลูปให้กรอกราคาใหม่)
                        while (true) {
                            System.out.print("New Price (Current: " + s.getPrice() + "): ");
                            String priceInput = scanner.nextLine().trim();
                            if (priceInput.isEmpty()) {
                                System.out.println("Price cannot be empty.");
                                continue;
                            }
                            try {
                                double newPrice = Double.parseDouble(priceInput);
                                if (newPrice <= 0) {
                                    System.out.println("Price must be greater than 0.");
                                } else {
                                    s.setPrice(newPrice);
                                    break;
                                }
                            } catch (Exception e) {
                                System.out.println("Invalid Price. Please enter numbers only.");
                            }
                        }

                        System.out.println("Service update successful.");
                        saveData();
                        isFound = true;
                        break;
                    }
                }
                // ถ้าวนลูปจนจบแล้วยังหา ID ไม่เจอ
                if (!isFound) {
                    System.out.println("Service ID not found.");
                }

                // MENU 3: Delete Service (ลบบริการทิ้ง)
            } else if (choice.equals("3")) {
                System.out.print("Enter Service ID to Delete: ");
                String id = scanner.nextLine().trim().toUpperCase();

                // find service by ID. (ค้นหาบริการจาก ID)
                Service targetService = null;
                for (int i = 0; i < serviceList.size(); i++) {
                    Service s = serviceList.get(i);
                    if (s.getItemId().equals(id)) {
                        targetService = s;
                        break;
                    }
                }

                // remove and save.
                if (targetService != null) {
                    serviceList.remove(targetService);
                    System.out.println("Service delete successful.");
                    saveData();
                } else {
                    System.out.println("Service ID not found.");
                }
            }
        }
    }

    public void billHistory() {
        System.out.println("===== Bill History & Status =====");

        // check if there are any orders. (เช็คว่าอู่เราเคยรับงานซ่อมบ้างหรือยัง ถ้ายังให้จบการทำงาน)
        if (orderList.isEmpty()) {
            System.out.println("No orders found in the system.\n");
            return;
        }

        // PART 1: DISPLAY GROUPED ORDERS (แสดงรายการออเดอร์โดยจัดกลุ่มตามสถานะ)
        String[] allStatuses = { "New", "Waiting for parts", "Repair in progress", "Repair completed" };

        // วนตามหัวข้อสถานะทีละอัน
        for (int s = 0; s < allStatuses.length; s++) {
            String currentStatus = allStatuses[s];
            System.out.println(currentStatus);

            boolean hasOrder = false;

            // search orders matching current status. (ค้นหาออเดอร์ที่มีสถานะตรงกับหัวข้อในรอบนี้)
            for (int i = 0; i < orderList.size(); i++) {
                Order o = orderList.get(i);
                if (o.getStatus().equals(currentStatus)) {
                    // ถ้าตรงกัน ให้ปริ้นท์โชว์เลขบิล ชื่อลูกค้า และชื่อช่าง
                    System.out.println("- Order No: " + o.getOrderNo() +
                            " | Customer: " + o.getBilledTo().getName() +
                            " | Mechanic: " + o.getMechanic().getName());
                    hasOrder = true;
                }
            }

            // if no orders in this status.
            if (!hasOrder) {
                System.out.println("- No orders");
            }
            System.out.println();
        }

        // VIEW ORDER DETAILS (ค้นหาเพื่อดูรายละเอียดบิลฉบับเต็ม)
        while (true) {
            System.out.print("Enter Order No to view details (0 to go back): ");
            String searchOrderNo = scanner.nextLine().trim();

            if (searchOrderNo.equals("0")) {
                System.out.println("Return to main menu\n");
                return;
            }

            if (searchOrderNo.isEmpty()) {
                System.out.println("Order No cannot be empty. Please try again.");
                continue;
            }

            boolean isFound = false;

            // search for the exact order. (ค้นหาเลขบิลที่พิมพ์มา)
            for (int i = 0; i < orderList.size(); i++) {
                Order o = orderList.get(i);
                if (o.getOrderNo().equals(searchOrderNo)) {
                    // ถ้าเจอ เรียกใช้ฟังก์ชันพิมพ์ใบเสร็จของ Object Order
                    o.printBill();
                    isFound = true;
                    break;
                }
            }

            // check search result.
            if (isFound) {
                break; // ดูบิลเสร็จแล้ว หลุดจากหน้า History เพื่อกลับไปเมนูหลัก
            } else {
                System.out.println("Order No '" + searchOrderNo + "' not found.");
            }
        }
    }
}