package com.nhn.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.homework5.TcpThread;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.os.Handler;
import android.util.Log;
import android.view.View;

public class FaceDetectionView extends View implements Camera.FaceDetectionListener {
	
	static final String	TAG = FaceDetectionView.class.getSimpleName();

	private String	ipAddr;
	
	private Camera	mCamera;
	private Rect	mOldRect;
	private Paint	mPaint;
	private Face[]	mFaces;
	
	TcpThread tcpChat;
	
	public FaceDetectionView(Context context, Camera camera, String ipAddr) {
		super(context);
		initPaint();
		mCamera = camera;
		this.ipAddr = ipAddr;
	}
	
	private void initPaint() {
    	mPaint = new Paint();
    	mPaint.setStyle(Paint.Style.STROKE);
    	mPaint.setColor(0xff2b002b);
    	mPaint.setStrokeWidth(10f);
		mPaint.setAntiAlias(true);
    }

	@Override
	public void onFaceDetection(Face[] faces, Camera camera) {
		if (faces.length > 0){
          mFaces = faces;
          invalidate();
//          Rect rect = faces[0].rect;
//          Rect newRect = new Rect();
//          
//          newRect.left = rect.left + 1000;
//  		newRect.top = rect.top + 1000;
//  		newRect.right = rect.right + 1000;
//  		newRect.bottom = rect.bottom + 1000;
//  		
//  		float rate = getWidth() / 2000f;
//  		newRect.left = (int) (newRect.left * rate);
//  		newRect.right = (int) (newRect.right * rate);
//  				
//  		rate = getHeight() / 2000f;
//  		newRect.top = (int) (newRect.top * rate);
//  		newRect.bottom = (int) (newRect.bottom * rate);
//          
//		Log.e("rect!", rect.left + ", " + rect.right + ", " + rect.top + ", " + rect.bottom);
//		Log.e("new rect!", newRect.left + ", " + newRect.right + ", " + newRect.top + ", " + newRect.bottom);
//          String info = "(" + faces[0].leftEye.x + ", " + faces[0].leftEye.y + "), (" +
//        		  		faces[0].rightEye.x + ", " + faces[0].rightEye.y + "), (" +
//        		  		faces[0].mouth.x + ", " + faces[0].leftEye.y + ")";
//          
//          Log.e("tag", info);
      }
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (mFaces == null) {
			return;
		}
		
		for (Face face : mFaces) {
			canvas.drawRect(mappingToViewCoordinates(face.rect), mPaint);
		}
	}

	private Rect mappingToViewCoordinates(Rect rect) {
		float	rate;
		Rect	newRect = new Rect();
		
//		Log.e("rect!", rect.left + ", " + rect.right + ", " + rect.top + ", " + rect.bottom);
		
		newRect.left = rect.left + 1000;
		newRect.top = rect.top + 1000;
		newRect.right = rect.right + 1000;
		newRect.bottom = rect.bottom + 1000;
		
		rate = getWidth() / 2000f;
		newRect.left = (int) (newRect.left * rate);
		newRect.right = (int) (newRect.right * rate);
				
		rate = getHeight() / 2000f;
		newRect.top = (int) (newRect.top * rate);
		newRect.bottom = (int) (newRect.bottom * rate);
		
//		Log.e("new rect!", newRect.left + ", " + newRect.right + ", " + newRect.top + ", " + newRect.bottom);
		
		if (mOldRect == null)
			mOldRect = new Rect();
		else {
			if ((mOldRect.left != newRect.left) ||
				(mOldRect.right != newRect.right) ||
				(mOldRect.top != newRect.top) ||
				(mOldRect.bottom != newRect.bottom)){
				cameraCapture();
			}
		}
		mOldRect = newRect;
		
		Log.d(TAG, "after face view coordinates = " + newRect.left + ", " + newRect.top + ", " + newRect.right + ", " + newRect.bottom);
		return newRect;
	}
	
	@SuppressLint({ "SdCardPath", "SimpleDateFormat" }) 
	private void cameraCapture() {
		Camera.PictureCallback mPictureCallbackJpeg = new Camera.PictureCallback() {
//			public void onPictureTaken(byte[] data, Camera c) {
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
//				Date date = new Date();
//				String fileName = "/sdcard/camera/image/";
//				fileName += sdf.format(date);
//				fileName += ".jpg";
//				
//				try {
//					FileOutputStream fos = new FileOutputStream(fileName);
//					fos.write(data);
//					fos.close();
//					System.gc();
//				} catch (FileNotFoundException fnfe) {
//					Log.e("writing and scanning image ", fnfe.toString());
//					fnfe.printStackTrace();
//				} catch (IOException ioe) {
//					Log.e("writing and scanning image ", ioe.toString());
//					ioe.printStackTrace();					
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
			public void onPictureTaken(byte[] data, Camera camera) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
				Date date = new Date();
				String fileName = "/sdcard/DCIM/";
				fileName += sdf.format(date);
				fileName += ".jpg";
				
				File f = new File(fileName);
		        try {
		            FileOutputStream fos = new FileOutputStream(f);
		            fos.write(data);
		            fos.close();

		        }catch (IOException e) {
		            e.printStackTrace();              //<-------- show exception
		        }
				
				final Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
				    @Override
				    public void run() {
				        // Do something after 5s = 5000ms
				    	mCamera.startPreview();
				    }
				}, 5000);
		    }
		};
		mCamera.takePicture(null, null, mPictureCallbackJpeg);
		tcpChat = new TcpThread(ipAddr,12345);
		tcpChat.execute("kj");
	}
}