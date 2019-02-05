package com.admin.budgetrook.helpers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileChooserHelper {

    private static final String TAG = "budgetrook";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy_HH:mm:ss");

    public static Intent createFile(Context context, String mimeType) {
        final Intent chooserIntent = new Intent(context, DirectoryChooserActivity.class);

        final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                .newDirectoryName("test")
                .allowReadOnlyDirectory(false)
                .initialDirectory("/storage/sdcard0/Documents")
                .build();

        chooserIntent.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config);
        return chooserIntent;
    }

    public static File handleDirectoryChoice(String chosenDirectory) {
        File result = new File(chosenDirectory + "/" + buildFileName());
        try {
            result.createNewFile();
        } catch (Exception e){
            Log.e(TAG, "handleDirectoryChoice: ", e);
            return null;
        }
        return result;
    }

    private static String buildFileName() {
        String result = "";
        result += "BudgetRook-";
        result += dateFormat.format(new Date());
        result += ".csv";
        return  result;
    }

}
