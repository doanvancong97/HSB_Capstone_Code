package capstone.sonnld.hairsalonbooking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelNotify implements Serializable {
    @SerializedName("bookDate")
    @Expose
    private String bookDate;
    @SerializedName("bookTime")
    @Expose
    private String bookTime;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("modelAccount")
    @Expose
    private ModelAccount modelAccount;
    @SerializedName("modelSalon")
    @Expose
    private ModelSalon modelSalon;
    @SerializedName("notifyId")
    @Expose
    private Integer notifyId;
    @SerializedName("bookId")
    @Expose
    private Integer bookId;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("isRead")
    @Expose
    private String isRead;

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
    }

    public String getBookTime() {
        return bookTime;
    }

    public void setBookTime(String bookTime) {
        this.bookTime = bookTime;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public ModelAccount getModelAccount() {
        return modelAccount;
    }

    public void setModelAccount(ModelAccount modelAccount) {
        this.modelAccount = modelAccount;
    }

    public ModelSalon getModelSalon() {
        return modelSalon;
    }

    public void setModelSalon(ModelSalon modelSalon) {
        this.modelSalon = modelSalon;
    }

    public Integer getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(Integer notifyId) {
        this.notifyId = notifyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
