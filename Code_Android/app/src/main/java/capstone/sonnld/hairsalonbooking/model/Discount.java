package capstone.sonnld.hairsalonbooking.model;

public class Discount {
    private String user_id;

    private String discountUnit;

    private String validUntil;

    private String validFrom;

    private String discountId;

    private String discountValue;

    private String createDate;

    private String status;

    public String getUser_id ()
    {
        return user_id;
    }

    public void setUser_id (String user_id)
    {
        this.user_id = user_id;
    }

    public String getDiscountUnit ()
    {
        return discountUnit;
    }

    public void setDiscountUnit (String discountUnit)
    {
        this.discountUnit = discountUnit;
    }

    public String getValidUntil ()
    {
        return validUntil;
    }

    public void setValidUntil (String validUntil)
    {
        this.validUntil = validUntil;
    }

    public String getValidFrom ()
    {
        return validFrom;
    }

    public void setValidFrom (String validFrom)
    {
        this.validFrom = validFrom;
    }

    public String getDiscountId ()
    {
        return discountId;
    }

    public void setDiscountId (String discountId)
    {
        this.discountId = discountId;
    }

    public String getDiscountValue ()
    {
        return discountValue;
    }

    public void setDiscountValue (String discountValue)
    {
        this.discountValue = discountValue;
    }

    public String getCreateDate ()
    {
        return createDate;
    }

    public void setCreateDate (String createDate)
    {
        this.createDate = createDate;
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
