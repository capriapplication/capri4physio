package com.sundroid.capri4physio.pojo.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sunil on 05-03-2018.
 */

public class AdminUserPOJO implements Serializable{
    @SerializedName("id")
    private String id;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("email")
    private String email;
    @SerializedName("profile_pic")
    private String profilePic;
    @SerializedName("device_type")
    private String deviceType;
    @SerializedName("device_token")
    private String deviceToken;
    @SerializedName("user_type")
    private String userType;
    @SerializedName("user")
    private UserPOJO userPOJO;


    public AdminUserPOJO(String id, String firstName, String lastName, String email, String profilePic, String deviceType, String deviceToken, String userType) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profilePic = profilePic;
        this.deviceType = deviceType;
        this.deviceToken = deviceToken;
        this.userType = userType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public UserPOJO getUserPOJO() {
        return userPOJO;
    }

    public void setUserPOJO(UserPOJO userPOJO) {
        this.userPOJO = userPOJO;
    }
}
