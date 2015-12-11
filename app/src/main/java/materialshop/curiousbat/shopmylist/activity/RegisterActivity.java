package materialshop.curiousbat.shopmylist.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import materialshop.curiousbat.shopmylist.R;
import materialshop.curiousbat.shopmylist.database.ProductDatabase;
import materialshop.curiousbat.shopmylist.database.SQLiteHandler;
import materialshop.curiousbat.shopmylist.database.SessionManager;
import materialshop.curiousbat.shopmylist.logging.L;
import materialshop.curiousbat.shopmylist.model.User;

/**
 * Activity for loading the register layout
 * Created by Zied on 27/11/2015.
 */
public class RegisterActivity extends Activity {



    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;

    private TextInputLayout inputEmail;
    private TextInputLayout inputPassword;
    private TextInputLayout inputFullName;


    private ProgressDialog pDialog;

    private Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputEmail = (TextInputLayout) findViewById(R.id.emailWrapperRegister);
        inputFullName = (TextInputLayout) findViewById(R.id.nameWrapperRegister);
        inputPassword = (TextInputLayout) findViewById(R.id.passwordWrapperRegister);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScree);

        inputEmail.setHint("Your Email");
        inputFullName.setHint("Your Username");
        inputPassword.setHint("Your Password");

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                String name = inputFullName.getEditText().getText().toString();
                String email = inputEmail.getEditText().getText().toString();
                String password = inputPassword.getEditText().getText().toString();

                if(!isValidEmail(email)){
                    inputEmail.setError("Not A Valid Email Address");
                }else if (!validatePassword(password)){
                    inputPassword.setError("Not A Valid Password");
                }else {
                    inputPassword.setErrorEnabled(false);
                    inputEmail.setErrorEnabled(false);
                }

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()  && isValidEmail(email) && validatePassword(password)){
                    registerUser(name, email, password);
                }
                hideDialog();
            }
        });

        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        });

    }

    /**
     * Register the user into Sqlite
     * @param name name of the user
     * @param email email of the user
     * @param password password of the user
     */
    private void registerUser(final String name, final String email, final String password) {
        ProductDatabase mDatabase = new ProductDatabase(this);
        mDatabase.open();

        User registeredData = new User(name, email, password);
        Log.d("Insert User details : ", registeredData.toString());
        mDatabase.insertUser(registeredData);
        mDatabase.close();
        L.m("User Inserted");
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    /**
     * Email validation check. Returns true if the target is null or 0-length.
     * @param target the string to be examined
     * @return true if target is null or zero length
     */
    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    /**
     * Password validation check
     * @param password the user password
     * @return true if the user password is more than six character
     */
    public boolean validatePassword(String password){
        return password.length() > 6;
    }

    @Override
    public void onBackPressed() {

            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);

    }
}
