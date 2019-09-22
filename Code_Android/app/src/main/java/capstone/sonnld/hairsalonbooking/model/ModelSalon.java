package capstone.sonnld.hairsalonbooking.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ModelSalon implements Serializable {
    @SerializedName("close_time")
    @Expose
    private String closeTime;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("logo_url")
    @Expose
    private String logoUrl;
    @SerializedName("manager_id")
    @Expose
    private Integer managerId;
    @SerializedName("modelAddress")
    @Expose
    private ModelAddress modelAddress;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("open_time")
    @Expose
    private String openTime;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("salon_id")
    @Expose
    private Integer salonId;
    @SerializedName("slot_time")
    @Expose
    private Integer slotTime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("bookingDay")
    @Expose
    private Integer bookingDay;
    @SerializedName("bookingPerSlot")
    @Expose
    private int bookingPerSlot;
    @SerializedName("averageRating")
    @Expose
    private float averageRating;
    @SerializedName("avgDiscount")
    @Expose
    private float avgDiscount;

    private transient double distance;
    private transient double discountPoint;
    private transient double ratingPoint;
    private transient double distancePoint;
    private transient double avgPoint;

    public void setBookingDay(Integer bookingDay) {
        this.bookingDay = bookingDay;
    }

    public float getAvgDiscount() {
        return avgDiscount;
    }

    public void setAvgDiscount(float avgDiscount) {
        this.avgDiscount = avgDiscount;
    }

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

    public double getAvgPoint() {
        return avgPoint;
    }

    public void setAvgPoint(double avgPoint) {
        this.avgPoint = avgPoint;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public int getBookingPerSlot() {
        return bookingPerSlot;
    }

    public void setBookingPerSlot(int bookingPerSlot) {
        this.bookingPerSlot = bookingPerSlot;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public int getBookingDay() {
        return bookingDay;
    }

    public void setBookingDay(int bookingDay) {
        this.bookingDay = bookingDay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public ModelAddress getModelAddress() {
        return modelAddress;
    }

    public void setModelAddress(ModelAddress modelAddress) {
        this.modelAddress = modelAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getSalonId() {
        return salonId;
    }

    public void setSalonId(Integer salonId) {
        this.salonId = salonId;
    }

    public Integer getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(Integer slotTime) {
        this.slotTime = slotTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
