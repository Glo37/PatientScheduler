import org.mindrot.jbcrypt.BCrypt;
public class User {
    private String name;
    private String username;
    private String passwordHash;
    private Role role;

    public enum Role {
        NURSE, DOCTOR, ADMIN
    }

    public User(String name, String username, String plainPassword, Role role) {
        this.name = name;
        this.username = username;
        this.role = role;

        this.passwordHash = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public boolean authenticate(String username, String plainPassword) {
        if (!this.username.equals(username)) {
            return false;
        }

        return BCrypt.checkpw(plainPassword, this.passwordHash);
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (!authenticate(this.username, oldPassword)) {
            return false;
        }

        this.passwordHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        return true;
    }

    public boolean canDelete() {
        return role == Role.DOCTOR || role == Role.ADMIN;
    }

    public boolean canAddPatient(){
        return role == Role.DOCTOR || role == Role.NURSE;
    }

    public boolean canModifyPriority() {
        return role == Role.DOCTOR || role == Role.ADMIN;
    }

    public String getName(){
        return name;
    }

    public String getUsername(){
        return username;
    }

    public Role getRole(){
        return role;
    }

    @Override
    public String toString() {
        return String.format("User: %s (%s) - Role: %s", name, username, role);
    }
}
