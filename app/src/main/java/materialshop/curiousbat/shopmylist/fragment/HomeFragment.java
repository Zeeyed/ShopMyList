package materialshop.curiousbat.shopmylist.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import materialshop.curiousbat.shopmylist.R;
import materialshop.curiousbat.shopmylist.adapter.OrderAdapter;
import materialshop.curiousbat.shopmylist.database.ProductDatabase;
import materialshop.curiousbat.shopmylist.extra.Constants;
import materialshop.curiousbat.shopmylist.extra.ProductSorter;
import materialshop.curiousbat.shopmylist.model.Product;
import materialshop.curiousbat.shopmylist.network.VolleySingleton;

import static materialshop.curiousbat.shopmylist.extra.Keys.EndpointBoxOffice.KEY_BARECODE;
import static materialshop.curiousbat.shopmylist.extra.Keys.EndpointBoxOffice.KEY_CAT;
import static materialshop.curiousbat.shopmylist.extra.Keys.EndpointBoxOffice.KEY_ID;
import static materialshop.curiousbat.shopmylist.extra.Keys.EndpointBoxOffice.KEY_IMAGE;
import static materialshop.curiousbat.shopmylist.extra.Keys.EndpointBoxOffice.KEY_NAME;
import static materialshop.curiousbat.shopmylist.extra.Keys.EndpointBoxOffice.KEY_UNICOST;
import static materialshop.curiousbat.shopmylist.extra.UrlEndpoints.URL_PRODUCT;

/**
 * Created by Zied on 17/10/2015.
 */
public class HomeFragment extends Fragment  {

    private static final String STATE_NEWS = "stat_prod";


    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ArrayList<Product> listProducts = new ArrayList<>();

    // Adapter
    private OrderAdapter adapterOrder;
    //The list
    private RecyclerView listMovieRecy;
    // Text for handling Network errors
    private TextView textVolleyError;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ProductSorter newsTechSorter = new ProductSorter();




    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static String getRequestUrl() {
        return URL_PRODUCT;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();


        sendJsonRequest();

    }

    private void sendJsonRequest() {

        JsonArrayRequest request = new JsonArrayRequest(
                getRequestUrl(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            //L.t(getActivity(), response.toString());
                            textVolleyError.setVisibility(View.GONE);
                            listProducts = parseJSONResponse(response);
                            adapterOrder.setProductList(listProducts);
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleVolleyError(error);
            }
        });
        requestQueue.add(request);
    }

    /**
     * Catch Volley Errors
     * @param error
     */
    private void handleVolleyError(VolleyError error) {
        textVolleyError.setVisibility(View.VISIBLE);

        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            textVolleyError.setText(R.string.error_timeout);

        } else if (error instanceof AuthFailureError) {
            textVolleyError.setText(R.string.error_auth_fail);
        } else if (error instanceof ServerError) {
            textVolleyError.setText(R.string.error_server);

        } else if (error instanceof NetworkError) {
            textVolleyError.setText(R.string.error_network);

        } else if (error instanceof ParseError) {
            textVolleyError.setText(R.string.error_parse);

        }
    }

    /**
     * Get the JSON parsing response
     * @param response
     * @return List of products
     * @throws JSONException
     */
    private ArrayList<Product> parseJSONResponse(JSONArray response) throws JSONException {

        ArrayList<Product> listProducts = new ArrayList<>();
        if (response != null || response.length() > 0) {

            ProductDatabase mDatabase = new ProductDatabase(getActivity().getApplicationContext());
            mDatabase.open();
            mDatabase.removeAllProducts();

            int totalPrice = 0;
            for (int i = 0; i < response.length(); i++) {
                try {

                    JSONObject currentProduct = response.getJSONObject(i);

                    JSONObject currentCategory = currentProduct.getJSONObject(KEY_CAT);

                    String id = Constants.NA;
                    String name = Constants.NA;
                    String uniCost = Constants.NA;
                    String barcode = Constants.NA;
                    String image = Constants.NA;
                    String category = Constants.NA;

                    if (contains(currentProduct, KEY_ID)) {
                        id = currentProduct.getString(KEY_ID);
                    }
                    if (contains(currentProduct, KEY_NAME)) {
                        name = currentProduct.getString(KEY_NAME);
                    }
                    if (contains(currentProduct, KEY_UNICOST)) {
                        uniCost = currentProduct.getString(KEY_UNICOST);
                    }
                    if (contains(currentProduct, KEY_BARECODE)) {
                        barcode = currentProduct.getString(KEY_BARECODE);
                    }
                    if (contains(currentProduct, KEY_IMAGE)) {
                        image = currentProduct.getString(KEY_IMAGE).replace("http://localhost", "http://192.168.1.66");
                    }
                    if (contains(currentCategory, KEY_NAME)) {
                        category = (String) currentCategory.get("name");
                    }

                    Product product = new Product();

                    product.setId(id);
                    product.setName(name);
                    product.setUnitCost(uniCost);
                    product.setBarCode(barcode);
                    product.setImage(image);
                    product.setCategory(category);


                    if (!name.equals(Constants.NA)) {

                        listProducts.add(product);
                        mDatabase.insertTop(product);
                        Log.d("Database add it: ", mDatabase.toString(product));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            mDatabase.close();
        }
        return listProducts;
    }

    /**
     * Check if a JSON Object contains a specific key or not
     * @param jsonObject
     * @param key
     * @return
     */
    private boolean contains(JSONObject jsonObject, String key) {
        return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key) ? true : false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        textVolleyError = (TextView) view.findViewById(R.id.textVolleyError);
        listMovieRecy = (RecyclerView) view.findViewById(R.id.listMovieHits);
        listMovieRecy.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapterOrder = new OrderAdapter(getActivity());
        listMovieRecy.setAdapter(adapterOrder);

        if (savedInstanceState != null) {
            listProducts = savedInstanceState.getParcelableArrayList(STATE_NEWS);
            adapterOrder.setProductList(listProducts);
        } else {

            sendJsonRequest();
        }

        // Inflate the layout for this fragment
        return view;
    }


}