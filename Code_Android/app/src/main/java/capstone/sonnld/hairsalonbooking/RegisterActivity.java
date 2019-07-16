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

public class RegisterActivity extends AppCompatActivity {


    private EditText edtName, edtUsername, edtPassword, edtPhoneNumber, edtConfirmPassword;
    private ProgressBar loading;
    private TextView txtResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtName = findViewById(R.id.edtName);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        loading = findViewById(R.id.loading);
        txtResult = findViewById(R.id.txtResult);


    }


    public void clickToRegister(View view) {

        String name = edtName.getText().toString();
        String username = edtUsername.getText().toString();
        String phone = edtPhoneNumber.getText().toString();
        String password = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();


        if (edtName.getText().toString().isEmpty()) {
            edtName.setError("Nhập Tên");
        }
        if (edtUsername.getText().toString().isEmpty()) {
            edtUsername.setError("Nhập username");
        }
        if (edtPhoneNumber.getText().toString().isEmpty()) {
            edtPhoneNumber.setError("Nhập số điện thoại");
        }
        if (edtPassword.getText().toString().isEmpty()) {
            edtPassword.setError("Nhập mật khẩu");
        }
        if (edtConfirmPassword.getText().toString().isEmpty()) {
            edtConfirmPassword.setError("Nhập mật khẩu xác nhận");
        }

        if(!edtName.getText().toString().isEmpty() && !edtUsername.getText().toString().isEmpty() && !edtPhoneNumber.getText().toString().isEmpty() &&!edtPassword.getText().toString().isEmpty() && !edtConfirmPassword.getText().toString().isEmpty() ){
            loading.setVisibility(View.VISIBLE);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    loading.setVisibility(View.GONE);
                }
            }, 3000);


            Toast.makeText(this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();


            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);

        }else {

            txtResult.setText("Vui lòng nhập đầy đủ thông tin!");

        }





    }

    public void clickToRedirectToLogin(View view) {

        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

        startActivity(intent);
    }
}
