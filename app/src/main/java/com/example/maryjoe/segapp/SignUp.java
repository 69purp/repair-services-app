package com.example.maryjoe.segapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;


import android.util.Patterns;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.maryjoe.segapp.SignIn.EXTRA_MESSAGE;

public class SignUp extends AppCompatActivity {

    public static DatabaseReference database;

    public static String accountType, nameOfUser, emailOfUser, usernameOfUser, passwordOfUser, serviceType, priceOfService;

    public static FirebaseAuth mAuth;

    public static ProgressBar progBar;

    public static EditText editTextusername,editTextname,editTextemail,editTextpassword,editTextconfpass;

    public static boolean adminFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        database = FirebaseDatabase.getInstance().getReference();

        if (adminFlag) {
            Button adminButton = (Button) findViewById(R.id.admin);
            adminButton.setVisibility(View.INVISIBLE);
        }

        editTextname = (EditText) findViewById(R.id.nameTextField);
        editTextusername = (EditText) findViewById(R.id.usernameTextField);
        editTextemail = (EditText) findViewById(R.id.emailTextField);
        editTextpassword = (EditText) findViewById(R.id.passwordTextField);
        editTextconfpass = (EditText) findViewById(R.id.confirmPassTextField);

        mAuth= FirebaseAuth.getInstance();

        progBar = (ProgressBar) findViewById(R.id.progressBar);

        // findViewById(R.id.button3).setOnClickListener(this);

    }

    public void registerUser(){
        String name = editTextname.getText().toString().trim();
        String email = editTextemail.getText().toString().trim();
        String username = editTextusername.getText().toString().trim();
        String password = editTextpassword.getText().toString().trim();
        String confirmpass = editTextconfpass.getText().toString().trim();

        if(name.isEmpty()){
            editTextname.setError("Name is required ");
            editTextname.requestFocus();
            return;
        }
        if(username.isEmpty()){
            editTextusername.setError("Username is required");
            editTextusername.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editTextemail.setError("Email is required");
            editTextemail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextemail.setError("Enter a valid email");
            editTextemail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextpassword.setError("Password is required");
            editTextpassword.requestFocus();
            return;
        }
        if(password.length()<6) {
            editTextpassword.setError("Minimum length of password is six");
            editTextpassword.requestFocus();
            return;
        }

        if(confirmpass.isEmpty()){
            editTextconfpass.setError("Password is required");
            editTextconfpass.requestFocus();
            return;
        }
        if(!confirmpass.equals(password)){
            editTextconfpass.setError("Password does not match. Renter password");
            editTextconfpass.requestFocus();
            return;
        }
        progBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"User Registration Successful",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Error occurred",Toast.LENGTH_SHORT).show();

                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"You are already registered in the system",Toast.LENGTH_SHORT).show();
                    }

                    else{
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public void goToWelcome(View view) {
        if (adminFlag) {
            // opens a new activity when you sign up
            Intent intent = new Intent(this, AdminHome.class);
            startActivity(intent);
        } else {
            // opens a new activity when you sign up
            Intent intent = new Intent(this, WelcomePage.class);
            EditText editText = (EditText) findViewById(R.id.nameTextField);
            String message = editText.getText().toString();
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent);
        }
    }

    public void adminClick(View view) {
        registerUser();
        adminFlag = true;
        accountType = "Admin";

        EditText editTextName = (EditText) findViewById(R.id.nameTextField);
        nameOfUser = editTextName.getText().toString();

        database.child("Admin").child(nameOfUser);

        EditText editTextEmail = (EditText) findViewById(R.id.emailTextField);
        emailOfUser = editTextEmail.getText().toString();

        database.child("Admin").child(nameOfUser).child("Email").setValue(emailOfUser);

        EditText editTextUserName = (EditText) findViewById(R.id.usernameTextField);
        usernameOfUser = editTextUserName.getText().toString();

        database.child("Admin").child(nameOfUser).child("Username").setValue(usernameOfUser);

        EditText editTextPassword = (EditText) findViewById(R.id.passwordTextField);
        passwordOfUser = editTextPassword.getText().toString();

        database.child("Admin").child(nameOfUser).child("Password").setValue(passwordOfUser);
    }

    public void homeOwnerClick(View view) {
        registerUser();
        accountType = "Homeowner";

        EditText editTextName = (EditText) findViewById(R.id.nameTextField);
        nameOfUser = editTextName.getText().toString();

        database.child("Homeowner").child(nameOfUser);

        EditText editTextEmail = (EditText) findViewById(R.id.emailTextField);
        emailOfUser = editTextEmail.getText().toString();

        database.child("Homeowner").child(nameOfUser).child("Email").setValue(emailOfUser);

        EditText editTextUserName = (EditText) findViewById(R.id.usernameTextField);
        usernameOfUser = editTextUserName.getText().toString();

        database.child("Homeowner").child(nameOfUser).child("Username").setValue(usernameOfUser);

        EditText editTextPassword = (EditText) findViewById(R.id.passwordTextField);
        passwordOfUser = editTextPassword.getText().toString();

        database.child("Homeowner").child(nameOfUser).child("Password").setValue(passwordOfUser);
    }

    public void serviceProviderClick(View view) {
        registerUser();
        accountType = "Service Provider";

        LinearLayout serviceProLay = (LinearLayout) findViewById(R.id.serviceProviderLayout);
        serviceProLay.setVisibility(View.VISIBLE);

        EditText editTextName = (EditText) findViewById(R.id.nameTextField);
        nameOfUser = editTextName.getText().toString();

        database.child("Service Provider").child(nameOfUser);

        EditText editTextEmail = (EditText) findViewById(R.id.emailTextField);
        emailOfUser = editTextEmail.getText().toString();

        database.child("Service Provider").child(nameOfUser).child("Email").setValue(emailOfUser);

        EditText editTextUserName = (EditText) findViewById(R.id.usernameTextField);
        usernameOfUser = editTextUserName.getText().toString();

        database.child("Service Provider").child(nameOfUser).child("Username").setValue(usernameOfUser);

        EditText editTextPassword = (EditText) findViewById(R.id.passwordTextField);
        passwordOfUser = editTextPassword.getText().toString();

        database.child("Service Provider").child(nameOfUser).child("Password").setValue(passwordOfUser);

        EditText editTextService = (EditText) findViewById(R.id.serviceTypeField);
        serviceType = editTextService.getText().toString();

        database.child("Service Provider").child(nameOfUser).child("Service Type").setValue(serviceType);

        EditText editTextPrice = (EditText) findViewById(R.id.priceField);
        priceOfService = editTextPrice.getText().toString();

        database.child("Service Provider").child(nameOfUser).child("Price of Service").setValue(priceOfService);
    }

    public static String getNameOfUser() {
        return nameOfUser;
    }

    public static String getEmailOfUser() {
        return emailOfUser;
    }

    public static String getUsernameOfUser() {
        return usernameOfUser;
    }

    public static String getPasswordOfUser() {
        return passwordOfUser;
    }

    public static String getAccountType() {
        return accountType;
    }

}
