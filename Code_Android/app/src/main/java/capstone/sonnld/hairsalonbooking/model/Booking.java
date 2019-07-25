package capstone.sonnld.hairsalonbooking.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Booking implements Serializable {
    @SerializedName("account")
    @Expose
    private Account account;
    @SerializedName("bookingDetailsCollection")
    @Expose
    private ArrayList<BookingDetail> bookingDetailsCollection = null;
    @SerializedName("booking_date")
    @Expose
    private String bookingDate;
    @SerializedName("booking_id")
    @Expose
    private Integer bookingId;
    @SerializedName("booking_time")
    @Expose
    private String bookingTime;
    @SerializedName("customer_id")
    @Expose
    private Integer customerId;
    @SerializedName("status")
    @Expose
    private String status;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public ArrayList<BookingDetail> getBookingDetailsCollection() {
        return bookingDetailsCollection;
    }

    public void setBookingDetailsCollection(ArrayList<BookingDetail> bookingDetailsCollection) {
        this.bookingDetailsCollection = bookingDetailsCollection;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
