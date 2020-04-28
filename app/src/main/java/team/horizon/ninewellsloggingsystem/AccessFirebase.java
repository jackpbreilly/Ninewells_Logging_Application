package team.horizon.ninewellsloggingsystem;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

class AccessFirebase {
// This will likely be deleted from the project !*
    private Activity Activity_;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://ninewells-logging-system.appspot.com/");

    public AccessFirebase(Activity activity_) {
        Activity_ = activity_;
    }


    public void UploadFileToFirebaseStorage(String pathOfFileToUpload, final UI toast){

        Uri file = Uri.fromFile(new File(pathOfFileToUpload));
        StorageReference reference = this.storageRef.child(String.valueOf(System.currentTimeMillis())+".pdf");
        UploadTask uploadTask = reference.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                toast.SendToast("Unsuccessfully uploaded PDF to Server: " + exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                toast.SendToast("Successfully uploaded PDF to Server");

            }
        });
    }
}
