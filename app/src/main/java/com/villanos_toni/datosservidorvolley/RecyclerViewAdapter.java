package com.villanos_toni.datosservidorvolley;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

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

    private static final String URL_BASE = "https://apcpruebas.es/toni/villanos/images/";

    public RecyclerViewAdapter(ArrayList<Villano> listaVillanos, Context context) {
        this.context = context;

        //obtenemos una referencia a la cola de peticiones
        volleys = Volleys.getInstance(context);
        this.listaVillanos = listaVillanos;
        colaDePeticiones = volleys.getRequestQueue();

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
                        holder.imgVillano.setImageResource(R.drawable.villano);
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

        holder.imgEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSimpleDialog(position).show();

            }
        });
    }

    @Override
    public int getItemCount() {

            return listaVillanos.size();

    }


    private void eliminarVillano(final int position){

        Map<String, String> parametros = new HashMap<String, String>();
        parametros.put("id", String.valueOf(listaVillanos.get(position).getId()));

        Map<String, Map> cuerpo = new HashMap<String, Map>();
        cuerpo.put("eliminarVillano", parametros);
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
                                //eliminar el villano de la lista
                                listaVillanos.remove(position);
                                notifyDataSetChanged();
                                }

                            Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i("datos", error.toString());
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
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

    public AlertDialog createSimpleDialog(final int position){
        String mensaje = "¿Seguro que quieres eliminar a " + listaVillanos.get(position).getNombre() + " ?";
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Atención")
                .setMessage(mensaje)
                .setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //eliminar nota
                        eliminarVillano(position);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //cancelar operación
                    }
                });
        return builder.create();
    }

    static class ViewHolderVillanos extends RecyclerView.ViewHolder {

        // Referencias UI
        ImageView imgVillano;
        ImageView imgEliminar;
        TextView txtNombreVillano;
        TextView txtPelicula;
        TextView txtPoderes;



        public ViewHolderVillanos(View itemView) {
            super(itemView);
            imgVillano = (ImageView)itemView.findViewById(R.id.imgVillano);
            imgEliminar = (ImageView)itemView.findViewById(R.id.imgEliminar);
            txtNombreVillano = (TextView)itemView.findViewById(R.id.txtNombreVillano);
            txtPelicula = (TextView)itemView.findViewById(R.id.txtPelicula);
            txtPoderes = (TextView)itemView.findViewById(R.id.txtPoderes);

        }

    }
}
