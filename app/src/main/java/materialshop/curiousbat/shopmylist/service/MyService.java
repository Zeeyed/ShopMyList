package materialshop.curiousbat.shopmylist.service;


import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import materialshop.curiousbat.shopmylist.core.MyApplication;
import materialshop.curiousbat.shopmylist.extra.Constants;
import materialshop.curiousbat.shopmylist.model.Product;
import materialshop.curiousbat.shopmylist.network.VolleySingleton;
import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

import static materialshop.curiousbat.shopmylist.extra.Keys.EndpointBoxOffice.KEY_IMAGE;
import static materialshop.curiousbat.shopmylist.extra.Keys.EndpointBoxOffice.KEY_NAME;
import static materialshop.curiousbat.shopmylist.extra.Keys.EndpointBoxOffice.KEY_UNICOST;
import static materialshop.curiousbat.shopmylist.extra.UrlEndpoints.URL_PRODUCT;

/**
 * Created by Zied on 01/11/2015.
 */
public class MyService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        new MyTask(this).execute(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private static class MyTask extends AsyncTask<JobParameters, Void, JobParameters> {


        private VolleySingleton volleySingleton;
        private RequestQueue requestQueue;
        MyService myService;

        MyTask(MyService myService) {
            this.myService = myService;
            volleySingleton = VolleySingleton.getInstance();
            requestQueue = volleySingleton.getRequestQueue();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JobParameters doInBackground(JobParameters... params) {
            JSONArray response = sendJsonRequest();
            try {
                ArrayList<Product> listProducts = parseJSONResponse(response);
                //MyApplication.getWriableDatabse().insertProduct(listProducts, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return params[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            myService.jobFinished(jobParameters, false);
        }

        public static String getRequestUrl() {
            return URL_PRODUCT;
        }

        private JSONArray sendJsonRequest() {

            JSONArray response = null;

            RequestFuture<JSONArray> requestFuture = RequestFuture.newFuture();

            JsonArrayRequest request = new JsonArrayRequest(
                    getRequestUrl(),
                    requestFuture,
                    requestFuture
                    /*
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            //try {
                                //L.t(getActivity(), response.toString());
                                //textVolleyError.setVisibility(View.GONE);
                                //listProducts = parseJSONResponse(response);
                                //adapterOrder.setProductList(listProducts);
                            // }catch (JSONException e) {
                                //Toast.makeText(getActivity(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            //}

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //handleVolleyError(error);
                }
            }*/);
            requestQueue.add(request);
            try {
                response = requestFuture.get(30000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Log.d("Interuuuuption :", ""+e.getMessage());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            return response;
        }

        private ArrayList<Product> parseJSONResponse(JSONArray response) throws JSONException {

            ArrayList<Product> listProducts = new ArrayList<>();
            if (response != null || response.length() > 0) {

                int totalPrice = 0;
                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject currentProduct = response.getJSONObject(i);

                        String name = Constants.NA;
                        String uniCost = Constants.NA;
                        String image = Constants.NA;


                        if (contains(currentProduct, KEY_NAME)) {
                            name = currentProduct.getString(KEY_NAME);
                        }
                        if (contains(currentProduct, KEY_UNICOST)) {
                            uniCost = currentProduct.getString(KEY_UNICOST);
                        }
                        if (contains(currentProduct, KEY_IMAGE)) {
                            image = currentProduct.getString(KEY_IMAGE);
                        }

                        Product product = new Product();

                        product.setName(name);
                        product.setUnitCost(uniCost);
                        product.setImage(image);


                        if (!name.equals(Constants.NA)) {

                            listProducts.add(product);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            }
            return listProducts;
        }

        private boolean contains(JSONObject jsonObject, String key) {
            return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key) ? true : false;
        }

    }
}
