package team.horizon.ninewellsloggingsystem;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SignaturePad extends AppCompatActivity {
    final private int PERM_CODE_WRITE_EXTERNAL_STORAGE = 100;

    private com.github.gcacace.signaturepad.views.SignaturePad signature_pad;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signature_pad_activity);

        signature_pad = findViewById(R.id.signature_pad);
        context = getApplicationContext();

        final Button button_save = findViewById(R.id.button_save);
        final Button button_clear = findViewById(R.id.button_clear);

        button_save.setOnClickListener(new saveButtonOnClick());
        button_clear.setOnClickListener(new clearButtonOnClick());
    }

    class saveButtonOnClick implements View.OnClickListener {
        public void onClick(View v) {
            while (ContextCompat.checkSelfPermission(SignaturePad.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SignaturePad.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERM_CODE_WRITE_EXTERNAL_STORAGE);
            }

            Bitmap signature_bitmap = signature_pad.getSignatureBitmap();
            File output_path = new File("/sdcard/Download", "last_sig.bmp"); //TODO Fix the filename
            try (FileOutputStream out = new FileOutputStream(output_path)) { //this is essentially "with" from Python
                signature_bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"
                        + Environment.getExternalStorageDirectory())));
                Toast.makeText(context, "Saved signature to SD", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class clearButtonOnClick implements View.OnClickListener {
        public void onClick(View v) {
            signature_pad.clear();
        }
    }
}
