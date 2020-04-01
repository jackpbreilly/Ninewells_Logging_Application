package team.horizon.ninewellsloggingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.itextpdf.text.Image.getInstance;

public class MainActivity extends AppCompatActivity {

    Button submitBtn, signBtn;
    Spinner formSelectionSpinner;
    LinearLayout EditTextLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Checking and Requesting Permissions
        Permissions AppPermissions = new Permissions(this, this);
        UI UI_ = new UI(this, this);
        FileManager FileInformation_ = new FileManager(this);
        AccessFirebase Firebase = new AccessFirebase(this);
        PDF Pdf = new PDF();
        Listeners MainActivityListeners = new Listeners();

        initialiseUI();
        UI_.PopulateSpinner(FileInformation_.FileSearch("sdcard/Download/forms"), formSelectionSpinner, R.layout.support_simple_spinner_dropdown_item);

        HashMap<String, EditText> EditTextFieldsData = new HashMap<String, EditText>();
        try {
            EditTextFieldsData = UI_.GenerateEditText(Pdf.getFieldsInForm("sdcard/Download/forms/ExampleForm.pdf"), EditTextLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MainActivityListeners.MainActivitySubmitForm(submitBtn, Pdf, Firebase, EditTextFieldsData);
        MainActivityListeners.MainActivityLaunchSignaturePad(signBtn, UI_);
    }

    private void initialiseUI() {
        submitBtn = findViewById(R.id.submit);
        signBtn = findViewById(R.id.sign);
        formSelectionSpinner = findViewById(R.id.form);
        EditTextLayout = (LinearLayout) findViewById(R.id.EditTextLayout);
    }


}
