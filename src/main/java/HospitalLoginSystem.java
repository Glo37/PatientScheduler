import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HospitalLoginSystem {
    private Map<String, User> users = new HashMap<>();
    private User currentUser = null;
    private PatientScheduler scheduler;

    public HospitalLoginSystem() {
        this.scheduler = new PatientScheduler();
        setupDefaultUsers();
    }

    private void setupDefaultUsers() {
        addUser("Dr. Ethan Ramsey", "drramsey", "doctor123", User.Role.DOCTOR);
        addUser("Nurse Elma", "nelma", "nurse123", User.Role.NURSE);
        addUser("Admin", "admin", "admin123", User.Role.ADMIN);
    }

    public void addUser(String name, String username, String password, User.Role role) {
        User user = new User(name, username, password, role);
        users.put(username, user);
    }

    public boolean login(String username, String password) {
        User user = users.get(username);

        if (user == null) {
            System.out.println("Login failed - Username not found");
            return false;
        }

        if (user.authenticate(username, password)) {
            currentUser = user;
            System.out.println("\nLogin successful!");
            System.out.println("   Welcome, " + user.getName() + " (" + user.getRole() + ")");
            return true;
        } else {
            System.out.println("Login failed - Wrong username or password");
            return false;
        }
    }

    public void logout() {
        if (currentUser != null) {
            System.out.println("Goodbye, " + currentUser.getName());
            currentUser = null;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public PatientScheduler getScheduler() {
        return scheduler;
    }

    public void showLoginPrompt() {
        System.out.println("Welcome to Edenbrook Hospital Patient Scheduling System!");
    }

    // Main menu method
    public void showMenu(Scanner scanner) {

        while (true) {
            displayMenuHeader();
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            MenuOption option = MenuOption.fromNumber(choice);
            if (option == null) {
                System.out.println("Invalid option. Please choose 1-8.");
                continue;
            }

            switch (option) {
                case VIEW_SCHEDULE:
                    scheduler.printSchedule();
                    break;
                case ADD_PATIENT:
                    if (currentUser.canAddPatient()) {
                        addPatientMenu(scanner);
                    } else {
                        System.out.println("Permission denied: Only doctors and nurses can add patients.");
                    }
                    break;
                case CALL_NEXT:
                    System.out.println(scheduler.callNextPatient());
                    break;
                case REMOVE_PATIENT:
                    if (currentUser.canDelete()) {
                        System.out.print("Enter patient ID to remove: ");
                        int id = scanner.nextInt();
                        boolean removed = scheduler.removePatientById(id);
                        if (removed) {
                            System.out.println("Patient removed successfully.");
                        } else {
                            System.out.println("Patient not found.");
                        }
                    } else {
                        System.out.println("Permission denied: Only doctors and admins can delete patients.");
                    }
                    break;
                case VIEW_PRIORITY:
                    scheduler.printPriorityLevels();
                    break;
                case CHANGE_PASSWORD:
                    changePasswordMenu(scanner);
                    break;
                case LOGOUT:
                    logout();
                    return;
                case EXIT:
                    System.out.println("Goodbye!");
                    System.exit(0);
                    break;
            }
        }
    }

    private void displayMenuHeader() {
        System.out.println("\nEdenbrook Hospital Scheduler");
        System.out.printf("Logged in as: %-23s \n",
                currentUser.getName() + " (" + currentUser.getRole() + ")");
        System.out.println("\n1. View schedule");
        System.out.println("2. Add patient");
        System.out.println("3. Call next patient");
        System.out.println("4. Remove patient");
        System.out.println("5. View priority levels");
        System.out.println("6. Change password");
        System.out.println("7. Logout");
        System.out.println("8. Exit");
        System.out.println();
    }

    private void addPatientMenu(Scanner scanner) {
        System.out.println("\nAdd New Patient");

        System.out.print("ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("DOB (YYYY-MM-DD): ");
        String dob = scanner.nextLine();

        System.out.print("Weight (kg): ");
        double weight = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Primary Doctor: ");
        String doctor = scanner.nextLine();

        System.out.print("Scheduled Visit Date (YYYY-MM-DD): ");
        String visit = scanner.nextLine();

        System.out.print("Priority (1-10, 10 is highest): ");
        int priority = scanner.nextInt();

        Patient patient = new Patient(id, firstName, lastName, dob, weight, doctor, visit, priority);
        scheduler.addPatient(priority, patient);
        System.out.println("Patient added successfully!");
    }

    private void changePasswordMenu(Scanner scanner) {
        System.out.println("\nChange Password");

        System.out.print("Enter current password: ");
        String oldPass = scanner.nextLine();

        System.out.print("Enter new password (min 6 characters): ");
        String newPass = scanner.nextLine();

        System.out.print("Confirm new password: ");
        String confirmPass = scanner.nextLine();

        if (!newPass.equals(confirmPass)) {
            System.out.println("Passwords do not match.");
            return;
        }

        if (newPass.length() < 6) {
            System.out.println("Password must be at least 6 characters.");
            return;
        }

        if (currentUser.changePassword(oldPass, newPass)) {
            System.out.println("Password changed successfully!");
        } else {
            System.out.println("Current password is incorrect.");
        }
    }
}