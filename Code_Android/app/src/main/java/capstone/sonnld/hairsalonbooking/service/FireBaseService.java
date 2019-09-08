package capstone.sonnld.hairsalonbooking.service;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Random;

import capstone.sonnld.hairsalonbooking.R;


public class FireBaseService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        showNotify(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());


    }

    private void showNotify(String rawTitle, String rawBody) {
        // format UTF 8 to unicode
        String title = rawTitle;
        String body = rawBody;
        System.out.println("Title: " + title);

//        byte[] charsetTitle = new byte[0];
//        byte[] charsetBody = new byte[0];
//        try {
//            charsetTitle = rawTitle.getBytes("UTF-8");
//            charsetBody = rawBody.getBytes("UTF-8");
//            title = new String(charsetTitle, "UTF-8");
//            body = new String(charsetBody, "UTF-8");
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }


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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFY_CHANEL_ID);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.accept)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("");
        notificationManager.notify(new Random().nextInt(), builder.build());
    }

    private String formatUTF(String myString){

        String b = "";
        byte[] arr = new byte[0];
        try {
            arr = myString.getBytes("UTF-8");
            Charset charset = Charset.forName("UTF-8");
            b = new String(arr, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("Format B1111: " +b);

//        for(byte a: arr)
//        {
//            System.out.print(a);
//            b += a;
//        }
//        System.out.println("Format B: "  +b);
        return b;
    }


    private void showNotifyBackGround(Map<String, String> data) {

        String title = data.get("title").toString();
        String body = data.get("body").toString();


        NotificationManager notificationManager
                = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // copy ten package
        String NOTIFY_CHANEL_ID = "capstone.sonnld.hairsalonbooking.test";

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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFY_CHANEL_ID);

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.accept)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("");
        notificationManager.notify(new Random().nextInt(), builder.build());
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.d("TOKENFIREBASE", s);
    }
}
