package materialshop.curiousbat.shopmylist.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.avast.android.dialogs.fragment.ListDialogFragment;
import com.avast.android.dialogs.iface.IListDialogListener;
import com.avast.android.dialogs.iface.IMultiChoiceListDialogListener;


import java.util.ArrayList;

import materialshop.curiousbat.shopmylist.R;
import materialshop.curiousbat.shopmylist.activity.MainActivity;
import materialshop.curiousbat.shopmylist.activity.OrderSendActivity;
import materialshop.curiousbat.shopmylist.adapter.FavoritAdapter;

import materialshop.curiousbat.shopmylist.database.ProductDatabase;

import materialshop.curiousbat.shopmylist.model.Product;
import materialshop.curiousbat.shopmylist.network.VolleySingleton;


/**
 * Fragment for Order Adapter
 * Created by Zied on 17/10/2015.
 */
public class OrderFragment extends Fragment implements IListDialogListener, IMultiChoiceListDialogListener {

    Context context;

    private static final String STATE_NEWS = "stat_prod";

    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    private ArrayList<Product> listProducts = new ArrayList<>();

    private static final int REQUEST_PROGRESS = 1;
    private static final int REQUEST_LIST_SIMPLE = 9;
    private static final int REQUEST_LIST_MULTIPLE = 10;
    private static final int REQUEST_LIST_SINGLE = 11;
    private static final int REQUEST_DATE_PICKER = 12;
    private static final int REQUEST_TIME_PICKER = 13;
    private static final int REQUEST_SIMPLE_DIALOG = 42;

    private float totalPrice = 0;


    // Adapter
    private FavoritAdapter favoritAdapter;
    //The list
    private RecyclerView listMovieRecy;
    // Text for handling Network errors
    private TextView textVolleyError;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public OrderFragment() {
        // Required empty public constructor
    }

    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();

        sendJsonRequest();

    }

    private ArrayList<Product> sendJsonRequest() {

        ProductDatabase mDatabase = new ProductDatabase(getActivity().getApplicationContext());
        mDatabase.open();
        listProducts = mDatabase.selectAllFavorites();
        getData();
        mDatabase.close();
        return listProducts;

    }

    /**
     * Get Product List From SQLite
     * @return
     */
    public ArrayList<Product> getData() {
        ArrayList<Product> data = new ArrayList<>();
        ArrayList<Product> fav_sqlite;
        ProductDatabase mDatabase = new ProductDatabase(getActivity().getApplicationContext());
        mDatabase.open();
        fav_sqlite = mDatabase.selectAllFavorites();
        Log.i("Size Fav SQLite", "" + fav_sqlite.size());

        for (int i = 0; i < fav_sqlite.size(); i++) {
            Product product = new Product();

            product.setId(fav_sqlite.get(i).getId());
            product.setName(fav_sqlite.get(i).getName());
            product.setUnitCost(fav_sqlite.get(i).getUnitCost());
            product.setBarCode(fav_sqlite.get(i).getBarCode());
            product.setImage(fav_sqlite.get(i).getImage());
            product.setCategory(fav_sqlite.get(i).getCategory());

            Log.i("AAAAAAAABBBB", "id = " + product.getId());
            Log.i("AAAAAAAABBBB", "name = " + product.getName());
            Log.i("AAAAAAAABBBB", "unicost = " + product.getUnitCost());
            Log.i("AAAAAAAABBBB", "barcode = " + product.getBarCode());
            Log.i("AAAAAAAABBBB", "image = " + product.getImage());
            Log.i("AAAAAAAABBBB", "category = " + product.getCategory());

            totalPrice = totalPrice + Float.valueOf(product.getUnitCost());
            Log.d("Total price ====> ", " " + totalPrice);
            data.add(product);
        }
        Log.d("aSize  = ", "" + data.size());
        mDatabase.close();
        return data;
    }

    /**
     * Budget controller
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.context = container.getContext();
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        listMovieRecy = (RecyclerView) view.findViewById(R.id.listMovieHits);
        listMovieRecy.setLayoutManager(new LinearLayoutManager(getActivity()));

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptView = layoutInflater.inflate(R.layout.dialog_signin, null);

                AlertDialog.Builder alerBuilder = new AlertDialog.Builder(context);

                alerBuilder.setView(promptView);

                final EditText userInput = (EditText) promptView.findViewById(R.id.editTextDialogUserInput);

                alerBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        String budget = null;
                                        int budgetNew = 0;
                                        if (userInput != null) {
                                            budget = String.valueOf(userInput.getText().toString());
                                            if (!budget.trim().equals("")){
                                                if (Integer.valueOf(budget) > totalPrice && totalPrice != 0) {
                                                    Intent intent = new Intent(getActivity(), OrderSendActivity.class);
                                                    startActivity(intent);
                                                } else
                                                    Toast.makeText(context, "Your budget isn't enough ! : ", Toast.LENGTH_SHORT).show();

                                            } else
                                                Toast.makeText(context, "Enter A Budget ! : ", Toast.LENGTH_SHORT).show();
                                        }else
                                            Toast.makeText(context, "Enter A Budget ! : ", Toast.LENGTH_SHORT).show();

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialogB = alerBuilder.create();
                alertDialogB.show();
            }
        });

        favoritAdapter = new FavoritAdapter(getActivity());
        listMovieRecy.setAdapter(favoritAdapter);

        if (savedInstanceState != null) {
            listProducts = savedInstanceState.getParcelableArrayList(STATE_NEWS);
            favoritAdapter.setProductList(listProducts);
        } else {
            //listProducts = sendJsonRequest();
            favoritAdapter.setProductList(listProducts);
        }
        return view;
    }

    @Override
    public void onListItemSelected(CharSequence value, int number, int requestCode) {
        if (requestCode == REQUEST_LIST_SIMPLE || requestCode == REQUEST_LIST_SINGLE) {
            Toast.makeText(context, "Selected: " + value, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onListItemsSelected(CharSequence[] values, int[] selectedPositions, int requestCode) {
        if (requestCode == REQUEST_LIST_MULTIPLE) {
            StringBuilder sb = new StringBuilder();
            for (CharSequence value : values) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(value);

            }
            Toast.makeText(getActivity(), "Selected: " + sb.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
