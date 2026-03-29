
public enum MenuOption {

    VIEW_SCHEDULE(1, "View schedule"),
    ADD_PATIENT(2, "Add patient"),
    CALL_NEXT(3, "Call next patient"),
    REMOVE_PATIENT(4, "Remove patient"),
    VIEW_PRIORITY(5, "View priority levels"),
    CHANGE_PASSWORD(6, "Change password"),
    LOGOUT(7, "Logout"),
    EXIT(8, "Exit");

    private final int number;
    private final String description;

    MenuOption(int number, String description) {
        this.number = number;
        this.description = description;
    }

    public int getNumber() { return number; }
    public String getDescription() { return description; }

    public static MenuOption fromNumber(int number) {
        for (MenuOption option : values()) {
            if (option.number == number) {
                return option;
            }
        }
        return null;
    }

    public static void displayMenu() {
        System.out.println("     Edenbrook Hospital Scheduler    ");
        for (MenuOption option : values()) {
            System.out.printf(" %d. %-33s \n", option.number, option.description);
        }
        System.out.println();
    }
}