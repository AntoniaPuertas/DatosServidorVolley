package com.example.datosservidorvolley;

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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderVillanos> {

    private ArrayList<Villano> listaVillanos;
    private Context context;

    private RequestQueue colaDePeticiones;
    private Volleys volleys;

    private static final String URL_BASE = "https://apcpruebas.es/datosServidor/";

    public RecyclerViewAdapter(ArrayList<Villano> listaVillanos, Context context) {
        this.listaVillanos = listaVillanos;
        this.context = context;

        //obtenemos una referencia a la cola de peticiones
        volleys = Volleys.getInstance(context);
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
        return listaVillanos.size();
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
