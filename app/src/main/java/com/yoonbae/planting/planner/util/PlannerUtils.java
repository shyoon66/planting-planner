package com.yoonbae.planting.planner.util;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class PlannerUtils {
    public static String getFilePathFromURI(Context context, Uri contentUri) throws IOException {
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(context.getFilesDir() + File.separator + fileName);
            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    private static String getFileName(Uri uri) {
        if (uri == null) {
            return null;
        }
        String fileName = null;
        String path = uri.getPath();
        assert path != null;
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    private static void copy(Context context, Uri srcUri, File dstFile) throws IOException {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) {
                return;
            }
            OutputStream outputStream = new FileOutputStream(dstFile);
            IOUtils.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            throw new IOException("이미지 파일을 찾을 수 없습니다.");
        }
    }
}
