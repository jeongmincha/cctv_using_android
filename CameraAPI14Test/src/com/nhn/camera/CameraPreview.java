package com.nhn.camera;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressLint("ClickableViewAccessibility")
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	private static final String 	TAG = CameraPreview.class.getSimpleName();
	
	private SurfaceHolder	mHolder;
	private Camera			mCamera;
	
	private boolean			mUsableSelectionAutoFocus = false;
	
	@SuppressWarnings("deprecation")
	public CameraPreview(Context context, Camera camera) {
		super(context);
		
		mCamera = camera;
		
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			
			initPreviewSize();
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
			
			startFaceDetection(); // start face detection feature
		} catch (IOException e) {
			Log.d(TAG, "Error setting camera preview : " + e.getMessage());
		}
	}
	
	private void initPreviewSize() {
		Camera.Parameters	params = mCamera.getParameters();
		
		params.setPreviewSize(getWidth(), getHeight());
		mCamera.setParameters(params);
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if (mHolder.getSurface() == null) {
			return;
		}
		
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			
		}
		
		try {
			initPreviewSize();
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
			
			mUsableSelectionAutoFocus = isUsableSelectionAutoFocus();
			startFaceDetection(); // re-start face detection feature
		} catch (IOException e) {
			Log.d(TAG, "Error setting camera preview : " + e.getMessage());
		}
	}
	
	private boolean isUsableSelectionAutoFocus() {
		Log.d(TAG, "getMaxNumFocusAreas = " + mCamera.getParameters().getMaxNumFocusAreas());
		return mCamera.getParameters().getMaxNumFocusAreas() > 0;
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) { }
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean isTouchEvent = super.onTouchEvent(event);
		
		Log.i(TAG, "onTouchEvent");
		
		if (mUsableSelectionAutoFocus == false) {
			Log.i(TAG, "onTouchEvent mUsableSelection Auto Focus = " + mUsableSelectionAutoFocus);
			return isTouchEvent;
		}
		
		if (event.getAction() != MotionEvent.ACTION_DOWN) {
			
			return isTouchEvent;
		}
		
		autoFocusing(event.getX(), event.getY());
		
		return isTouchEvent;
	}
	
	private void autoFocusing(float touchPosX, float touchPosY) {
		final int 	focusRange = 200;
		final int 	halfOfFocusArea = (focusRange / 2);
		
		int 	viewWidth = this.getWidth();
		int 	viewHeight = this.getHeight();
		
		float	ratioWidth = 2000.0f / viewWidth;
		float 	ratioHeight = 2000.0f / viewHeight;
		
		int 	mappingPosX = (int) (ratioWidth * touchPosX);
		int 	mappingPosY = (int) (ratioHeight * touchPosY);
		
		if ((mappingPosX - halfOfFocusArea) < 0) {
			mappingPosX = halfOfFocusArea;
			
		} else if ((mappingPosX + halfOfFocusArea) > 2000) {
			mappingPosX = 2000 - halfOfFocusArea;
		}
		
		if ((mappingPosY - halfOfFocusArea) < 0) {
			mappingPosY = halfOfFocusArea;
			
		} else if ((mappingPosY + halfOfFocusArea) > 2000) {
			mappingPosY = 2000 - halfOfFocusArea;
		}
		
		
		Rect	focusArea = new Rect();
		
		focusArea.left 		= (mappingPosX - halfOfFocusArea) - 1000;
		focusArea.top 		= (mappingPosY - halfOfFocusArea) - 1000;
		focusArea.right 	= (mappingPosX + halfOfFocusArea) - 1000;
		focusArea.bottom 	= (mappingPosY + halfOfFocusArea) - 1000;
		
		Log.d(TAG, "autofocus touch x = " + touchPosX + ", y = " + touchPosY);
		Log.d(TAG, "autofocus mapping area (" + focusArea.toString() + ")");
		Camera.Parameters	params = mCamera.getParameters();
		
		List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
		meteringAreas.add(new Camera.Area(focusArea, 400));
		
		params.setMeteringAreas(meteringAreas);
		mCamera.setParameters(params);
		mCamera.autoFocus(null);
		
//		drawTouchRect(focusArea);
	}
	
//	private void drawTouchRect(Rect rect) {
//		SurfaceHolder 	holder = this.getHolder();
//		Rect 			drawRect = mappingToViewCoordinates(rect);
//		Canvas 			canvas = holder.lockCanvas();
//		Paint 			paint = new Paint();
//		
//		paint.setColor(0xffff00ff);
//		
//		canvas.drawRect(drawRect, paint);
//		holder.unlockCanvasAndPost(canvas);
//	}
	
//	private Rect mappingToViewCoordinates(Rect rect) {
//		float	rate;
//		Rect	newRect = new Rect();
//		
//		newRect.left = rect.left + 1000;
//		newRect.top = rect.top + 1000;
//		newRect.right = rect.right + 1000;
//		newRect.bottom = rect.bottom + 1000;
//		
//		rate = getWidth() / 2000f;
//		newRect.left = (int) (newRect.left * rate);
//		newRect.right = (int) (newRect.right * rate);
//				
//		rate = getHeight() / 2000f;
//		newRect.top = (int) (newRect.top * rate);
//		newRect.bottom = (int) (newRect.bottom * rate);
//		
//		Log.d(TAG, "after face view coordinates = " + newRect.left + ", " + newRect.top + ", " + newRect.right + ", " + newRect.bottom);
//		return newRect;
//	}

	public void startFaceDetection(){
	    // Try starting Face Detection
	    Camera.Parameters params = mCamera.getParameters();

	    // start face detection only *after* preview has started
	    if (params.getMaxNumDetectedFaces() > 0){
	        // camera supports face detection, so can start it:
	        mCamera.startFaceDetection();
	    }
	}
}
