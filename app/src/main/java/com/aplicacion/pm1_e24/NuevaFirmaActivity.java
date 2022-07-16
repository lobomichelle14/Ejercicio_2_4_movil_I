package com.aplicacion.pm1_e24;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.aplicacion.pm1_e24.configuraciones.SQLiteConexion;
import com.aplicacion.pm1_e24.configuraciones.Transacciones;

import java.io.ByteArrayOutputStream;

public class NuevaFirmaActivity extends AppCompatActivity {

    Lienzo lienzo;
    Button btnGuardar, btnLimpiar, btnRegresar;
    EditText editTextDescripcion;
    SQLiteConexion conexion;
    Bitmap imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_firma);

        init();
        setListeners();

    }

    void init(){
        conexion = new SQLiteConexion(this, Transacciones.NAME_DATABASE,null,1);

        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnRegresar = (Button) findViewById(R.id.btnRegresar);
        btnLimpiar = (Button) findViewById(R.id.btnLimpiar);

        editTextDescripcion = (EditText) findViewById(R.id.editTextDescripcion);

        lienzo = (Lienzo) findViewById(R.id.lienzo);
    }

    void setListeners(){

        btnRegresar.setOnClickListener(v -> onBackPressed());

        btnLimpiar.setOnClickListener(v -> dialogLimpiar());

        btnGuardar.setOnClickListener(v -> guardar());
    }


    void dialogLimpiar(){
        AlertDialog.Builder builder = new AlertDialog.Builder(NuevaFirmaActivity.this);
        builder.setMessage("¿Desea limpiar la firma?")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        lienzo.nuevoDibujo();
                        editTextDescripcion.setText("");
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {      }
                })
                .show();
    }

     void guardar(){

        if(lienzo.getLimpio()){
            showMessage("Por favor dibuje una firma");
            return;
        }

        if(editTextDescripcion.getText().toString().isEmpty()){
            showMessage("Por favor añada una descripcion");
            return;
        }

         SQLiteDatabase db = conexion.getWritableDatabase();

        try {


            ContentValues valores = new ContentValues();

            valores.put(Transacciones.KEY_DESCRIPCION, editTextDescripcion.getText().toString());

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(10480);
            Bitmap bitmap = Bitmap.createBitmap(lienzo.getWidth(), lienzo.getHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            lienzo.draw(canvas);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            String img = Base64.encodeToString(bytes,Base64.DEFAULT);

            valores.put(Transacciones.KEY_IMAGEN, img);

            Long result = db.insert(Transacciones.NAME_TABLE, Transacciones.KEY_ID, valores);

            if (result > 0){
                showMessage("Guardado correctamente !!!");
                lienzo.nuevoDibujo();
                editTextDescripcion.setText(null);
            }else{
                showMessage("Ha ocurrido un error al guardar");
            }

            db.close();
        }catch (Exception e){
            showMessage("Ha ocurrido un error al guardar");
            db.close();
        }

    }

    void showMessage(String message){

        Toast.makeText(NuevaFirmaActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}