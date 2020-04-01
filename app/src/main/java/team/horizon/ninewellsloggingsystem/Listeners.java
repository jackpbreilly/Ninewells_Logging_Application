package team.horizon.ninewellsloggingsystem;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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

    public void MainActivitySubmitForm(Button btn, final PDF pdf, final AccessFirebase Firebase, final HashMap<String, EditText> fieldsData){
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    String fileToUploadToFirebase = pdf.CreateNewPDF("sdcard/Download/forms/ExampleForm.pdf", fieldsData,"sdcard/Download/last_sig.bmp");
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
                UI_.LaunchNewIntent(SignaturePad.class);
            }
        });
    }
}
