package next.com.camaraimagen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class Video extends AppCompatActivity{

    Button fab, abrir;

    private static final String FORMATO_VIDEO = ".mp4";
    private static final String FOLDER = "/GIFs/";
    private TextView txtRuta;

    private static final String NOMBRE = "gif";

    private VideoView videoView;

    private Uri videoUri;

    private static int REQUEST_CODE = 1;

    private static final String[] PERMISOS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int leer = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(leer == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,PERMISOS,REQUEST_CODE);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        videoView = (VideoView)findViewById(R.id.video_view);
        txtRuta = (TextView) findViewById(R.id.txt_video_ruta);
        fab = (Button) findViewById(R.id.fab);
        abrir = (Button) findViewById(R.id.btn_video_abrir);

        abrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent videoAbrir = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(videoAbrir, 2);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent videoIntent = new Intent (MediaStore.ACTION_VIDEO_CAPTURE);

                File videosFolder = new File(Environment.getExternalStorageDirectory(),FOLDER);

                videosFolder.mkdirs();

                File video = new File(videosFolder,NOMBRE + FORMATO_VIDEO);

                videoUri = Uri.fromFile(video);

                videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);

                startActivityForResult(videoIntent,REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            Toast.makeText(Video.this, "Se guardó el video en:\n"+
                    Environment.getExternalStorageDirectory() + FOLDER + NOMBRE + FORMATO_VIDEO, Toast.LENGTH_LONG).show();

            txtRuta.setText("Ruta:\n"+
                    Environment.getExternalStorageDirectory() + FOLDER + NOMBRE + FORMATO_VIDEO);
            videoView.setVideoURI(videoUri);
            videoView.start();

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    videoView.start();
                }
            });

        }if (requestCode == 2 && null != data){
            txtRuta.setText("Ruta:\n"+
                    Environment.getExternalStorageDirectory() + FOLDER + NOMBRE + FORMATO_VIDEO);

            Uri videoSelec = data.getData();
            String [] path = {MediaStore.Video.Media.DATA};
            Cursor cursor = getContentResolver().query(videoSelec,path,null,null,null);
            cursor.moveToFirst();
            int columna = cursor.getColumnIndex(path[0]);
            String pathVideo = cursor.getString(columna);
            cursor.close();


            videoView.setVideoPath(pathVideo);
            videoView.start();
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    videoView.start();
                }
            });
        }

        else{
            Toast.makeText(Video.this, "Ha ocurrido un error al guardar el video", Toast.LENGTH_SHORT).show();
        }
    }
}
