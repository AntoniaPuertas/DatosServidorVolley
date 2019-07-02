package com.villanos_toni.datosservidorvolley;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EditarActivity extends AppCompatActivity {

    TextView txtTituloAccion;
    EditText etNombre;
    EditText etPelicula;
    EditText etPoderes;
    ImageView imgNuevaImagen;
    Button btnGetImagen;
    Button btnCancelar;
    Button btnGuardar;

    String accion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        txtTituloAccion = findViewById(R.id.txtTituloAccion);
        etNombre = findViewById(R.id.etNombre);
        etPelicula = findViewById(R.id.etPelicula);
        etPoderes = findViewById(R.id.etPoderes);
        imgNuevaImagen = findViewById(R.id.imgNuevaImagen);
        btnGetImagen = findViewById(R.id.btnGetImagen);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnGuardar = findViewById(R.id.btnGuardar);

        accion = getIntent().getExtras().getString("accion");

        if(accion.equals("nuevo")){
            nuevoVillano();
        }else if(accion.equals("modificar")){
            modificarVillano();
        }


        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnGetImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:seleccionar una imagen
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: guardar los datos
            }
        });
    }

    private void nuevoVillano(){

    }

    private void modificarVillano(){

    }
}
