package com.example.datosservidorvolley;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderVillanos> {

    private ArrayList<Villano> listaVillanos;


    public RecyclerViewAdapter(ArrayList<Villano> listaVillanos) {
        this.listaVillanos = listaVillanos;
    }



    @Override
    public ViewHolderVillanos onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista, parent, false);
        return new ViewHolderVillanos(listItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolderVillanos holder, final int position) {

        holder.imgVillano.setImageResource(R.drawable.ic_mood_bad_black_24dp);
        holder.txtNombreVillano.setText(listaVillanos.get(position).getNombre());
        holder.txtPelicula.setText(listaVillanos.get(position).getPelicula());
        holder.txtPoderes.setText(listaVillanos.get(position).getPoderes());
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
