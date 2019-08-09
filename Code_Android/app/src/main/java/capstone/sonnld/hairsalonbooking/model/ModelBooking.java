package capstone.sonnld.hairsalonbooking.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ModelBooking implements Serializable {
    @SerializedName("modelAccount")
    @Expose
    private ModelAccount modelAccount;
    @SerializedName("modelBookingDetailsCollection")
    @Expose
    private ArrayList<ModelBookingDetail> modelBookingDetailsCollection = null;
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

    public ModelAccount getModelAccount() {
        return modelAccount;
    }

    public void setModelAccount(ModelAccount modelAccount) {
        this.modelAccount = modelAccount;
    }

    public ArrayList<ModelBookingDetail> getModelBookingDetailsCollection() {
        return modelBookingDetailsCollection;
    }

    public void setModelBookingDetailsCollection(ArrayList<ModelBookingDetail> modelBookingDetailsCollection) {
        this.modelBookingDetailsCollection = modelBookingDetailsCollection;
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
