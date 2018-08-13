package com.admin.budgetrook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cz.msebera.android.httpclient.entity.mime.Header;

public class RestController {

    public static final String FILE_UP_PATH = "";
    public static final int TIMEOUT = 50000;
    private static final String TAG = "BUDGET_ROOK";

    public static void sendPhoto(File fileToSend){
        try {
            Log.d(TAG, "Przyszlo zdjecie");
            FileInputStream fis = new FileInputStream(fileToSend);
            byte[] bytes = new byte[(int) fileToSend.length()];
            fis.read(bytes);
            String imageDataAsString = encodeImage(bytes);
            manageConnection(imageDataAsString);
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File upload failed: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Reading file failed: " + e.getMessage());
        }

    }

    public static void sendPhoto(byte[] bytesToSend){
            Log.d(TAG, "Przyszlo zdjecie");
            String imageDataAsString = encodeImage(bytesToSend);
            sendAllData(bytesToSend);
    }


    private static String encodeImage(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private static void manageConnection(String dataToSend){
        try {
            Log.d(TAG, "Wysylam");
            URL url = new URL(FILE_UP_PATH);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);
            connection.connect();
            OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
            osw.write(dataToSend);
            osw.close();
            Log.d(TAG, "Poszlo");
        } catch (MalformedURLException e) {
            Log.d(TAG, "URL malformed exception! :" + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error opening connection: " + e.getMessage());
        }

    }

    public static void sendAllData(byte[] bytesToSend) {
        SyncHttpClient client = new SyncHttpClient();
        RequestParams params = new RequestParams();
        try {
            params.put("pic", storeImage(bytesToSend));
        } catch (FileNotFoundException e) {
            Log.d(TAG, "sendAllData: ERROR: " + e.getMessage());
        }
        client.post(FILE_UP_PATH, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                Log.d(TAG, "onSuccess: OK: " + response.toString());
            }

            @Override
            public void onFailure(int statusCode , cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, "onFailure: NOT OK: " + errorResponse.toString() + " " + throwable.getMessage());
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG, "onFailure: NOT OK: " + responseString);
            }
        });

    }

    private static File storeImage(byte[] bytes) {
        String filename = "anyName";
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;

        File file = new File(extStorageDirectory, filename + ".jpg");
        try {
            outStream = new FileOutputStream(file);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            Bitmap rotated = CameraHelper.RotateBitmap(bitmap, 90);
            rotated.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            Log.d(TAG, "storeImage: SAVE FAILED: " + e.getMessage());
        }
        return file;
    }

}
