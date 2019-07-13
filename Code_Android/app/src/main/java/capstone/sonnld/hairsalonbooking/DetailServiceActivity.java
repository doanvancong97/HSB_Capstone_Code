
package capstone.sonnld.hairsalonbooking;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.badoualy.datepicker.DatePickerTimeline;
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewExtraServiceAdapter;
import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.dto.BookingDetailsDTO;
import capstone.sonnld.hairsalonbooking.model.SalonService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static capstone.sonnld.hairsalonbooking.R.drawable.button_time;


public class DetailServiceActivity extends AppCompatActivity implements DatePickerListener {


    private Toolbar mToolbar;

    private TextView txtSalonName;
    private TextView txtSalonService;
    private TextView txtDescription;
    private TextView txtServicePrice;
    private TextView txtServiceSalePrice;
    private TextView txtAddress;
    private Button btnConfirm;
    private RecyclerView recyclerView;
    private ImageView imgThumb;
    private ImageView imgLogo;
    DatePickerTimeline timeline;
    LinearLayout linearTimePiker;

    //Button time
    private boolean isChoose = false;
    private int slotIDisChoose = 0;
    private int slotID = 0;
    private String bookedTime = "";


    private Spinner spAddress, spService;
    private static final String[] listAddress =
            {"1084 Quang Trung, F.12, Quận Gò Vấp, TP. HCM", "18 Nguyễn Ảnh Thủ, F. Trung Mỹ Tây, Quận 12, TP. HCM", "30 Nguyễn Trãi, F.10,  Quận 10"};

    private RecyclerView recyclerViewTimeSlot;

    private HairSalonAPI hairSalonAPI;
    private RecyclerViewExtraServiceAdapter extraServiceAdapter;
    private String bookedDate = "";
    private ArrayList<SalonService> chkService = new ArrayList<>();
    private ArrayList<BookingDetailsDTO> bookingDetailsDTOList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_service);

        // setup spinner address
