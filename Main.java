import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Mechanic myMechanic = new Mechanic("Kacha Punturat", "0935835149", "Hatyai", "M001", "Senior");

        myMechanic.loadData();

        while (true) {
            System.out.println("========================================");
            System.out.println("   Auto Repair Shop Management System   ");
            System.out.println("========================================");
            System.out.println("Active Mechanic: " + myMechanic.getName());
            System.out.println("----------------------------------------");
            System.out.println("[1] Register New Customer & Car (ลงทะเบียนลูกค้าและรถใหม่)");
            System.out.println("[2] Edit/Delete Customer Info (แก้ไข/ลบ ข้อมูลลูกค้า)");
            System.out.println("[3] View All Members (ดูรายชื่อสมาชิกทั้งหมด)");
            System.out.println("[4] Register Additional Car (เพิ่มรถคันใหม่ให้ลูกค้าเดิม)");
            System.out.println("[5] Edit Car Info (แก้ไขข้อมูลรถ)");
            System.out.println("[6] Delete Car (ลบรถออกจากระบบ)");
            System.out.println("[7] Manage Orders & Repair Status (จัดการบิลและสถานะการซ่อม)");
            System.out.println("[8] Bill History & View Receipts (ประวัติบิลและพิมพ์ใบเสร็จ)");
            System.out.println("[9] Product & Stock Management (จัดการอะไหล่และสต๊อก)");
            System.out.println("[10] Service Management (จัดการบริการและค่าแรง)");
            System.out.println("[0] Exit Program (ออกจากโปรแกรม)");
            System.out.println("----------------------------------------");
            System.out.print("Please enter number (0-10): ");

            // Read choice safely using nextLine() to avoid buffer issues
            // อ่านค่าด้วย nextLine เพื่อป้องกันปัญหาการรับข้อมูลผิดพลาด
            String input = scanner.nextLine().trim();
            int choice = -1;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    myMechanic.register();
                    break;
                case 2:
                    myMechanic.editInformation();
                    break;
                case 3:
                    myMechanic.memberList();
                    break;
                case 4:
                    myMechanic.addCar();
                    break;
                case 5:
                    myMechanic.editCar();
                    break;
                case 6:
                    myMechanic.deleteCar();
                    break;
                case 7:
                    myMechanic.openOrder();
                    break;
                case 8:
                    myMechanic.billHistory();
                    break;
                case 9:
                    myMechanic.manageProducts();
                    break;
                case 10:
                    myMechanic.manageServices();
                    break;
                case 0:
                    System.out.println("Exit Program. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}
