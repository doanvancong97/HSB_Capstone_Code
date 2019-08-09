package capstone.sonnld.hairsalonbooking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.model.ModelAccount;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText edtOldPass;
    private EditText edtNewPass;
    private EditText edtRetypeNewPass;
    private Button btnSave;
    private Button btnCancel;

    // user detail
    private ModelAccount modelAccount;

    // api
    private HairSalonAPI hairSalonAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //init api
        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);

        edtNewPass = findViewById(R.id.edt_new_pass);
        edtOldPass = findViewById(R.id.edt_old_pass);
        edtRetypeNewPass = findViewById(R.id.edt_retype_new_pass);
        btnCancel = findViewById(R.id.btn_cancel);
        btnSave = findViewById(R.id.btn_save);

        // get data from UserDetailActivity
        Intent intent = getIntent();
        modelAccount = (ModelAccount) intent.getSerializableExtra("UserDetail");





        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = edtNewPass.getText().toString();
                ModelAccount newModelAccount = new ModelAccount(password);
                updateUserDetail(newModelAccount);
            }
        });
    }

    private void updateUserDetail(ModelAccount newModelAccount) {
        int userId = modelAccount.getUserId();
        Call<Void> call = hairSalonAPI.updateUser(userId, newModelAccount);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.code() == 200) {
//                    Toast.makeText(UpdateProfileActivity.this,
//                            "Save user success", Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}
