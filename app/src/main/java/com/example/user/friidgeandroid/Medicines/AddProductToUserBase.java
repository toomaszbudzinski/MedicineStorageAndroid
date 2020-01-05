package com.example.user.friidgeandroid.Medicines;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.friidgeandroid.JSONParser;
import com.example.user.friidgeandroid.MainActivity;
import com.example.user.friidgeandroid.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//z neta
import es.dmoral.toasty.Toasty;


public class AddProductToUserBase extends Activity {
    private boolean successInsert;
    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputName;
    Spinner inputYear;
    Spinner inputMonth;
    Spinner inputDay;
    TextView txEan;
    String day;
    String month;
    String year;

    // url to create new product
    private static String url_create_product = Configuration.ipAdressServer +"/createproduct.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addproduct);

        // Edit Text
        inputName = (EditText) findViewById(R.id.inputName);
        inputYear = (Spinner) findViewById(R.id.spinnerYearAdd);
        inputMonth = (Spinner) findViewById(R.id.spinnerMonthAdd);
        inputDay = (Spinner) findViewById(R.id.spinnerDayAdd);
        txEan = (TextView) findViewById(R.id.textViewEanAdd);

        if(Configuration.isEanRecognized) {
            System.out.println("☻ Rozpoznalem w bazie głównej");
            inputName.setFocusable(false);
            //inputName.setEnabled(false);
        }

        txEan.setText(PreAddProduct.eanFinded);
        if(!PreAddProduct.nameFinded.equals("")){
            inputName.setText(PreAddProduct.nameFinded);
        }





        //day !!!!
        final String[] days=new String[31];
        for(int iD=0 ;iD<31;iD++) days[iD] = (iD<9) ? days[iD]="0"+(iD+1) : (iD+1)+"";


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.my_simple_spinner_item, days);

        inputDay.setAdapter(adapter);
        inputDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //Log.v("item", (String) parent.getItemAtPosition(position));
                day=inputDay.getSelectedItem().toString();
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
                month=inputMonth.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });



        //!!month

        String[] months=new String[12];
        for(int iD=0 ;iD<12;iD++) months[iD] = (iD<9) ? months[iD]="0"+(iD+1) : (iD+1)+"";


        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                R.layout.my_simple_spinner_item, months);

        inputMonth.setAdapter(adapter3);
        inputMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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




















        // Create button
        Button btnCreateProduct = (Button) findViewById(R.id.btnCreateProduct);

        // button click event
        btnCreateProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new CreateNewProduct().execute();
                System.out.println(successInsert);

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent inte = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(inte);
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
            pDialog = new ProgressDialog(AddProductToUserBase.this);
            pDialog.setMessage("Creating Product..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            String name = inputName.getText().toString();
            /*String year = inputYear.getText().toString();
            String month = inputMonth.getText().toString();
            String day = inputDay.getText().toString();
            month = (month.length()==1) ? "0"+month : month;
            day = (day.length()==1) ? "0"+day : day;
            year =(year.length()==2) ? "20"+year : year;*/
            String fullDate =day+"-"+month+"-"+year;
            Configuration.name=name;

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("ean", Configuration.scannedEan));
            params.add(new BasicNameValuePair("expireDate", fullDate));

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
                    successInsert=true;
                    //
                    // closing this screen
                    finish();
                } else {
                    successInsert=false;
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(!Configuration.isEanRecognized){
                //
                Intent i = new Intent(getApplicationContext(),AddProductToMainBase.class);
                startActivity(i);
            }else{
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
            return null;
        }

        protected boolean isSuccess(){
            return successInsert;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            if(successInsert){
                Toasty.success(getApplicationContext(),"Dodano produkt", Toast.LENGTH_LONG).show();
            }else{
                Toasty.error(getApplicationContext(),"Nie udane dodanie produktu", Toast.LENGTH_LONG).show();
            }
            pDialog.dismiss();
        }

    }
}
