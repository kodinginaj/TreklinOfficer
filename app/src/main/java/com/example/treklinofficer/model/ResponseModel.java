package com.example.treklinofficer.model;

import java.util.List;

public class ResponseModel {

    String status, message;
    OfficerModel dataOfficer;
    List<ComplaintModel> data;

    public ResponseModel(String status, String message, OfficerModel dataOfficer,  List<ComplaintModel> data) {
        this.status = status;
        this.message = message;
        this.dataOfficer = dataOfficer;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OfficerModel getDataOfficer() {
        return dataOfficer;
    }

    public void setDataOfficer(OfficerModel dataOfficer) {
        this.dataOfficer = dataOfficer;
    }

    public List<ComplaintModel> getDataComplaint() {
        return data;
    }

    public void setDataComplaint(List<ComplaintModel> data) {
        this.data = data;
    }
}
