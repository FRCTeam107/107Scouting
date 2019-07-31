package com.frc107.scouting;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.frc107.scouting.analysis.attribute.AttributeAnalysisActivity;
import com.frc107.scouting.form.eTable;
import com.frc107.scouting.pit.PitActivity;
import com.frc107.scouting.utils.FileService;
import com.frc107.scouting.utils.PermissionUtils;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.frc107.scouting.utils.StringUtils;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;

import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        checkForPermissions();

        initializeSettings();
    }

    private void initializeSettings() {
        SharedPreferences pref = getSharedPreferences(Scouting.PREFERENCES_NAME, MODE_PRIVATE);

        String eventKey = pref.getString(Scouting.EVENT_KEY_PREFERENCE, "");
        Scouting.getInstance().setEventKey(eventKey);

        String initials = pref.getString(Scouting.USER_INITIALS_PREFERENCE, "");
        Scouting.getInstance().setUserInitials(initials);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.pit:
                drawerLayout.closeDrawer(GravityCompat.START);
                tryToGoToPit();
                break;
            case R.id.concat:
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, ConcatActivity.class));
                break;
            case R.id.send_match_data:
                sendMatchData();
                break;
            case R.id.send_pit_data:
                sendPitData();
                break;
            case R.id.send_concat_match_data:
                sendConcatMatchData();
                break;
            case R.id.send_concat_pit_data:
                sendConcatPitData();
                break;
            case R.id.send_pit_photos:
                sendPitPhotos();
                break;
            case R.id.attribute_analysis:
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, AttributeAnalysisActivity.class));
                break;
            case R.id.settings:
                break;
        }
        return true;
    }

    private void sendMatchData() {
        //File file = Scouting.FILE_SERVICE.getMatchFile(false);
        //sendFile(file);
    }

    private void sendPitData() {
        //File file = Scouting.FILE_SERVICE.getPitFile(false);
        //sendFile(file);
    }

    private void sendConcatMatchData() {
        //File file = Scouting.FILE_SERVICE.getMatchFile(true);
        //sendFile(file);
    }

    private void sendConcatPitData() {
        //File file = Scouting.FILE_SERVICE.getPitFile(true);
        //sendFile(file);
    }

    private void sendPitPhotos() {
        if (!PermissionUtils.checkForPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/jpeg");
        intent.setPackage("com.android.bluetooth");

        ArrayList<Uri> uriList = (ArrayList<Uri>) Scouting.FILE_SERVICE.getPhotoUriList();
        if (uriList.isEmpty()) {
            Toast.makeText(this, "No photos to send.", Toast.LENGTH_SHORT).show();
        } else {
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
            startActivity(Intent.createChooser(intent, "Share app"));
        }
    }

    public void onMainScreenTap(View view) {
        tryToGoToPit();
    }

    private void tryToGoToPit() {
        String initials = Scouting.getInstance().getUserInitials();
        if (!StringUtils.isEmptyOrNull(initials)) {
            startActivity(new Intent(this, PitActivity.class));
            return;
        }

        showInitialsDialog(() -> startActivity(new Intent(this, PitActivity.class)));
    }

    public void tryToSendData(View view) {
        /*
         * TODO:
         * ask user which file to send
         *
         * show listview of filedefinitions sorted by date (new at top)
         * send whichever one is tapped
         */

        String initials = Scouting.getInstance().getUserInitials();
        if (!StringUtils.isEmptyOrNull(initials)) {
            File file = Scouting.FILE_SERVICE.getMostRecentFileDefinition(eTable.PIT, initials).getFile();
            sendFile(file);
            return;
        }

        showInitialsDialog(() -> {
            File file = Scouting.FILE_SERVICE.getMostRecentFileDefinition(eTable.PIT, initials).getFile();
            sendFile(file);
        });
    }
}
