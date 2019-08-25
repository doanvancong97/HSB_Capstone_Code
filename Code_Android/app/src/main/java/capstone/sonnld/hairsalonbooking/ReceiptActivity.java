package capstone.sonnld.hairsalonbooking;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import capstone.sonnld.hairsalonbooking.adapter.RecyclerViewSelectedServiceAdapter;
import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.dto.BookingDetailsDTO;
import capstone.sonnld.hairsalonbooking.model.ModelSalonService;
import retrofit2.Retrofit;

public class ReceiptActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private Button btnAccept;

    private TextView txtBookedDate;
    private TextView txtBookedTime;
    private TextView txtTotalPrice;
    private TextView txtAddress;

    private ArrayList<BookingDetailsDTO> bookingDetailsDTOList = new ArrayList<>();

    // api
    private HairSalonAPI hairSalonAPI;
    TextView txtDirection;

    // user detail
    private TextView txtUsername;

    private ArrayList<ModelSalonService> modelSalonServices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        //init api
        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);

        txtDirection = findViewById(R.id.txtDirection);
        txtTotalPrice = findViewById(R.id.txt_total);
        btnAccept = findViewById(R.id.btn_accept);
        txtBookedDate = findViewById(R.id.txt_booked_date);
        txtBookedTime = findViewById(R.id.txt_booked_time);
        txtAddress = findViewById(R.id.txt_address);
        txtUsername = findViewById(R.id.txt_username);

        Intent intent = getIntent();
        modelSalonServices = (ArrayList<ModelSalonService>) intent.getSerializableExtra("modelSalonServices");
        String fullName = intent.getExtras().getString("fullname");
        String bookedTime = intent.getExtras().getString("bookedTime");
        String bookedDate = intent.getExtras().getString("bookedDate");
        String address = intent.getExtras().getString("address");
        String totalPrice = intent.getExtras().getString("totalPrice");

        txtBookedDate.setText(bookedDate);
        txtBookedTime.setText(bookedTime);
        txtAddress.setText(address);
        txtUsername.setText(fullName);
        txtTotalPrice.setText(totalPrice);

        recyclerView = findViewById(R.id.recycler_selected_service);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        RecyclerViewSelectedServiceAdapter serviceAdapter =
                new RecyclerViewSelectedServiceAdapter(this, modelSalonServices);
        recyclerView.setAdapter(serviceAdapter);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goMainActivity = new Intent(ReceiptActivity.this, MainActivity.class);
                startActivity(goMainActivity);
                finish();
            }
        });

        txtDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri gmmIntentUri = Uri.parse("google.navigation:q="+ txtAddress.getText().toString());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }
}
