package com.nhn.camera;

import java.util.ArrayList;
import java.util.List;

import com.nhn.android.test.camera.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

public class FaceDetectionActivity extends Activity {
	private static final String 	TAG = FaceDetectionActivity.class.getSimpleName();
	private Camera 		mCamera;
	CameraPreview		mPreview;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Create an instance of Camera
        mCamera = getCameraInstance();
        settingCameraPreview(mCamera);
        
        Camera.Parameters params = mCamera.getParameters();

        if (params.getMaxNumMeteringAreas() > 0){ // check that metering areas are supported
            List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();

            Rect areaRect1 = new Rect(-100, -100, 100, 100);    // specify an area in center of image
            meteringAreas.add(new Camera.Area(areaRect1, 600)); // set weight to 60%
            Rect areaRect2 = new Rect(800, -1000, 1000, -800);  // specify an area in upper right of image
            meteringAreas.add(new Camera.Area(areaRect2, 400)); // set weight to 40%
            params.setMeteringAreas(meteringAreas);
            
            Log.d(TAG, "..........................");
        }

        mCamera.setParameters(params);

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        
        Intent intent = getIntent();
        String ipAddr = intent.getStringExtra("ip_address");
        
        FaceDetectionView	view = new FaceDetectionView(this, mCamera, ipAddr);
        preview.addView(view);
        
        mCamera.setFaceDetectionListener(view);
    }
    
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	/** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera camera = null;
        try {
//            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT); // attempt to get a Camera instance
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return camera; // returns null if camera is unavailable
    }
    
    private void settingCameraPreview(Camera camera) {
//    	Camera.Parameters	params = camera.getParameters();
//    	
//    	List<Camera.Size> 	supportedPreviewSize = params.getSupportedPreviewSizes();
//    	
//    	params.setPreviewSize(600, 600);
    }
}