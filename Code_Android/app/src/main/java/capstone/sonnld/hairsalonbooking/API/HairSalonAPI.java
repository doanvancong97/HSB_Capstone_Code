package capstone.sonnld.hairsalonbooking.api;

import java.util.List;

import capstone.sonnld.hairsalonbooking.model.SalonService;
import retrofit2.Call;
import retrofit2.http.GET;

public interface HairSalonAPI {
    @GET("salonservice")
    Call<List<SalonService>> getAllServiceByDiscountValue();

    @GET("salonservices")
    Call<List<SalonService>> getAllServiceByRating();
}
