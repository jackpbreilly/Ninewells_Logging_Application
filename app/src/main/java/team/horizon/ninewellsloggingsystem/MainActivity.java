package team.horizon.ninewellsloggingsystem;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.itextpdf.text.Annotation;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PushbuttonField;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.itextpdf.text.Image.getInstance;

public class MainActivity extends AppCompatActivity {

    Button submitBtn, signBtn;
    Spinner formSpinner;

    EditText jobNo, name;

    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();
        initializeUI();
        //populateSpinner();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                try {
                    addInputToForm("/sdcard/Download/forms/ExampleForm.pdf");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }

            }
        });

        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSignaturePad();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addInputToForm(String copyFormPath) throws IOException, DocumentException {
        PdfReader readerOriginalDoc = new PdfReader(copyFormPath);
        PdfStamper stamper = new PdfStamper(readerOriginalDoc,new FileOutputStream("/sdcard/Download/newStamper.pdf"));
        AcroFields form = stamper.getAcroFields();
        form.setField("jobId", String.valueOf(jobNo.getText()));
        form.setField("Name", String.valueOf(name.getText()));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();

        form.setField("Date", String.valueOf(dtf.format(localDate)));
        PushbuttonField signature = form.getNewPushbuttonFromField("Signature");
        signature.setLayout(PushbuttonField.LAYOUT_ICON_ONLY);
        signature.setProportionalIcon(true);
        signature.setImage(Image.getInstance("/sdcard/Download/last_sig.bmp"));
        form.replacePushbuttonField("Signature", signature.getField());
        stamper.close();
    }



    private void initializeUI() {
        submitBtn = findViewById(R.id.submit);
        signBtn = findViewById(R.id.sign);
        formSpinner = findViewById(R.id.form);
        jobNo = findViewById(R.id.jobNo);
        name = findViewById(R.id.userName);
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
            return;
        }
    }

    private void launchSignaturePad(){
        Intent intent = new Intent(MainActivity.this, SignaturePad.class);
        startActivity(intent);
    }

    private void populateSpinner(){
        ArrayList<String> spinnerData = new ArrayList<>();
        String path = "/sdcard/Download/forms";
        File directory = new File(path);
        File[] files = directory.listFiles();
        if(files != null) {

            for (int i = 0; i < files.length; i++) {
                spinnerData.add(files[i].getName().toString());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinnerData);
            arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            formSpinner.setAdapter(arrayAdapter);
        }
    }

}
