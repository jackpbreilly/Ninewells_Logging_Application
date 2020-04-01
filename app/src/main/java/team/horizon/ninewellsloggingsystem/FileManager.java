package team.horizon.ninewellsloggingsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

class FileManager {

    Context Context_;

    public FileManager(Context context_) {
        Context_ = context_;
    }
    // Returns ArrayList of Files in Directory
    public ArrayList<String> FileSearch(String pathToSearch) {
        ArrayList<String> fileData = new ArrayList<>();
        File directory = new File(pathToSearch);
        File[] files = directory.listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                fileData.add(files[i].getName());
            }
        }
        return fileData;
    }

    public void SaveSignature(final com.github.gcacace.signaturepad.views.SignaturePad signaturePad){
        Bitmap signature_bitmap = signaturePad.getSignatureBitmap();
        File output_path = new File("/sdcard/Download", "last_sig.bmp");

        try (FileOutputStream out = new FileOutputStream(output_path)) { //this is essentially "with" from Python
            signature_bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            Context_.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"
                    + Environment.getExternalStorageDirectory())));
            Toast.makeText(Context_, "Saved signature to SD", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(Context_, "Error occurred", Toast.LENGTH_SHORT).show();
        }
    }

}
