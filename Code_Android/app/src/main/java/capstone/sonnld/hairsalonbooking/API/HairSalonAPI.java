package capstone.sonnld.hairsalonbooking.api;

import java.util.List;

import capstone.sonnld.hairsalonbooking.model.BookingDetail;
import capstone.sonnld.hairsalonbooking.model.SalonService;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HairSalonAPI {
    @GET("salonservice")
    Observable<List<SalonService>> getAllServiceByDiscountValue();

    @GET("booking-details")
    Observable<List<BookingDetail>> getAllServiceByRating();

    @GET("salonservice/{id}")
    Observable<List<SalonService>> getSalonServiceBySalonId(@Path("id") int id);
}
