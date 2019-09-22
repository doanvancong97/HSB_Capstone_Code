package capstone.sonnld.hairsalonbooking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelSalonService implements Serializable {

    @SerializedName("executeTime")
    @Expose
    private Integer executeTime;
    @SerializedName("modelDiscount")
    @Expose
    private ModelDiscount modelDiscount;
    @SerializedName("modelSalon")
    @Expose
    private ModelSalon modelSalon;
    @SerializedName("modelService")
    @Expose
    private ModelService modelService;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("salon_service_id")
    @Expose
    private Integer salonServiceId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("thumb_url")
    @Expose
    private String thumbUrl;

    private transient double distance;
    private transient double discountPoint;
    private transient double ratingPoint;
    private transient double distancePoint;
    private transient double avgPoint;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDiscountPoint() {
        return discountPoint;
    }

    public void setDiscountPoint(double discountPoint) {
        this.discountPoint = discountPoint;
    }

    public double getRatingPoint() {
        return ratingPoint;
    }

    public void setRatingPoint(double ratingPoint) {
        this.ratingPoint = ratingPoint;
    }

    public double getDistancePoint() {
        return distancePoint;
    }

    public void setDistancePoint(double distancePoint) {
        this.distancePoint = distancePoint;
    }

    public void setDistancePoint(int distancePoint) {
        this.distancePoint = distancePoint;
    }

    public double getAvgPoint() {
        return avgPoint;
    }

    public void setAvgPoint(double avgPoint) {
        this.avgPoint = avgPoint;
    }

    public void setDiscountPoint(int discountPoint) {
        this.discountPoint = discountPoint;
    }

    public Integer getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Integer executeTime) {
        this.executeTime = executeTime;
    }

    public ModelDiscount getModelDiscount() {
        return modelDiscount;
    }

    public void setModelDiscount(ModelDiscount modelDiscount) {
        this.modelDiscount = modelDiscount;
    }

    public ModelSalon getModelSalon() {
        return modelSalon;
    }

    public void setModelSalon(ModelSalon modelSalon) {
        this.modelSalon = modelSalon;
    }

    public ModelService getModelService() {
        return modelService;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
}
