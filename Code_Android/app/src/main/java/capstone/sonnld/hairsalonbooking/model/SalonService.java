package capstone.sonnld.hairsalonbooking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalonService {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("salon")
    @Expose
    private Salon salon;
    @SerializedName("service")
    @Expose
    private Service service;
    @SerializedName("discount")
    @Expose
    private Discount discount;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("status")
    @Expose
    private Object status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Salon getSalon() {
        return salon;
    }

    public void setSalon(Salon salon) {
        this.salon = salon;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }
}
