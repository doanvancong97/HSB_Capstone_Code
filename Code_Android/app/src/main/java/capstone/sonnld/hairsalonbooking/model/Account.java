package capstone.sonnld.hairsalonbooking.model;

import java.io.Serializable;

public class Account implements Serializable {
    private String password;

    private String birthdate;

    private String address;

    private String phoneNumber;

    private String user_id;

    private String roleId;

    private String fullname;

    private String avatar;

    private String job;

    private String email;

    private String username;

    private String status;

    public String getPassword ()
    {
        return password;
    }

    public void setPassword (String password)
    {
        this.password = password;
    }

    public String getBirthdate ()
    {
        return birthdate;
    }

    public void setBirthdate (String birthdate)
    {
        this.birthdate = birthdate;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getPhoneNumber ()
    {
        return phoneNumber;
    }

    public void setPhoneNumber (String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getUser_id ()
    {
        return user_id;
    }

    public void setUser_id (String user_id)
    {
        this.user_id = user_id;
    }

    public String getRoleId ()
    {
        return roleId;
    }

    public void setRoleId (String roleId)
    {
        this.roleId = roleId;
    }

    public String getFullname ()
    {
        return fullname;
    }

    public void setFullname (String fullname)
    {
        this.fullname = fullname;
    }

    public String getAvatar ()
    {
        return avatar;
    }

    public void setAvatar (String avatar)
    {
        this.avatar = avatar;
    }

    public String getJob ()
    {
        return job;
    }

    public void setJob (String job)
    {
        this.job = job;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }
}
