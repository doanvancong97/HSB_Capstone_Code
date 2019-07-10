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

import capstone.sonnld.hairsalonbooking.model.SessionManager;

public class LoginActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    TextView txtResult;
    ProgressBar loading;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        txtResult=findViewById(R.id.txtResult);
        loading=findViewById(R.id.loading);

        sessionManager = new SessionManager(this);



    }

    public void clickToLogin(View view) {

        if(edtUsername.getText().toString().isEmpty()){
            edtUsername.setError("Nhập username");
        }
        if(edtPassword.getText().toString().isEmpty()){
            edtPassword.setError("Nhập password");
        }

        if(!edtPassword.getText().toString().isEmpty() && !edtPassword.getText().toString().isEmpty()){

            if (edtUsername.getText().toString().equals("congdv") && edtPassword.getText().toString().equals("123")) {


                loading.setVisibility(View.VISIBLE);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        loading.setVisibility(View.GONE);
                    }
                }, 3000);

                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                sessionManager.createSession(edtUsername.getText().toString().trim());



                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("username",edtUsername.getText().toString());
                finish();
                startActivity(intent);
            } else {
                txtResult.setText("Tên đăng nhập hoặc mật khẩu không đúng!");

            }

        }




    }

    public void clickToRedirectToRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
