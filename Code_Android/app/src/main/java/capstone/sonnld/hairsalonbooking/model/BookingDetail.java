package capstone.sonnld.hairsalonbooking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BookingDetail implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("salon_service_id")
    @Expose
    private Integer salonServiceId;
    @SerializedName("review_id")
    @Expose
    private Integer reviewId;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("salonName")
    @Expose
    private String salonName;
    @SerializedName("serviceName")
    @Expose
    private String serviceName;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("salonService")
    @Expose
    private SalonService salonService;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSalonServiceId() {
        return salonServiceId;
    }

    public void setSalonServiceId(Integer salonServiceId) {
        this.salonServiceId = salonServiceId;
    }

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getSalonName() {
        return salonName;
    }

    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SalonService getSalonService() {
        return salonService;
    }

    public void setSalonService(SalonService salonService) {
        this.salonService = salonService;
    }
}
