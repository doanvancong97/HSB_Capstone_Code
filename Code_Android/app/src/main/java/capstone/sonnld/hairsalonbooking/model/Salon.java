package capstone.sonnld.hairsalonbooking.model;

public class Salon {

    private String salonName;
    private String promotionName;
    private String address;
    private String description;
    private String thumbnailUrl;
    private String salonServiceListName;
    private String saleValue;
    private double rate;

    public Salon() {
    }

    public Salon(String salonName, String address, String description, String thumbnailUrl, String salonServiceListName, String saleValue) {
        this.salonName = salonName;
        this.address = address;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.salonServiceListName = salonServiceListName;
        this.saleValue = saleValue;
    }

    public Salon(String salonName, String address, String description, String thumbnailUrl, String saleValue, double rate) {
        this.salonName = salonName;
        this.address = address;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.saleValue = saleValue;
        this.rate = rate;
    }

    public Salon(String salonName, String promotionName, String address, String description, String thumbnailUrl, String saleValue, double rate) {
        this.salonName = salonName;
        this.promotionName = promotionName;
        this.address = address;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.saleValue = saleValue;
        this.rate = rate;
    }

    public Salon(String salonName, String address, String description, String thumbnailUrl) {
        this.salonName = salonName;
        this.address = address;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
    }

    public Salon(String salonName, String address, String description, String thumbnailUrl, String saleValue) {
        this.salonName = salonName;
        this.address = address;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.saleValue = saleValue;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getSalonName() {
        return salonName;
    }

    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getSalonServiceListName() {
        return salonServiceListName;
    }

    public void setSalonServiceListName(String salonServiceListName) {
        this.salonServiceListName = salonServiceListName;
    }

    public String getSaleValue() {
        return saleValue;
    }

    public void setSaleValue(String saleValue) {
        this.saleValue = saleValue;
    }
}

