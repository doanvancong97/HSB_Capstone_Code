package capstone.sonnld.hairsalonbooking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.model.Account;
import capstone.sonnld.hairsalonbooking.model.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserDetailActivity extends AppCompatActivity {

    // api
    private HairSalonAPI hairSalonAPI;

    // user detail
//    private String username;
    private String fullname;
    private ImageView imgAvatar;
    private TextView txtUsername;
    private TextView txtEmail;
    private TextView txtPhone;
    private Account currentAcc;

    // session
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        sessionManager = new SessionManager(getApplicationContext());


        //init api
        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);

        //
        imgAvatar = findViewById(R.id.img_avatar);
        txtUsername = findViewById(R.id.txt_username);
        txtEmail = findViewById(R.id.txt_email);
        txtPhone = findViewById(R.id.txt_phone);

        // get data from main activity, UpdateProfileActivity
        Intent intent = getIntent();
        String username = intent.getExtras().getString("username");
        initUserDetail(username);


    }

    private void initUserDetail(String username){
        Call<Account> accountCall = hairSalonAPI.getUserDetail(username);
        accountCall.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                currentAcc = response.body();
                String avatarUrl = currentAcc.getAvatar();
                String email = currentAcc.getEmail();
                String phoneNumer = currentAcc.getPhoneNumber();
                fullname = currentAcc.getFullname();
                Picasso.with(UserDetailActivity.this).load(avatarUrl).into(imgAvatar);
                txtUsername.setText(fullname);
                txtEmail.setText(email);
                txtPhone.setText(phoneNumer);
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {

            }
        });
    }


    @Override
    protected void onRestart() {
        super.onRestart();

    }

    public void clicktoLogout(View view) {

        sessionManager.logout();
        startActivity(new Intent(this,MainActivity.class));
        finish();

    }

    public void clickToUpdateProfile(View view) {

        //send data to UpdateProfileActivity
        Intent intent = new Intent(this,UpdateProfileActivity.class);
        intent.putExtra("UserDetail",currentAcc);
        startActivity(intent);
        finish();
    }

    public void clickToChangePassword(View view) {
        Intent intent = new Intent(this,ChangePasswordActivity.class);
        intent.putExtra("UserDetail",currentAcc);
        startActivity(intent);
        finish();
    }

}
