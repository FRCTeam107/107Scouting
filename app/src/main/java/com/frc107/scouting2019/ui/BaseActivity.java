package com.frc107.scouting2019.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.frc107.scouting2019.BuildConfig;
import com.frc107.scouting2019.MainActivity;
import com.frc107.scouting2019.R;
import com.frc107.scouting2019.Scouting;
import com.frc107.scouting2019.admin.AdminActivity;
import com.frc107.scouting2019.utils.PermissionUtils;

import java.io.File;

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
                sendFile(Scouting.FILE_UTILS.getMatchFile());
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
}
