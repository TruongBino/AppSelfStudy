package com.example.appselfstudy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appselfstudy.models.videoUploadDetails;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.apache.commons.io.FilenameUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Uri videoUri;
    TextView text_video_selected;
    String videoCategory;
    String videoTitle;
    String currenuid;
    StorageReference mstorageRef;
    StorageTask mUploadsTask;
    DatabaseReference referenceVideos;
    EditText video_description;
    private ImageButton btn_SignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_video_selected = findViewById(R.id.textVideoSelected);
        video_description = findViewById(R.id.video_Description);
        referenceVideos = FirebaseDatabase.getInstance().getReference().child("videos");
        mstorageRef = FirebaseStorage.getInstance().getReference().child("video");

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<>();
        categories.add("Hoc hanh 1");
        categories.add("Hoc hanh 2");
        categories.add("Hoc hanh 3");
        categories.add("Hoc hanh 4");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);


        // Sự kiện click ImageButton SignOut
        btn_SignOut = findViewById(R.id.btnSignOut);
        btn_SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đăng xuất người dùng
                FirebaseAuth.getInstance().signOut();
                // Sau khi đăng xuất, chuyển người dùng đến màn hình đăng nhập hoặc màn hình chính khác
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
                finish(); // Đóng Activity hiện tại
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        videoCategory = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(this, "slected: " + videoCategory, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void openVideoFiles(View view) {
        Intent in = new Intent(Intent.ACTION_GET_CONTENT);
        in.setType("video/*");
        startActivityForResult(in, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && requestCode == RESULT_OK && data.getData() != null) {
            videoUri = data.getData();

            String path = null;
            Cursor cursor;
            int coloum_index_data;
            String[] projection = {MediaStore.MediaColumns.DATA,MediaStore.Video.Media.BUCKET_DISPLAY_NAME
            ,MediaStore.Video.Media._ID,MediaStore.Video.Thumbnails.DATA};
            final String oderby = MediaStore.Video.Media.DEFAULT_SORT_ORDER;
            cursor = MainActivity.this.getContentResolver().query(videoUri,projection,null,
                    null,oderby);
            coloum_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            while(cursor.moveToNext()){
                path = cursor.getString(coloum_index_data);
                videoTitle = FilenameUtils.getBaseName(path);
            }
            text_video_selected.setText(videoTitle);
        }
    }
    public void uploadFileToFireBase(View v){
        if(text_video_selected.equals("no video selected")){
            Toast.makeText(this,"Please selected an video!",Toast.LENGTH_SHORT).show();
        }else{
            if(mUploadsTask != null && mUploadsTask.isInProgress()){
                Toast.makeText(this,"video uploads is all ready in progress...",Toast.LENGTH_SHORT).show();
            }else {
                uploadFiles();
            }
        }
    }

    private void uploadFiles() {
        if(videoUri != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("video uploading...");
            progressDialog.show();
            final StorageReference storageReference = mstorageRef.child(videoTitle);
            mUploadsTask = storageReference.putFile(videoUri).addOnSuccessListener
                    (new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String video_url = uri.toString();
                                    videoUploadDetails videoUploadDetails = new videoUploadDetails("","",
                                            "",
                                    video_url,videoTitle,video_description.getText().toString(),videoCategory);
                                    String uploadsid = referenceVideos.push().getKey();
                                    referenceVideos.child(uploadsid).setValue(videoUploadDetails);
                                    currenuid = uploadsid;
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress =(100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("uploaded"+((int)progress)+"%...");
                }
            });
        }else {
            Toast.makeText(this,"no video selected to upload",Toast.LENGTH_SHORT).show();
        }
    }
}