package com.villanos_toni.datosservidorvolley;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
    Bitmap bitmap;
    Villano villano;

    RequestQueue colaDePeticiones;
    Volleys volleys;

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

        //obtenemos una referencia a la cola de peticiones
        volleys = Volleys.getInstance(this);
        colaDePeticiones = volleys.getRequestQueue();

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
                //guardar los datos
                String nombreArray = "nuevoVillano";
                if(accion.equals("modificar")){
                    nombreArray = "modificaVillano";
                }
                guardarVillano(nombreArray);
            }
        });
    }

    private void nuevoVillano(){

    }

    private void modificarVillano(){
        final int posicion = getIntent().getExtras().getInt("id_villano");
        villano = Datos.getListaVillanos().get(posicion);

        final byte[] byteArray = getIntent().getByteArrayExtra("imagen");
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        txtTituloAccion.setText("Modificar Villano");
        etNombre.setText(villano.getNombre());
        etPelicula.setText(villano.getPelicula());
        etPoderes.setText(villano.getPoderes());
        imgNuevaImagen.setImageBitmap(bitmap);
    }

    private void guardarVillano(String nombreArray){
        final Villano villanoAux;
        final String nombre = etNombre.getText().toString();
        final String pelicula = etPelicula.getText().toString();
        final String poderes = etPoderes.getText().toString();
        villanoAux = new Villano(0, nombre, pelicula, poderes, "");
        if(accion.equals("modificar")){
            villanoAux.setId(villano.getId());
            villanoAux.setImagen(villano.getImagen());
        }

        Map<String, String> parametros = new HashMap<String, String>();
        parametros.put("id", String.valueOf(villanoAux.getId()));
        parametros.put("nombre", villanoAux.getNombre());
        parametros.put("pelicula", villanoAux.getPelicula());
        parametros.put("poderes", villanoAux.getPoderes());
        parametros.put("imagen", villanoAux.getImagen());

        Map<String, Map> cuerpo = new HashMap<String, Map>();
        cuerpo.put(nombreArray, parametros);
        JSONObject jsonObject = new JSONObject(cuerpo);



        JsonObjectRequest peticion = new JsonObjectRequest(
                Request.Method.POST,
                "https://apcpruebas.es/toni/villanos/controladores/controlVillanos.php",

                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // array disponible

                        try {
                            int resultado = response.getInt("estado");
                            String mensaje = response.getString("mensaje");
                            if(resultado == 0){
                                if(accion.equals("modificar")){
                                    villano.setNombre(nombre);
                                    villano.setPelicula(pelicula);
                                    villano.setPoderes(poderes);
                                }else{
                                    //TODO:conseguir el id del nuevo villano
                                    Datos.getListaVillanos().add(villanoAux);
                                }

                                finish();
                            }
                                Toast.makeText(EditarActivity.this, mensaje, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i("datos", error.toString());
                Toast.makeText(EditarActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }

        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Auto", "21232F297A57A5A743894A0E4A801FC3");
                return headers;
            }
        };

        colaDePeticiones.add(peticion);
    }

}
