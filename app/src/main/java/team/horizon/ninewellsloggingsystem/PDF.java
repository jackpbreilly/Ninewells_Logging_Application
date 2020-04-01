package team.horizon.ninewellsloggingsystem;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PushbuttonField;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class PDF {


    // Clones PDF and returns new path and name
    public String CreateNewPDF(String pathToFileToClone, HashMap<String, EditText> fieldsData, String pathToSignature) throws IOException, DocumentException {
        PdfReader readerOriginalDoc = new PdfReader(pathToFileToClone);
        // Creates New PDF Named: [Current_MilliSec].pdf
        String documentNameAndPath = "/sdcard/Download/" +System.currentTimeMillis() + ".pdf";
        PdfStamper stamper = new PdfStamper(readerOriginalDoc,new FileOutputStream(documentNameAndPath));
        AddInputToForm(stamper, fieldsData, pathToSignature);

        stamper.close();

        return documentNameAndPath;
    }

    public void AddInputToForm(PdfStamper PDF, HashMap<String, EditText> fieldsData, String pathToSignature) throws IOException, DocumentException {
        AcroFields fields = PDF.getAcroFields();

        for(Map.Entry<String, EditText> field : fieldsData.entrySet()) {
            String key = field.getKey();
            EditText value = field.getValue();
            fields.setField(key, String.valueOf(value.getText()));
        }

        // Adding signature to form
        PushbuttonField signature = fields.getNewPushbuttonFromField("Signature");
        signature.setLayout(PushbuttonField.LAYOUT_ICON_ONLY);
        signature.setProportionalIcon(true);
        signature.setImage(Image.getInstance(pathToSignature));
        fields.replacePushbuttonField("Signature", signature.getField());
        PDF.close();
    }

    public Map<String, AcroFields.Item> getFieldsInForm(String PDFPath) throws IOException {
        PdfReader PDF = new PdfReader(PDFPath);
        AcroFields fields = PDF.getAcroFields();
        Map<String, AcroFields.Item> fieldNames = fields.getFields();
        return fieldNames;
    }
}
