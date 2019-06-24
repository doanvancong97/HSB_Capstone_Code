package capstone.sonnld.hairsalonbooking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BookingDetail implements Serializable {
    @SerializedName("booking_details_id")
    @Expose
    private Integer bookingDetailsId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("booking")
    @Expose
    private Booking booking;
    @SerializedName("salonService")
    @Expose
    private SalonService salonService;
    @SerializedName("review")
    @Expose
    private Review review;

    public Integer getBookingDetailsId() {
        return bookingDetailsId;
    }

    public void setBookingDetailsId(Integer bookingDetailsId) {
        this.bookingDetailsId = bookingDetailsId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public SalonService getSalonService() {
        return salonService;
    }

    public void setSalonService(SalonService salonService) {
        this.salonService = salonService;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
