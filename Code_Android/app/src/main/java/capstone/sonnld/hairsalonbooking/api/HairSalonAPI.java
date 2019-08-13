package capstone.sonnld.hairsalonbooking.api;

import java.util.ArrayList;

import capstone.sonnld.hairsalonbooking.dto.BookingDTO;
import capstone.sonnld.hairsalonbooking.model.ModelAccount;
import capstone.sonnld.hairsalonbooking.model.ModelBooking;
import capstone.sonnld.hairsalonbooking.model.ModelSalonService;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HairSalonAPI {

    @GET("salonservice/getAllActiveSalonService")
    Call<ArrayList<ModelSalonService>> getAllServiceByDiscountValue();

    @GET("salonservice/{id}")
    Call<ArrayList<ModelSalonService>> getSalonServiceBySalonId(@Path("id") int id);

    @GET("users-details/{username}")
    Call<ModelAccount> getUserDetail(@Path("username") String username);

    @POST("booking")
    Call<BookingDTO> postBooking(@Body BookingDTO bookingDTO);

    @POST("login")
    Call<Void> checkLogin(@Body ModelAccount modelAccount);

    @POST("users/{id}")
    Call<Void> updateUser(@Path("id") int id, @Body ModelAccount modelAccount);

    @POST("sign-up")
    Call<String> registerUser(@Body ModelAccount modelAccount);

    @GET("bookingHistories/{id}")
    Call<ArrayList<ModelBooking>> getBookingHistoryByUserID(@Path("id") int accountId);

    @POST("cancelBooking/{id}")
    Call<ModelBooking> cancelBooking(@Path("id")int bookingId);
}
