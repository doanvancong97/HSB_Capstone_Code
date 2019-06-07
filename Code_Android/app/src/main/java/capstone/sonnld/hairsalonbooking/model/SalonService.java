package capstone.sonnld.hairsalonbooking.model;

public class SalonService {
    private String serviceName;
    private int serviceIcon;
    private int price;

    public SalonService(String serviceName, int serviceIcon) {
        this.serviceName = serviceName;
        this.serviceIcon = serviceIcon;
    }

    public SalonService(String serviceName, int serviceIcon, int price) {
        this.serviceName = serviceName;
        this.serviceIcon = serviceIcon;
        this.price = price;
    }

    public SalonService() {
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getServiceIcon() {
        return serviceIcon;
    }

    public void setServiceIcon(int serviceIcon) {
        this.serviceIcon = serviceIcon;
    }
}
