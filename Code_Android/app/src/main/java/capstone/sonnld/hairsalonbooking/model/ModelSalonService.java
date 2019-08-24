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
