package capstone.sonnld.hairsalonbooking.api;

import java.util.ArrayList;

import capstone.sonnld.hairsalonbooking.dto.BookingDTO;
import capstone.sonnld.hairsalonbooking.model.Account;
import capstone.sonnld.hairsalonbooking.model.Booking;
import capstone.sonnld.hairsalonbooking.model.BookingDetail;
import capstone.sonnld.hairsalonbooking.model.Salon;
import capstone.sonnld.hairsalonbooking.model.SalonService;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HairSalonAPI {

    @GET("salonservice")
    Call<ArrayList<SalonService>> getAllServiceByDiscountValue();

    @GET("booking-details")
    Call<ArrayList<BookingDetail>> getAllServiceByRating();

    @GET("salonservice/{id}")
    Call<ArrayList<SalonService>> getSalonServiceBySalonId(@Path("id") int id);

    @GET("users-details/{username}")
    Call<Account> getUserDetail(@Path("username") String username);

    @POST("booking")
    Call<BookingDTO> postBooking(@Body BookingDTO bookingDTO);

    @POST("login")
    Call<Void> checkLogin(@Body Account account);

    @POST("users/{id}")
    Call<Void> updateUser(@Path("id") int id, @Body Account account);

    @POST("sign-up")
    Call<String> registerUser(@Body Account account);

    @GET("bookingHistories/{id}")
    Call<ArrayList<Booking>> getBookingHistoryByUserID(@Path("id") int accountId);

}
