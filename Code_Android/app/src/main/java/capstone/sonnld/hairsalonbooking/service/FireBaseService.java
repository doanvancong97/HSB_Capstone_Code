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
import android.view.View;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import capstone.sonnld.hairsalonbooking.HistoryDetailActivity;
import capstone.sonnld.hairsalonbooking.MainActivity;
import capstone.sonnld.hairsalonbooking.R;
import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.dto.NotifyDTO;
import capstone.sonnld.hairsalonbooking.model.ModelAccount;
import capstone.sonnld.hairsalonbooking.model.ModelBookingDetail;
import capstone.sonnld.hairsalonbooking.model.ModelNotify;
import capstone.sonnld.hairsalonbooking.support.SessionManager;
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

    //Session Login
    private SessionManager sessionManager;
    private int userID;
    private int salonId;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //init api
        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);


        sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isLogin()) {

            HashMap<String, String> user = sessionManager.getUserDetail();
            userID = Integer.parseInt(user.get("userId"));

        }

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
        salonId = Integer.parseInt(data.get("salonId"));

        // save notify to DB
        saveNotifyToDB();

        //onclick notify
        getListBookingDetail(bookId);

    }

    private void updateNotifyNumber() {
        countUnOpenNotify();
    }

    private void countUnOpenNotify() {
        Call<Integer> integerCall = hairSalonAPI.countUnOpenNotify(userID);
        integerCall.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                int numberOfNotify = response.body();
                Intent intent = new Intent();
                intent.putExtra("extra", numberOfNotify);
                intent.setAction("capstone.sonnld.hairsalonbooking.onMessageReceived");
                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }
    private void saveNotifyToDB() {
        String statusCode = "";
        if(status.equals("bị hủy")){
            statusCode = "3salonCancel";
        }else if(status.equals("hoàn thành")){
            statusCode = "4finish";
        }
        String isRead = "no";
        NotifyDTO notifyDTO = new NotifyDTO(bookDate,bookTime,userID,salonId,bookId,statusCode,isRead);
        saveNotify(notifyDTO);
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
                    statusCode = "3salonCancel";
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
                intent.putExtra("Seen","yes");

                PendingIntent pendingIntent = PendingIntent.getActivity(FireBaseService.this,
                        1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                // format date time
                String[] bookedDateArr = bookDate.split("-");
                String formatedBookDate = bookedDateArr[2]+"/"+bookedDateArr[1]+"/"+bookedDateArr[0];
                String[] bookedTimeArr = bookTime.split(":");
                String formatedBookTime = bookedTimeArr[0]+":"+bookedTimeArr[1];

                NotificationCompat.Builder builder = new NotificationCompat.Builder(FireBaseService.this, NOTIFY_CHANEL_ID);
                builder.setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.my_logo)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setContentTitle("Lịch đặt chỗ đã " + status)
                        .setContentText("Lịch đặt chỗ ngày " + formatedBookDate + " lúc " + formatedBookTime + " đã " + status)
                        .setContentInfo("");
                notificationManager.notify(new Random().nextInt(), builder.build());

            }

            @Override
            public void onFailure(Call<ArrayList<ModelBookingDetail>> call, Throwable t) {

            }
        });
    }

    private void saveNotify(NotifyDTO notifyDTO) {
        Call<ModelNotify> modelNotifyCall = hairSalonAPI.saveNotify(notifyDTO);

        modelNotifyCall.enqueue(new Callback<ModelNotify>() {
            @Override
            public void onResponse(Call<ModelNotify> call, Response<ModelNotify> response) {
                // update number of notify
                updateNotifyNumber();
            }

            @Override
            public void onFailure(Call<ModelNotify> call, Throwable t) {

            }
        });
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.d("TOKENFIREBASE", s);
    }
}
