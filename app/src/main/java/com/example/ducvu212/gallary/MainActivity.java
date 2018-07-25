package com.example.ducvu212.gallary;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final int READ_STORAGE_PERMISSION = 111;
    private boolean mIsSdCard;
    public static ArrayList<String> sPaths;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        findViewByIds();
        initComponents();
        if (mIsSdCard) {
            loadImages(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        loadImages(MediaStore.Images.Media.INTERNAL_CONTENT_URI);
    }

    private ArrayList<String> loadImages(Uri uri) {
        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media.DATE_ADDED;
        Cursor cursor = getContentResolver().query(uri, columns, null, null, orderBy);
        int count = cursor.getCount();
        String[] arrPath = new String[count];
        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            arrPath[i] = cursor.getString(dataColumnIndex);
        }
        cursor.close();
        sPaths.addAll(Arrays.asList(arrPath));
        return sPaths;
    }

    private void initComponents() {
        mIsSdCard = android.os.Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
        sPaths = new ArrayList<>();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_main, GridFragment.newInstance());
        transaction.commit();
    }

    private void findViewByIds() {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                    READ_STORAGE_PERMISSION);
        }
    }
}
