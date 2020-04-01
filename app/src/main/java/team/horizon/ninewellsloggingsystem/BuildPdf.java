package team.horizon.ninewellsloggingsystem;

import android.app.Activity;
import android.os.Build;
import android.view.View;

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

class BuildPdf {


    // Clones PDF and returns new path and name
    public String CreateNewPDF(String pathToFileToClone, String jobNo, String name, String date, String pathToSignature) throws IOException, DocumentException {
        PdfReader readerOriginalDoc = new PdfReader(pathToFileToClone);
        // Creates New PDF Named: [Current_MilliSec].pdf
        String documentNameAndPath = "/sdcard/Download/" +System.currentTimeMillis() + ".pdf";
        PdfStamper stamper = new PdfStamper(readerOriginalDoc,new FileOutputStream(documentNameAndPath));
        AddInputToForm(stamper, jobNo, name,date, pathToSignature);

        stamper.close();

        return documentNameAndPath;
    }

    public void AddInputToForm(PdfStamper PDF, String jobNo, String name, String date, String pathToSignature) throws IOException, DocumentException {
        AcroFields fields = PDF.getAcroFields();

        // Pass in map?????? LOOK INTO THIS!!!!!***
        fields.setField("jobId", jobNo);
        fields.setField("Name", name);
        fields.setField("Date", date);

        // Adding signature to form
        PushbuttonField signature = fields.getNewPushbuttonFromField("Signature");
        signature.setLayout(PushbuttonField.LAYOUT_ICON_ONLY);
        signature.setProportionalIcon(true);
        signature.setImage(Image.getInstance(pathToSignature));
        fields.replacePushbuttonField("Signature", signature.getField());
        PDF.close();

    }
}
