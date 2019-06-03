package nl.utwente.model;

public class UserIDBookingIDPair {
    private int userid;
    private int bookingid;

    public int getUserid() {
        return userid;
    }

    public int getBookingid() {
        return bookingid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setBookingid(int bookingid) {
        this.bookingid = bookingid;
    }
}
