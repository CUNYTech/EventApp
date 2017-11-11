package com.example.natia.flock1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

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

import static com.example.natia.flock1.R.id.genderSpinnerAct;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText email;
    private EditText firstName;
    private EditText lastName;
    private EditText password;
    private String gender;
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
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        spinner = (Spinner) findViewById(genderSpinnerAct);
        adapter = ArrayAdapter.createFromResource(this,R.array.gender_options,R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mDatabase = FirebaseDatabase.getInstance();

        //Creates a new database which will hold our users
        mDatabaseReference = mDatabase.getReference().child("MUsers");
        mAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance().getReference().child("MFlock_Profile_Pics");
        mProgressDialog = new ProgressDialog(this);

        email = (EditText) findViewById(R.id.emailAct);
        firstName = (EditText) findViewById(R.id.firstNameAct);
        lastName = (EditText) findViewById(R.id.lastNameAct);
        password = (EditText) findViewById(R.id.passwordAct);
        gender = spinner.getSelectedItem().toString();
        age = (EditText) findViewById(R.id.ageAct);
        createAccountBtn = (Button) findViewById(R.id.createAccountAct);
        profilePic = (ImageButton)  findViewById(R.id.profilePicAct);

        //button create account
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();             //call createAccount Method
            }
        });



        //when you click the Profile Pic button
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
        final String gen = gender.toString();
        final String ag = age.getText().toString().trim();
        //final String pic = resultUri.toString().trim();


        if (resultUri == null){
            //will use this if pic is not included
            if(!TextUtils.isEmpty(em) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(lname)
                    && !TextUtils.isEmpty(pwd)) {

                mProgressDialog.setMessage("Creating Account...");
                mProgressDialog.show();

                mAuth.createUserWithEmailAndPassword(em, pwd)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (authResult != null) {

                            //this creates the user and then adds properties to that user as its children
                            //will be shown in the database as a json object
                            String userid = mAuth.getCurrentUser().getUid();

                            DatabaseReference currentUserDb = mDatabaseReference.child(userid);
                            currentUserDb.child("firstName").setValue(name);
                            currentUserDb.child("lastName").setValue(lname);
                            currentUserDb.child("gender").setValue(gen);
                            currentUserDb.child("age").setValue(ag);
                            currentUserDb.child("image").setValue("none");


                            mProgressDialog.dismiss();


                            //Send users to Map
                            Intent intent = new Intent(CreateAccountActivity.this, ProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //brings this activity to the top

                            startActivity(intent);
                        }
                    }

                });

            }
        } else {
            //will use this if everything is filled out minus gender & age but including the pic
            if (!TextUtils.isEmpty(em) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(lname)
                    && !TextUtils.isEmpty(pwd)) {

                mProgressDialog.setMessage("Creating Account...");
                mProgressDialog.show();

                mAuth.createUserWithEmailAndPassword(em, pwd)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (authResult != null) {

                            StorageReference imagePath = mFirebaseStorage.child("MFlock_Profile_Pics")
                                    .child(resultUri.getLastPathSegment());

                            imagePath.putFile(resultUri).addOnSuccessListener
                                (new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    //this creates the user and then adds properties to that user as its children
                                    //will be shown in the database as a json object
                                    String userid = mAuth.getCurrentUser().getUid();

                                    DatabaseReference currentUserDb = mDatabaseReference.child(userid);
                                    currentUserDb.child("firstName").setValue(name);
                                    currentUserDb.child("lastName").setValue(lname);
                                    currentUserDb.child("gender").setValue(gen);
                                    currentUserDb.child("age").setValue(ag);
                                    currentUserDb.child("image").setValue(resultUri.toString());


                                    mProgressDialog.dismiss();


                                    //Send users to Map
                                    Intent intent = new Intent(CreateAccountActivity.this, MainHub.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //brings this activity to the top

                                    startActivity(intent);
                                }
                            });

                        }
                    }
                });
            } else {
                Toast.makeText(CreateAccountActivity.this, "Failed to Create Account",
                        Toast.LENGTH_LONG).show();
            }
        }



    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        //lets us crop the image selected by clicking the icon
        //got from https://github.com/ArthurHub/Android-Image-Cropper
        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK){
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
