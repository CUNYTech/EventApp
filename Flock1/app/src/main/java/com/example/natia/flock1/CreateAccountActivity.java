package com.example.natia.flock1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText email;
    private EditText firstName;
    private EditText lastName;
    private EditText password;
    private EditText gender;
    private EditText age;
    private Button createAccountBtn;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private StorageReference mFirebaseStorage;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;
    private ImageButton profilePic;
    private final static int GALLERY_CODE = 1;
    private Uri resultUri = null;   //needed to copied out of code below so we could use it globally

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("MUsers");
        mAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance().getReference().child("MFlock_Profile_Pics");
        mProgressDialog = new ProgressDialog(this);

        email = (EditText) findViewById(R.id.emailAct);
        firstName = (EditText) findViewById(R.id.firstNameAct);
        lastName = (EditText) findViewById(R.id.lastNameAct);
        password = (EditText) findViewById(R.id.passwordAct);
        gender = (EditText) findViewById(R.id.genderAct);
        age = (EditText) findViewById(R.id.ageAct);
        createAccountBtn = (Button) findViewById(R.id.createAccountAct);
        profilePic = (ImageButton)  findViewById(R.id.profilePicAct);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
            }
        });
    }

    private void createNewAccount() {
        final String em = email.getText().toString().trim();
        final String name = firstName.getText().toString().trim();
        final String lname = lastName.getText().toString().trim();
        final String pwd = password.getText().toString().trim();
        final String gen = gender.getText().toString().trim();
        final String ag = age.getText().toString().trim();

        if(!TextUtils.isEmpty(em) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(lname)
                && !TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(gen) && !TextUtils.isEmpty(ag)) {

            mProgressDialog.setMessage("Creating Account...");
            mProgressDialog.show();

            mAuth.createUserWithEmailAndPassword(em, pwd)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                if(authResult != null) {

                    StorageReference imagePath = mFirebaseStorage.child("MFlock_Profile_Pics")
                            .child(resultUri.getLastPathSegment());

                    imagePath.putFile(resultUri).addOnSuccessListener
                            (new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //this creates the user and then adds properties to that user as its children
                            //will be shown in the database as a json object
                            String userid = mAuth.getCurrentUser().getUid();

                            DatabaseReference currenUserDb = mDatabaseReference.child(userid);
                            currenUserDb.child("Email").setValue(em);
                            currenUserDb.child("First Name").setValue(name);
                            currenUserDb.child("Last Name").setValue(lname);
                            currenUserDb.child("Password").setValue(pwd);
                            currenUserDb.child("Gender").setValue(gen);
                            currenUserDb.child("Age").setValue(ag);
                            currenUserDb.child("Image").setValue(resultUri.toString());

                            mProgressDialog.dismiss();

                            //Send users to MainHub
                            Intent intent = new Intent(CreateAccountActivity.this, MapsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //brings this activity to the top

                            startActivity(intent);
                        }
                    });


                }
                }
            });
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        //lets us crop the image selected by clicking the icon
        //got from https://github.com/ArthurHub/Android-Image-Cropper
        if(requestCode ==GALLERY_CODE && resultCode == RESULT_OK){
            Uri mImageUri = data.getData();

            CropImage.activity(mImageUri)
                    .setAspectRatio(1,1)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }

        //lets the cropped image picked go on the Create Account page
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();

                profilePic.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
