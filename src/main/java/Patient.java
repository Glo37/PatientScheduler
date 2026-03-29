public class Patient {

    private int id;
    private String firstName, lastName;

    private final String dob;

    private double weight;

    private String primaryDoctor;

    private String scheduledVisit;
    private String comments = "";
    private int priority;

    public Patient(int id, String firstName, String lastName, String dob, double weight, String primaryDoctor, String scheduledVisit, int priority) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.weight = weight;
        this.primaryDoctor = primaryDoctor;
        this.scheduledVisit = scheduledVisit;
        this.priority = priority;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s %s | DOB: %s | Priority: %d | Doctor: %s | Visit: %s", id, firstName, lastName, dob, priority, primaryDoctor, scheduledVisit);
    }

    public int getId() { return id; }
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getDob() {return dob;}
    public double getWeight() {return weight;}
    public String getPrimaryDoctor() {return primaryDoctor;}
    public String getScheduledVisit() {return scheduledVisit;}
    public String getComments() {return comments;}
    public void setComments(String comments) {this.comments = comments;}
    public int getPriority() {return priority;}

    public void setPriority(int priority) {this.priority = priority;}
}

