package com.example.user.friidgeandroid.Medicines;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.user.friidgeandroid.JSONParser;
import com.example.user.friidgeandroid.MainActivity;
import com.example.user.friidgeandroid.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AddProductToMainBase extends Activity {

        // Progress Dialog
        private ProgressDialog pDialog;

        JSONParser jsonParser = new JSONParser();


        // url to create new product
        private static String url_create_product = Configuration.ipAdressServer +"/createproducttomainbase.php";

        // JSON Node names
        private static final String TAG_SUCCESS = "success";

        @Override
        public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicinefromuserbase);

        new CreateNewProduct().execute();
    }

        /**
         * Background Async Task to Create new product
         * */
        class CreateNewProduct extends AsyncTask<String, String, String> {

            /**
             * Before starting background thread Show Progress Dialog
             * */
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(AddProductToMainBase.this);
                pDialog.setMessage("Creating Product..");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }

            /**
             * Creating product
             * */
            protected String doInBackground(String... args) {


                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", Configuration.name));
                params.add(new BasicNameValuePair("ean", Configuration.scannedEan));

                // getting JSON Object
                // Note that create product url accepts POST method
                JSONObject json = jsonParser.makeHttpRequest(url_create_product,
                        "POST", params);

                // check log cat fro response
                Log.d("Create Response", json.toString());

                // check for success tag
                try {
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        // successfully created product
                        //Intent i = new Intent(getApplicationContext(),DisplayAllProduct.class);
                        //startActivity(i);
                        System.out.println("Dodano produkt");
                        Intent i=new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        // closing this screen
                        finish();
                    } else {
                        // failed to create product
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }



            /**
             * After completing background task Dismiss the progress dialog
             * **/
            protected void onPostExecute(String file_url) {
                // dismiss the dialog once done
                pDialog.dismiss();
            }

        }
}
