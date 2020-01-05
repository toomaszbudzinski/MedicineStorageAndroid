package com.example.user.friidgeandroid.Medicines;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.user.friidgeandroid.DisplayAllProduct;
import com.example.user.friidgeandroid.EditProduct;
import com.example.user.friidgeandroid.JSONParser;
import com.example.user.friidgeandroid.MainActivity;
import com.example.user.friidgeandroid.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2018-05-16.
 */

public class DisplayEditProduct extends Activity {

    EditText inputName;
    Spinner inputYear;
    Spinner inputMonth;
    Spinner inputDay;
    TextView txEan1;
    Button btnSave;
    Button btnDelete;
    String day;
    String year;
    String month;
    String pid;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // single product url
    // single product url
    private static final String url_product_detials = Configuration.ipAdressServer +"/getproductdetails.php";

    // url to update product
    private static final String url_update_product = Configuration.ipAdressServer +"/updateproduct.php";

    // url to delete product
    private static final String url_delete_product = Configuration.ipAdressServer +"/deleteproductdisplayall.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    private static final String TAG_DESCRIPTION = "description";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displayeditproduct);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        inputName = (EditText) findViewById(R.id.inputNameEdit);
        inputYear = (Spinner) findViewById(R.id.spinnerYearEdit);
        inputMonth = (Spinner) findViewById(R.id.spinnerMonthEdit);
        inputDay = (Spinner) findViewById(R.id.spinnerDayEdit);
        txEan1 = (TextView) findViewById(R.id.textViewEanEdit);
        // save button
        btnSave = (Button) findViewById(R.id.btnSaveEdit);
        btnDelete = (Button) findViewById(R.id.btnDeleteEdit);

        // getting product details from intent
        Intent i = getIntent();

        // getting product id (pid) from intent
        pid = i.getStringExtra(TAG_PID);

        // Getting complete product details in background thread
        new GetProductDetails().execute();


        //day !!!!
        final String[] days=new String[31];
        for(int iD=0 ;iD<31;iD++) days[iD] = (iD<9) ? days[iD]="0"+(iD+1) : (iD+1)+"";

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.my_simple_spinner_item, days);
                //android.R.layout.simple_spinner_item, days);

        inputDay.setAdapter(adapter);
        inputDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //Log.v("item", (String) parent.getItemAtPosition(position));
                day=inputDay.getSelectedItem().toString();
                if(position == 1){
                    btnSave.setEnabled(false);
                }
                System.out.println(day+"↓"+inputDay.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        //!!year
        String[] years=new String[7];

        for(int iD=0 ;iD<7;iD++) years[iD] = (Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR))+iD)+"";

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                R.layout.my_simple_spinner_item, years);

        inputYear.setAdapter(adapter2);
        inputYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //Log.v("item", (String) parent.getItemAtPosition(position));
                year=inputYear.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });



        //!!month
        String[] months = new String[12];
        for(int iD=0 ;iD<12;iD++) months[iD] = (iD<9) ? months[iD]="0"+(iD+1) : (iD+1)+"";


        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                R.layout.my_simple_spinner_item, months);

        inputMonth.setAdapter(adapter3);
        inputMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //Log.v("item", (String) parent.getItemAtPosition(position));
                month=inputMonth.getSelectedItem().toString();
                System.out.println("Í"+month);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });







        // save button click event
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // starting background task to update product
                new SaveProductDetails().execute();
            }
        });

        // Delete button click event
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // deleting product in background thread
                new DeleteProduct().execute();

    }});
    }



    /**
     * Background Async Task to Get complete product details
     * */
    class GetProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DisplayEditProduct.this);
            pDialog.setMessage("Loading product details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("pid", pid));

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

                            // product with this pid found
                            // Edit Text

                           /* txtName = (EditText) findViewById(R.id.inputNameEdit);
                            txtPrice = (EditText) findViewById(R.id.input);
                            txtDesc = (EditText) findViewById(R.id.inputDesc);*/

                            // display product data in EditText
//                            txtName.setText(product.getString(TAG_NAME));
//                            txtPrice.setText(product.getString(TAG_PRICE));
//                            txtDesc.setText(product.getString(TAG_DESCRIPTION));
                            inputName.setText(product.getString("name"));
                            txEan1.setText(product.getString("ean"));
                            day = ((product.getString("expireDate").substring(0,2).length())!=2) ? product.getString("expireDate").substring(1,2) : product.getString("expireDate").substring(0,2);

                            inputDay.setSelection(Integer.valueOf(day)-1);
                            //inputDay.setText(product.getString("expireDate").substring(0,2));
                            //inputMonth.setText(product.getString("expireDate").substring(3,5));
                            month = ((product.getString("expireDate").substring(3,5).length())!=2) ? product.getString("expireDate").substring(4,5) : product.getString("expireDate").substring(3,5);
                            inputMonth.setSelection(Integer.valueOf(month)-1);
                            //inputYear.setText(product.getString("expireDate").substring(6));
                            inputYear.setSelection(getYear(product.getString("expireDate").substring(6)));
                            year=product.getString("expireDate").substring(6);

                        }else{
                            // product with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }

        protected int getYear(String stringYear){
            int idYear=0;
            for(int iD=0 ;iD<7;iD++) {
                if(String.valueOf(Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR)) + iD).equals(stringYear))
                return iD;
            }
           return idYear;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }

    /**
     * Background Async Task to  Save product Details
     * */
    class SaveProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DisplayEditProduct.this);
            pDialog.setMessage("Saving product ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {

            // getting updated data from EditTexts
            String name = inputName.getText().toString();
            //String year = inputYear.getText().toString();
            //String month = inputMonth.getText().toString();
            //String day = inputDay.getText().toString();
            String ean = txEan1.getText().toString();
            System.out.println(ean+"■");
          /*  month = (month.length()==1) ? "0"+month : month;
            day = (day.length()==1) ? "0"+day : day;
            year =(year.length()==2) ? "20"+year : year;*/
            String fullDate =day+"-"+month+"-"+year;
            Configuration.name=name;


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_PID, pid));
            params.add(new BasicNameValuePair(TAG_NAME, name));
            //params.add(new BasicNameValuePair("ean", ean));
            params.add(new BasicNameValuePair("expireDate", fullDate));

            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_product,
                    "POST", params);

            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully updated
                    Intent i = getIntent();
                    // send result code 100 to notify about product update
                    setResult(100, i);
                    finish();
                    Intent ij=new Intent(getApplicationContext(),DisplayUserProducts.class);
                    startActivity(ij);
                } else {
                    // failed to update product
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
            // dismiss the dialog once product uupdated
            pDialog.dismiss();
        }
    }

    /*****************************************************************
     * Background Async Task to Delete Product
     * */
    class DeleteProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DisplayEditProduct.this);
            pDialog.setMessage("Deleting Product...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Deleting product
         * */
        protected String doInBackground(String... args) {

            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("pid", pid));

                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        url_delete_product, "POST", params);

                // check your log for json response
                Log.d("Delete Product", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // product successfully deleted
                    // notify previous activity by sending code 100
                    Intent i = getIntent();
                    // send result code 100 to notify about product deletion
                    setResult(100, i);
                    finish();
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
            // dismiss the dialog once product deleted
            pDialog.dismiss();

        }

    }
}
