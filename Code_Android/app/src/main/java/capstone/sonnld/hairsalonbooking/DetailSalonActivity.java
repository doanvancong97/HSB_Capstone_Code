
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
    private TextView txtPromotionName;
    private TextView txtDescription;

    private ImageView imgThumb;
    private RecyclerView recyclerView;
    private ImageView imgLogo;

    private Spinner spAddress,spService;
    private static final String[] listAddress =
            {"1084 Quang Trung, F.12, Quận Gò Vấp, TP. HCM", "18 Nguyễn Ảnh Thủ, F. Trung Mỹ Tây, Quận 12, TP. HCM", "30 Nguyễn Trãi, F.10,  Quận 10"};

    List<SalonService> shop1ServiceList = new ArrayList<>();
    List<SalonService> shop2ServiceList = new ArrayList<>();
    List<SalonService> shop3ServiceList;
    List<SalonService> shop4ServiceList;

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



        //setup tool bar
//        mToolbar = (Toolbar) findViewById(R.id.nav_action_bar);
//        setSupportActionBar(mToolbar);


        txtPromotionName = findViewById(R.id.txt_promotion_name);
        txtSalonName = findViewById(R.id.txtSalonName);
        txtDescription = findViewById(R.id.txt_description);
        imgThumb = findViewById(R.id.img_thumbnail);
//        recyclerView = findViewById(R.id.detail_salon_service);
        imgLogo = findViewById(R.id.img_logo);



        shop1ServiceList.add(new SalonService("Gội Đầu", R.drawable.hair_wash, 87));
        shop1ServiceList.add(new SalonService("Đắp mặt nạ", R.drawable.beauty1, 87));
        shop1ServiceList.add(new SalonService("Tấy tóc", R.drawable.glue, 87));
        shop1ServiceList.add(new SalonService("Nhuộm tóc", R.drawable.dye, 200));



        shop2ServiceList.add(new SalonService("Gội Đầu", R.drawable.hair_wash, 100));
        shop2ServiceList.add(new SalonService("Đắp mặt nạ", R.drawable.beauty1, 287));
        shop2ServiceList.add(new SalonService("Tấy tóc", R.drawable.glue, 90));
        shop2ServiceList.add(new SalonService("Nhuộm tóc", R.drawable.dye, 200));



        //Receive data from view adapter
        Intent intent = getIntent();
        String salonName = intent.getExtras().getString("SalonName");
        String promotionName = intent.getExtras().getString("PromotionName");
        String description = intent.getExtras().getString("Description");
        String imgUrl = intent.getExtras().getString("Thumbnail");
//        String serviceListName = intent.getExtras().getString("ServiceListName");

//        RecyclerViewDetailServiceAdapter detailServiceAdapter = new RecyclerViewDetailServiceAdapter(this,shop1ServiceList);
//        if(serviceListName.equals("shop1Service")){
//            detailServiceAdapter = new RecyclerViewDetailServiceAdapter(this,shop1ServiceList);
//
//        }else if(serviceListName.equals("shop2Service")){
//            detailServiceAdapter = new RecyclerViewDetailServiceAdapter(this,shop2ServiceList);
//        }
//        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
//        recyclerView.setAdapter(detailServiceAdapter);
        //set new value for view
        txtSalonName.setText(salonName);
        txtPromotionName.setText(promotionName);
        txtDescription.setText(description);
        Picasso.with(this).load(imgUrl).into(imgThumb);


    }

    public void clickToConfirm(View view) {
        Intent intent = new Intent(this, BookingDetailActivity.class);
        intent.putExtra("des", txtDescription.getText());
        this.startActivity(intent);
    }
}
