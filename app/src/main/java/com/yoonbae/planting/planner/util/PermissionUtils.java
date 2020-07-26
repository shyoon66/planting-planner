package com.yoonbae.planting.planner.util;

import android.Manifest;
import android.app.Activity;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

public abstract class PermissionUtils {
    public static PermissionType request(Activity activity) {
        final PermissionType[] permissionType = new PermissionType[1];
        Dexter.withActivity(activity)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            permissionType[0] = PermissionType.GRANTED;
                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            permissionType[0] = PermissionType.DENIED;
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(error -> Toast.makeText(activity, "Error occurred! " + error.toString(), Toast.LENGTH_SHORT).show())
                .check();

        return permissionType[0];
    }

    public static PermissionType vibrationRequest(Activity activity) {
        final PermissionType[] permissionType = new PermissionType[1];
        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.ACCESS_NOTIFICATION_POLICY)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        permissionType[0] = PermissionType.GRANTED;
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        permissionType[0] = PermissionType.DENIED;
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(error -> Toast.makeText(activity, "Error occurred! " + error.toString(), Toast.LENGTH_SHORT).show())
                .check();

        return permissionType[0];
    }
}
