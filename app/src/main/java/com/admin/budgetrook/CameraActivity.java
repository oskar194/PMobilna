package com.admin.budgetrook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CameraActivity extends Activity {
    public static final String IMAGE_BYTE_ARRAY = "image";
    public static final String IMAGE_PATH = "image_path";

    private static final String TAG = "BUDGET_ROOK";
    private Camera mCamera;
    private CameraPreview mPreview;
    private CameraHelper helper = new CameraHelper();
    public static final int MEDIA_TYPE_IMAGE = 1;
    private String savedImagePath = "";
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            new SaveImageTask().execute(data);
            mCamera.stopPreview();
        }
    };

    private void toggleButtons(View view, List<Button> buttons) {
        for (Button button : buttons) {
            switch (button.getVisibility()) {
                case View.VISIBLE:
                    button.setVisibility(View.INVISIBLE);
                    break;
                case View.INVISIBLE:
                    button.setVisibility(View.VISIBLE);
                    break;
            }
        }

    }

    private void retakePhoto() {
        mCamera.startPreview();
    }

    private void sendPhoto() {
        byte[] image = createThumbnailByteArray(savedImagePath);
        Intent intent = new Intent();
        intent.putExtra(IMAGE_BYTE_ARRAY, image);
        intent.putExtra(IMAGE_PATH, savedImagePath);
        setResult(NewExpenseActivity.RESULT_CODE, intent);
        finish();

//        new Thread(new Runnable() {
//            public void run() {
//                Log.d(TAG, "Thread Odpalony");
//                RestController.sendPhoto(mData);
//            }
//        }).start();
    }

    private byte[] createThumbnailByteArray(String savedImagePath) {
        Bitmap bitmap = decodeSampledBitmapFromFile(savedImagePath, 100, 100);
        bitmap = CameraHelper.RotateBitmap(bitmap, 90);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (helper.checkCameraHardware(this)) {
            mCamera = helper.getCameraInstance();
        }
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        final Button captureButton = (Button) findViewById(R.id.button_capture);
        final Button retakeButton = (Button) findViewById(R.id.button_retake);
        final Button sendButton = (Button) findViewById(R.id.button_send);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCamera.takePicture(null, null, mPicture);
                        toggleButtons(v, Arrays.asList(captureButton, sendButton, retakeButton));
                    }
                }
        );
        retakeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        retakePhoto();
                        toggleButtons(v, Arrays.asList(captureButton, sendButton, retakeButton));
                    }
                }
        );
        sendButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendPhoto();
                        toggleButtons(v, Arrays.asList(captureButton, sendButton, retakeButton));
                    }
                }
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Budgetrook");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Budgetrook", "failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private class SaveImageTask extends AsyncTask<byte[], String, String> {

        @Override
        protected String doInBackground(byte[]... bytes) {

            byte[] imageToSave = bytes[0];

            File file = getOutputMediaFile(MEDIA_TYPE_IMAGE);

            try {
                FileOutputStream fos = new FileOutputStream(file);
//                Bitmap bitmapImage = BitmapFactory.decodeByteArray(mData, 0, mData.length);

//                rotated.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.write(imageToSave);
                fos.flush();
                fos.close();
                return file.getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String filePath) {
            savedImagePath = filePath;
        }
    }

}
