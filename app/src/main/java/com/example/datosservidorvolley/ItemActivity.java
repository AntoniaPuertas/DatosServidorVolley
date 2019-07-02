package com.example.datosservidorvolley;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemActivity extends AppCompatActivity {

    ImageView imgItemVillano;
    TextView txtItemNombre;
    TextView txtItemPelicula;
    TextView txtItemPoderes;

    Bitmap bitmap;
    Villano villano;

    //variables para hacer zoom en la imagen
    ScaleGestureDetector scaleGestureDetector;
    float scala;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        imgItemVillano = findViewById(R.id.imgItemVillano);
        txtItemNombre = findViewById(R.id.txtItemNombre);
        txtItemPelicula = findViewById(R.id.txtItemPelicula);
        txtItemPoderes = findViewById(R.id.txtItemPoderes);

        int posicion = getIntent().getExtras().getInt("id_villano");
        villano = Datos.getListaVillanos().get(posicion);

        byte[] byteArray = getIntent().getByteArrayExtra("imagen");
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        imgItemVillano.setImageBitmap(bitmap);
        txtItemNombre.setText(villano.getNombre());
        txtItemPelicula.setText(villano.getPelicula());
        txtItemPoderes.setText(villano.getPoderes());

        //para el zoom
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        scala = 1.0f;

    }

    // Este método redirecciona todos los eventos touch de la actividad hacia el gesture detector
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return scaleGestureDetector.onTouchEvent(event);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        // Cuando se detecta el gesto de escala este método redimensiona la imagen
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            //establecemos un tamaño máximo y mínimo para la escala
            scala = Math.max(0.1f, Math.min(scala, 5.0f));
            scala *= scaleGestureDetector.getScaleFactor();
            imgItemVillano.setScaleX(scala);
            imgItemVillano.setScaleY(scala);
            return true;
        }
    }
}
