package capstone.sonnld.hairsalonbooking;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import capstone.sonnld.hairsalonbooking.model.SalonService;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static capstone.sonnld.hairsalonbooking.R.drawable.button_time;

public class DetailSalonActivity extends AppCompatActivity implements DatePickerListener {
    private TextView txtSalonName;
    private TextView txtAddress;
    private TextView txtDescription;
    private HairSalonAPI hairSalonAPI;
    private Button btnConfirm;
    private RecyclerView recyclerView;
    private ImageView imgThumb;
    private ImageView imgLogo;

    private LinearLayout linearTimePiker;

    //Button time
    private boolean isChoose = false;
    private int slotIDisChoose = 0;
    private int slotID = 0;
    private String bookedTime="";





    private RecyclerViewExtraServiceAdapter extraServiceAdapter;
    private String bookedDate = "";
    private ArrayList<SalonService> chkService = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_salon);

//        // setup spinner address
//        ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, listAddress);
//
//        spAddress = findViewById(R.id.spAddress);
//        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spAddress.setAdapter(addressAdapter);

        txtAddress = findViewById(R.id.txt_address);
        txtSalonName = findViewById(R.id.txtSalonName);
        txtDescription = findViewById(R.id.txt_description);
        imgThumb = findViewById(R.id.img_thumbnail);
        btnConfirm = findViewById(R.id.btn_confirm);
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

        linearTimePiker = findViewById(R.id.linearTimePicker);

        String maxHour = "23:15";
        String[] splitMaxHour = maxHour.split(":");
        int maxH = Integer.parseInt(splitMaxHour[0]);
        String minHour = "8:15";
        String[] splitMinHour = minHour.split(":");
        int minH = Integer.parseInt(splitMinHour[0]);
        double step=15;

        double run = (maxH-minH)/(step/60) + Integer.parseInt(splitMaxHour[1])/step- Integer.parseInt(splitMinHour[1])/step;

        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {

            Date start = format.parse(minHour);
            Date end = format.parse(maxHour);
            calendar.setTime(start);
            calendar2.setTime(end);
            for (int i = 0;i<=run; i++) {
                slotID++;
                final Button slot = new Button(this);
                slot.setId(slotID);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(10, 10, 10, 10);
                slot.setLayoutParams(params);
                slot.setBackgroundResource(button_time);
                slot.setTextColor(Color.parseColor("#DB1507"));
                slot.setText(calendar.getTime().getHours() + ":"+calendar.getTime().getMinutes());


                linearTimePiker.addView(slot);
                calendar.add(Calendar.MINUTE,(int)step);
                slot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(DetailSalonActivity.this, slot.getText(), Toast.LENGTH_SHORT).show();
                        bookedTime=slot.getText().toString();
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


        //init retro
        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);

        // get data from map activity
        Intent intent = getIntent();
        int salonId = intent.getExtras().getInt("SalonId");
        String salonName = intent.getExtras().getString("SalonName");
        txtSalonName.setText(salonName);


        // recycler for extra service
        recyclerView = findViewById(R.id.recycler_view_salon_service);
        recyclerView.setLayoutManager(new GridLayoutManager(DetailSalonActivity.this, 1));
        setupSalonDetail(salonId);
        String des = "ÁP DỤNG KHI DÙNG DỊCH VỤ TẠI CỬA HÀNG* \n" +
                "\n" +
                "- Giảm 20% tổng hóa đơn áp dụng cho tất cả các dịch vụ \n" +
                "- Áp dụng cho khách hàng nữ \n" +
                "- Mỗi mã ưu đãi đổi được nhiều suất trong suốt chương trình \n" +
                "- Khách hàng có thể lấy nhiều mã trong suốt chương trình \n" +
                "\n" +
                "THỜI GIAN ÁP DỤNG \n" +
                "- Khung giờ: 9h30 - 19h00\t\n" +
                "- Áp dụng tất cả các ngày trong tuần \n" +
                "- Không áp dụng các ngày lễ, Tết: 30/4, 1/5 \n" +
                "\n" +
                "Chi tiết địa điểm xem tại \"Điểm áp dụng\" \n" +
                "\n" +
                "Vui lòng bấm XÁC NHẬN ĐẶT CHỖ để nhận mã giảm giá \n" +
                "\n" +
                "LƯU Ý \n" +
                "- Chương trình chỉ áp dụng với khách dùng dịch vụ tại cửa hàng \n" +
                "- Không áp dụng đồng thời với các chương trình khác của MIA.Nails & Cafe \n" +
                "- Không áp dụng phụ thu \n" +
                "- Ưu đãi chưa bao gồm VAT \n" +
                "- Khách hàng được phép đến sớm hoặc muộn hơn 15 phút so với giờ hẹn đến \n" +
                "- Mã giảm giá không có giá trị quy đổi thành tiền mặt ";
        txtDescription.setText(des);



        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setup data for booking

                chkService = extraServiceAdapter.getCheckedSalonServices();
                if(chkService.size() == 0){
                    Toast.makeText(DetailSalonActivity.this,"Bạn chưa chọn dịch vụ",Toast.LENGTH_LONG).show();
                }else{

                    // send data to booking detail activity
                    Intent sendDataToBooking =
                            new Intent(DetailSalonActivity.this, BookingDetailActivity.class);
                    sendDataToBooking.putExtra("chkService", chkService);
                    sendDataToBooking.putExtra("bookedDate", bookedDate);
                    sendDataToBooking.putExtra("bookedTime",bookedTime);
                    sendDataToBooking.putExtra("salonAddress",txtAddress.getText());
                    startActivity(sendDataToBooking);
                }

            }
        });
    }

    private void setupSalonDetail(int salonId) {
        hairSalonAPI.getSalonServiceBySalonId(salonId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<SalonService>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<SalonService> services) {
                        String imgUrl = services.get(0).getSalon().getUrl();
                        String address = services.get(0).getSalon().getAddress().getStreetNumber() + ", "
                                + services.get(0).getSalon().getAddress().getStreet();
                        String logUrl = services.get(0).getSalon().getLogoUrl();
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
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

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


    private void displayExtraService(ArrayList<SalonService> salonServices) {
        extraServiceAdapter = new RecyclerViewExtraServiceAdapter(DetailSalonActivity.this, salonServices);
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
}
