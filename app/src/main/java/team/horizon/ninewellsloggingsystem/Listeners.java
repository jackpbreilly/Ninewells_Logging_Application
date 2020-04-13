package team.horizon.ninewellsloggingsystem;

import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
                    String fileToUploadToFirebase = pdf.CreateNewPDF(Environment.getExternalStorageDirectory().getPath() +"/Download/Forms/"+ spinner.getSelectedItem().toString(), EditTextFieldsData,Environment.getExternalStorageDirectory().getPath() + "/Download/last_sig.bmp");
                    if (fileToUploadToFirebase != ""){
                        Firebase.UploadFileToFirebaseStorage(fileToUploadToFirebase,UI_);
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

    public void MainActivitySpinnerChange(final Spinner spinner, final UI UI_, final PDF Pdf, final LinearLayout layout){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    layout.removeAllViews();
                    EditTextFieldsData = UI_.GenerateEditText(Pdf.getFieldsInForm("sdcard/Download/Forms/" + spinner.getSelectedItem().toString()), layout);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
    }
}
