package com.hairbooking.hairsalon.dto;



public class SalonServiceDTO {
    private String serviceName;
    private String salonName;
    private String salonAddress;
//    private String discountValue;
    private String salonImageUrl;

    public SalonServiceDTO(String serviceName, String salonName, String salonAddress, String salonImageUrl) {
        this.serviceName = serviceName;
        this.salonName = salonName;
        this.salonAddress = salonAddress;
        this.salonImageUrl = salonImageUrl;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getSalonName() {
        return salonName;
    }

    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }

    public String getSalonAddress() {
        return salonAddress;
    }

    public void setSalonAddress(String salonAddress) {
        this.salonAddress = salonAddress;
    }

    public String getSalonImageUrl() {
        return salonImageUrl;
    }

    public void setSalonImageUrl(String salonImageUrl) {
        this.salonImageUrl = salonImageUrl;
    }
}