//        ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, listAddress);
//
//        spAddress = findViewById(R.id.spAddress);
//
//
//        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spAddress.setAdapter(addressAdapter);


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


        HorizontalPicker picker = (HorizontalPicker) findViewById(R.id.datePicker);

        // initialize it and attach a listener

        // at init time
        picker
                .setListener(this)
                .setTodayDateTextColor(R.color.red2)
                .setDays(20)
                .setOffset(0)
                .showTodayButton(false)
                .init();

        picker.setDate(new DateTime());



        //Receive data from view adapter
        Intent intent = getIntent();
        int salonId = intent.getExtras().getInt("SalonID");
        String salonName = intent.getExtras().getString("SalonName");
        String salonServiceName = intent.getExtras().getString("SalonService");
        String description = intent.getExtras().getString("Description");
        String imgUrl = intent.getExtras().getString("Thumbnail");
        String salonServicePrice = intent.getExtras().getString("SalonServicePrice");
        String discountValue = intent.getExtras().getString("DiscountValue");
        String address = intent.getExtras().getString("Address");
        String logoUrl = intent.getExtras().getString("Logo");

        //set new value for view
        txtSalonName.setText(salonName);
        txtSalonService.setText(uppercaseFirstLetter(salonServiceName) + " (-" + discountValue + "%) ");
        txtServicePrice.setText(salonServicePrice);
        txtServicePrice.setPaintFlags(txtServicePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        txtServiceSalePrice.setText(getSalePrice(salonServicePrice, discountValue));
        txtDescription.setText(description);
        txtAddress.setText(address);
        Picasso.with(this).load(imgUrl).into(imgThumb);
        Picasso.with(this).load(logoUrl).into(imgLogo);


        recyclerViewTimeSlot = findViewById(R.id.recycler_view_time_slot);
        recyclerViewTimeSlot.setHasFixedSize(true);

        // intit retro
        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);

        // recycler for extra service
        recyclerView = findViewById(R.id.recycler_view_more_service);
        recyclerView.setLayoutManager(new GridLayoutManager(DetailServiceActivity.this, 1));
        getAllExtraService(salonId);


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setup data for booking

                chkService = extraServiceAdapter.getCheckedSalonServices();
                Intent sendDataToBooking =
                        new Intent(DetailServiceActivity.this, BookingDetailActivity.class);
                sendDataToBooking.putExtra("chkService", chkService);
                sendDataToBooking.putExtra("bookedDate", bookedDate);
                sendDataToBooking.putExtra("bookedTime", bookedTime);
                sendDataToBooking.putExtra("salonAddress",txtAddress.getText());
                startActivity(sendDataToBooking);
            }
        });

    }

    @Override
    public void onDateSelected(@NonNull final DateTime dateSelected) {
        // log it for demo

//        Toast.makeText(this, dateSelected.getDayOfMonth()
//                + "/" + dateSelected.getMonthOfYear()
//                + "/" + dateSelected.getYear(), Toast.LENGTH_SHORT).show();

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
            isChoose=false;
            generateSlotToday();


        } else {
            Toast.makeText(this, bookedDate, Toast.LENGTH_SHORT).show();
            linearTimePiker.removeAllViews();
            isChoose=false;

            generateSlot();
        }



    }



    private void getAllExtraService(int salonId) {
        hairSalonAPI.getSalonServiceBySalonId(salonId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<SalonService>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<SalonService> salonServices) {
                        displayExtraService(salonServices);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void displayExtraService(ArrayList<SalonService> salonServices) {
        extraServiceAdapter = new RecyclerViewExtraServiceAdapter(DetailServiceActivity.this, salonServices);
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

    public void generateSlot(){

//        Date date = new Date();   // given date
//        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
//        calendar.setTime(date);   // assigns calendar to given date
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String maxHour = "23:00";
        String[] splitMaxHour = maxHour.split(":");
        int maxH = Integer.parseInt(splitMaxHour[0]);
        String minHour = "08:00";
        String[] splitMinHour = minHour.split(":");
        int minH = Integer.parseInt(splitMinHour[0]);
        double step = 30;

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
                if(calendar.getTime().getMinutes()<10)

                    minute = "0"+calendar.getTime().getMinutes();
                else minute = calendar.getTime().getMinutes()+"";
                slot.setText(calendar.getTime().getHours() + ":" + minute);


                linearTimePiker.addView(slot);
                calendar.add(Calendar.MINUTE, (int) step);
                slot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(DetailServiceActivity.this, slot.getText(), Toast.LENGTH_SHORT).show();
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


    public void generateSlotToday(){

//        Date date = new Date();   // given date
//        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
//        calendar.setTime(date);   // assigns calendar to given date
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        double step = 30;
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        String currentHour = dateFormat.format(date);
        String[] currentHourArr = currentHour.split(":");



        String maxHour = "23:00";
        String minHour = "";
        String[] splitMaxHour = maxHour.split(":");
        int maxH = Integer.parseInt(splitMaxHour[0]);

        if(Integer.parseInt(currentHourArr[1])+step>=60){
             minHour = (Integer.parseInt(currentHourArr[0])+1)+":00";

        }
        else {
             minHour = (Integer.parseInt(currentHourArr[0])+":"+(int)step);

        }

        String[] splitMinHour = minHour.split(":");
        int minH = Integer.parseInt(splitMinHour[0]);


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
                if(calendar.getTime().getMinutes()<10)

                 minute = "0"+calendar.getTime().getMinutes();
                else minute = calendar.getTime().getMinutes()+"";
                slot.setText(calendar.getTime().getHours() + ":" + minute);


                linearTimePiker.addView(slot);
                calendar.add(Calendar.MINUTE, (int) step);
                slot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(DetailServiceActivity.this, slot.getText(), Toast.LENGTH_SHORT).show();
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



}
