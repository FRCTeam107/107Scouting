package com.frc107.scouting;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.frc107.scouting.analysis.attribute.AttributeAnalysisActivity;
import com.frc107.scouting.analysis.team.TeamAnalysisActivity;
import com.frc107.scouting.pit.PitActivity;
import com.frc107.scouting.utils.PermissionUtils;

import android.provider.Settings;
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

        // TODO: load records of local scouting files

        checkForPermissions();

        initializeSettings();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initializeSettings() {
        // todo: dont' think we need this anyumore, just use initial sand time stanp for filenames
        String uniqueId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Scouting.getInstance().setUniqueId(uniqueId);

        SharedPreferences pref = getSharedPreferences(Scouting.PREFERENCES_NAME, MODE_PRIVATE);
        String eventKey = pref.getString(Scouting.EVENT_KEY_PREFERENCE, "");
        Scouting.getInstance().setEventKey(eventKey);
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
                startActivity(new Intent(this, PitActivity.class));
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
            case R.id.team_analysis:
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, TeamAnalysisActivity.class));
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

    public void goToPit(View view) {
        String initials = Scouting.getInstance().getUserInitials();
        if (!StringUtils.isEmptyOrNull(initials)) {
            startActivity(new Intent(this, PitActivity.class));
            return;
        }

        showInitialsDialog(value -> {
                    if (StringUtils.isEmptyOrNull(value)) {
                        return "Can't use empty initials.";
                    }

                    Scouting.getInstance().setUserInitials(value);
                    startActivity(new Intent(this, PitActivity.class));
                    return null;
                }, "Cannot scout with empty initials.");
    }
}
