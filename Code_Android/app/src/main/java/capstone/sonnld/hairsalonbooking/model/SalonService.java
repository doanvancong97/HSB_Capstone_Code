package capstone.sonnld.hairsalonbooking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SalonService implements Serializable {

    @SerializedName("discount")
    @Expose
    private Discount discount;
    @SerializedName("icon_url")
    @Expose
    private String iconUrl;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("salon")
    @Expose
    private Salon salon;
    @SerializedName("salon_service_id")
    @Expose
    private Integer salonServiceId;
    @SerializedName("service")
    @Expose
    private Service service;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("thumb_url")
    @Expose
    private String thumbUrl;

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Salon getSalon() {
        return salon;
    }

    public void setSalon(Salon salon) {
        this.salon = salon;
    }

    public Integer getSalonServiceId() {
        return salonServiceId;
    }

    public void setSalonServiceId(Integer salonServiceId) {
        this.salonServiceId = salonServiceId;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
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
