package nl.utwente.model;

public class InputUser extends UserAdministration {

    public InputUser(String username, String password, String fullName, boolean admin){
        super(username, password);
        this.fullName = fullName;
        this.admin = admin;
    }

    // Used for registering / creating users
    private String fullName;
    private  boolean admin;

    /**
     * Gets admin
     *
     * @return value of admin
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Sets admin to admin
     *
     * @param admin new value of admin
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public InputUser(){
        super();
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
