package capstone.sonnld.hairsalonbooking.api;

import java.util.ArrayList;

import capstone.sonnld.hairsalonbooking.dto.BookingDTO;
import capstone.sonnld.hairsalonbooking.dto.BookingDetailsDTO;
import capstone.sonnld.hairsalonbooking.model.BookingDetail;
import capstone.sonnld.hairsalonbooking.model.SalonService;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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

//    @FormUrlEncoded
//    @POST("booking")
//    Call<BookingDTO> postBooking(
//            @Field("status") String status,
//            @Field("customerPhone") String phone,
//            @Field("customerName") String cusName,
//            @Field("customerId") int cusId,
//            @Field("bookingTime") String bookingTime,
//            @Field("bookingDate") String bookingDate,
//            @Field("bookingDetailList") ArrayList<BookingDetailsDTO> bookingDetails);


    @POST("booking")
    Call<BookingDTO> postBooking(@Body BookingDTO bookingDTO);
}
