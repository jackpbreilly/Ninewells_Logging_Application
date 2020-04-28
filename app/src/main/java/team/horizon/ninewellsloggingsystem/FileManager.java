package team.horizon.ninewellsloggingsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.system.Os;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.os.Environment.getExternalStorageDirectory;

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

    // Saves signature to SD card
    public void SaveSignature(final com.github.gcacace.signaturepad.views.SignaturePad signaturePad, UI toast){
        Bitmap signature_bitmap = signaturePad.getSignatureBitmap();
        File output_path = new File(Environment.getExternalStorageDirectory().getPath() +"/Download", "last_sig.bmp");
        try (FileOutputStream out = new FileOutputStream(output_path)) { //this is essentially "with" from Python
            signature_bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            Context_.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"
                    + getExternalStorageDirectory())));
        } catch (IOException e) {
            e.printStackTrace();
toast.SendToast("Error Saving Signature: " + e);
        }
    }

    // Looks in res/assets for forms to move to sd and then moves them
    public void moveAssetToStorageDir(String path, UI toast) {

        File file = new File(Environment.getExternalStorageDirectory().getPath() +"/Download/Forms");
        if (!file.exists() && !file.isDirectory()) {
            file.mkdir();

            String rootPath = file.getPath() + "/";
            try {
                String[] paths = Context_.getAssets().list(path);
                for (int i = 0; i < paths.length; i++) {
                    if (paths[i].indexOf(".") == -1) {
                        File dir = new File(rootPath + paths[i]);
                        dir.mkdir();
                        moveAssetToStorageDir(paths[i], toast);
                    } else {
                        File dest = null;
                        InputStream in = null;
                        if (path.length() == 0) {
                            dest = new File(rootPath + paths[i]);
                            in = Context_.getAssets().open(paths[i]);
                        } else {
                            dest = new File(rootPath + "/" + paths[i]);
                            in = Context_.getAssets().open(path + "/" + paths[i]);
                        }
                        dest.createNewFile();
                        FileOutputStream out = new FileOutputStream(dest);
                        byte[] buff = new byte[in.available()];
                        in.read(buff);
                        out.write(buff);
                        out.close();
                        in.close();
                    }


                    toast.SendToast("Successfully Imported Forms");
                }
            } catch (Exception exp) {
                toast.SendToast("Unsuccessfully Imported Forms: " + exp
                );

            }
        }
    }

    // removed file from sd
    public void deleteFile(String filename) throws IOException {
        File file = new File(filename);
        file.delete();

        if(file.exists()){
            file.getCanonicalFile().delete();
            if(file.exists()){
                Context_.getApplicationContext().deleteFile(file.getName());
            }
    }
}
}
