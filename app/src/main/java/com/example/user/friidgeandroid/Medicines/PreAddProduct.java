package com.example.user.friidgeandroid.Medicines;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.user.friidgeandroid.JSONParser;
import com.example.user.friidgeandroid.MainActivity;
import com.example.user.friidgeandroid.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Program patrzy czy dany ean znajduje sie w ogolnej bazie danych
 */

public class PreAddProduct extends Activity {



    public static String nameFinded = "";
    public static String eanFinded = "";
    private ProgressDialog pDialog;


    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // single product url
    private static final String url_product_detials = Configuration.ipAdressServer + "/getproductdetailsfromall.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_EAN = "ean";
    private static final String TAG_expireDate = "expireDate";

    @Override
    public void onBackPressed() {
        Intent inte=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(inte);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicinefromuserbase);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


                new PreAddProduct.GetProductDetails().execute();


    }

    /**
     * Background Async Task to Get complete product details
     */
    class GetProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PreAddProduct.this);
            pDialog.setMessage("Loading product details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("ean", Configuration.scannedEan));

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

                            // Edit Text
                            System.out.println("■■" + product.getString(TAG_NAME) + "");
                            nameFinded= product.getString(TAG_NAME)+"";
                            eanFinded=Configuration.scannedEan;
                            Configuration.isEanRecognized=true;
                            Intent i = new Intent(getApplicationContext(),AddProductToUserBase.class);
                            startActivity(i);
                            //System.out.println("■■" + product.getString(TAG_EAN) + "");
                           // System.out.println("■■" + product.getString(TAG_expireDate) + "");
                            //Toast.makeText(getApplicationContext(),product.getString(TAG_NAME)+"",Toast.LENGTH_LONG).show();
                           /* txtName = (EditText) findViewById(R.id.inputName);
                            txtPrice = (EditText) findViewById(R.id.inputPrice);
                            txtDesc = (EditText) findViewById(R.id.inputDesc);

                            // display product data in EditText
                            txtName.setText(product.getString(TAG_NAME));
                            txtPrice.setText(product.getString(TAG_PRICE));
                            txtDesc.setText(product.getString(TAG_DESCRIPTION));*/

                        } else {

                            eanFinded=Configuration.scannedEan;
                            Configuration.isEanRecognized=false;
                            Intent i = new Intent(getApplicationContext(),AddProductToUserBase.class);
                            startActivity(i);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }
}