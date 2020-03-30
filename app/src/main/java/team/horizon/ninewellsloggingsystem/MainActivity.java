package team.horizon.ninewellsloggingsystem;

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
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    Button submitBtn, signBtn;
    Spinner formSpinner;
    EditText editText;

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
        populateSpinner();

        submitBtn.setOnClickListener(new View.OnClickListener() {
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


    private void addInputToForm(String copyFormPath) throws IOException, DocumentException {
        PdfReader readerOriginalDoc = new PdfReader(copyFormPath);
        PdfStamper stamper = new PdfStamper(readerOriginalDoc,new FileOutputStream("/sdcard/Download/newStamper.pdf"));
        PdfContentByte content = stamper.getOverContent(1);
        Image image = Image.getInstance("/sdcard/Download/last_sig.bmp");
        image.scaleAbsolute(500, 200);
        image.setAbsolutePosition(20, 30);
        image.setAnnotation(new Annotation(0, 0, 0, 0, 3));
        content.addImage(image);
        stamper.close();
    }


    private void createPdf2(){
        Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/Download/last_sig.bmp");


        PdfDocument document = new PdfDocument();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();

        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        Rect rectangle = new Rect(0,0,100,100);
        canvas.drawBitmap(bitmap, null, rectangle, paint);
        document.finishPage(page);

        String directory_path = "/sdcard/Download";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"/form" + Calendar.getInstance().getTime()+".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }

    private void initializeUI() {
        submitBtn = findViewById(R.id.submit);
        signBtn = findViewById(R.id.sign);
        formSpinner = findViewById(R.id.form);
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
