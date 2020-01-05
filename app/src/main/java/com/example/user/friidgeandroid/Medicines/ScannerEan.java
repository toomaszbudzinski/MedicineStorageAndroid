package com.example.user.friidgeandroid.Medicines;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.user.friidgeandroid.MainActivity;
import com.example.user.friidgeandroid.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;



public class ScannerEan extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicinesscannerean);

        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();

    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            //scannedEan = scanningResult.getContents();

            //String scanFormat = scanningResult.getFormatName();
            //String contents = intent.getStringExtra("SCAN_RESULT");
            Configuration.scannedEan = intent.getStringExtra("SCAN_RESULT");

            //String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

            System.out.println("■■■■■■■"+Configuration.scannedEan);
            Intent i = new Intent(getApplicationContext(),RecognizeEan.class);
            startActivity(i);

        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {
        Intent inte = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(inte);
    }
}
