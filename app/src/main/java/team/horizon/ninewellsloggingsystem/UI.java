package team.horizon.ninewellsloggingsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.itextpdf.text.pdf.AcroFields;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public void LaunchNewIntent(Class class_){
        Intent intent = new Intent(Context_, class_);
        Context_.startActivity(intent);
    }

    public HashMap<String, EditText> GenerateEditText(Map<String, AcroFields.Item> data, LinearLayout layout){
        HashMap<String, EditText> fieldData = new HashMap<String, EditText>();
        for (String field:data.keySet()) {
            if(field.equals("Signature"))
                continue;
            EditText element = new EditText(Context_);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            element.setLayoutParams(p);
            element.setText(field);
            int id = (int) System.currentTimeMillis();
            element.setId(id);
            layout.addView(element);
            fieldData.put(field, element);
        }
        return fieldData;
    }

    public void SendToast(String text){
        Toast.makeText(Activity_, text,
                Toast.LENGTH_LONG).show();
    }
}
