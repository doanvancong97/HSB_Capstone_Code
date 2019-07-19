package capstone.sonnld.hairsalonbooking;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.model.Account;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {


    private EditText edtName;
    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtPhoneNumber;
    private EditText edtConfirmPassword;
    private EditText edtEmail;
    private ProgressBar loading;
    private TextView txtResult;

    // api
    private HairSalonAPI hairSalonAPI;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //init api
        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);

        edtName = findViewById(R.id.edtName);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        edtEmail = findViewById(R.id.edt_email);
        loading = findViewById(R.id.loading);
        txtResult = findViewById(R.id.txtResult);
    }


    private void registerUser(Account account){
        Call<Void> call = hairSalonAPI.registerUser(account);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    Toast.makeText(RegisterActivity.this,
                            "Register OK", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void clickToRegister(View view) {

        String fullname = edtName.getText().toString();
        String username = edtUsername.getText().toString();
        String phone = edtPhoneNumber.getText().toString();
        String password = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();
        String email = edtEmail.getText().toString();


        if (fullname.isEmpty()) {
            edtName.setError("Nhập Tên");
        }


        if (password.isEmpty()) {
            edtPassword.setError("Nhập mật khẩu");
        }

        if (confirmPassword.isEmpty()) {
            edtConfirmPassword.setError("Nhập mật khẩu xác nhận");
        }else if(!confirmPassword.equals(password)){
            edtConfirmPassword.setError("Mật khẩu xác nhận chưa đúng");
        }

        if(username.length()<6){
            edtUsername.setError("Tên đăng nhập có ít nhất 6 kí tự");

        }

        if(!isValidMail(email)){
            edtEmail.setError("Email không đúng định dạng!");

        }
        if (!isValidMobile(phone)) {
            edtPhoneNumber.setError("Số điện thoại không tồn tại");
        }






        if(edtName.getError() == null
                && edtUsername.getError() == null
                && edtPhoneNumber.getError() == null
                && edtPassword.getError() == null
                && edtConfirmPassword.getError() == null
                && edtEmail.getError() == null ){
            loading.setVisibility(View.VISIBLE);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    loading.setVisibility(View.GONE);
                }
            }, 3000);

            Account account = new Account(username,password,fullname,email,phone);
            registerUser(account);

            Toast.makeText(this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }else {

            txtResult.setText("Vui lòng nhập đầy đủ thông tin!");
        }

    }

    public void clickToRedirectToLogin(View view) {

        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

        startActivity(intent);
    }

    private boolean isValidMail(String email) {

        String EMAIL_STRING = "^\\w+[@]\\w+";

        return Pattern.compile(EMAIL_STRING).matcher(email).matches();

    }

    private boolean isValidMobile(String phone) {
        String PHONE_STRING = "^[0]\\d{9}";

        return Pattern.compile(PHONE_STRING).matcher(phone).matches();
    }




//    private boolean isValidMail(String email) {
//        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
//    }
}
