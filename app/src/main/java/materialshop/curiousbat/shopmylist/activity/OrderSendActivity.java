package materialshop.curiousbat.shopmylist.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;

import materialshop.curiousbat.shopmylist.R;

/**
 * Activity for loading order
 * Created by Zied on 29/11/2015.
 */
public class OrderSendActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);
    }

    /**
     * Called when the back button is pressed
     */
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to back to Order?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        OrderSendActivity.super.onBackPressed();
                        overridePendingTransition(R.anim.left_in, R.anim.right_out);
                    }
                }).create().show();
    }
}
