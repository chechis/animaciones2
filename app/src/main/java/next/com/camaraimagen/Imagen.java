package next.com.camaraimagen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class Imagen extends AppCompatActivity {

    private Button botonCamara, botonAbrir;
    private ImageView imgCamara;
    private TextView txtRuta;

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
        txtRuta = (TextView) findViewById(R.id.txt_imagen_ruta);

        botonAbrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 2);

            }
        });


        botonCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File imagenFolder= new File(Environment.getExternalStorageDirectory(), "CamaraExam");
                imagenFolder.mkdirs();

                File imagen = new File(imagenFolder, "fotoExam.jpg");

                Uri uriImagen = Uri.fromFile(imagen);

                camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImagen);

                startActivityForResult(camaraIntent, REQUEST_CODE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){

            Toast.makeText(this, "Se ha guardado la imagen:\n"+Environment.getExternalStorageDirectory() + "/CamaraExam/fotoExam.jpg", Toast.LENGTH_SHORT).show();

            txtRuta.setText("Ruta:\n"+Environment.getExternalStorageDirectory() + "/CamaraExam/fotoExam.jpg");

            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+ "/CamaraExam/fotoExam.jpg");

            int height = bitmap.getHeight();
            int width = bitmap.getWidth();

            float scaleA = ((float)(width/2))/width;
            float scaleB = ((float)(height/2))/height;

            Matrix matrix = new Matrix();
            matrix.postScale(scaleA, scaleB);

            Bitmap nuevaImagen = Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
            imgCamara.setImageBitmap(nuevaImagen);

        }if (requestCode == 2 && null != data){

            txtRuta.setText("Ruta:\n"+Environment.getExternalStorageDirectory() + "/CamaraExam/fotoExam.jpg");

            Uri imagenseleccionada = data.getData();
            String[] path = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(imagenseleccionada,path,null,null,null);
            cursor.moveToFirst();
            int columna = cursor.getColumnIndex(path[0]);
            String pathimagen = cursor.getString(columna);
            cursor.close();

            Bitmap bitmap = BitmapFactory.decodeFile(pathimagen);
            BitmapFactory.Options options= new BitmapFactory.Options();
            int height= bitmap.getHeight();
            int width=bitmap.getWidth();
            float scaleA =((float)(width/2))/width;
            float scaleB =((float)(height/2))/height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleA,scaleB);
            Bitmap nuevaimagen= Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
            imgCamara.setImageBitmap(nuevaimagen);

        }
    }
}
