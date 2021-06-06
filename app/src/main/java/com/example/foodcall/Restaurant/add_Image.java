package com.example.foodcall.Restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.foodcall.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class add_Image extends AppCompatActivity {

    public static final String TAG = "Inside add_Image";

    ImageView image;
    String image_name = "header";
    ProgressDialog load;
    Button load_image;
    Button upload_image;

    Uri imageUri = Uri.EMPTY;
    String img_64 = null;
    private static final int PICK_IMAGE = 100;

    Integer iWid;
    Integer iLen;

    private StorageReference sRef;
    private FirebaseAuth mAuth;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__image);

        image = findViewById(R.id.selected_image);
        load_image = findViewById(R.id.open_gallery);
        upload_image = findViewById(R.id.upload_img);

        load = new ProgressDialog(add_Image.this);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        sRef = FirebaseStorage.getInstance().getReference();

        load_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imageUri != Uri.EMPTY) {
                    load.setMessage("Uploading Image to Server.");
                    load.show();

                    final String uid = mAuth.getCurrentUser().getUid();

                    //Setting path for file to be uploaded
                    StorageReference ref = sRef.child("images/users/" + uid + "/" + image_name + ".jpg");
                    ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageUri = taskSnapshot.getUploadSessionUri();
                            //Toast.makeText(getApplicationContext(), imageUri.toString(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Image uploaded successfully!"
                                    , Toast.LENGTH_SHORT).show();
                            Log.d(TAG, " image URI returned : " + imageUri.toString());

                            //gettting uploaded url
                            FirebaseStorage store = FirebaseStorage.getInstance();
                            StorageReference image_ref = store.getReference()
                                    .child("images").child("users")
                                    .child(uid).child("header.jpg");

                            image_ref.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.d(TAG, "Image Read Successful for " + uid +
                                                    "with Uri: " + uri.toString());

                                            //Adding image link to user's realtime db
                                            myRef = mFirebaseDatabase.getReference().
                                                    child("users")
                                                    .child(uid);

                                            myRef.child("image_header").setValue(uri.toString())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d(TAG, "Image added to database successfully");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.d(TAG, " Image not added to database");
                                                        }
                                                    });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, " Getting image URL failed");
                                }
                            });
                            load.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Image upload failed!"
                                    , Toast.LENGTH_SHORT).show();
                            load.dismiss();
                        }
                    });
                }
                else
                    Toast.makeText(getApplicationContext(), "Please select an image first!"
                            , Toast.LENGTH_SHORT).show();
            }
        });

    }

    void openGallery() {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            Toast toast = Toast.makeText(getApplicationContext(), "Picture read from Gallery", Toast.LENGTH_SHORT);
            toast.show();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            File f = new File(imageStream.toString(), "");

            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            image.setImageBitmap(selectedImage);

            //img_64 = encodeImage(selectedImage);
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }
}
