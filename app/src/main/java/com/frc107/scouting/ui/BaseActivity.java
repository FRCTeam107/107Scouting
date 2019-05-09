package com.frc107.scouting.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.frc107.scouting.BuildConfig;
import com.frc107.scouting.MainActivity;
import com.frc107.scouting.R;
import com.frc107.scouting.Scouting;
import com.frc107.scouting.admin.AdminActivity;
import com.frc107.scouting.utils.PermissionUtils;
import com.frc107.scouting.utils.ViewUtils;

import java.io.File;
import java.io.FileNotFoundException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

public class BaseActivity extends AppCompatActivity {
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
                finish();
                return true;
            case R.id.admin_activity:
                startActivity(new Intent(this, AdminActivity.class));
                finish();
                return true;
            case R.id.send_data:
                sendData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendData() {
        File matchFile = Scouting.FILE_UTILS.getMatchFile();
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

        if(PermissionUtils.getPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.setPackage("com.android.bluetooth");
            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file));
            startActivity(Intent.createChooser(intent, "Share app"));
        }
    }

    public void focusOnView(int viewId) {
        ViewUtils.requestFocusToUnfinishedQuestion(findViewById(viewId), this);
    }
}
