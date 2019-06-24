package capstone.sonnld.hairsalonbooking.api;

import java.util.ArrayList;

import capstone.sonnld.hairsalonbooking.dto.BookingDTO;
import capstone.sonnld.hairsalonbooking.dto.BookingDetailsDTO;
import capstone.sonnld.hairsalonbooking.model.BookingDetail;
import capstone.sonnld.hairsalonbooking.model.SalonService;
import io.reactivex.Observable;

import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HairSalonAPI {
    @GET("salonservice")
    Observable<ArrayList<SalonService>> getAllServiceByDiscountValue();

    @GET("booking-details")
    Observable<ArrayList<BookingDetail>> getAllServiceByRating();

    @GET("salonservice/{id}")
    Observable<ArrayList<SalonService>> getSalonServiceBySalonId(@Path("id") int id);

    @POST("booking")
    Observable<BookingDTO> postBooking(@Field("customerId") int cusId,
                                       @Field("customerName") String cusName,
                                       @Field("customerPhone") String phone,
                                       @Field("bookingDetailList") ArrayList<BookingDetailsDTO> bookingDetails);


}
