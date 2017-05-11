package com.apray.plugin;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import static android.graphics.ImageFormat.JPEG;
import static java.lang.Thread.sleep;

import org.apache.cordova.PermissionHelper;



public class ATTensorflow extends CordovaPlugin {

    public static final int TAKE_PIC_SEC = 0;

    public static final int SERVERPORT = 8080;
    private static final String SERVER_IP = "127.0.0.1";
    private org.apache.cordova.CallbackContext callbackContext;
    int mQuality = 50;
    private String Tag = "ATTensorflow";


    protected final static String[] permissions = { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE };



    @Override
    public boolean execute(String action, JSONArray data, CallbackContext _callbackContext) throws JSONException {
        callbackContext =_callbackContext;

        if (action.equals("greet")) {

            String name = data.getString(0);
            String message = "Hello, " + name;
            callbackContext.success(message);

            return true;

        }else if (action.equals("NoNo")) {
            callTakePicture();
            return true;
        } else {
            
            return false;

        }
    }



    public void callTakePicture() {



        boolean saveAlbumPermission = PermissionHelper.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        // CB-10120: The CAMERA permission does not need to be requested unless it is declared
        // in AndroidManifest.xml. This plugin does not declare it, but others may and so we must
        // check the package info to determine if the permission is present.

        if (!saveAlbumPermission) {
            PermissionHelper.requestPermission(this, TAKE_PIC_SEC, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        Log.d(Tag,"processing callTakePicture");
        new Thread(new ClientThread()).start();

    }



    public static Bitmap changeBitmapContrastBrightness(Bitmap bmp, float contrast, float brightness)
    {
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        contrast, 0, 0, 0, brightness,
                        0, contrast, 0, 0, brightness,
                        0, 0, contrast, 0, brightness,
                        0, 0, 0, 1, 0
                });

        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bmp, 0, 0, paint);

        return ret;
    }


    public void processPicture(Bitmap bitmap, int encodingType) {
        Log.d(Tag,"processing Picture");

        ByteArrayOutputStream jpeg_data = new ByteArrayOutputStream();
        Bitmap.CompressFormat compressFormat = encodingType == JPEG ?
                Bitmap.CompressFormat.JPEG :
                Bitmap.CompressFormat.PNG;

        try {
            if (bitmap.compress(compressFormat, mQuality, jpeg_data)) {
                byte[] code = jpeg_data.toByteArray();
                byte[] output = Base64.encode(code, Base64.NO_WRAP);
                String js_out = new String(output);
                Log.d(Tag,"sending Image");

                callbackContext.success(js_out);
                js_out = null;
                output = null;
                code = null;
            }
        } catch (Exception e) {
            this.failPicture("Error compressing image.");
        }
        jpeg_data = null;
    }



    public void failPicture(String err) {
        callbackContext.error(err);
    }

    class ClientThread implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;



        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                long now = System.currentTimeMillis();

                socket = new Socket(serverAddr, SERVERPORT);
                out= new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())), true);

                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Log.d(Tag,"ClientThread Running Image Capture");

                out.println("cd /sdcard/Pictures");
                //out.println("touch bitmap.loc");
                sleep(10);
                out.println("gt_crl_test_sensors.sh ov5670_1080");
                //printout();
                out.println("raw2vec bd 1920 1080 ov5670_1080_000000.bin temp.raw");
                //printout();
                out.println("raw2bmp temp.raw out.bmp 1920 1080 16 3");
                //printout();
                //out.println("cp out.bmp image.bmp");
                //printout();

                //out.println("rm bitmap.loc");


                String storageDir = Environment.getExternalStorageDirectory().getAbsolutePath();
                String fileName = "/Pictures/out.bmp";
                File imageFile= new File(storageDir+fileName);

                while (!in.readLine().contains("file temp.raw")){

                }
                Log.d(Tag,imageFile.getAbsolutePath());

                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                bitmap = Bitmap.createScaledBitmap(bitmap,400,400,false);
                bitmap = changeBitmapContrastBrightness(bitmap,10f,90f);

                processPicture(bitmap,JPEG);


                //out.println("rm out.bmp");
                //out.println("rm ov5670_1080_000000.bin");
                //out.println("rm temp.raw");
                //sleep(1000);
                bitmap.recycle();
                out.close();
                socket.close();

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
                Log.e(Tag,"UnKnowHost");
                callbackContext.error("UnknownHostException");
            } catch (IOException e1) {
                //e1.printStackTrace();
                //Log.e(Tag,"IOException");
                callbackContext.error("IOException");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
