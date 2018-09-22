package next.com.camaraimagen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class Audios extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private static final String[] PERMISOS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };

    private Button botonGrabar;

    private static String nombreAudio = null;

    private MediaRecorder mediaRecorder = null;

    private MediaPlayer mediaPlayer = null;

    private Button botonReproducir;
    private Button botonAbrir;

    boolean verificacion = true;
    boolean verificacion2 = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int leer = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int leer2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

        if (leer == PackageManager.PERMISSION_DENIED || leer2 == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,PERMISOS,REQUEST_CODE);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audios);


        nombreAudio = Environment.getExternalStorageDirectory() + "/audio.3gp";


        botonGrabar = (Button) findViewById(R.id.btn_audio_iniciar);
        botonGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grabando(verificacion);

                if (verificacion){
                    botonGrabar.setText("Detener Grabación");
                }else{
                    botonGrabar.setText("Iniciar Grabación");
                }

                verificacion = !verificacion;
            }
        });

        botonReproducir = (Button) findViewById(R.id.btn_audio_reproducir);
        botonReproducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlay(verificacion2);
                if (verificacion2){
                    botonReproducir.setText("Detener reproducción");
                }else{
                    botonReproducir.setText("Reproducir");
                }
                verificacion2 = !verificacion2;
            }
        });

        botonAbrir = (Button) findViewById(R.id.btn_audio_abrir);
        botonAbrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent audioAbrir = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(audioAbrir, REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && null != data){

            Uri audioSelec = data.getData();
            String [] path = {MediaStore.Audio.Media.DATA};
            Cursor cursor = getContentResolver().query(audioSelec, path, null, null, null);
            cursor.moveToFirst();
            int columna = cursor.getColumnIndex(path[0]);
            String pathAudio = cursor.getString(columna);
            cursor.close();

            mediaPlayer = new MediaPlayer();
            try{
                mediaPlayer.setDataSource(pathAudio);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }catch(IOException e){
                Toast.makeText(Audios.this, "Ha ocurrido un error en la reproducción", Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(this, "Ha ocurrido un error al abrir el audio", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaRecorder != null){
            mediaRecorder.release();
            mediaRecorder = null;
        }

        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }

    private void detenerGrabacion(){
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

        Toast.makeText(Audios.this, "Se ha guardado el audio en:\n" + Environment.getExternalStorageDirectory() + "/audio.3gp", Toast.LENGTH_LONG).show();
    }

    private void grabando(boolean comenzado){
        if (comenzado){
            comenzarGrabacion();
        }else{
            detenerGrabacion();
        }
    }

    private void comenzarGrabacion(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(nombreAudio);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try{
            mediaRecorder.prepare();
        }catch(IOException e){
            Toast.makeText(Audios.this, "No se grabará correctamente", Toast.LENGTH_SHORT).show();
        }

        mediaRecorder.start();
    }

    private void comenzarReproduccion(){
        mediaPlayer = new MediaPlayer();
        try{
            mediaPlayer.setDataSource(nombreAudio);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch(IOException e){
            Toast.makeText(Audios.this, "Ha ocurrido un error en la reproducción", Toast.LENGTH_SHORT).show();
        }
    }

    private void detenerReproduccion(){
        mediaPlayer.release();
        mediaPlayer = null;
    }

    private void onPlay(boolean comenzarRep){
        if (comenzarRep){
            comenzarReproduccion();
        }else{
            detenerReproduccion();
        }
    }

}
