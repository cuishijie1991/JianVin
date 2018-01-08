package com.csj.jianvin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.csj.jianvin.view.CameraPreview;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by cuishijie on 2018/1/7.
 */

public class CameraActivity extends FragmentActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    public static final String TAG = CameraPreview.class.getSimpleName();
    private Context mContext = this;
    private CameraPreview mPreview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mPreview = new CameraPreview(mContext);
        FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
        preview.addView(mPreview);
        SettingFragment.passCamera(mPreview.getCamera());
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SettingFragment.setDefault(PreferenceManager.getDefaultSharedPreferences(this));
        SettingFragment.init(PreferenceManager.getDefaultSharedPreferences(this));
    }

    public void onSettingClick(View view) {
        getFragmentManager().beginTransaction()
                .replace(R.id.cameraPreview, new SettingFragment())
                .addToBackStack(null)
                .commit();
    }

    public void onTakePicture(View view) {
        mPreview.takePicture(getOutputMediaFile(MEDIA_TYPE_IMAGE), new CameraPreview.OnTakePictureCallback() {
            @Override
            public void onTakePicSuccess() {
                ImageView preview = (ImageView) findViewById(R.id.media_preview);
                preview.setImageURI(outputMediaFileUri);
            }
        });
    }

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private Uri outputMediaFileUri;
    private String outputMediaFileType;

    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), TAG);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
            outputMediaFileType = "image/*";
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
            outputMediaFileType = "video/*";
        } else {
            return null;
        }
        outputMediaFileUri = Uri.fromFile(mediaFile);
        return mediaFile;
    }




}
