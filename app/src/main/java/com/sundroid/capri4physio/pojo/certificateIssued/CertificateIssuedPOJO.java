package com.sundroid.capri4physio.pojo.certificateIssued;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunil on 11-03-2018.
 */

public class CertificateIssuedPOJO {
    @SerializedName("success")
    String success;
    @SerializedName("result")
    List<CertificateIssuedResultPOJO> certificateIssuedResultPOJOS;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<CertificateIssuedResultPOJO> getCertificateIssuedResultPOJOS() {
        return certificateIssuedResultPOJOS;
    }

    public void setCertificateIssuedResultPOJOS(List<CertificateIssuedResultPOJO> certificateIssuedResultPOJOS) {
        this.certificateIssuedResultPOJOS = certificateIssuedResultPOJOS;
    }
}
