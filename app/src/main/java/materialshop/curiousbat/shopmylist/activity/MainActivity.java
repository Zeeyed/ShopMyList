package materialshop.curiousbat.shopmylist.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import materialshop.curiousbat.shopmylist.R;
import materialshop.curiousbat.shopmylist.database.ProductDatabase;
import materialshop.curiousbat.shopmylist.fragment.OrderFragment;
import materialshop.curiousbat.shopmylist.fragment.FragmentDrawer;
import materialshop.curiousbat.shopmylist.fragment.HomeFragment;
import materialshop.curiousbat.shopmylist.fragment.ScanFragment;
import materialshop.curiousbat.shopmylist.model.Product;

/**
 * Activity for loading the main layout
 * Created by Zied on 27/11/2015.
 */
public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {


    int selectedPosition = 0;

    List<Product> productList = new ArrayList<>();

    private static String TAG = MainActivity.class.getSimpleName();

    private FragmentDrawer drawerFragment;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        ProductDatabase mDatabase = new ProductDatabase(this);
        mDatabase.open();

        productList = mDatabase.selectAll();
        int prodSize = productList.size();

        String prodSizeStr = String.valueOf(prodSize);

        Log.i("number Products : ", prodSizeStr);

        mDatabase.close();

        // display the first navigation drawer view on app launch
        displayView(0);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        selectedPosition = position;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new OrderFragment();
                title = getString(R.string.title_order);
                break;
            case 2:
                fragment = new ScanFragment();
                title = getString(R.string.title_scan);
                break;
            case 3:
                LogOut();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }


    }

    /**
     * When you click on Logout button
     */
    private void LogOut() {

        ProductDatabase mDatabase = new ProductDatabase(this);
        mDatabase.open();
        mDatabase.removeAllFavorites();
        mDatabase.close();
            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                            overridePendingTransition(R.anim.left_in, R.anim.right_out);
                        }
                    }).create().show();
    }

    @Override
    public void onBackPressed() {
        if (selectedPosition != 0) {
            displayView(0);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
    }
}
