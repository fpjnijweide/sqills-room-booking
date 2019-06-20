package nl.utwente.model;

import java.io.Serializable;
import java.security.Principal;

public class User implements Serializable, Principal {
    private int userid;
    private String name;
    private String email;
    private boolean administrator;

    public User() {
    }

    public User(int userid, String name, String email, boolean administrator) {
        this.userid = userid;
        this.name = name;
        this.email = email;
        this.administrator = administrator;
    }

    public int getUserid() {
        return userid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }
}
