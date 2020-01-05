package com.example.user.friidgeandroid.Medicines;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.user.friidgeandroid.MainActivity;
import com.example.user.friidgeandroid.R;

public class DisplayDialogPreScan extends AppCompatActivity {

    Button bok;
    Button bcancel;
    Intent i;

    @Override
    public void onBackPressed() {
        Intent inte = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(inte);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppFullScreenThemeS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicinefromuserbase);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        /*openDialog(getApplicationContext());

        bcancel=(Button)findViewById(R.id.dialog_cancel);
       bcancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i=new Intent(getApplicationContext(), MainActivity.class);
               startActivity(i);
           }
       });*/
        AlertDialog.Builder builder1 = new AlertDialog.Builder(DisplayDialogPreScan.this);
        builder1.setMessage("Zaraz zostanie zeskanowany kod EAN produktu.\n\nW zależności od wyniku program:\n■ Doda do Twojej bazy produkt \n■ Usunię w przypadku w którym produkt już w niej był \n \nTakie rozwiązanie rozwiązuje problem bezproblemowego dodawanie i usuwanie produktów z Twojej bazy ");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Przejdź dalej",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //dialog.cancel();
                        i = new Intent(getApplicationContext(),RecognizeEan.class);
                        startActivity(i);
                    }
                });

        builder1.setNegativeButton(
                "Cofnij",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        i = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i);
                    }
                });

        AlertDialog alert11 = builder1.create();

        alert11.show();
        //alert11.getWindow().setBackgroundDrawableResource(android.R.color.darker_gray);
        alert11.getWindow().setBackgroundDrawableResource(android.R.color.background_light);
        alert11.getWindow().setTitleColor(Color.YELLOW);
        alert11.getButton(alert11.BUTTON_POSITIVE).setTextColor(Color.rgb(27, 124, 46));
        alert11.getButton(alert11.BUTTON_POSITIVE).setTextSize(20);
        alert11.getButton(alert11.BUTTON_NEGATIVE).setTextColor(Color.rgb(114, 25, 37));
/*
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (!isFinishing()){
                    new AlertDialog.Builder(DisplayDialogPreScan.this)
                            .setTitle("Your Alert")
                            .setMessage("Your Message")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Whatever...
                                }
                            }).show();
                }
            }
        });*/
    }


    public void openDialog(Context context) {
        final Dialog dialog = new Dialog(DisplayDialogPreScan.this); // Context, this, etc.
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("Dialog");
        dialog.show();
    }

}