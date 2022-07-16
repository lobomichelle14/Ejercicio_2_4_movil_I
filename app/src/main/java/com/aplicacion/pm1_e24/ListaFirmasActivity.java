package com.aplicacion.pm1_e24;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.aplicacion.pm1_e24.adapters.AdapterSignaturess;
import com.aplicacion.pm1_e24.configuraciones.SQLiteConexion;
import com.aplicacion.pm1_e24.configuraciones.Transacciones;
import com.aplicacion.pm1_e24.modelos.Signaturess;

import java.util.ArrayList;
import java.util.List;

public class ListaFirmasActivity extends AppCompatActivity {
    SQLiteConexion conexion;
    RecyclerView recyclerView;
    List<Signaturess> signaturessList;
    List<String> signaturessListString;



    Button btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_firmas);


        init();

        getSignaturessForDatabase();

        setListener();

        AdapterSignaturess adapter = new AdapterSignaturess(signaturessList);



//        Toast.makeText(this, adapter.getItemCount()+"", Toast.LENGTH_SHORT).show();

        recyclerView.setAdapter(adapter);

    }




    void init(){
        conexion = new SQLiteConexion(this, Transacciones.NAME_DATABASE, null, 1);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        signaturessList = new ArrayList<>();
        signaturessListString = new ArrayList<>();

        btnRegresar = (Button) findViewById(R.id.btnRegresarLista);
    }

    void setListener(){
        btnRegresar.setOnClickListener(v -> onBackPressed());
    }

    private void getSignaturessForDatabase() {

        SQLiteDatabase db = conexion.getReadableDatabase();
        Signaturess signaturess = null;
        Cursor cursor = db.rawQuery("SELECT * FROM "+ Transacciones.NAME_TABLE,null);

        while (cursor.moveToNext()){
            signaturess = new Signaturess();

            signaturess.id = cursor.getInt(0);
            signaturess.descripcion = cursor.getString(1);
            signaturess.imagen = cursor.getString(2);


            signaturessList.add(signaturess);
            signaturessListString.add(signaturess.descripcion);
        }

//        Toast.makeText(ListaFirmasActivity.this, signaturessList.size() + "", Toast.LENGTH_SHORT).show();
    }


}