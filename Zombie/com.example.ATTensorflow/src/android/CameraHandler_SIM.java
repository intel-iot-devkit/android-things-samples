package com.example.androidthings.imageclassifier;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;

import static com.apray.plugin.ATCamara.changeBitmapContrastBrightness;
import static java.lang.Thread.sleep;

/**
 * Created by apray on 2/13/2017.
 */

public class CameraHandler_SIM {



    public interface CameraHandler_SIMInterface {
        void onDownloadFinished(Bitmap result);
    }



    public enum Dir {
        Up,
        Down,
        Left,
        Right
    }


    private static final String TAG = CameraHandler_SIM.class.getSimpleName();


    public static final int IMAGE_WIDTH = 299;
    public static final int IMAGE_HEIGHT = 299;
    private CameraHandler_SIMInterface imageAvailableListener;

    public boolean Moveleft = true;
    public int MovePos =0;
    public int Movecount =40;
    public void takePicture() {
        String storageDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "/Pictures/out.bmp";
        File imageFile= new File(storageDir+fileName);
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        bitmap = Bitmap.createScaledBitmap(bitmap,IMAGE_WIDTH,IMAGE_WIDTH,true);
        bitmap = changeBitmapContrastBrightness(bitmap,10f,90f);
        imageAvailableListener.onDownloadFinished(bitmap);
        //new loadBitmap().execute(url);

    }

    private static class InstanceHolder {
        private static CameraHandler_SIM mCamera = new CameraHandler_SIM();
    }

    public static CameraHandler_SIM getInstance() {
        return CameraHandler_SIM.InstanceHolder.mCamera;
    }

    /**
     * Initialize the camera device
     */
    public void initializeCamera(Context context,
                                 Handler backgroundHandler, CameraHandler_SIMInterface imageAvailableListener) {
        this.imageAvailableListener = imageAvailableListener;
    }


    private class loadBitmap extends AsyncTask<String, Void, String> {
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;

        @Override
        protected String doInBackground(String... params) {
            try
            {
                Log.d(TAG,"URL: "+params[0]);
                URLConnection conn = new URL(params[0]).openConnection();
                conn.connect();
                is = conn.getInputStream();
                bis = new BufferedInputStream(is, 8192);
                bm = BitmapFactory.decodeStream(bis);
                imageAvailableListener.onDownloadFinished(Bitmap.createScaledBitmap(bm, IMAGE_WIDTH, IMAGE_HEIGHT, true));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }finally {

                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {



        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
