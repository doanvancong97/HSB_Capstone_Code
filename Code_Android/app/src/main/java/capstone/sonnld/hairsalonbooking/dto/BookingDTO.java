package capstone.sonnld.hairsalonbooking.dto;

import java.sql.Date;
import java.util.List;

public class BookingDTO {
    private Integer id;//
    private Integer customerId; // 1
    private String customerName;
    private String customerPhone;
    private String bookingDate;
    private String bookingTime;
    private String status;///process
    private List<BookingDetailsDTO> bookingDetailsList;

    public BookingDTO(Integer customerId, String customerName, String customerPhone, String bookingDate, String bookingTime, String status, List<BookingDetailsDTO> bookingDetailsList) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.status = status;
        this.bookingDetailsList = bookingDetailsList;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }


    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    public List<BookingDetailsDTO> getBookingDetailsList() {
        return bookingDetailsList;
    }

    public void setBookingDetailsList(List<BookingDetailsDTO> bookingDetailsList) {
        this.bookingDetailsList = bookingDetailsList;
    }
}
