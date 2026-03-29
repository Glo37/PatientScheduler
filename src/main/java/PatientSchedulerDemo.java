public class PatientSchedulerDemo {
    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        HospitalLoginSystem loginSystem = new HospitalLoginSystem();

        loginSystem.showLoginPrompt();

        // Login loop
        while (!loginSystem.isLoggedIn()) {
            System.out.print("Username: ");
            String username = scanner.nextLine();

            // Hide password input if console is available
            String password;
            if (System.console() != null) {
                char[] passwordChars = System.console().readPassword("Password: ");
                password = new String(passwordChars);
            } else {
                System.out.print("Password: ");
                password = scanner.nextLine();
            }

            loginSystem.login(username, password);
        }

        // Add sample patients
        Patient patient1 = new Patient(1, "Emily", "Vega", "1990-05-15", 65.5,
                "Dr. Ramsey", "2025-03-25", 6);
        Patient patient2 = new Patient(2, "Lena", "Morales", "1985-08-22", 70.2,
                "Dr. Ramsey", "2025-03-25", 9);
        Patient patient3 = new Patient(3, "Carlos", "Bennett", "1978-03-10", 80.0,
                "Dr. Ramsey", "2025-03-25", 5);
        Patient patient4 = new Patient(4, "Avery", "Chen", "1995-11-30", 55.3,
                "Dr. Ramsey", "2025-03-25", 7);

        loginSystem.getScheduler().addPatient(6, patient1);
        loginSystem.getScheduler().addPatient(9, patient2);
        loginSystem.getScheduler().addPatient(5, patient3);
        loginSystem.getScheduler().addPatient(7, patient4);

        System.out.println("\nSystem ready!");
        System.out.println("   Logged in as: " + loginSystem.getCurrentUser().getName());

        System.out.println("Next patient: " + loginSystem.getScheduler().peekNextPatient().getFullName());
        System.out.println("Total patients: " + loginSystem.getScheduler().getPatientCount());

        // Show the main menu
        loginSystem.showMenu(scanner);

        scanner.close();
    }
}