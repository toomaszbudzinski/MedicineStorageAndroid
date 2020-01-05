package com.example.user.friidgeandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.user.friidgeandroid.Medicines.Configuration;
import com.example.user.friidgeandroid.Medicines.DisplayDialogPreScan;
import com.example.user.friidgeandroid.Medicines.DisplayUserProducts;
import com.example.user.friidgeandroid.Medicines.RecognizeEan;
import com.example.user.friidgeandroid.Medicines.ScannerEan;

public class MainActivity extends AppCompatActivity {

    //private FButton buttonAddDelProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        this.setContentView(R.layout.activity_main);

        Toast.makeText(getApplicationContext(), Configuration.ipAdressServer,Toast.LENGTH_LONG).show();
        Button buttonAddDel = (Button)findViewById(R.id.buttonAddDeleteProduct);
        Button buttonLook = (Button)findViewById(R.id.buttonLookProducts);
        buttonAddDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ScannerEan.class);
                startActivity(i);
            }
        });

        buttonLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),DisplayUserProducts.class);
                startActivity(i);
            }
        });
        //buttonAddDel = (FButton)findViewById(R.id.primary_button);


    }
    @Override
    public void onBackPressed() {
        Intent inte = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(inte);
    }

   /* public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        buttonAddDelProduct.setShadowEnabled(isChecked);
        updateShadowHeight(30);
    }

    private void updateShadowHeight(int height) {
        //Convert from dp to pixel
        int shadowHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());
        buttonAddDelProduct.setShadowHeight(shadowHeight);
    }*/
}
