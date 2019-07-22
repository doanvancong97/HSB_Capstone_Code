package capstone.sonnld.hairsalonbooking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BookingDetail implements Serializable {
    @SerializedName("booking_details_id")
    @Expose
    private Integer bookingDetailsId;
    @SerializedName("booking_id")
    @Expose
    private Integer bookingId;
    @SerializedName("review")
    @Expose
    private Review review;
    @SerializedName("review_id")
    @Expose
    private Integer reviewId;
    @SerializedName("salonService")
    @Expose
    private SalonService salonService;
    @SerializedName("salon_service_id")
    @Expose
    private Integer salonServiceId;
    @SerializedName("status")
    @Expose
    private String status;

    public Integer getBookingDetailsId() {
        return bookingDetailsId;
    }

    public void setBookingDetailsId(Integer bookingDetailsId) {
        this.bookingDetailsId = bookingDetailsId;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public SalonService getSalonService() {
        return salonService;
    }

    public void setSalonService(SalonService salonService) {
        this.salonService = salonService;
    }

    public Integer getSalonServiceId() {
        return salonServiceId;
    }

    public void setSalonServiceId(Integer salonServiceId) {
        this.salonServiceId = salonServiceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
