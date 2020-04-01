package team.horizon.ninewellsloggingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


import com.itextpdf.text.DocumentException;

import java.io.IOException;

import static com.itextpdf.text.Image.getInstance;

public class MainActivity extends AppCompatActivity {

    Button submitBtn, signBtn;
    Spinner formSelectionSpinner;

    EditText jobNo, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Checking and Requesting Permissions
        Permissions AppPermissions = new Permissions(this, this);
        UI UI_ = new UI(this, this);
        FileManager FileInformation_ = new FileManager(this);
        AccessFirebase Firebase = new AccessFirebase(this);
        BuildPdf Pdf = new BuildPdf();
        Listeners MainActivityListeners = new Listeners();

        initialiseUI();


        UI_.PopulateSpinner(FileInformation_.FileSearch("sdcard/Download/forms"), formSelectionSpinner, R.layout.support_simple_spinner_dropdown_item);
        MainActivityListeners.MainActivitySubmitForm(submitBtn, Pdf, Firebase);
        MainActivityListeners.MainActivityLaunchSignaturePad(signBtn, UI_);
    }

    private void initialiseUI() {
        submitBtn = findViewById(R.id.submit);
        signBtn = findViewById(R.id.sign);
        formSelectionSpinner = findViewById(R.id.form);
        jobNo = findViewById(R.id.jobNo);
        name = findViewById(R.id.userName);
    }


}
