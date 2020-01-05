package com.example.user.friidgeandroid.Medicines;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;
import com.example.user.friidgeandroid.JSONParser;
import com.example.user.friidgeandroid.MainActivity;
import com.example.user.friidgeandroid.R;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class DeleteProductFromUserBase extends Activity {
    private boolean successInsert;
    // single product url
    private static final String url_product_detials = Configuration.ipAdressServer +"/getproductdetails.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_EAN = "ean";
    private static final String TAG_expireDate = "expireDate";

    private static final String url_delete_product = Configuration.ipAdressServer +"/deleteproduct.php";

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicinefromuserbase);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //new GetProductDetails().execute();
        new DeleteProduct().execute();
    }

       /* class GetProductDetails extends AsyncTask<String, String, String> {

            *//**
             * Before starting background thread Show Progress Dialog
             *//*
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(com.example.user.friidgeandroid.Medicines.DeleteProductFromUserBase.this);
                pDialog.setMessage("Loading product details. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }

            *//**
             * Getting product details in background thread
             *//*
            protected String doInBackground(String... params) {

                // updating UI from Background Thread
                runOnUiThread(new Runnable() {
                    public void run() {
                        // Check for success tag
                        int success;
                        try {
                            // Building Parameters
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("pid", Configuration.pid));

                            // getting product details by making HTTP request
                            // Note that product details url will use GET request
                            JSONObject json = jsonParser.makeHttpRequest(
                                    url_product_detials, "GET", params);

                            // check your log for json response
                            Log.d("Single Product Details", json.toString());

                            // json success tag
                            success = json.getInt(TAG_SUCCESS);
                            if (success == 1) {
                                // successfully received product details
                                JSONArray productObj = json
                                        .getJSONArray(TAG_PRODUCT); // JSON Array

                                // get first product object from JSON Array
                                JSONObject product = productObj.getJSONObject(0);


                            } else {
                                // product with pid not found
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                return null;
            }

            *//**
             * After completing background task Dismiss the progress dialog
             **//*
            protected void onPostExecute(String file_url) {
                // dismiss the dialog once got all details
                pDialog.dismiss();
            }
        }*/


        class DeleteProduct extends AsyncTask<String, String, String> {

            /**
             * Before starting background thread Show Progress Dialog
             */
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //StyleableToast.makeText(context, "Hello World!", Toast.LENGTH_LONG, R.style.mytoast).show();
                pDialog = new ProgressDialog(com.example.user.friidgeandroid.Medicines.DeleteProductFromUserBase.this);
                pDialog.setMessage("Deleting Product...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }

            /**
             * Deleting product
             */
            protected String doInBackground(String... args) {

                // Check for success tag
                int success;
                try {
                    // Building Parameters
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("ean", Configuration.scannedEan));
                /*
                    System.out.println("■■"+params);*/
                    // getting product details by making HTTP request
                    JSONObject json = jsonParser.makeHttpRequest(
                            url_delete_product, "POST", params);

                    // check your log for json response
                    Log.d("Delete Product", json.toString());

                    // json success tag
                    success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        //Toasty.success(getApplicationContext(),"Usunieto produkt", Toast.LENGTH_LONG).show();
                        // product successfully deleted
                        // notify previous activity by sending code 100
                        successInsert=true;
                        Intent i = getIntent();
                        // send result code 100 to notify about product deletion
                        setResult(100, i);
                        finish();
                        Intent ii=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(ii);
                    }else{
                        successInsert=false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            /**
             * After completing background task Dismiss the progress dialog
             **/
            protected void onPostExecute(String file_url) {
                // dismiss the dialog once product deleted
                if(successInsert){
                    Toasty.success(getApplicationContext(),"Usunięto produkt", Toast.LENGTH_LONG).show();
                }else{
                    Toasty.error(getApplicationContext(),"Nie udało sie usunać produktu", Toast.LENGTH_LONG).show();
                }
                pDialog.dismiss();

            }

        }

}
