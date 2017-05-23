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
import org.json.JSONObject;


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
        
    }


    @Override
    public boolean execute(String action, JSONArray data, CallbackContext _callbackContext) throws JSONException {
        callbackContext =_callbackContext;

        if (action.equals("Classifier")) {
            JSONObject reader = new JSONObject(data.getString(0));
            String name;
            if( reader.has("Name")) name = reader.getString("Name"); else name = "out.bmp";
            float Contrast;
            if( reader.has("Contrast")) Contrast = (float) reader.getInt("Contrast"); else Contrast = 10f;
            float Brightness;
            if( reader.has("Brightness")) Brightness = (float) reader.getInt("Brightness"); else Brightness = 90f;

            String MODEL_FILE;
            String LABEL_FILE;

            int NUM_CLASSES;
            int INPUT_SIZE;
            int IMAGE_MEAN;
            int IMAGE_STD;

            String INPUT_NAME;
            String OUTPUT_NAME;

            if( reader.has("MODEL_FILE")) MODEL_FILE = reader.getString("MODEL_FILE"); else MODEL_FILE = "file:///android_asset/www/retrained_graph.pb";
            if( reader.has("LABEL_FILE")) LABEL_FILE = reader.getString("LABEL_FILE"); else LABEL_FILE = "file:///android_asset/www/retrained_labels.txt";

            if( reader.has("NUM_CLASSES")) NUM_CLASSES = reader.getInt("NUM_CLASSES"); else NUM_CLASSES = 2;
            if( reader.has("INPUT_SIZE")) INPUT_SIZE = reader.getInt("INPUT_SIZE"); else INPUT_SIZE = 299;
            if( reader.has("IMAGE_MEAN")) IMAGE_MEAN = reader.getInt("IMAGE_MEAN"); else IMAGE_MEAN = 128;
            if( reader.has("IMAGE_STD")) IMAGE_STD = reader.getInt("IMAGE_STD"); else IMAGE_STD = 128;

            if( reader.has("INPUT_NAME")) INPUT_NAME = reader.getString("INPUT_NAME"); else INPUT_NAME = "Mul:0";
            if( reader.has("OUTPUT_NAME")) OUTPUT_NAME = reader.getString("OUTPUT_NAME"); else OUTPUT_NAME = "final_result:0";


            mTensorFlowClassifier = new TensorFlowImageClassifier(ATTensorflow.this.cordova.getActivity().getAssets(),MODEL_FILE,LABEL_FILE, NUM_CLASSES, INPUT_SIZE, IMAGE_MEAN, IMAGE_STD,
            INPUT_NAME, OUTPUT_NAME);
            mCameraHandler.takePicture(name,Contrast,Brightness);
            setReady(true);
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
            Classifier.Recognition mr = results.get(0);
            for (int i = 0; i < results.size(); i++) {
                Classifier.Recognition r = results.get(i);
                Log.w(TAG, r.toString());
                if(mr.getConfidence() < r.getConfidence() ) mr = r;
            }
            callbackContext.success(mr.getTitle()+" ("+(mr.getConfidence()*100)+"%)");
            Log.d(TAG,mr.getTitle()+" ("+(mr.getConfidence()*100)+"%)");

        }else{
            callbackContext.error("No Classifation found");
        }



    }


}
