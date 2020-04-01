package team.horizon.ninewellsloggingsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.ResourceBundle;

class UI {

    private Context Context_;
    private Activity Activity_;

    public UI(Context context, Activity activity) {
        Context_ = context;
        Activity_ = activity;
    }

    public void PopulateSpinner(ArrayList<String> data, Spinner spinnerEl, int resource){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Context_, resource, data);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerEl.setAdapter(arrayAdapter);
    }
    public void launchNewIntent(Class class_){
        Intent intent = new Intent(Context_, class_);
        Context_.startActivity(intent);
    }

}
