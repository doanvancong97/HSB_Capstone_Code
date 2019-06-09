package capstone.sonnld.hairsalonbooking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Discount {
    @SerializedName("discountId")
    @Expose
    private Integer discountId;
    @SerializedName("discountValue")
    @Expose
    private String discountValue;
    @SerializedName("discountUnit")
    @Expose
    private String discountUnit;
    @SerializedName("createDate")
    @Expose
    private String createDate;
    @SerializedName("validFrom")
    @Expose
    private String validFrom;
    @SerializedName("validUntil")
    @Expose
    private String validUntil;
    @SerializedName("couponCode")
    @Expose
    private Object couponCode;
    @SerializedName("minimumBookingValue")
    @Expose
    private Object minimumBookingValue;
    @SerializedName("maximumDiscountAmount")
    @Expose
    private Object maximumDiscountAmount;
    @SerializedName("status")
    @Expose
    private Object status;

    public Integer getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
    }

    public String getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(String discountValue) {
        this.discountValue = discountValue;
    }

    public String getDiscountUnit() {
        return discountUnit;
    }

    public void setDiscountUnit(String discountUnit) {
        this.discountUnit = discountUnit;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }

    public Object getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(Object couponCode) {
        this.couponCode = couponCode;
    }

    public Object getMinimumBookingValue() {
        return minimumBookingValue;
    }

    public void setMinimumBookingValue(Object minimumBookingValue) {
        this.minimumBookingValue = minimumBookingValue;
    }

    public Object getMaximumDiscountAmount() {
        return maximumDiscountAmount;
    }

    public void setMaximumDiscountAmount(Object maximumDiscountAmount) {
        this.maximumDiscountAmount = maximumDiscountAmount;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }
}
