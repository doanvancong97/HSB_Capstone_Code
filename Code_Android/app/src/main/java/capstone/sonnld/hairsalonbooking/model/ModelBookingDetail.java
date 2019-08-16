package capstone.sonnld.hairsalonbooking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelBookingDetail implements Serializable {
    @SerializedName("booking_details_id")
    @Expose
    private Integer bookingDetailsId;
    @SerializedName("booking_id")
    @Expose
    private Integer bookingId;
    @SerializedName("modelReview")
    @Expose
    private ModelReview modelReview;
    @SerializedName("review_id")
    @Expose
    private Integer reviewId;
    @SerializedName("modelSalonService")
    @Expose
    private ModelSalonService modelSalonService;
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

    public ModelReview getModelReview() {
        return modelReview;
    }

    public void setModelReview(ModelReview modelReview) {
        this.modelReview = modelReview;
    }

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public ModelSalonService getModelSalonService() {
        return modelSalonService;
    }

    public void setModelSalonService(ModelSalonService modelSalonService) {
        this.modelSalonService = modelSalonService;
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
