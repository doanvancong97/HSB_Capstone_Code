package capstone.sonnld.hairsalonbooking;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import capstone.sonnld.hairsalonbooking.api.HairSalonAPI;
import capstone.sonnld.hairsalonbooking.api.RetrofitClient;
import capstone.sonnld.hairsalonbooking.model.ModelAccount;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpdateProfileActivity extends AppCompatActivity {

    private Button btnChooseFile;
    private Button btnSave;
    private Button btnCancel;
    private EditText edtUsername;
    private EditText edtEmail;
    private EditText edtPhoneNumber;
    private ImageView imgAvatar;
    private ProgressBar progressBar;

    // user detail
    private ModelAccount modelAccount;

    // api
    private HairSalonAPI hairSalonAPI;

    // fire base
    private static final int PICK_IMG_REQ = 1;
    private StorageReference storageReference;
    private StorageTask uploadTask = null;

    private Uri imgUri;
    private String imgUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        //init api
        Retrofit retrofit = RetrofitClient.getInstance();
        hairSalonAPI = retrofit.create(HairSalonAPI.class);

        btnCancel = findViewById(R.id.btn_cancel);
        btnChooseFile = findViewById(R.id.btn_choose_file);



        btnSave = findViewById(R.id.btn_save);

        edtEmail = findViewById(R.id.edt_email);
        edtPhoneNumber = findViewById(R.id.edt_phone);
        edtUsername = findViewById(R.id.edt_username);
        imgAvatar = findViewById(R.id.img_avatar);
        progressBar = findViewById(R.id.progress_bar);
        storageReference = FirebaseStorage.getInstance().getReference("AvatarImages");

        // get data from UserDetailActivity
        Intent intent = getIntent();
        modelAccount = (ModelAccount) intent.getSerializableExtra("UserDetail");

        // set data to fields
        String username = modelAccount.getFullname();
        String email = modelAccount.getEmail();
        String phone = modelAccount.getPhoneNumber();
        String avatarUrl = modelAccount.getAvatar();
        edtUsername.setText(username);
        edtEmail.setText(email);
        edtPhoneNumber.setText(phone);
        Picasso.with(this).load(avatarUrl).into(imgAvatar);

        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = edtUsername.getText().toString();
                String phone = edtPhoneNumber.getText().toString();
                String email = edtEmail.getText().toString();
                if(uploadTask != null){
                    if(uploadTask.isInProgress()){
                        Toast.makeText(UpdateProfileActivity.this,
                                "Đang tải hình ảnh, xin vui lòng chờ đợi", Toast.LENGTH_SHORT).show();
                    }else{

                        ModelAccount newModelAccount = new ModelAccount(fullName, email, phone, imgUrl);
                        updateUserDetail(newModelAccount);
                        Toast.makeText(UpdateProfileActivity.this,
                                "Cập nhật profile thành công", Toast.LENGTH_SHORT).show();
                        Intent saveIntent = new Intent(UpdateProfileActivity.this,UserDetailActivity.class);
                        saveIntent.putExtra("username", modelAccount.getUsername());
                        startActivity(saveIntent);
                        finish();
                    }
                }else {
                    ModelAccount newModelAccount = new ModelAccount(fullName, email, phone);
                    updateUserDetail(newModelAccount);
                    Toast.makeText(UpdateProfileActivity.this,
                            "Cập nhật profile thành công", Toast.LENGTH_SHORT).show();
                    Intent saveIntent = new Intent(UpdateProfileActivity.this,UserDetailActivity.class);
                    saveIntent.putExtra("username", modelAccount.getUsername());
                    startActivity(saveIntent);
                    finish();
                }


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent saveIntent = new Intent(UpdateProfileActivity.this,UserDetailActivity.class);
                saveIntent.putExtra("username", modelAccount.getUsername());
                startActivity(saveIntent);
                finish();
            }
        });


    }

    private void updateUserDetail(ModelAccount newModelAccount) {
        int userId = modelAccount.getUserId();
        Call<Void> call = hairSalonAPI.updateUser(userId, newModelAccount);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    //
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFile() {
        if (imgUri != null) {
            // complete upload path( ex: AvatarImages/12312312.jpg )
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imgUri));

            uploadTask = fileReference.putFile(imgUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 3000);
//                            Toast.makeText(UpdateProfileActivity.this,
//                                    "Tải ảnh thành công", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            // wait until url task is done
                            while (!urlTask.isSuccessful()) ;
                            Uri downloadUrl = urlTask.getResult();
                            imgUrl = downloadUrl.toString();

                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdateProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            // show progress on progress bar
                            progressBar.setVisibility(View.VISIBLE);
                            double progress = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) (progress));

                        }
                    });
        } else {
//            Toast.makeText(UpdateProfileActivity.this, "No file is selected", Toast.LENGTH_LONG).show();
//            imgUrl = modelAccount.getAvatar();
        }
    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMG_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMG_REQ && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imgUri = data.getData();
            Picasso.with(UpdateProfileActivity.this).load(imgUri).into(imgAvatar);
            uploadFile();
        }
    }
}
