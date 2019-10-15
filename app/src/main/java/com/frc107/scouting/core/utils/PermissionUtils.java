package com.frc107.scouting.core.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtils {
    private PermissionUtils() {}

    public static boolean checkForPermission(Activity activity, String type) {
        int permission = ContextCompat.checkSelfPermission(activity, type);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{type}, 1);
        }
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean verifyWritePermissions(Activity activity) {
        boolean hasWritePermissions = PermissionUtils.checkForPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWritePermissions)
            return true;

        Toast.makeText(activity.getApplicationContext(), "No write permissions.", Toast.LENGTH_LONG).show();
        return false;
    }
}
