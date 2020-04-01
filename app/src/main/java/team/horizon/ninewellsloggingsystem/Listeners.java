package team.horizon.ninewellsloggingsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

class Listeners {


    public void SignaturePadClear(Button btn, final com.github.gcacace.signaturepad.views.SignaturePad signaturePad){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signaturePad.clear();
            }
        });
    }

    public void SignaturePadSave(Button btn, final com.github.gcacace.signaturepad.views.SignaturePad signaturePad, final FileManager saveSignature){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSignature.SaveSignature(signaturePad);
            }
        });
    }

    public void MainActivitySubmitForm(Button btn, final BuildPdf pdf, final AccessFirebase Firebase ){
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    String fileToUploadToFirebase = pdf.CreateNewPDF("sdcard/Download/forms/ExampleForm.pdf", "1","Jack","23/01/01","sdcard/Download/last_sig.bmp");
                    Firebase.UploadFileToFirebaseStorage(fileToUploadToFirebase);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }

            }

        });
    }
    public void MainActivityLaunchSignaturePad(Button btn, final UI UI_){
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UI_.launchNewIntent(SignaturePad.class);
            }
        });
    }
}
