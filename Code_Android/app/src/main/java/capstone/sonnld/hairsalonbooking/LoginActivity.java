package capstone.sonnld.hairsalonbooking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    TextView txtResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        txtResult=findViewById(R.id.txtResult);


    }

    public void clickToLogin(View view) {

        if(edtUsername.getText().toString().isEmpty()){
            edtUsername.setError("Nhap username");
        }
        if(edtPassword.getText().toString().isEmpty()){
            edtPassword.setError("Nhap pass");
        }

        if(!edtPassword.getText().toString().isEmpty() && !edtPassword.getText().toString().isEmpty()){

            if (edtUsername.getText().toString().equals("user") && edtPassword.getText().toString().equals("123")) {

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("phonenumber",edtUsername.getText().toString());
                startActivity(intent);
            } else {
                txtResult.setText("Số điện thoại hoặc mật khẩu không đúng!");

            }

        }




    }

    public void clickToRedirectToRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
