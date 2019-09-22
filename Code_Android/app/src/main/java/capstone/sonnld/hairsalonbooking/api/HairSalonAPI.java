package capstone.sonnld.hairsalonbooking.api;

import java.util.ArrayList;
import java.util.List;

import capstone.sonnld.hairsalonbooking.dto.BookingDTO;
import capstone.sonnld.hairsalonbooking.dto.NotifyDTO;
import capstone.sonnld.hairsalonbooking.dto.RatingDTO;
import capstone.sonnld.hairsalonbooking.model.ModelAccount;
import capstone.sonnld.hairsalonbooking.model.ModelBooking;
import capstone.sonnld.hairsalonbooking.model.ModelBookingDetail;
import capstone.sonnld.hairsalonbooking.model.ModelNotify;
import capstone.sonnld.hairsalonbooking.model.ModelReview;
import capstone.sonnld.hairsalonbooking.model.ModelSalon;
import capstone.sonnld.hairsalonbooking.model.ModelSalonService;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HairSalonAPI {

    // user account
    @GET("users-details/{username}")
    Call<ModelAccount> getUserDetail(@Path("username") String username);

    @POST("login")
    Call<Void> checkLogin(@Body ModelAccount modelAccount);

    @POST("users/{id}")
    Call<Void> updateUser(@Path("id") int id, @Body ModelAccount modelAccount);

    @POST("sign-up")
    Call<String> registerUser(@Body ModelAccount modelAccount);

    // review
    @POST("addReviewToBooking")
    Call<ModelBooking> addReviewToBooking(@Body RatingDTO ratingDTO);

    @GET("review/getAllReviewBySalonId/{id}")
    Call<ArrayList<ModelReview>> getAllReviewBySalonId(@Path("id") int salonId);

    // salon API
    @GET("salon/getAllSalonByRating")
    Call<ArrayList<ModelSalon>> getAllSalonByRating();

    @GET("salon/getSalonIdByBookingId/{id}")
    Call<Integer> getSalonIdByBookingId(@Path("id") int bookingId);


    // salon service
    @GET("salonservice/searchServiceByName/{serviceName}")
    Call<ArrayList<ModelSalonService>> searchServiceByName(@Path("serviceName") String serviceName);

    @GET("salonservice/getAllActiveSalonService")
    Call<ArrayList<ModelSalonService>> getAllServiceByDiscountValue();

    @GET("salonservice/{salonId}")
    Call<ArrayList<ModelSalonService>> getSalonServiceBySalonId(@Path("salonId") int id);

    // booking
    @GET("booking-details/getBookingDetailByBookingId/{bookingId}")
    Call<ArrayList<ModelBookingDetail>> getBookingDetailByBookingId(@Path("bookingId") int bookingId);

    @GET("bookingHistories/{id}")
    Call<ArrayList<ModelBooking>> getBookingHistoryByUserID(@Path("id") int accountId);

    @POST("customerCancelBooking/{id}")
    Call<ModelBooking> cancelBooking(@Path("id") int bookingId);

    @GET("countNumberOfBooking/{bookedDate}/{bookedTime}/{salonId}")
    Call<Integer> countNumberOfBooking(@Path("bookedDate") String bookedDate,
                                       @Path("bookedTime") String bookedTime,
                                       @Path("salonId") int salonId);
    @POST("booking")
    Call<BookingDTO> postBooking(@Body BookingDTO bookingDTO);

    // notify API
    @POST("notify/saveNotify")
    Call<ModelNotify> saveNotify(@Body NotifyDTO notifyDTO);

    @GET("notify/getAllNotifyByCusId/{id}")
    Call<List<ModelNotify>> getAllNotifyByCusId(@Path("id") int id);

    @POST("notify/updateNotifyIsRead/{id}")
    Call<ModelNotify> updateIsRead(@Path("id") int bookingID);

    @GET("notify/countUnOpenNotify/{id}")
    Call<Integer> countUnOpenNotify(@Path("id") int id);

}
