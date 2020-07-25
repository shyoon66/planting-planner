package com.yoonbae.planting.planner;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;
import java.util.Optional;

public class OpenSourceLicenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_source_license);
        initToolBar();
        initActionBar();
        Intent intent = getIntent();
        String openSourceName = intent.getStringExtra("openSourceName");
        initToolBarTitle(openSourceName);
        String license = getLicense(intent);
        initLicenseTextView(license);
    }

    private String getLicense(Intent intent) {
        String license = Optional.ofNullable(intent.getStringExtra("license")).orElse("");
        return license.replaceAll("\n", Objects.requireNonNull(System.getProperty("line.separator")));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(OpenSourceLicenseActivity.this, OpenSourceListActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initToolBarTitle(String openSourceName) {
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(openSourceName);
    }

    private void initLicenseTextView(String license) {
        TextView licenseTextView = findViewById(R.id.license);
        licenseTextView.setText(license);
        licenseTextView.setMovementMethod(new ScrollingMovementMethod());
    }
}