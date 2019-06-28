package com.example.datosservidorvolley;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RequestQueue colaDePeticiones;
    RecyclerView rvListaVillanos;
    RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvListaVillanos = findViewById(R.id.rvListaVillanos);

        colaDePeticiones = Volley.newRequestQueue(this);

        getDatos();
    }

    void getDatos() {
        JsonObjectRequest peticion = new JsonObjectRequest(
                Request.Method.GET,
                "https://apcpruebas.es/datosServidor",

                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // array disponible
                        try {
                            JSONArray jsonArray = response.getJSONArray("mensaje");
                            ArrayList<Villano> listaVillanos = Datos.getListaVillanos();

                            for (int i = 0; i < jsonArray.length (); ++i) {
                                listaVillanos.add(new Villano(jsonArray.getString(0), jsonArray.getString(1), jsonArray.getString(2)));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mostrarListView();
                    }
                },new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.i("datos", error.toString());
                }

                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Auto", "2F4A58256CF53C5AF94D8BA7A9D08DB0");
                return headers;
            }
        };

        colaDePeticiones.add(peticion);
    }

    public void mostrarListView( ){

        //getListaDatos(jsonObject);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvListaVillanos.setLayoutManager(llm);
        recyclerViewAdapter = new RecyclerViewAdapter(Datos.getListaVillanos());
        rvListaVillanos.setAdapter(recyclerViewAdapter);
    }

    public void getListaDatos(JSONObject jsonObject){
        ArrayList<Villano> listaVillanos = Datos.getListaVillanos();
        JSONArray keys = jsonObject.names ();

        try{
            for (int i = 0; i < keys.length (); ++i) {
                listaVillanos.add(new Villano(jsonObject.getString("nombre"), jsonObject.getString("pelicula"), jsonObject.getString("poderes")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
