package com.frc107.scouting;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.frc107.scouting.admin.AdminActivity;
import com.frc107.scouting.callbacks.ICallback;
import com.frc107.scouting.callbacks.ICallbackWithParam;
import com.frc107.scouting.callbacks.ICallbackWithParamAndResult;
import com.frc107.scouting.pit.PitActivity;
import com.frc107.scouting.utils.PermissionUtils;
import com.frc107.scouting.utils.StringUtils;
import com.frc107.scouting.utils.ViewUtils;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_activity:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.admin_activity:
                startActivity(new Intent(this, AdminActivity.class));
                finish();
                return true;
            case R.id.send_data:
                sendData();
                return true;
            case R.id.end_shift:
                showInitialsDialog(() -> { });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendData() {
        File matchFile = Scouting.FILE_SERVICE.getFile("ConcatenatedMatch.csv");
        if (matchFile != null)
            sendFile(matchFile);
    }

    protected void checkForPermissions() {
        int writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int internetPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if (writePermission != PackageManager.PERMISSION_GRANTED ||
                cameraPermission != PackageManager.PERMISSION_GRANTED ||
                readPermission != PackageManager.PERMISSION_GRANTED ||
                internetPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.INTERNET
            }, 1);
        }
    }

    public void sendFile(File file) {
        if (file == null) {
            Toast.makeText(this, "File does not exist.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(PermissionUtils.checkForPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.setPackage("com.android.bluetooth");
            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file));
            startActivity(Intent.createChooser(intent, "Share app"));
        }
    }

    public void showInitialsDialog(ICallback onFinish) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Enter initials:");
        final EditText editText = new EditText(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(layoutParams);

        alertBuilder.setView(editText);
        alertBuilder.setPositiveButton("OK", (dialog, which) -> {
            String text = editText.getText().toString();
            if (text.length() == 0) {
                showMessage("Cannot use empty initials. Try again.", Toast.LENGTH_SHORT);
                return;
            }

            Scouting.getInstance().setUserInitials(text);

            SharedPreferences pref = getApplicationContext().getSharedPreferences(Scouting.USER_INITIALS_PREFERENCE, MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = pref.edit();
            prefEditor.putString(Scouting.EVENT_KEY_PREFERENCE, text);
            prefEditor.apply();

            onFinish.call();
        });
        alertBuilder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });
        alertBuilder.setOnCancelListener(dialog -> {
            dialog.dismiss();
            showMessage("No initials set.", Toast.LENGTH_SHORT);
        });
        alertBuilder.show();
    }

    protected void showMessage(String message, int length) {
        Toast.makeText(this, message, length).show();
    }

    public void focusOnView(int viewId) {
        ViewUtils.requestFocusToUnfinishedQuestion(findViewById(viewId), this);
    }
}
