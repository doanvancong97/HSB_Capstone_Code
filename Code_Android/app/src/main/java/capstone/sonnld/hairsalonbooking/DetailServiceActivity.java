
package capstone.sonnld.hairsalonbooking;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Locale;

import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewExtraServiceAdapter;
import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewReviewAdapter;
import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.dto.BookingDetailsDTO;
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


public class DetailServiceActivity extends AppCompatActivity implements DatePickerListener {


    //views
    private TextView txtSalonName;
    private TextView txtSalonService;
    private TextView txtDescription;
    private TextView txtServicePrice;
    private TextView txtServiceSalePrice;
    private TextView txtAddress;
    private TextView txtAvgRating;
    private Button btnConfirm;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewReview;
    private ImageView imgThumb;
    private ImageView imgLogo;

    private LinearLayout linearTimePiker;

    //Setup time
    private boolean isChoose = false;
    private int slotIDisChoose = 0;
    private int slotID = 0;
    private String bookedTime = "";

    private String salonStartTime;
    private String salonEndTime;
    private int salonSlotTime;
    private int salonBookingDay;


    private RecyclerView recyclerViewTimeSlot;

    private HairSalonAPI hairSalonAPI;
    private RecyclerViewExtraServiceAdapter extraServiceAdapter;
    private RecyclerViewReviewAdapter viewReviewAdapter;
    private String bookedDate = "";
    private ArrayList<ModelSalonService> chkService = new ArrayList<>();
    private ArrayList<BookingDetailsDTO> bookingDetailsDTOList = new ArrayList<>();

    // user detail
    private String mUserName;
    private SessionManager sessionManager;
    private String fullName;
    private int userId;
    private String phone;

    private int salonId;

    private int numberOfPeopleBooked = 5;
    private int bookingPerSlot = 0;

    private double step;

    private  String BASE_URL = "http://192.168.1.11:8080/api/";

