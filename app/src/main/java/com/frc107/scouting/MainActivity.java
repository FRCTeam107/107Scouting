package com.frc107.scouting;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.frc107.scouting.core.analysis.AnalysisActivity;
import com.frc107.scouting.core.concat.ConcatActivity;
import com.frc107.scouting.match.sandstorm.SandstormActivity;
import com.frc107.scouting.pit.PitActivity;
import com.frc107.scouting.core.ui.BaseActivity;
import com.frc107.scouting.core.utils.PermissionUtils;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.frc107.scouting.core.utils.StringUtils;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;

import android.widget.Toast;

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
        SharedPreferences pref = getSharedPreferences(ScoutingStrings.PREFERENCES_NAME, MODE_PRIVATE);

        String eventKey = pref.getString(ScoutingStrings.EVENT_KEY_PREFERENCE, "");
        Scouting.getInstance().setEventKey(eventKey);

        String initials = pref.getString(ScoutingStrings.USER_INITIALS_PREFERENCE, "");
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
            case R.id.match:
                drawerLayout.closeDrawer(GravityCompat.START);
                tryToGoToMatch();
                break;
            case R.id.concat:
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, ConcatActivity.class));
                break;
            case R.id.send_pit_photos:
                sendPitPhotos();
                break;
            case R.id.attribute_analysis:
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, AnalysisActivity.class));
                break;
            case R.id.settings:
                break;
        }
        return true;
    }

    private void sendPitPhotos() {
        if (!PermissionUtils.checkForPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/jpeg");
        intent.setPackage("com.android.bluetooth");

        ArrayList<Uri> uriList = (ArrayList<Uri>) Scouting.getFileService().getPhotoUriList();
        if (uriList.isEmpty()) {
            Toast.makeText(this, "No photos to send.", Toast.LENGTH_SHORT).show();
        } else {
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
            startActivity(Intent.createChooser(intent, "Share app"));
        }
    }

    public void onMainScreenTap(View view) {
        tryToGoToMatch();
    }

    private void tryToGoToPit() {
        String initials = Scouting.getInstance().getUserInitials();
        if (!StringUtils.isEmptyOrNull(initials)) {
            startActivity(new Intent(this, PitActivity.class));
            return;
        }

        showInitialsDialog(() -> startActivity(new Intent(this, PitActivity.class)));
    }

    private void tryToGoToMatch() {
        String initials = Scouting.getInstance().getUserInitials();
        if (!StringUtils.isEmptyOrNull(initials)) {
            startActivity(new Intent(this, SandstormActivity.class));
            return;
        }

        showInitialsDialog(() -> startActivity(new Intent(this, SandstormActivity.class)));
    }

    public void tryToGoToSendDataScreen(View view) {
        tryToGoToSendDataScreen();
    }
}
