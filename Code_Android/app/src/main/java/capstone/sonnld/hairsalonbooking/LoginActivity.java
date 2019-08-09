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

import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.model.ModelAccount;
import capstone.sonnld.hairsalonbooking.support.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUsername, edtPassword;
    private TextView txtResult;
    private ProgressBar loading;
    private SessionManager sessionManager;

    // api
    private HairSalonAPI hairSalonAPI;

    //login

    private ModelAccount modelAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //init api
        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        txtResult=findViewById(R.id.txtResult);
        loading=findViewById(R.id.loading);

        sessionManager = new SessionManager(this);



    }

    public void checkLogin(){

        Call<Void> call = hairSalonAPI.checkLogin(modelAccount);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(LoginActivity.this, response.code() + "", Toast.LENGTH_SHORT).show();
                if(response.code() == 200){
                    loading.setVisibility(View.VISIBLE);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            loading.setVisibility(View.GONE);
                        }
                    }, 3000);

                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    sessionManager.createSession(edtUsername.getText().toString().trim());

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("username",edtUsername.getText().toString());
                    finish();
                    startActivity(intent);
                } else {
                    txtResult.setText("Tên đăng nhập hoặc mật khẩu không đúng!");

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });


    }


    public void clickToLogin(View view) {


        if(edtUsername.getText().toString().isEmpty()){
            edtUsername.setError("Nhập username");
        }
        if(edtPassword.getText().toString().isEmpty()){
            edtPassword.setError("Nhập password");
        }

        if(!edtPassword.getText().toString().isEmpty() && !edtPassword.getText().toString().isEmpty()){

            String username = edtUsername.getText().toString();
            String password = edtPassword.getText().toString();
            modelAccount = new ModelAccount(username,password);
            checkLogin();

//            if (check == true) {
//
//                loading.setVisibility(View.VISIBLE);
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        loading.setVisibility(View.GONE);
//                    }
//                }, 3000);
//
//                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
//                sessionManager.createSession(edtUsername.getText().toString().trim());
//
//
//                Intent intent = new Intent(this, MainActivity.class);
//                intent.putExtra("username",edtUsername.getText().toString());
//                finish();
//                startActivity(intent);
//            } else {
//                txtResult.setText("Tên đăng nhập hoặc mật khẩu không đúng!");
//
//            }

        }
    }

    public void clickToRedirectToRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
