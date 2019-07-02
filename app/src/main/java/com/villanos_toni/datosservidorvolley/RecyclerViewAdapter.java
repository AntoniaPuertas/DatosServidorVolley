package com.villanos_toni.datosservidorvolley;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderVillanos> {

    private ArrayList<Villano> listaVillanos;
    private Context context;

    private RequestQueue colaDePeticiones;
    private Volleys volleys;

    private static final String URL_BASE = "https://apcpruebas.es/datosServidor/";

    public RecyclerViewAdapter(ArrayList<Villano> listaVillanos, Context context) {
        this.context = context;

        //obtenemos una referencia a la cola de peticiones
        volleys = Volleys.getInstance(context);
        this.listaVillanos = listaVillanos;
        colaDePeticiones = volleys.getRequestQueue();
        getDatos();

    }



    @Override
    public ViewHolderVillanos onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista, parent, false);
        return new ViewHolderVillanos(listItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolderVillanos holder, final int position) {


        // Petición para obtener la imagen
        ImageRequest request = new ImageRequest(
                URL_BASE + listaVillanos.get(position).getImagen(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        holder.imgVillano.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        holder.imgVillano.setImageResource(R.drawable.ic_mood_bad_black_24dp);
                        Log.d("Adapter", "Error en respuesta Bitmap: "+ error.getMessage());
                    }
                });

        // Añadir petición a la cola
        colaDePeticiones.add(request);

        //holder.imgVillano.setImageResource(R.drawable.ic_mood_bad_black_24dp);
        holder.txtNombreVillano.setText(listaVillanos.get(position).getNombre());
        holder.txtPelicula.setText(listaVillanos.get(position).getPelicula());
        holder.txtPoderes.setText(listaVillanos.get(position).getPoderes());

        holder.imgVillano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemActivity.class);
                intent.putExtra("id_villano", position);
                ImageView imageView = (ImageView)v;
                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream btStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, btStream);
                byte[] byteArray = btStream.toByteArray();

                intent.putExtra("imagen", byteArray);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        int num = 0;
        if(listaVillanos != null){
            num = listaVillanos.size();
        }
        return num;
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
                            JSONArray array = response.getJSONArray("mensaje");
                            listaVillanos = getListaDatos(array);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //mostrarListView();
                        notifyDataSetChanged();
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

    public ArrayList<Villano> getListaDatos(JSONArray jsonArray) throws JSONException {

        //ArrayList<Villano> listaVillanos = new ArrayList<>();
        ArrayList<Villano> mlistaVillanos = Datos.getListaVillanos();
        if(jsonArray.length() > 0){
            mlistaVillanos.clear();
        }

        for(int i = 0 ; i < jsonArray.length() ; i++){
            JSONObject villano = jsonArray.getJSONObject(i);
            String nombre = villano.getString("nombre");
            String pelicula = villano.getString("pelicula");
            String poderes = villano.getString("poderes");
            String imagen = villano.getString("imagen");
            mlistaVillanos.add(new Villano(nombre, pelicula, poderes, imagen));
        }

        return mlistaVillanos;
    }

    static class ViewHolderVillanos extends RecyclerView.ViewHolder {

        // Referencias UI
        ImageView imgVillano;
        TextView txtNombreVillano;
        TextView txtPelicula;
        TextView txtPoderes;



        public ViewHolderVillanos(View itemView) {
            super(itemView);
            imgVillano = (ImageView)itemView.findViewById(R.id.imgVillano);
            txtNombreVillano = (TextView)itemView.findViewById(R.id.txtNombreVillano);
            txtPelicula = (TextView)itemView.findViewById(R.id.txtPelicula);
            txtPoderes = (TextView)itemView.findViewById(R.id.txtPoderes);

        }

    }
}
