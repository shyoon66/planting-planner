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
        Intent intent = getIntent();
        String openSourceName = intent.getStringExtra("openSourceName");
        initActionBar();
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(openSourceName);
        String license = Optional.ofNullable(intent.getStringExtra("license")).orElse("");
        license = license.replaceAll("\n", Objects.requireNonNull(System.getProperty("line.separator")));
        TextView licenseTextView = findViewById(R.id.license);
        licenseTextView.setText(license);
        licenseTextView.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
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
}