    //countdown
    private CountDownTimer countDownTimer;
    private  TextView txtCountDown;
    private long timeLeft=259200000;
    Date now = new Date();
    LinearLayout lnCountDown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_service);
        // intit retro
        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);
        BASE_URL = RetrofitClient.getBaseUrl();

        // find view
        txtAddress = findViewById(R.id.txt_address);
        txtSalonService = findViewById(R.id.txt_salon_service_name);
        txtServicePrice = findViewById(R.id.txt_salon_service_price);
        txtServiceSalePrice = findViewById(R.id.txt_service_sale_price);
        txtSalonName = findViewById(R.id.txtSalonName);
        txtDescription = findViewById(R.id.txt_description);
        imgThumb = findViewById(R.id.img_thumbnail);
        btnConfirm = findViewById(R.id.btn_confirm);
        linearTimePiker = findViewById(R.id.linearTimePicker);
        imgLogo = findViewById(R.id.img_logo);
        txtAvgRating = findViewById(R.id.txt_avg_rating);


        //Receive data from RecyclerViewServiceByDiscountAdapter/ RecyclerViewFilterServiceAdapter
        Intent intent = getIntent();
        salonId = intent.getExtras().getInt("SalonID");
        bookingPerSlot = intent.getExtras().getInt("BookingPerSlot");
        String salonName = intent.getExtras().getString("SalonName");
        String salonServiceName = intent.getExtras().getString("ModelSalonService");
        String description = intent.getExtras().getString("Description");
        String imgUrl = intent.getExtras().getString("Thumbnail");
        String discountEndDate = intent.getExtras().getString("DiscountEndDate");
        String discountStartDate = intent.getExtras().getString("DiscountStartDate");
        String salonServicePrice = intent.getExtras().getInt("SalonServicePrice") + "";
        int discountValue = intent.getExtras().getInt("DiscountValue");
        String address = intent.getExtras().getString("ModelAddress");
        String logoUrl = intent.getExtras().getString("Logo");
        salonStartTime = intent.getExtras().getString("SalonStartTime");
        salonEndTime = intent.getExtras().getString("SalonCloseTime");
        salonSlotTime = intent.getExtras().getInt("SalonSlotTime");
        salonBookingDay = intent.getExtras().getInt("SalonBookingDay");
        float avgRating = intent.getExtras().getFloat("AvgRating");


        // countdown

        lnCountDown = findViewById(R.id.lnCountDown);
        txtCountDown=findViewById(R.id.txtCountDown);
        String startDate = discountStartDate + " 00:00:00";
        String endDate = discountEndDate + " 23:59:59";
       // Toast.makeText(this, startDate + "", Toast.LENGTH_LONG).show(); //2019-10-29
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date eDate = null;
        try {
            eDate = sdf.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = eDate.getTime();
        timeLeft=millis-now.getTime();

        if(timeLeft>0)
            startCountDown();
        else
            lnCountDown.setVisibility(View.GONE);

        Date sDate = null;
        try {
            sDate = sdf.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        timeLeft=sDate.getTime()-now.getTime();

        if(timeLeft<0)
            startCountDown();
        else
            lnCountDown.setVisibility(View.GONE);

        //set new value for view
        txtSalonName.setText(salonName);

        txtServicePrice.setText(salonServicePrice + "k");
        if(discountValue ==0){
            txtServiceSalePrice.setVisibility(View.GONE);
            txtServicePrice.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            txtServicePrice.setGravity(Gravity.CENTER);
            txtSalonService.setText(uppercaseFirstLetter(salonServiceName));
        }else{
            txtServicePrice.setPaintFlags(txtServicePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            txtServiceSalePrice.setText(getSalePrice(salonServicePrice, discountValue + ""));
            txtSalonService.setText(uppercaseFirstLetter(salonServiceName) + " (-" + discountValue + "%) ");
        }
        txtDescription.setText(description);
        txtAddress.setText(address);
        txtAvgRating.setText("Đánh giá trung bình: " + Math.floor(avgRating*10)/10);
        Picasso.with(this).load(imgUrl).into(imgThumb);
        Picasso.with(this).load(logoUrl).into(imgLogo);

        //  init time
        HorizontalPicker picker = (HorizontalPicker) findViewById(R.id.datePicker);
        picker.setListener(this)
                .setTodayDateTextColor(R.color.red2)
                .setDays(salonBookingDay)
                .setOffset(0)
                .showTodayButton(false)
                .init();
        picker.setDate(new DateTime());

        recyclerViewTimeSlot = findViewById(R.id.recycler_view_time_slot);
        recyclerViewTimeSlot.setHasFixedSize(true);


        // recycler for extra service
        recyclerView = findViewById(R.id.recycler_view_more_service);
        recyclerView.setLayoutManager(new GridLayoutManager(DetailServiceActivity.this, 1));
        getAllExtraService(salonId);

        // recycler for review
        recyclerViewReview = findViewById(R.id.recycler_view_review);
        recyclerViewReview.setLayoutManager(new GridLayoutManager(DetailServiceActivity.this, 1));
        getAllReview(salonId);
        // setup user
        sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isLogin()) {
            HashMap<String, String> user = sessionManager.getUserDetail();
            mUserName = user.get(sessionManager.getUSERNAME());
            initUserDetail();
        }


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setup data for booking

                chkService = extraServiceAdapter.getCheckedModelSalonServices();

                if (fullName == null) {
                    Toast.makeText(DetailServiceActivity.this,
                            "Hãy đăng nhập để tiếp tục đặt lịch!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(DetailServiceActivity.this, LoginActivity.class));
                } else if (chkService.size() == 0) {
                    Toast.makeText(DetailServiceActivity.this, "Bạn chưa chọn dịch vụ!", Toast.LENGTH_LONG).show();

                } else if (!isChoose) {
                    Toast.makeText(DetailServiceActivity.this,
                            "Bạn chưa chọn thời gian đến!", Toast.LENGTH_LONG).show();
                } else {
                    // send data to booking detail activity
                    Intent sendDataToBooking =
                            new Intent(DetailServiceActivity.this, BookingDetailActivity.class);
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

    private void startCountDown() {

        countDownTimer = new CountDownTimer(timeLeft,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft=millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void updateCountDownText() {

        int hour = (int) (timeLeft/1000/60/60);

        int day = hour/24;
        int h = hour-day*24;

        int minute = (int) ((timeLeft/1000-hour*60*60)/60);
        int second = (int) ((timeLeft/1000)%60);
        String timeLeftFormat = String.format(Locale.getDefault(),"%d:%02d:%02d",h,minute,second);
        txtCountDown.setText(day+" ngày "+timeLeftFormat);


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

            linearTimePiker.removeAllViews();
            isChoose = false;
            generateSlotToday();


        } else {

            linearTimePiker.removeAllViews();
            isChoose = false;

            generateSlot();
        }
        bookedDate = year + "-" + monthOfYear + "-" + dayOfMonth;

    }

    private void getAllExtraService(int salonId) {
        Call call = hairSalonAPI.getSalonServiceBySalonId(salonId);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                ArrayList<ModelSalonService> services = (ArrayList<ModelSalonService>) response.body();
                displayExtraService(services);
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private void displayExtraService(ArrayList<ModelSalonService> modelSalonServices) {
        extraServiceAdapter = new RecyclerViewExtraServiceAdapter(DetailServiceActivity.this, modelSalonServices);
        recyclerView.setAdapter(extraServiceAdapter);
    }

    public String getSalePrice(String price, String discountValue) {

        int nSalePrice = Integer.parseInt(price);
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

                }


                calendar.add(Calendar.MINUTE, (int) step);

                // onclick select time
                slot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       // Toast.makeText(DetailServiceActivity.this, slot.getText(), Toast.LENGTH_SHORT).show();
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
                }

                calendar.add(Calendar.MINUTE, (int) step);
                slot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                     //   Toast.makeText(DetailServiceActivity.this, slot.getText(), Toast.LENGTH_SHORT).show();
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
        String url = BASE_URL + "countNumberOfBooking/"+ bookedDate + "/" + bookedTime + "/" + salonId;
        String respone = "";
        try {
            URL urll = new URL(url);
            HttpGetRequest httpGetRequest = new HttpGetRequest();
            respone = httpGetRequest.execute(urll.openStream()).get();
            result = Integer.parseInt(respone);


        }catch (Exception e){
          //  Toast.makeText(DetailServiceActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    private static class HttpGetRequest extends AsyncTask<InputStream, Void, String>  {


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
