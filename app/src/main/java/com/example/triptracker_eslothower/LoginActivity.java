package com.example.triptracker_eslothower;
import com.backendless.Backendless;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    EditText mEmailEditText;
    EditText mPasswordEditText;
    EditText mNameEditText;
    Button mLoginButton;
    TextView mSignUpTextView;
    Button mSignUpButton;
    String APP_ID;
    String API_KEY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmailEditText = (EditText)findViewById(R.id.enter_email);
        mPasswordEditText = (EditText)findViewById(R.id.enter_password);
        mNameEditText = (EditText)findViewById(R.id.enter_name);
        mLoginButton = (Button)findViewById(R.id.login_button);
        mSignUpTextView = (TextView)findViewById(R.id.sign_up_text);
        mSignUpButton = (Button)findViewById(R.id.signup_button);
        APP_ID = getString(R.string.APP_ID);
        API_KEY = getString(R.string.API_KEY);
        Backendless.initApp(this, APP_ID, API_KEY);


        // On click listener for the sign up text view
        // This will modify the view for user registration
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSignUpTextView.getText() == getString(R.string.sign_up_text)) {
                    mLoginButton.setVisibility(View.GONE);
                    mNameEditText.setVisibility(View.VISIBLE);
                    mSignUpButton.setVisibility(View.VISIBLE);
                    mSignUpTextView.setText("Cancel Sign Up");

                } else {
                    mNameEditText.setVisibility(View.GONE);
                    mSignUpButton.setVisibility(View.GONE);
                    mLoginButton.setVisibility(View.VISIBLE);
                    mSignUpTextView.setText(getString(R.string.sign_up_text));

                }
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = mEmailEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();
                String name = mNameEditText.getText().toString().trim();


                if (!userEmail.isEmpty() &&!password.isEmpty() && !name.isEmpty()) {

                    /* register the user in Backendless */
                    BackendlessUser user = new BackendlessUser();
                    user.setEmail(userEmail);
                    user.setPassword(password);
                    user.setProperty("name", name);

                    Backendless.UserService.register(user,
                            new AsyncCallback<BackendlessUser>() {
                                @Override
                                public void handleResponse( BackendlessUser backendlessUser ) {
                                    Log.i(TAG, "Registration successful for " + backendlessUser.getEmail());
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("Registration successful for " + backendlessUser.getEmail());
                                    builder.setTitle(R.string.authentication_success_title);
                                    builder.setPositiveButton(android.R.string.ok, null);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                                @Override
                                public void handleFault( BackendlessFault fault ) {
                                    Log.i(TAG, "Registration failed: " + fault.getMessage());



                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage(fault.getMessage());
                                    builder.setTitle(R.string.authentication_error_title);
                                    builder.setPositiveButton(android.R.string.ok, null);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            } );
                }
                else {
                    warnUser(getString(R.string.empty_field_signup_error));

                }
            }
        });


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = mEmailEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();
                String name = mNameEditText.getText().toString().trim();


                if (!userEmail.isEmpty() &&!password.isEmpty()) {

                    /* register the user in Backendless */
                    BackendlessUser user = new BackendlessUser();
                    user.setEmail(userEmail);
                    user.setPassword(password);
                    user.setProperty("name", name);

                    Backendless.UserService.login(userEmail, password,
                            new AsyncCallback<BackendlessUser>() {
                                @Override
                                public void handleResponse( BackendlessUser backendlessUser ) {
                                    Log.i(TAG, "Login successful for " + backendlessUser.getEmail());

                                }
                                @Override
                                public void handleFault( BackendlessFault fault ) {
                                    Log.i(TAG, "Login failed: " + fault.getMessage());
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage(fault.getMessage() + "\nAlso, please enter your email and password");
                                    builder.setTitle("Error");
                                    builder.setPositiveButton(android.R.string.ok, null);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            } );
                }
                else {
                    warnUser(getString(R.string.empty_field_signup_error));

                }
            }
        });


        // On click listener for Sign up button

    }

    public void warnUser(String warning){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(warning);
        builder.setTitle(R.string.authentication_error_title);
        builder.setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public boolean validation(String email, String password){
        if (!email.contains("@") || !email.contains(".")){
            if (password.length() >= 6){
                if (password != email){
                    return true;
                } else{
                    warnUser("Password may not be equal to your email.");
                }

            } else {
                warnUser("Password must be 6 or more characters long.");
            }

        } else{
            warnUser("Your email must contain an @ sign and a .");
        }
        return false;
    }


}
