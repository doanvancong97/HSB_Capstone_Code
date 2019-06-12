
package capstone.sonnld.hairsalonbooking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import capstone.sonnld.hairsalonbooking.model.SalonService;

public class DetailSalonActivity extends AppCompatActivity {


    private Toolbar mToolbar;


    private TextView txtSalonName;
    private TextView txtSalonService;
    private TextView txtDescription;

    private ImageView imgThumb;
    private ImageView imgLogo;

    private Spinner spAddress,spService;
    private static final String[] listAddress =
            {"1084 Quang Trung, F.12, Quận Gò Vấp, TP. HCM", "18 Nguyễn Ảnh Thủ, F. Trung Mỹ Tây, Quận 12, TP. HCM", "30 Nguyễn Trãi, F.10,  Quận 10"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_salon);

        // setup spinner address
        ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,listAddress);

        spAddress = findViewById(R.id.spAddress);


        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAddress.setAdapter(addressAdapter);


        txtSalonService = findViewById(R.id.txt_salon_service_name);
        txtSalonName = findViewById(R.id.txtSalonName);
        txtDescription = findViewById(R.id.txt_description);
        imgThumb = findViewById(R.id.img_thumbnail);

        imgLogo = findViewById(R.id.img_logo);


        //Receive data from view adapter
        Intent intent = getIntent();
        String salonName = intent.getExtras().getString("SalonName");
        String salonServiceName = intent.getExtras().getString("SalonService");
        String description = intent.getExtras().getString("Description");
        String imgUrl = intent.getExtras().getString("Thumbnail");

        //set new value for view
        txtSalonName.setText(salonName);
        txtSalonService.setText(capFirstLetter(salonServiceName));
        txtDescription.setText(description);
        Picasso.with(this).load(imgUrl).into(imgThumb);


    }

    public String capFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);

    }
    public void clickToConfirm(View view) {
        Intent intent = new Intent(this, BookingDetailActivity.class);
        intent.putExtra("des", txtDescription.getText());
        this.startActivity(intent);
    }
}
