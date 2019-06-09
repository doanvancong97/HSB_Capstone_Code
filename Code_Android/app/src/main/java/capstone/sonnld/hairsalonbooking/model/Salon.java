package capstone.sonnld.hairsalonbooking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Salon {
    @SerializedName("salon_id")
    @Expose
    private Integer salonId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("status")
    @Expose
    private Object status;
    @SerializedName("manager_id")
    @Expose
    private Integer managerId;
    @SerializedName("url")
    @Expose
    private String url;

    public Integer getSalonId() {
        return salonId;
    }

    public void setSalonId(Integer salonId) {
        this.salonId = salonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
