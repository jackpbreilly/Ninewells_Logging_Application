package team.horizon.ninewellsloggingsystem;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.core.content.FileProvider;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static android.os.Environment.getExternalStorageDirectory;
import static com.itextpdf.text.pdf.PdfVisibilityExpression.OR;

class Listeners {

    HashMap<String, EditText> EditTextFieldsData = new HashMap<String, EditText>();

    public HashMap<String, EditText> getEditTextFieldsData() {
        return EditTextFieldsData;
    }

    public void SignaturePadClear(Button btn, final com.github.gcacace.signaturepad.views.SignaturePad signaturePad){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signaturePad.clear();
            }
        });
    }


    public void MainActivitySubmitForm(Button btn, final PDF pdf, final AccessFirebase Firebase, final Spinner spinner, final com.github.gcacace.signaturepad.views.SignaturePad signaturePad, final FileManager saveSignature, final UI UI_){
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    saveSignature.SaveSignature(signaturePad, UI_);
                    // creates pdf on device
                    String fileToUploadToFirebase = pdf.CreateNewPDF(Environment.getExternalStorageDirectory().getPath() +"/Download/Forms/"+ spinner.getSelectedItem().toString(), EditTextFieldsData,Environment.getExternalStorageDirectory().getPath() + "/Download/last_sig.bmp");
                   // uploads local pdf to firebase
                    if (fileToUploadToFirebase != ""){
                        Firebase.UploadFileToFirebaseStorage(fileToUploadToFirebase,UI_);
                       // deletes file after uploaded to firebase
                        saveSignature.deleteFile(fileToUploadToFirebase);
                        saveSignature.deleteFile(Environment.getExternalStorageDirectory().getPath() +"/Download/last_sig.bmp");
                    }
                    else{
                        UI_.SendToast("Error with Inputted Data");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }

            }

        });
    }


    // shows input boxes to display on change of spinner
    public void MainActivitySpinnerChange(final Spinner spinner, final UI UI_, final PDF Pdf, final LinearLayout layout){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    layout.removeAllViews();
                    EditTextFieldsData = UI_.GenerateEditText(Pdf.getFieldsInForm(Environment.getExternalStorageDirectory().getPath() + "/Download/Forms/" + spinner.getSelectedItem().toString()), layout);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
    }

    // opens select form in local pdf reader
    public void MainActivityViewForm(Button btn, final Spinner pdfname, final Context context) {

    btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            File path = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/Forms/");
            File file = new File(path, pdfname.getSelectedItem().toString());

            // Get URI and MIME type of file
            Uri uri = FileProvider.getUriForFile(context, "team.horizon.ninewellsloggingsystem" + ".fileprovider", file);
            String mime = context.getContentResolver().getType(uri);

            // Open file with user selected app
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, mime);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        }
    });
}
}
