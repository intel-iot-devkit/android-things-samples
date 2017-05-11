package com.example.androidthings.imageclassifier;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import static java.lang.Thread.sleep;

/**
 * Created by apray on 2/13/2017.
 */

class CameraHandler_SIM {

    public enum Dir {
        Up,
        Down,
        Left,
        Right
    }


    private static final String TAG = CameraHandler_SIM.class.getSimpleName();

    public String baseurl = "http://192.168.1.88:81/";
    public String LoginBits = "&loginuse=admin&loginpas=tmeftw&user=admin&pwd=tmeftw";

    public static final int IMAGE_WIDTH = 224;
    public static final int IMAGE_HEIGHT = 224;
    private ImageClassifierActivity imageAvailableListener;

    public boolean Moveleft = true;
    public int MovePos =0;
    public int Movecount =40;
    public void takePicture() {
      new loadBitmap().execute(baseurl+"snapshot.cgi?lres=0"+LoginBits);

    }

    public void moveCamara(Dir Direction){
        String urlLow = "decoder_control.cgi?onestep=1&";
        String urlHigh = "decoder_control.cgi?onestep=0&";
        if( Direction ==Dir.Up) {
            urlLow += "command=0";
            urlHigh += "command=1";
        }else if(Direction ==Dir.Down){
            urlLow += "command=2";
            urlHigh += "command=3";
        }else if(Direction ==Dir.Left){
            urlLow += "command=4";
            urlHigh += "command=5";
        }else if(Direction ==Dir.Right){
            urlLow += "command=6";
            urlHigh += "command=7";
        }
        new getPage().execute(baseurl+urlLow+LoginBits);
        new getPage().execute(baseurl+urlHigh+LoginBits);
    }

    public void shutDown() {

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
                                 Handler backgroundHandler, ImageClassifierActivity imageAvailableListener) {
        this.imageAvailableListener = imageAvailableListener;
    }


    private class getPage extends AsyncTask<String, Void, String>{
        InputStream is = null;
        BufferedInputStream bis = null;
        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG,"URL: "+params[0]);
            URLConnection conn = null;
            try {
                conn = new URL(params[0]).openConnection();
                conn.connect();
                is = conn.getInputStream();
                bis = new BufferedInputStream(is, 8192);
            } catch (IOException e) {
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

    }

    private class loadBitmap extends AsyncTask<String, Void, String> {
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;

        @Override
        protected String doInBackground(String... params) {


            if( MovePos++ < Movecount && Moveleft) {
                moveCamara(Dir.Left);
            }else if (!Moveleft &&  MovePos < Movecount){
                moveCamara(Dir.Right);
            }else if (MovePos > Movecount){
                MovePos =0;
                Moveleft = !Moveleft;
            }
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            try
            {
                Log.d(TAG,"URL: "+params[0]);
                URLConnection conn = new URL(params[0]).openConnection();
                conn.connect();
                is = conn.getInputStream();
                bis = new BufferedInputStream(is, 8192);
                bm = BitmapFactory.decodeStream(bis);
                imageAvailableListener.onImageAvailable(Bitmap.createScaledBitmap(bm, IMAGE_WIDTH, IMAGE_HEIGHT, true));
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
