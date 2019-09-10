package capstone.sonnld.hairsalonbooking.service;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import capstone.sonnld.hairsalonbooking.HistoryDetailActivity;
import capstone.sonnld.hairsalonbooking.R;
import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.model.ModelBookingDetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class FireBaseService extends FirebaseMessagingService {

    // api
    private HairSalonAPI hairSalonAPI;

    String bookDate;
    String bookTime;
    int bookId;
    String status;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //init api
        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);


        showNotify(remoteMessage.getData());

    }

    private void showNotify(Map<String, String> data) {

        bookDate = data.get("date");
        bookTime = data.get("time");
        bookId = Integer.parseInt(data.get("bookId"));
        if (data.get("status").equals("cancel")) {
            status = "bị hủy";
        } else {
            status = "hoàn thành";
        }

        //onclick notify
        getListBookingDetail(bookId);


    }

    private void getListBookingDetail(int id) {
        Call<ArrayList<ModelBookingDetail>> call = hairSalonAPI.getBookingDetailByBookingId(id);
        call.enqueue(new Callback<ArrayList<ModelBookingDetail>>() {
            @Override
            public void onResponse(Call<ArrayList<ModelBookingDetail>> call, Response<ArrayList<ModelBookingDetail>> response) {
                ArrayList<ModelBookingDetail> selectedService = response.body();
                String address = selectedService.get(0).getModelSalonService()
                        .getModelSalon().getModelAddress().getStreetNumber() + ", " +
                        selectedService.get(0).getModelSalonService()
                                .getModelSalon().getModelAddress().getStreet() + ", " +
                        selectedService.get(0).getModelSalonService()
                                .getModelSalon().getModelAddress().getModelDistrict().getDistrictName() + ", " +
                        selectedService.get(0).getModelSalonService()
                                .getModelSalon().getModelAddress().getModelDistrict().getModelCity().getCityName();
                String salonName = selectedService.get(0).getModelSalonService().getModelSalon().getName();
                String statusCode = "";
                if(status.equals("bị hủy")){
                    statusCode = "2cancel";
                }else if(status.equals("hoàn thành")){
                    statusCode = "4finish";
                }

                // setup notify
                NotificationManager notificationManager
                        = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                // copy ten package
                String NOTIFY_CHANEL_ID = "day01.sonnld.firebasemess.test";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel =
                            new NotificationChannel(NOTIFY_CHANEL_ID, "Notification",
                                    NotificationManager.IMPORTANCE_DEFAULT);
                    notificationChannel.setDescription("Son notify");
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.BLUE);
                    notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                    notificationChannel.enableLights(true);
                    notificationManager.createNotificationChannel(notificationChannel);
                }


                Intent intent = new Intent(FireBaseService.this, HistoryDetailActivity.class);
                intent.putExtra("BookedDate", bookDate);
                intent.putExtra("BookedTime", bookTime);
                intent.putExtra("BookingId", bookId);
                intent.putExtra("SalonName", salonName);
                intent.putExtra("BookingStatus", statusCode);
                intent.putExtra("ModelAddress", address);
                intent.putExtra("SelectedService", selectedService);

                PendingIntent pendingIntent = PendingIntent.getActivity(FireBaseService.this,
                        1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(FireBaseService.this, NOTIFY_CHANEL_ID);
                builder.setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.my_logo)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setContentTitle("Lịch đặt chỗ đã " + status)
                        .setContentText("Lịch đặt chỗ ngày " + bookDate + " lúc " + bookTime + "đã " + status)
                        .setContentInfo("");
                notificationManager.notify(new Random().nextInt(), builder.build());
            }

            @Override
            public void onFailure(Call<ArrayList<ModelBookingDetail>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.d("TOKENFIREBASE", s);
    }
}
