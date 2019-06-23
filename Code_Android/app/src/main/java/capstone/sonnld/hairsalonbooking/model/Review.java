package capstone.sonnld.hairsalonbooking.model;

public class Review {
    private String review_id;

    private String user_id;

    private String rating;

    private String create_date;

    private String status;

    public String getReview_id ()
    {
        return review_id;
    }

    public void setReview_id (String review_id)
    {
        this.review_id = review_id;
    }

    public String getUser_id ()
    {
        return user_id;
    }

    public void setUser_id (String user_id)
    {
        this.user_id = user_id;
    }

    public String getRating ()
    {
        return rating;
    }

    public void setRating (String rating)
    {
        this.rating = rating;
    }

    public String getCreate_date ()
    {
        return create_date;
    }

    public void setCreate_date (String create_date)
    {
        this.create_date = create_date;
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
