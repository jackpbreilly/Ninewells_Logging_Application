package team.horizon.ninewellsloggingsystem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SignaturePad extends AppCompatActivity {

    private com.github.gcacace.signaturepad.views.SignaturePad signaturePad;
    Button saveBtn, clearBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signature_pad_activity);

        FileManager FileManager_ = new FileManager(this);

        initialiseUI();

        Listeners SignaturePadListeners = new Listeners();
        SignaturePadListeners.SignaturePadClear(clearBtn, signaturePad);
        SignaturePadListeners.SignaturePadSave(saveBtn, signaturePad, FileManager_);
    }


    private void initialiseUI() {
        saveBtn = findViewById(R.id.save);
        clearBtn = findViewById(R.id.clear);
        signaturePad = findViewById(R.id.signature_pad);
    }
}
