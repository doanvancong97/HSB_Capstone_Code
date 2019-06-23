package capstone.sonnld.hairsalonbooking.model;



public class Booking {
    private String booking_id;

    private String booking_date;

    private String booking_time;

    private String customer_id;

    private Account account;

    private String status;

    public String getBooking_id ()
    {
        return booking_id;
    }

    public void setBooking_id (String booking_id)
    {
        this.booking_id = booking_id;
    }

    public String getBooking_date ()
    {
        return booking_date;
    }

    public void setBooking_date (String booking_date)
    {
        this.booking_date = booking_date;
    }

    public String getBooking_time ()
    {
        return booking_time;
    }

    public void setBooking_time (String booking_time)
    {
        this.booking_time = booking_time;
    }

    public String getCustomer_id ()
    {
        return customer_id;
    }

    public void setCustomer_id (String customer_id)
    {
        this.customer_id = customer_id;
    }

    public Account getAccount ()
    {
        return account;
    }

    public void setAccount (Account account)
    {
        this.account = account;
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
