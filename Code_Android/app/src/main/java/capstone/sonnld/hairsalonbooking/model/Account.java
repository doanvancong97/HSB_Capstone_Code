package capstone.sonnld.hairsalonbooking.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Account implements Serializable {
    private transient final String DEFAULT_AVATAR_URL = "https://firebasestorage.googleapis.com/v0/b/codeandroid-b6876.appspot.com/o/default%20logo.PNG?alt=media&token=6e06341b-d09c-4e08-8077-1022954a5858";

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("birthdate")
    @Expose
    private String birthdate;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("job")
    @Expose
    private String job;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("create_time")
    @Expose
    private Object createTime;
    @SerializedName("update_time")
    @Expose
    private Object updateTime;
    @SerializedName("role_id")
    @Expose
    private Integer roleId;


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

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }

    public Object getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Object updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
