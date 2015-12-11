package materialshop.curiousbat.shopmylist.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import materialshop.curiousbat.shopmylist.R;
import materialshop.curiousbat.shopmylist.database.ProductDatabase;
import materialshop.curiousbat.shopmylist.model.Product;

/**
 * Fragment for Scan
 * Created by Zied on 17/10/2015.
 */
public class ScanFragment extends Fragment {

    private String toast;

    List<Product> products = new ArrayList<>();

    public ScanFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        displayToast();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        Button scan = (Button) view.findViewById(R.id.scan_from_fragment);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanFromFragment();
            }
        });
        return view;
    }

    public void scanFromFragment() {
        IntentIntegrator.forSupportFragment(this).initiateScan();
    }

    private void displayToast() {
        if (getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
            toast = null;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                toast = "Cancelled ";
            } else {
                ProductDatabase mDatabase = new ProductDatabase(getActivity().getApplicationContext());
                mDatabase.open();
                products = mDatabase.selectAll();

                for (Product p: products) {
                    if ((result.getContents().equals(p.getBarCode()))) {
                        String name = p.getName();
                        String barcode = p.getBarCode();
                        String category = p.getCategory();
                        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        alert.setMessage("Nom : " + name + "Code Bar : " + barcode);
                        alert.setTitle(category);
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        });
                        alert.show();
                        Toast.makeText(getActivity(), result.getContents(), Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(getActivity(), result.getContents(), Toast.LENGTH_LONG).show();
                }
            }

            // At this point we may or may not have a reference to the activity
            displayToast();
        }
    }

    @Override
    public void onAttach (Activity activity){
        super.onAttach(activity);
    }

    @Override
    public void onDetach () {
        super.onDetach();
    }
}