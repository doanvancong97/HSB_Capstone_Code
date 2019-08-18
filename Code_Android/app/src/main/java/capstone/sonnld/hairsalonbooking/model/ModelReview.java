package capstone.sonnld.hairsalonbooking.model;

import android.accounts.Account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelReview implements Serializable {
    @SerializedName("account")
    @Expose
    private ModelAccount account;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("review_id")
    @Expose
    private Integer reviewId;
    @SerializedName("status")
    @Expose
    private String status;

    public ModelAccount getAccount() {
        return account;
    }

    public void setAccount(ModelAccount account) {
        this.account = account;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
