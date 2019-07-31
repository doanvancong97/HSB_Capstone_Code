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

    private String check = "wait";

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

        Call<String> call = hairSalonAPI.registerUser(account);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.code() == 200){
                    Toast.makeText(RegisterActivity.this,
                            response.body()+ "", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void checkDuplicateUsername(String username) {
        Call<Account> call = hairSalonAPI.getUserDetail(username);

        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if(response.code() == 200){
                    edtUsername.setError("Tên đăng nhập đã tồn tại");

                }

            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {

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
        checkDuplicateUsername(username);


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
            edtPhoneNumber.setError("Số điện thoại không đúng");
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

        String EMAIL_STRING = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        return Pattern.compile(EMAIL_STRING).matcher(email).matches();


    }

    private boolean isValidMobile(String phone) {
        String PHONE_STRING = "^[0-9]{10}";

        return Pattern.compile(PHONE_STRING).matcher(phone).matches();

    }

}
