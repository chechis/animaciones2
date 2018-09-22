package next.com.camaraimagen;

import android.Manifest;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class Audios extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private static final String[] PERMISOS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };

    private Button botonGrabar;

    private static String nombreAudio = null;

    private MediaRecorder mediaRecorder = null;

    private void detenerGrabacion(){
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

        Toast.makeText(Audios.this, "Se ha guardado el audio en:\n" + Environment.getExternalStorageDirectory() + "/audio.3gp", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audios);
    }
}
