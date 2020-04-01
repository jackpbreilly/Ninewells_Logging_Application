// Class used to check and request user permissions

package team.horizon.ninewellsloggingsystem;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

public class Permissions {

    private Context Context_;
    private Activity Activity_;

    // Permissions which will be checked and requested
    private String[] Permissions_ = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
    };

    public Permissions(Context context, Activity activity) {
        this.Context_ = context;
        this.Activity_ = activity;
        CheckPermissions();
    }

    private boolean CheckPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : this.Permissions_) {
            result = ContextCompat.checkSelfPermission(Context_, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(Activity_, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    private void onRequestPermissionsResult(int requestCode, String Permissions_[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
            return;
        }
    }
}
