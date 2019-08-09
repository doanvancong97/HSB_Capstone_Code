package capstone.sonnld.hairsalonbooking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelAddress implements Serializable {

    @SerializedName("address_id")
    @Expose
    private Integer addressId;
    @SerializedName("modelDistrict")
    @Expose
    private ModelDistrict modelDistrict;
    @SerializedName("street")
    @Expose
    private String street;
    @SerializedName("street_number")
    @Expose
    private String streetNumber;

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public ModelDistrict getModelDistrict() {
        return modelDistrict;
    }

    public void setModelDistrict(ModelDistrict modelDistrict) {
        this.modelDistrict = modelDistrict;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }
}
