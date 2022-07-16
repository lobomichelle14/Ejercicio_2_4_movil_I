package com.aplicacion.pm1_e24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnNuevaFirma, btnListaFirma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();
        setListeners();
    }

    void init(){
        btnNuevaFirma = (findViewById(R.id.btnNuevaFirma));
        btnListaFirma = (findViewById(R.id.btnListaFirmas));
    }

    void setListeners(){

        btnNuevaFirma.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), NuevaFirmaActivity.class);
            startActivity(intent);
        });

        btnListaFirma.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ListaFirmasActivity.class);
            startActivity(intent);
        });
    }
}