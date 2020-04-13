package team.horizon.ninewellsloggingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static com.itextpdf.text.Image.getInstance;

public class MainActivity extends AppCompatActivity {

    Button submitBtn, clearBtn;
    Spinner formSelectionSpinner;
    LinearLayout EditTextLayout;
    private com.github.gcacace.signaturepad.views.SignaturePad signaturePad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // Checking and Requesting Permissions
        Permissions AppPermissions = new Permissions(this, this);
        UI UI_ = new UI(this, this);
        FileManager FileInformation_ = new FileManager(this);
        AccessFirebase Firebase = new AccessFirebase(this);
        PDF Pdf = new PDF();
        Listeners MainActivityListeners = new Listeners();
        initialiseUI();

            FileInformation_.moveAssetToStorageDir("forms");


        UI_.PopulateSpinner(FileInformation_.FileSearch("sdcard/Download/Forms"), formSelectionSpinner, R.layout.support_simple_spinner_dropdown_item);

        MainActivityListeners.SignaturePadClear(clearBtn, signaturePad);
        MainActivityListeners.MainActivitySpinnerChange(formSelectionSpinner, UI_, Pdf, EditTextLayout);
        MainActivityListeners.MainActivitySubmitForm(submitBtn, Pdf, Firebase, formSelectionSpinner, signaturePad, FileInformation_, UI_);
    }

    private void initialiseUI() {
        submitBtn = findViewById(R.id.submit);
        clearBtn = findViewById(R.id.clear);
        formSelectionSpinner = findViewById(R.id.form);
        signaturePad = findViewById(R.id.signature_pad);
        EditTextLayout = (LinearLayout) findViewById(R.id.EditTextLayout);

    }


}
