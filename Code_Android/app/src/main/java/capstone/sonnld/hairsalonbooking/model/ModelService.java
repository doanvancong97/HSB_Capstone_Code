package capstone.sonnld.hairsalonbooking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelService implements Serializable {
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("modelCategory")
    @Expose
    private ModelCategory modelCategory;
    @SerializedName("service_id")
    @Expose
    private Integer serviceId;
    @SerializedName("service_name")
    @Expose
    private String serviceName;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public ModelCategory getModelCategory() {
        return modelCategory;
    }

    public void setModelCategory(ModelCategory modelCategory) {
        this.modelCategory = modelCategory;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
