import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.List;

public class JsonStorageManager {

    // Saves customer list to JSON (บันทึกรายชื่อลูกค้าลงไฟล์ JSON)
    public static void saveCustomers(List<Customer> customerList) {
        JSONArray custArray = new JSONArray();
        for (Customer c : customerList) {
            JSONObject obj = new JSONObject();
            obj.put("customerId", c.getCustomerId());
            obj.put("name", c.getName());
            obj.put("phone", c.getPhone());
            obj.put("address", c.getAddress());
            custArray.add(obj);
        }
        try (FileWriter file = new FileWriter("customers.json")) {
            file.write(custArray.toJSONString());
        } catch (java.io.IOException e) { // การอ่าน/เขียนไฟล์
            System.err.println("Error saving customers: " + e.getMessage());
        }
    }

    // Saves car list to JSON (บันทึกรายชื่อรถลงไฟล์ JSON)
    public static void saveCars(List<Car> carList) {
        JSONArray carArray = new JSONArray();
        for (Car car : carList) {
            JSONObject obj = new JSONObject();
            obj.put("licensePlate", car.getLicensePlate());
            obj.put("brand", car.getBrand());
            obj.put("carModel", car.getCarModel());
            obj.put("ownerId", car.getOwner().getCustomerId());
            carArray.add(obj);
        }
        try (FileWriter file = new FileWriter("cars.json")) {
            file.write(carArray.toJSONString());
        } catch (Exception e) {
            System.err.println("Error saving cars: " + e.getMessage());
        }
    }

    // Saves product list to JSON (บันทึกข้อมูลสินค้าลงไฟล์ JSON)
    public static void saveProducts(List<Product> productList) {
        JSONArray prodArray = new JSONArray();
        for (Product p : productList) {
            JSONObject obj = new JSONObject();
            obj.put("itemId", p.getItemId());
            obj.put("name", p.getName());
            obj.put("price", String.valueOf(p.getPrice()));
            obj.put("brand", p.getBrand());
            obj.put("carType", p.getCarType());
            prodArray.add(obj);
        }
        try (FileWriter file = new FileWriter("products.json")) {
            file.write(prodArray.toJSONString());
        } catch (java.io.IOException e) { // การอ่าน/เขียนไฟล์
            System.err.println("Error saving products: " + e.getMessage());
        }
    }

    // Saves service list to JSON (บันทึกข้อมูลบริการลงไฟล์ JSON)
    public static void saveServices(List<Service> serviceList) {
        JSONArray servArray = new JSONArray();
        for (Service s : serviceList) {
            JSONObject obj = new JSONObject();
            obj.put("itemId", s.getItemId());
            obj.put("name", s.getName());
            obj.put("price", String.valueOf(s.getPrice()));
            servArray.add(obj);
        }
        try (FileWriter file = new FileWriter("services.json")) {
            file.write(servArray.toJSONString());
        } catch (Exception e) {
            System.err.println("Error saving services: " + e.getMessage());
        }
    }

    // Saves active and completed orders (บันทึกข้อมูลบิลทั้งที่ค้างอยู่และที่เสร็จแล้ว)
    public static void saveOrders(List<Order> orderList) {
        JSONArray activeOrderArray = new JSONArray();
        JSONArray completedOrderArray = new JSONArray();

        for (Order o : orderList) {
            JSONObject obj = new JSONObject();
            obj.put("orderNo", o.getOrderNo());
            obj.put("customerId", o.getBilledTo().getCustomerId());
            obj.put("licensePlate", o.getTargetCar().getLicensePlate());
            obj.put("inputDate", o.getInputDate().toString());
            obj.put("status", o.getStatus());
            obj.put("paymentStatus", o.getPaymentStatus());

            JSONArray itemsArray = new JSONArray();
            for (Item item : o.getItems()) {
                JSONObject itemObj = new JSONObject();
                itemObj.put("name", item.getName());
                itemObj.put("price", String.valueOf(item.getPrice()));
                itemsArray.add(itemObj);
            }
            obj.put("items", itemsArray);

            if (o.getStatus().equals("Repair completed")) {
                completedOrderArray.add(obj);
            } else {
                activeOrderArray.add(obj);
            }
        }

        try (FileWriter file = new FileWriter("orders.json")) {
            file.write(activeOrderArray.toJSONString());
        } catch (Exception e) {
            System.err.println("Error saving active orders: " + e.getMessage());
        }

        try (FileWriter file = new FileWriter("completed_orders.json")) {
            file.write(completedOrderArray.toJSONString());
        } catch (Exception e) {
            System.err.println("Error saving completed orders: " + e.getMessage());
        }
    }

