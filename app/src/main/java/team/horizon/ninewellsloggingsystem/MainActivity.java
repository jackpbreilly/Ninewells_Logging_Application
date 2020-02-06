package team.horizon.ninewellsloggingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchSignaturePad(View v){
        Intent intent = new Intent(MainActivity.this, SignaturePad.class);
        startActivity(intent);
    }
}
