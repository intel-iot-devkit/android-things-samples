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
import android.os.Handler;
import android.os.HandlerThread;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Base64;
import android.util.Log;

import com.example.androidthings.imageclassifier.CameraHandler_SIM;
import com.example.androidthings.imageclassifier.ImagePreprocessor;
import com.example.androidthings.imageclassifier.classifier.Classifier;
import com.example.androidthings.imageclassifier.classifier.TensorFlowImageClassifier;

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
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import static android.graphics.ImageFormat.JPEG;
import static java.lang.Thread.sleep;

import org.apache.cordova.PermissionHelper;



public class ATTensorflow extends CordovaPlugin {
    private static final String TAG = "ATTensorflow";
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private CallbackContext callbackContext;
    private AtomicBoolean mReady = new AtomicBoolean(false);


    private ImagePreprocessor mImagePreprocessor;
    private TextToSpeech mTtsEngine;
    private CameraHandler_SIM mCameraHandler;

    private TensorFlowImageClassifier mTensorFlowClassifier;


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        //mBackgroundThread = new HandlerThread("BackgroundThread");
        //mBackgroundThread.start();
        //mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
        //mBackgroundHandler.post(mInitializeOnBackground);
        mCameraHandler = new CameraHandler_SIM();

        mImagePreprocessor = new ImagePreprocessor(CameraHandler_SIM.IMAGE_WIDTH,
                CameraHandler_SIM.IMAGE_HEIGHT, TensorFlowImageClassifier.INPUT_SIZE);

        mCameraHandler = CameraHandler_SIM.getInstance();
        mCameraHandler.initializeCamera(
                ATTensorflow.this.cordova.getActivity(), mBackgroundHandler,
                new CameraHandler_SIM.CameraHandler_SIMInterface() {
                    @Override
                    public void onDownloadFinished(Bitmap result) {
                        onImageAvailable(result);
                    }
                });

        mTensorFlowClassifier = new TensorFlowImageClassifier(ATTensorflow.this.cordova.getActivity());
        setReady(true);
    }


    @Override
    public boolean execute(String action, JSONArray data, CallbackContext _callbackContext) throws JSONException {
        callbackContext =_callbackContext;

        if (action.equals("Classifier")) {
            mCameraHandler.takePicture();
            return true;
        } else {
            
            return false;

        }
    }






    private void setReady(boolean ready) {
        mReady.set(ready);
    }


    public void onImageAvailable(Bitmap bitmdap) {

        final Bitmap Out_bitmap = mImagePreprocessor.preprocessImage(bitmdap);

        Log.w(TAG,"getHeight: "+ Out_bitmap.getHeight()+"  , getWidth: "+Out_bitmap.getWidth());

        final List<Classifier.Recognition> results = mTensorFlowClassifier.recognizeImage(Out_bitmap);
        Log.d(TAG, "results: " + results.size());
        if(results.size() > 0) {
            for (int i = 0; i < results.size(); i++) {
                Classifier.Recognition r = results.get(i);
                Log.w(TAG, r.toString());
                callbackContext.success(r.getTitle()+" ("+(r.getConfidence()*100)+"%)");
            }
        }else{
            callbackContext.error("No Classifation found");
        }



    }


}
