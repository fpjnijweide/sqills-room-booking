package nl.utwente.model;

public class InputUser extends UserAdministration {
    // Used for registering / creating users
    private String fullName;

    public InputUser(){
        super();
    }

    public InputUser(String username, String password, String fullName){
        super(username, password);
        this.fullName = fullName;
    }

    /**
     * Gets fullName
     *
     * @return value of fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets fullName to fullName
     *
     * @param fullName new value of fullName
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
