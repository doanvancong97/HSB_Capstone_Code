package capstone.sonnld.hairsalonbooking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelDistrict implements Serializable {
    @SerializedName("districtId")
    @Expose
    private Integer districtId;
    @SerializedName("districtName")
    @Expose
    private String districtName;
    @SerializedName("modelCity")
    @Expose
    private ModelCity modelCity;

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public ModelCity getModelCity() {
        return modelCity;
    }

    public void setModelCity(ModelCity modelCity) {
        this.modelCity = modelCity;
    }

}
