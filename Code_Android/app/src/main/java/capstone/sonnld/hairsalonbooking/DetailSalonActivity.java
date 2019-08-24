package capstone.sonnld.hairsalonbooking;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewExtraServiceAdapter;
import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewReviewAdapter;
import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.model.ModelAccount;
import capstone.sonnld.hairsalonbooking.model.ModelReview;
import capstone.sonnld.hairsalonbooking.model.ModelSalonService;
import capstone.sonnld.hairsalonbooking.support.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static capstone.sonnld.hairsalonbooking.R.drawable.button_full;
import static capstone.sonnld.hairsalonbooking.R.drawable.button_time;

public class DetailSalonActivity extends AppCompatActivity implements DatePickerListener {
    private TextView txtSalonName;
    private TextView txtAddress;
    private ReadMoreTextView txtDescription;
    private HairSalonAPI hairSalonAPI;
    private Button btnConfirm;
    private RecyclerView recyclerView;
    private ImageView imgThumb;
    private ImageView imgLogo;
    private TextView txtAvgRating;

    private LinearLayout linearTimePiker;

    // user detail
    private String mUserName;
    private SessionManager sessionManager;
    private String fullName;
    private int userId;
    private String phone;

    //Button time
    private boolean isChoose = false;
    private int slotIDisChoose = 0;
    private int slotID = 0;
    private String bookedTime = "";

    private int salonId;
    private int numberOfPeopleBooked = 5;
    private int bookingPerSlot = 0;
    private double step;

    private String salonStartTime;
    private String salonEndTime;
    private int salonSlotTime;
    private int salonBookingDay;

