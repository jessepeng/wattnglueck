package org.fu.swphcc.wattnglueck;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class KameraPreview extends SurfaceView implements SurfaceHolder.Callback{

    public Bitmap mBitmap;
    private SurfaceHolder holder;
    private Camera mCamera;
    private Camera.Size mPreviewSize;
    private KameraTapAction tapAction;

    public KameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);

        holder = getHolder();
        holder.addCallback(this);
    }

    private Size getOptimalPreviewSize(Camera.Parameters parameters, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (parameters == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;
        
        List<Size> sizes = parameters.getSupportedPreviewSizes();

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		try {
	    	if (mCamera == null) {
	                mCamera = Camera.open();
	    	}
	    	if (mCamera != null) {
	    		Camera.Parameters parameters = mCamera.getParameters();
	    		mPreviewSize = getOptimalPreviewSize(parameters, width, height);
	            if (mPreviewSize != null)
	            	parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
	            //layout(width, height);
		        mCamera.setParameters(parameters);
	            mCamera.setPreviewDisplay(holder);

		        mCamera.startPreview();
	    	} 
    	} catch (Exception e) {
        }
    }
    
    private void layout(int width, int height) {
        final View camera = findViewById(R.id.kamera_preview);

        int previewWidth = width;
        int previewHeight = height;

        if (mPreviewSize != null) {
            previewWidth = mPreviewSize.width;
            previewHeight = mPreviewSize.height;
        }

        // Center the child SurfaceView within the parent.
        if (width * previewHeight > height * previewWidth) {
            final int scaledChildWidth = previewWidth * height / previewHeight;
	          camera.layout((width - scaledChildWidth) / 2, 0,
	          (width + scaledChildWidth) / 2, height);
        } else {
            final int scaledChildHeight = previewHeight * width / previewWidth;
            camera.layout(0, (height - scaledChildHeight) / 2,
                    width, (height + scaledChildHeight) / 2);
        }
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    	
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
    }
    
    @Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction()==MotionEvent.ACTION_UP) {
			takeAPicture();
		}
		return(true);
	}
    
    public void setKameraTap(KameraTapAction action) {
    	this.tapAction = action;
    }
    /***
     * 
     *  Take a picture and and convert it from bytes[] to Bitmap.
     *  
     */
    public void takeAPicture(){  

        Camera.PictureCallback mPictureCallback = new PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {

                BitmapFactory.Options options = new BitmapFactory.Options();
                mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        		if (tapAction != null) tapAction.kameraTap();
            }
        };
        mCamera.takePicture(null, null, mPictureCallback);
    }
    
    public interface KameraTapAction {
    	public void kameraTap();
    }
}
