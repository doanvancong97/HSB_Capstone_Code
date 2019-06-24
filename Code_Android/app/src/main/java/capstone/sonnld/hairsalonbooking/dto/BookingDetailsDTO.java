package capstone.sonnld.hairsalonbooking.dto;

public class BookingDetailsDTO {
    Integer id;
    Integer salon_service_id;
    Integer review_id;
    String serviceName;
    String status;// process

    public BookingDetailsDTO(Integer salon_service_id, Integer review_id, String serviceName, String status) {
        this.salon_service_id = salon_service_id;
        this.review_id = review_id;
        this.serviceName = serviceName;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSalon_service_id() {
        return salon_service_id;
    }

    public void setSalon_service_id(Integer salon_service_id) {
        this.salon_service_id = salon_service_id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getReview_id() {
        return review_id;
    }

    public void setReview_id(Integer review_id) {
        this.review_id = review_id;
    }
}
