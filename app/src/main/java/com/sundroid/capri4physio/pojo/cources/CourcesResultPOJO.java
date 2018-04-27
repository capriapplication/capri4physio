package com.sundroid.capri4physio.pojo.cources;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sunil on 29-07-2017.
 */

public class CourcesResultPOJO implements Serializable{
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("comt")
    private String comt;
    @SerializedName("from_date")
    private String fromDate;
    @SerializedName("to_date")
    private String toDate;
    @SerializedName("place")
    private String place;
    @SerializedName("seat_available")
    private String seatAvailable;
    @SerializedName("rem_seat")
    private String remSeat;
    @SerializedName("showing_seat")
    private String showingSeat;
    @SerializedName("reg_fees")
    private String regFees;
    @SerializedName("rem_fees")
    private String remFees;
    @SerializedName("full_fees")
    private String fullFees;
    @SerializedName("phone")
    private String phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComt() {
        return comt;
    }

    public void setComt(String comt) {
        this.comt = comt;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getSeatAvailable() {
        return seatAvailable;
    }

    public void setSeatAvailable(String seatAvailable) {
        this.seatAvailable = seatAvailable;
    }

    public String getRemSeat() {
        return remSeat;
    }

    public void setRemSeat(String remSeat) {
        this.remSeat = remSeat;
    }

    public String getShowingSeat() {
        return showingSeat;
    }

    public void setShowingSeat(String showingSeat) {
        this.showingSeat = showingSeat;
    }

    public String getRegFees() {
        return regFees;
    }

    public void setRegFees(String regFees) {
        this.regFees = regFees;
    }

    public String getRemFees() {
        return remFees;
    }

    public void setRemFees(String remFees) {
        this.remFees = remFees;
    }

    public String getFullFees() {
        return fullFees;
    }

    public void setFullFees(String fullFees) {
        this.fullFees = fullFees;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
