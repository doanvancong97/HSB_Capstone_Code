package capstone.sonnld.hairsalonbooking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Account implements Serializable {
    private transient final String DEFAULT_AVATAR_URL = "https://firebasestorage.googleapis.com/v0/b/codeandroid-b6876.appspot.com/o/AvatarImages%2Favatar-doi-dep-38_015656136.jpg?alt=media&token=0bcf00fd-b979-4958-9b61-6a4875e52de8";

    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("birthdate")
    @Expose
    private String birthdate;
    @SerializedName("create_time")
    @Expose
    private String createTime;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("job")
    @Expose
    private String job;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("role_id")
    @Expose
    private Integer roleId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("update_time")
    @Expose
    private String updateTime;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("username")
    @Expose
    private String username;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Account(String username, String password, String fullname, String email, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.avatar = DEFAULT_AVATAR_URL;
    }

    public Account(String fullname, String email, String phoneNumber, String avatar) {
        this.fullname = fullname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.avatar = avatar;
    }

    public Account(String fullname, String email, String phoneNumber) {
        this.fullname = fullname;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Account(String password) {
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
