
package capstone.sonnld.hairsalonbooking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.badoualy.datepicker.DatePickerTimeline;
import com.github.badoualy.datepicker.MonthView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import capstone.sonnld.hairsalonbooking.model.SalonService;

public class DetailSalonActivity extends AppCompatActivity {


    private Toolbar mToolbar;



    private TextView txtSalonName;
    private TextView txtSalonService;
    private TextView txtDescription;

    private ImageView imgThumb;
    private ImageView imgLogo;
    DatePickerTimeline timeline;

    private Spinner spAddress, spService;
    private static final String[] listAddress =
            {"1084 Quang Trung, F.12, Quận Gò Vấp, TP. HCM", "18 Nguyễn Ảnh Thủ, F. Trung Mỹ Tây, Quận 12, TP. HCM", "30 Nguyễn Trãi, F.10,  Quận 10"};

    private static final String[] listSlots = {"8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00"};

    private RecyclerView recyclerViewTimeSlot;
    private RecyclerView.Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_salon);

        // setup spinner address
        ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listAddress);

        spAddress = findViewById(R.id.spAddress);


        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAddress.setAdapter(addressAdapter);


        txtSalonService = findViewById(R.id.txt_salon_service_name);
        txtSalonName = findViewById(R.id.txtSalonName);
        txtDescription = findViewById(R.id.txt_description);
        imgThumb = findViewById(R.id.img_thumbnail);

        imgLogo = findViewById(R.id.img_logo);
        timeline = findViewById(R.id.timeline);

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int month = Calendar.getInstance().get(Calendar.MONTH);


        timeline.setFirstVisibleDate(year, month, day);
        timeline.setLastVisibleDate(year, month , day+10);



        timeline.setDateLabelAdapter(new MonthView.DateLabelAdapter() {
            @Override
            public CharSequence getLabel(Calendar calendar, int index) {
                return Integer.toString(calendar.get(Calendar.MONTH) + 1) + "/" + (calendar.get(Calendar.YEAR) % 2000);
            }
        });

        timeline.setOnDateSelectedListener(new DatePickerTimeline.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int index) {
                Toast.makeText(DetailSalonActivity.this, day + "/" + (month+1) + "/" + year, Toast.LENGTH_SHORT).show();

            }
        });


        //Receive data from view adapter
        Intent intent = getIntent();
        String salonName = intent.getExtras().getString("SalonName");
        String salonServiceName = intent.getExtras().getString("SalonService");
        String description = intent.getExtras().getString("Description");
        String imgUrl = intent.getExtras().getString("Thumbnail");

        //set new value for view
        txtSalonName.setText(salonName);
        txtSalonService.setText(uppercaseFirstLetter(salonServiceName));
        txtDescription.setText(description);
        Picasso.with(this).load(imgUrl).into(imgThumb);


        recyclerViewTimeSlot = findViewById(R.id.recycler_view_time_slot);
        recyclerViewTimeSlot.setHasFixedSize(true);


    }

    public String uppercaseFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public void clickToConfirm(View view) {
        Intent intent = new Intent(DetailSalonActivity.this, BookingDetailActivity.class);
        intent.putExtra("des", txtDescription.getText());
        this.startActivity(intent);
    }


}
