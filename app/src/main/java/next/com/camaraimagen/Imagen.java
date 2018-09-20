package next.com.camaraimagen;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Imagen extends AppCompatActivity {

    private Button botonCamara, botonAbrir;
    private ImageView imgCamara;

    private static final int REQUEST_CODE=1;

    private static final String [] PERMISOS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int leer = ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(leer == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, PERMISOS, REQUEST_CODE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen);

        botonCamara = (Button) findViewById(R.id.btn_camara);
        imgCamara = (ImageView) findViewById(R.id.img_camara);
        botonAbrir = (Button) findViewById(R.id.btn_imagen_abrir);

        botonCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}
