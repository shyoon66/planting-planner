package com.yoonbae.planting.planner.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public abstract class PlannerUtils {
    public static Uri getUriFromPath(ContentResolver resolver, String filePath) {
        long photoId;
        Uri photoUri = MediaStore.Images.Media.getContentUri("external");
        String[] projection = {MediaStore.Images.ImageColumns._ID};
        Cursor cursor = resolver.query(photoUri, projection, MediaStore.Images.ImageColumns.DATA + " LIKE ?", new String[] { filePath }, null);
        assert cursor != null;
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(projection[0]);
        photoId = cursor.getLong(columnIndex);

        cursor.close();
        return Uri.parse(photoUri.toString() + "/" + photoId);
    }
}