    // Load data with error checking (โหลดข้อมูลพร้อมตรวจสอบความถูกต้อง)
    public static void loadCustomers(List<Customer> customerList) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("customers.json")) {
            JSONArray custArray = (JSONArray) parser.parse(reader);
            for (Object o : custArray) {
                JSONObject obj = (JSONObject) o;
                customerList.add(new Customer(
                    (String) obj.get("customerId"),
                    (String) obj.get("name"),
                    (String) obj.get("phone"),
                    (String) obj.get("address")
                ));
            }
        } catch (Exception e) { /* System.out.println("No customer data file."); */ }
    }

    public static void loadCars(List<Car> carList, List<Customer> customerList) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("cars.json")) {
            JSONArray carArray = (JSONArray) parser.parse(reader);
            for (Object o : carArray) {
                JSONObject obj = (JSONObject) o;
                String ownerId = (String) obj.get("ownerId");
                Customer owner = null;
                for (Customer c : customerList) {
                    if (c.getCustomerId().equals(ownerId)) { owner = c; break; }
                }
                if (owner != null) {
                    carList.add(new Car(
                        (String) obj.get("licensePlate"),
                        (String) obj.get("brand"),
                        (String) obj.get("carModel"),
                        owner
                    ));
                }
            }
        } catch (Exception e) { /* No file */ }
    }

    public static void loadProducts(List<Product> productList) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("products.json")) {
            JSONArray prodArray = (JSONArray) parser.parse(reader);
            for (Object o : prodArray) {
                JSONObject obj = (JSONObject) o;
                productList.add(new Product(
                    (String) obj.get("name"),
                    Double.parseDouble((String) obj.get("price")),
                    (String) obj.get("itemId"),
                    (String) obj.get("brand"),
                    (String) obj.get("carType")
                ));
            }
        } catch (java.io.FileNotFoundException e) {
            // ไม่ต้องทำอะไร (เป็นปกติของรันครั้งแรก ปล่อยผ่านได้)
            // นี่คือเหตุผลที่เราไม่ควรใช้ Exception ธรรมดา เพราะมันจะแยกไม่ออกระหว่าง "ไฟล์ไม่มี" กับ "ไฟล์พัง"
        } catch (org.json.simple.parser.ParseException e) {
            System.err.println("ไฟล์ products.json โครงสร้างผิดพลาด อ่านข้อมูลไม่ได้!");
        } catch (NumberFormatException e) {
            System.err.println("ข้อมูลราคาสินค้าในไฟล์ products.json ไม่ใช่ตัวเลข!");
        } catch (java.io.IOException e) {
            System.err.println("เกิดปัญหาในการอ่านไฟล์ products.json");
        }
    }

    public static void loadServices(List<Service> serviceList) {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("services.json")) {
            JSONArray servArray = (JSONArray) parser.parse(reader);
            for (Object o : servArray) {
                JSONObject obj = (JSONObject) o;
                serviceList.add(new Service(
                    (String) obj.get("itemId"),
                    (String) obj.get("name"),
                    Double.parseDouble((String) obj.get("price"))
                ));
            }
        } catch (Exception e) { /* No file */ }
    }

    public static void loadOrders(List<Order> orderList, List<Customer> customerList, List<Car> carList, Mechanic mechanic) {
        JSONParser parser = new JSONParser();
        String[] files = {"orders.json", "completed_orders.json"};
        for (String fileName : files) {
            try (FileReader reader = new FileReader(fileName)) {
                JSONArray orderArray = (JSONArray) parser.parse(reader);
                for (Object o : orderArray) {
                    JSONObject obj = (JSONObject) o;
                    String cId = (String) obj.get("customerId");
                    String plate = (String) obj.get("licensePlate");
                    String status = (String) obj.get("status");
                    String paymentStatus = (String) obj.get("paymentStatus");
                    if (paymentStatus == null) paymentStatus = "Unpaid"; 

                    Customer customer = null;
                    for (Customer c : customerList) if (c.getCustomerId().equals(cId)) { customer = c; break; }

                    Car car = null;
                    for (Car c : carList) if (c.getLicensePlate().equals(plate)) { car = c; break; }

                    if (customer != null && car != null) {
                        Order order = new Order(
                            (String) obj.get("orderNo"),
                            customer, car, mechanic,
                            LocalDate.parse((String) obj.get("inputDate")),
                            status,
                            paymentStatus 
                        );
                        JSONArray items = (JSONArray) obj.get("items");
                        if (items != null) {
                            for (Object iObj : items) {
                                JSONObject item = (JSONObject) iObj;
                                order.getItems().add(new Item(
                                    (String) item.get("name"),
                                    Double.parseDouble((String) item.get("price"))
                                ));
                            }
                        }
                        orderList.add(order);
                    }
                }
            } catch (java.io.FileNotFoundException e) { 
            } catch (org.json.simple.parser.ParseException e) {
                System.err.println("ไฟล์ " + fileName + " โครงสร้างผิดพลาด อ่านข้อมูลไม่ได้");
            } catch (NumberFormatException e) {
                System.err.println("ข้อมูลราคาสินค้าในไฟล์ " + fileName + " ไม่ใช่ตัวเลข");
            } catch (java.time.format.DateTimeParseException e) {
                System.err.println("รูปแบบวันที่ในไฟล์ " + fileName + " ผิดพลาด");
            } catch (java.io.IOException e) {
                System.err.println("เกิดปัญหาในการอ่านไฟล์ " + fileName);
            }
        }
    }
}
