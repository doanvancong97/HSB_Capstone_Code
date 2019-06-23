package capstone.sonnld.hairsalonbooking.model;

import com.google.gson.annotations.SerializedName;

public class SalonService {

    @SerializedName("salon_service_id")
    private Integer salonServiceId;
    @SerializedName("salon")

    private Salon salon;
    @SerializedName("service")

    private Service service;
    @SerializedName("discount")

    private Discount discount;
    @SerializedName("price")

    private String price;
    @SerializedName("status")

    private Object status;

    public Integer getSalonServiceId() {
        return salonServiceId;
    }

    public void setSalonServiceId(Integer salonServiceId) {
        this.salonServiceId = salonServiceId;
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
