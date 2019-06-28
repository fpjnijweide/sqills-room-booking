package nl.utwente.model;

public class UserAdministration {
    // TODO why is this field called username, not email?
    private String username;
    private String password;

    public UserAdministration() {
    }

    public UserAdministration(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