    private RecyclerView recyclerViewReview;
    private RecyclerViewReviewAdapter viewReviewAdapter;
    private RecyclerViewExtraServiceAdapter extraServiceAdapter;
    private String bookedDate = "";
    private ArrayList<ModelSalonService> chkService = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_salon);

        //init retro
        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);

        // find view
        txtAddress = findViewById(R.id.txt_address);
        txtSalonName = findViewById(R.id.txtSalonName);
        txtDescription = findViewById(R.id.txt_description2);
        imgThumb = findViewById(R.id.img_thumbnail);
        btnConfirm = findViewById(R.id.btn_confirm);
        imgLogo = findViewById(R.id.img_logo);
        txtAvgRating = findViewById(R.id.txt_avg_rating);
        recyclerViewReview = findViewById(R.id.recycler_view_review);

        HorizontalPicker picker = (HorizontalPicker) findViewById(R.id.datePicker);

        // setup user
        sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isLogin()) {
            HashMap<String, String> user = sessionManager.getUserDetail();
            mUserName = user.get(sessionManager.getUSERNAME());
            initUserDetail();
        }

        // get data from map activity/ RecyclerViewSalonByRatingAdapter
        Intent intent = getIntent();
        salonId = intent.getExtras().getInt("SalonId");
        String salonName = intent.getExtras().getString("SalonName");
        salonStartTime = intent.getExtras().getString("SalonStartTime");
        salonEndTime = intent.getExtras().getString("SalonEndTime");
        salonSlotTime = intent.getExtras().getInt("SalonSlotTime");
        salonBookingDay = intent.getExtras().getInt("SalonBookingDay");
        bookingPerSlot = intent.getExtras().getInt("SalonBookingPerSlot");
        float avgRating = intent.getExtras().getFloat("AvgRating");

        txtSalonName.setText(salonName);
        txtAvgRating.setText("Đánh giá trung bình: " + Math.floor(avgRating*10)/10);
        // recycler for review
        recyclerViewReview = findViewById(R.id.recycler_view_review);
        recyclerViewReview.setLayoutManager(new GridLayoutManager(DetailSalonActivity.this, 1));
        getAllReview(salonId);

        //  init time
        picker.setListener(this)
                .setTodayDateTextColor(R.color.red2)
                .setDays(salonBookingDay)
                .setOffset(0)
                .showTodayButton(false)
                .init();
        picker.setDate(new DateTime());

        linearTimePiker = findViewById(R.id.linearTimePicker);

        String maxHour = salonEndTime;
        String[] splitMaxHour = maxHour.split(":");
        int maxH = Integer.parseInt(splitMaxHour[0]);
        String minHour = salonStartTime;
        String[] splitMinHour = minHour.split(":");
        int minH = Integer.parseInt(splitMinHour[0]);
        double step = salonSlotTime;

        double run = (maxH - minH) / (step / 60) + Integer.parseInt(splitMaxHour[1]) / step - Integer.parseInt(splitMinHour[1]) / step;

        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {

            Date start = format.parse(minHour);
            Date end = format.parse(maxHour);
            calendar.setTime(start);
            calendar2.setTime(end);
            for (int i = 0; i <= run; i++) {
                slotID++;
                final Button slot = new Button(this);
                slot.setId(slotID);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(10, 10, 10, 10);
                slot.setLayoutParams(params);
                slot.setBackgroundResource(button_time);
                slot.setTextColor(Color.parseColor("#DB1507"));
                slot.setText(calendar.getTime().getHours() + ":" + calendar.getTime().getMinutes());


                linearTimePiker.addView(slot);
                calendar.add(Calendar.MINUTE, (int) step);
                slot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(DetailSalonActivity.this, slot.getText(), Toast.LENGTH_SHORT).show();
                        bookedTime = slot.getText().toString();
                        if (isChoose == false) {
                            slot.setBackgroundResource(R.drawable.button_time_choose);
                            slot.setTextColor(Color.WHITE);
                            isChoose = true;
                            slotIDisChoose = slot.getId();

                        } else {

                            Button b = findViewById(slotIDisChoose);
                            b.setBackgroundResource(button_time);
                            b.setTextColor(Color.parseColor("#DB1507"));


                            slot.setBackgroundResource(R.drawable.button_time_choose);
                            slot.setTextColor(Color.WHITE);
                            isChoose = true;
                            slotIDisChoose = slot.getId();
                        }
                    }
                });

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        // recycler for extra service
        recyclerView = findViewById(R.id.recycler_view_salon_service);
        recyclerView.setLayoutManager(new GridLayoutManager(DetailSalonActivity.this, 1));
        setupSalonDetail(salonId);


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setup data for booking

                chkService = extraServiceAdapter.getCheckedModelSalonServices();
                if (fullName == null) {
                    Toast.makeText(DetailSalonActivity.this,
                            "Hãy đăng nhập để tiếp tục đặt lịch!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(DetailSalonActivity.this, LoginActivity.class));
                } else if (chkService.size() == 0) {
                    Toast.makeText(DetailSalonActivity.this, "Bạn chưa chọn dịch vụ!", Toast.LENGTH_LONG).show();

                } else if (!isChoose) {
                    Toast.makeText(DetailSalonActivity.this,
                            "Bạn chưa chọn thời gian đến!", Toast.LENGTH_LONG).show();
                } else {
                    // send data to booking detail activity
                    Intent sendDataToBooking =
                            new Intent(DetailSalonActivity.this, BookingDetailActivity.class);
                    sendDataToBooking.putExtra("chkService", chkService);
                    sendDataToBooking.putExtra("bookedDate", bookedDate);
                    sendDataToBooking.putExtra("bookedTime", bookedTime);
                    sendDataToBooking.putExtra("salonAddress", txtAddress.getText());
                    sendDataToBooking.putExtra("fullname", fullName);
                    sendDataToBooking.putExtra("userId", userId);
                    sendDataToBooking.putExtra("phoneNum", phone);
                    sendDataToBooking.putExtra("description", txtDescription.getText().toString());
                    startActivity(sendDataToBooking);
                }

            }
        });
    }

    private void getAllReview(int salonId) {
        Call<ArrayList<ModelReview>> listCall = hairSalonAPI.getAllReviewBySalonId(salonId);
        listCall.enqueue(new Callback<ArrayList<ModelReview>>() {
            @Override
            public void onResponse(Call<ArrayList<ModelReview>> call, Response<ArrayList<ModelReview>> response) {
                ArrayList<ModelReview> modelReviews = response.body();
                displayAllReview(modelReviews);
            }

            @Override
            public void onFailure(Call<ArrayList<ModelReview>> call, Throwable t) {

            }
        });
    }
    public void displayAllReview(ArrayList<ModelReview> modelReviews) {
        viewReviewAdapter = new RecyclerViewReviewAdapter(this, modelReviews);
        recyclerViewReview.setAdapter(viewReviewAdapter);
    }
    private void initUserDetail() {
        Call<ModelAccount> call = hairSalonAPI.getUserDetail(mUserName);
        call.enqueue(new Callback<ModelAccount>() {
            @Override
            public void onResponse(Call<ModelAccount> call, Response<ModelAccount> response) {
                ModelAccount currentAcc = response.body();
                fullName = currentAcc.getFullname();
                userId = currentAcc.getUserId();
                phone = currentAcc.getPhoneNumber();
            }

            @Override
            public void onFailure(Call<ModelAccount> call, Throwable t) {

            }
        });
    }

    private void setupSalonDetail(int salonId) {
        Call call = hairSalonAPI.getSalonServiceBySalonId(salonId);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                ArrayList<ModelSalonService> services = (ArrayList<ModelSalonService>) response.body();
                String imgUrl = services.get(0).getModelSalon().getUrl();
                String address = services.get(0).getModelSalon().getModelAddress().getStreetNumber() + ", "
                        + services.get(0).getModelSalon().getModelAddress().getStreet();
                String logUrl = services.get(0).getModelSalon().getLogoUrl();
                String des = services.get(0).getModelSalon().getDescription();

                txtDescription.setText(des);
                txtAddress.setText(address);
                Picasso.with(DetailSalonActivity.this).
                        load(logUrl)
                        .into(imgLogo);
                Picasso.with(DetailSalonActivity.this).
                        load(imgUrl)
                        .into(imgThumb);
                displayExtraService(services);
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    @Override
    public void onDateSelected(@NonNull final DateTime dateSelected) {
        String dayOfMonth = dateSelected.getDayOfMonth() + "";
        String monthOfYear = dateSelected.getMonthOfYear() + "";
        String year = dateSelected.getYear() + "";

        if (Integer.parseInt(dayOfMonth) < 10) dayOfMonth = "0" + dayOfMonth;
        if (Integer.parseInt(monthOfYear) < 10) monthOfYear = "0" + monthOfYear;


        bookedDate = dayOfMonth
                + "/" + monthOfYear
                + "/" + year;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
        Date today = Calendar.getInstance().getTime();

        if (bookedDate.equals(dateFormat.format(today))) {
            Toast.makeText(this, "Today", Toast.LENGTH_SHORT).show();
            linearTimePiker.removeAllViews();
            isChoose = false;
            generateSlotToday();


        } else {
            Toast.makeText(this, bookedDate, Toast.LENGTH_SHORT).show();
            linearTimePiker.removeAllViews();
            isChoose = false;

            generateSlot();
        }
        bookedDate = year + "-" + monthOfYear + "-" + dayOfMonth;

    }


    private void displayExtraService(ArrayList<ModelSalonService> modelSalonServices) {
        extraServiceAdapter = new RecyclerViewExtraServiceAdapter(DetailSalonActivity.this, modelSalonServices);
        recyclerView.setAdapter(extraServiceAdapter);
    }

    public String getSalePrice(String price, String discountValue) {

        String sSalePrice = price.substring(0, price.length() - 1);
        int nSalePrice = Integer.parseInt(sSalePrice);
        int nDiscountValue = Integer.parseInt(discountValue);
        nSalePrice = nSalePrice - (nSalePrice * nDiscountValue / 100);

        return "" + nSalePrice + "K";
    }

    public String uppercaseFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public void generateSlot() {

        String maxHour = salonEndTime;
        String[] splitMaxHour = maxHour.split(":");
        int maxH = Integer.parseInt(splitMaxHour[0]);
        String minHour = salonStartTime;
        String[] splitMinHour = minHour.split(":");
        int minH = Integer.parseInt(splitMinHour[0]);
        double step = salonSlotTime;

        double run = (maxH - minH) / (step / 60) + Integer.parseInt(splitMaxHour[1]) / step - Integer.parseInt(splitMinHour[1]) / step;

        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {

            Date start = format.parse(minHour);
            Date end = format.parse(maxHour);


            calendar.setTime(start);
            calendar2.setTime(end);
            String minute = "";


            for (int i = 0; i <= run; i++) {
                slotID++;
                final Button slot = new Button(this);
                slot.setId(slotID);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(10, 10, 10, 10);
                slot.setLayoutParams(params);
                slot.setBackgroundResource(button_time);
                slot.setTextColor(Color.parseColor("#DB1507"));
                if (calendar.getTime().getMinutes() < 10)

                    minute = "0" + calendar.getTime().getMinutes();
                else minute = calendar.getTime().getMinutes() + "";

                slot.setText(calendar.getTime().getHours() + ":" + minute);


                linearTimePiker.addView(slot);

                numberOfPeopleBooked = getNumberOfPeopleBooked(bookedDate, slot.getText().toString());

                if (numberOfPeopleBooked >= bookingPerSlot) {
                    slot.setEnabled(false);
                    slot.setBackgroundResource(button_full);
                    slot.setText("Hết chỗ");

                }


                calendar.add(Calendar.MINUTE, (int) step);

                // onclick select time
                slot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(DetailSalonActivity.this, slot.getText(), Toast.LENGTH_SHORT).show();
                        bookedTime = slot.getText().toString();
                        if (isChoose == false) {
                            slot.setBackgroundResource(R.drawable.button_time_choose);
                            slot.setTextColor(Color.WHITE);
                            isChoose = true;
                            slotIDisChoose = slot.getId();

                        } else {

                            Button b = findViewById(slotIDisChoose);
                            b.setBackgroundResource(button_time);
                            b.setTextColor(Color.parseColor("#DB1507"));
                            slot.setBackgroundResource(R.drawable.button_time_choose);
                            slot.setTextColor(Color.WHITE);
                            isChoose = true;
                            slotIDisChoose = slot.getId();
                        }
                    }
                });
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public void generateSlotToday() {

//        Date date = new Date();   // given date
//        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
//        calendar.setTime(date);   // assigns calendar to given date
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        step = salonSlotTime; //30 mins
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        String currentHour = dateFormat.format(date);
        String[] spliCurrentHour = currentHour.split(":");

        String maxSlot = salonEndTime;
        String minSlot = salonStartTime;
        String[] splitMaxSlot = maxSlot.split(":");
        String[] splitMinSlot = minSlot.split(":");

        int maxSlotHour = Integer.parseInt(splitMaxSlot[0]);
        int minSlotHour = Integer.parseInt(splitMinSlot[0]);


        if (minSlotHour <= Integer.parseInt(spliCurrentHour[0])) {
            if (Integer.parseInt(spliCurrentHour[1]) + step >= 60) {
                minSlot = (Integer.parseInt(spliCurrentHour[0]) + 1) + ":00";
                minSlotHour = Integer.parseInt(spliCurrentHour[0]);
                splitMinSlot = spliCurrentHour;

            } else {
                minSlot = (Integer.parseInt(spliCurrentHour[0]) + ":" + (int) step);
                minSlotHour = Integer.parseInt(spliCurrentHour[0]);
            }
        } else {

            if (Integer.parseInt(splitMinSlot[1]) + step >= 60) {
                minSlot = (minSlotHour + 1) + ":00";

            } else {
                minSlot = (minSlotHour + ":00");
            }
        }
        // number of button will generate
        double run = (maxSlotHour - minSlotHour) / (step / 60) + Integer.parseInt(splitMaxSlot[1]) / step - Integer.parseInt(splitMinSlot[1]) / step - 1;

        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {

            Date start = format.parse(minSlot);
            Date end = format.parse(maxSlot);

            calendar.setTime(start);
            calendar2.setTime(end);
            String minute = "";


            for (int i = 0; i <= run; i++) {
                slotID++;
                final Button slot = new Button(this);
                slot.setId(i);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(10, 10, 10, 10);
                slot.setLayoutParams(params);
                slot.setBackgroundResource(button_time);
                slot.setTextColor(Color.parseColor("#DB1507"));
                if (calendar.getTime().getMinutes() < 10)

                    minute = "0" + calendar.getTime().getMinutes();
                else minute = calendar.getTime().getMinutes() + "";
                slot.setText(calendar.getTime().getHours() + ":" + minute);

                linearTimePiker.addView(slot);


                numberOfPeopleBooked = getNumberOfPeopleBooked(bookedDate, slot.getText().toString());
                if (numberOfPeopleBooked >= bookingPerSlot) {
                    slot.setEnabled(false);
                    slot.setBackgroundResource(button_full);
                    slot.setText("Hết chỗ");
                }

                calendar.add(Calendar.MINUTE, (int) step);
                slot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(DetailSalonActivity.this, slot.getText(), Toast.LENGTH_SHORT).show();
                        bookedTime = slot.getText().toString();
                        if (isChoose == false) {
                            slot.setBackgroundResource(R.drawable.button_time_choose);
                            slot.setTextColor(Color.WHITE);
                            isChoose = true;
                            slotIDisChoose = slot.getId();

                        } else {

                            Button b = findViewById(slotIDisChoose);
                            b.setBackgroundResource(button_time);
                            b.setTextColor(Color.parseColor("#DB1507"));

                            slot.setBackgroundResource(R.drawable.button_time_choose);
                            slot.setTextColor(Color.WHITE);
                            isChoose = true;
                            slotIDisChoose = slot.getId();
                        }
                    }
                });

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getNumberOfPeopleBooked(String bookedDate, String bookedTime) {

        String[] abc = bookedDate.split("/");
        String bod = abc[2] + "-" + abc[1] + "-" + abc[0];

        return countNumberOfBooking(bod, bookedTime);
    }

    public int countNumberOfBooking(String bookedDate, String bookedTime) {
        int result = 0;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String url = "http://192.168.1.4:8080/api/countNumberOfBooking/" + bookedDate + "/" + bookedTime + "/" + salonId;
        String respone = "";
        try {
            URL urll = new URL(url);
            DetailSalonActivity.HttpGetRequest httpGetRequest = new DetailSalonActivity.HttpGetRequest();
            respone = httpGetRequest.execute(urll.openStream()).get();
            result = Integer.parseInt(respone);


        } catch (Exception e) {
            Toast.makeText(DetailSalonActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return result;
    }


    private class HttpGetRequest extends AsyncTask<InputStream, Void, String> {


        @Override
        protected String doInBackground(InputStream... inputStreams) {
            BufferedReader reader = null;
            InputStream in = inputStreams[0];
            StringBuffer response = new StringBuffer();
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return response.toString();
        }
    }
}
