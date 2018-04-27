package com.sundroid.capri4physio.pojo.studentcourse;

import com.google.gson.annotations.SerializedName;
import com.sundroid.capri4physio.pojo.cources.CourcesResultPOJO;

import java.io.Serializable;

/**
 * Created by sunil on 31-07-2017.
 */

public class StudentCourseResultPOJO implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("c_id")
    private String cId;
    @SerializedName("stu_id")
    private String stuId;
    @SerializedName("stu_app_id")
    private String stuAppId;
    @SerializedName("a_stu_app_id")
    private String aStuAppId;
    @SerializedName("photo_upload")
    private String photoUpload;
    @SerializedName("a_photo_upload")
    private String aPhotoUpload;
    @SerializedName("cert_upload")
    private String certUpload;
    @SerializedName("a_cert_upload")
    private String aCertUpload;
    @SerializedName("id_upload")
    private String idUpload;
    @SerializedName("a_id_upload")
    private String aIdUpload;
    @SerializedName("stu_reg_fees")
    private String stuRegFees;
    @SerializedName("a_stu_reg_fees")
    private String aStuRegFees;
    @SerializedName("stu_rem_fees")
    private String stuRemFees;
    @SerializedName("a_stu_rem_fees")
    private String aStuRemFees;
    @SerializedName("stu_full_fees")
    private String stuFullFees;
    @SerializedName("a_stu_full_fees")
    private String aStuFullFees;
    @SerializedName("stu_date")
    private String stuDate;
    @SerializedName("stu_time")
    private String stuTime;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("email")
    private String email;
    @SerializedName("gender")
    private String gender;
    @SerializedName("profile_pic")
    private String profilePic;
    @SerializedName("city")
    private String city;
    @SerializedName("state")
    private String state;
    @SerializedName("address")
    private String address;
    @SerializedName("country")
    private String country;
    @SerializedName("pincode")
    private String pincode;
    @SerializedName("course")
    CourcesResultPOJO courcesResultPOJO;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getStuAppId() {
        return stuAppId;
    }

    public void setStuAppId(String stuAppId) {
        this.stuAppId = stuAppId;
    }

    public String getaStuAppId() {
        return aStuAppId;
    }

    public void setaStuAppId(String aStuAppId) {
        this.aStuAppId = aStuAppId;
    }

    public String getPhotoUpload() {
        return photoUpload;
    }

    public void setPhotoUpload(String photoUpload) {
        this.photoUpload = photoUpload;
    }

    public String getaPhotoUpload() {
        return aPhotoUpload;
    }

    public void setaPhotoUpload(String aPhotoUpload) {
        this.aPhotoUpload = aPhotoUpload;
    }

    public String getCertUpload() {
        return certUpload;
    }

    public void setCertUpload(String certUpload) {
        this.certUpload = certUpload;
    }

    public String getaCertUpload() {
        return aCertUpload;
    }

    public void setaCertUpload(String aCertUpload) {
        this.aCertUpload = aCertUpload;
    }

    public String getIdUpload() {
        return idUpload;
    }

    public void setIdUpload(String idUpload) {
        this.idUpload = idUpload;
    }

    public String getaIdUpload() {
        return aIdUpload;
    }

    public void setaIdUpload(String aIdUpload) {
        this.aIdUpload = aIdUpload;
    }

    public String getStuRegFees() {
        return stuRegFees;
    }

    public void setStuRegFees(String stuRegFees) {
        this.stuRegFees = stuRegFees;
    }

    public String getaStuRegFees() {
        return aStuRegFees;
    }

    public void setaStuRegFees(String aStuRegFees) {
        this.aStuRegFees = aStuRegFees;
    }

    public String getStuRemFees() {
        return stuRemFees;
    }

    public void setStuRemFees(String stuRemFees) {
        this.stuRemFees = stuRemFees;
    }

    public String getaStuRemFees() {
        return aStuRemFees;
    }

    public void setaStuRemFees(String aStuRemFees) {
        this.aStuRemFees = aStuRemFees;
    }

    public String getStuFullFees() {
        return stuFullFees;
    }

    public void setStuFullFees(String stuFullFees) {
        this.stuFullFees = stuFullFees;
    }

    public String getaStuFullFees() {
        return aStuFullFees;
    }

    public void setaStuFullFees(String aStuFullFees) {
        this.aStuFullFees = aStuFullFees;
    }

    public String getStuDate() {
        return stuDate;
    }

    public void setStuDate(String stuDate) {
        this.stuDate = stuDate;
    }

    public String getStuTime() {
        return stuTime;
    }

    public void setStuTime(String stuTime) {
        this.stuTime = stuTime;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public CourcesResultPOJO getCourcesResultPOJO() {
        return courcesResultPOJO;
    }

    public void setCourcesResultPOJO(CourcesResultPOJO courcesResultPOJO) {
        this.courcesResultPOJO = courcesResultPOJO;
    }
}
