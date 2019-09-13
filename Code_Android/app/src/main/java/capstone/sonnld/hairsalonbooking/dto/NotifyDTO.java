package capstone.sonnld.hairsalonbooking.dto;

public class NotifyDTO {

    private String bookDate;
    private String bookTime;
    private int customerId;
    private int salonId;
    private int bookId;
    private String status;
    private String isRead;

    public NotifyDTO(String bookDate, String bookTime, int customerId, int salonId, int bookId, String status, String isRead) {
        this.bookDate = bookDate;
        this.bookTime = bookTime;
        this.customerId = customerId;
        this.salonId = salonId;
        this.bookId = bookId;
        this.status = status;
        this.isRead = isRead;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
    }

    public String getBookTime() {
        return bookTime;
    }

    public void setBookTime(String bookTime) {
        this.bookTime = bookTime;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getSalonId() {
        return salonId;
    }

    public void setSalonId(int salonId) {
        this.salonId = salonId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
