package materialshop.curiousbat.shopmylist.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import materialshop.curiousbat.shopmylist.R;
import materialshop.curiousbat.shopmylist.database.ProductDatabase;
import materialshop.curiousbat.shopmylist.database.SQLiteHandler;
import materialshop.curiousbat.shopmylist.database.SessionManager;
import materialshop.curiousbat.shopmylist.logging.L;
import materialshop.curiousbat.shopmylist.model.User;

/**
 * Activity for loading Login layout
 * Created by Zied on 27/11/2015.
 */
public class LoginActivity extends AppCompatActivity {


    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\\\"(),:;<>@\\\\[\\\\]\\\\\\\\]+@[a-zA-Z0-9-]+(\\\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private TextInputLayout inputEmail;
    private TextInputLayout inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        inputEmail = (TextInputLayout) findViewById(R.id.emailWrapper);
        inputPassword = (TextInputLayout) findViewById(R.id.passwordWrapper);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        inputEmail.setHint("Your Username");
        inputPassword.setHint("Your Password");

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //Session Manager
        session = new SessionManager(getApplicationContext());



        // Check if user is logged in or not
        if (session.isLoggedIn()){
            Intent intent  = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click event
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getEditText().getText().toString();
                String password = inputPassword.getEditText().getText().toString();

                if (!validatePassword(password)){
                    inputPassword.setError("Not A Valid Password");
                }else {
                    inputPassword.setErrorEnabled(false);
                    inputEmail.setErrorEnabled(false);
                }

                // check if empty data in the form
                if (email.trim().length() > 0 && password.trim().length() > 0 && validatePassword(password)) {
                    LogIn(email, password);
                }

                hideKeyboard();
            }
        });

        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void LogIn(String email, String pass) {
        ProductDatabase mDatabase = new ProductDatabase(this);
        mDatabase.open();
        User user = mDatabase.searchPassword(email, pass);
        Log.d("User SQLIte : ", user.toString());
        if (user.getName() != null){
            L.m("Can Log in");
            showDialog();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            hideDialog();
        }
        else {
            inputPassword.setError("Password or Username Not Match");
            inputEmail.setError("Password or Username Not Match");
        }
        mDatabase.close();

    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if(view != null){
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public boolean validatePassword(String password){
        return password.length() > 6;
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }
}
