package org.fu.swphcc.wattnglueck;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class Kamera extends WattnActivity {
	
	private CameraPreview mPreview;
    Camera mCamera;
    int cameraCurrentlyLocked;
    int cameraId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        mPreview = new CameraPreview(this);
		setContentView(mPreview);
		
		// Find the ID of the default camera
        CameraInfo cameraInfo = new CameraInfo();
        if (Camera.getNumberOfCameras() > 1)
            for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                	cameraId = i;
                }
            }
        else cameraId = 0;
		
		initViews();
	}

    @Override
    protected void onResume() {
        super.onResume();

        // Open the default i.e. the first rear facing camera.
        mCamera = Camera.open();
        cameraCurrentlyLocked = cameraId;
        mPreview.setCamera(mCamera);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Because the Camera object is a shared resource, it's very
        // important to release it when the activity is paused.
        if (mCamera != null) {
            mPreview.setCamera(null);
            mCamera.release();
            mCamera = null;
        }
    }
	
	@Override
	protected List<TextView> getTextViewsForFont() {
		return null;
	}

	@Override
	protected boolean showOptionsMenu() {
		return false;
	}

	@Override
	protected List<TextView> getButtonTextViews() {
		return null;
	}

	@Override
	public boolean onClick(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private class CameraPreview extends ViewGroup implements SurfaceHolder.Callback {
		
		SurfaceView mSurfaceView;
	    SurfaceHolder mHolder;
	    Size mPreviewSize;
	    List<Size> mSupportedPreviewSizes;
	    Camera mCamera;

	    CameraPreview(Context context) {
	        super(context);

	        mSurfaceView = (SurfaceView) findViewById(R.id.view_kamera);
	        addView(mSurfaceView);

	        // Install a SurfaceHolder.Callback so we get notified when the
	        // underlying surface is created and destroyed.
	        mHolder = mSurfaceView.getHolder();
	        mHolder.addCallback(this);
	    }
		
	    public void setCamera(Camera camera) {
	        mCamera = camera;
	        if (mCamera != null) {
	            mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
	            requestLayout();
	        }
	    }
	    
	    @Override
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	        // We purposely disregard child measurements because act as a
	        // wrapper to a SurfaceView that centers the camera preview instead
	        // of stretching it.
	        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
	        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
	        setMeasuredDimension(width, height);

	        if (mSupportedPreviewSizes != null) {
	            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
	        }
	    }

	    @Override
	    protected void onLayout(boolean changed, int l, int t, int r, int b) {
	        if (changed && getChildCount() > 0) {
	            final View child = getChildAt(0);

	            final int width = r - l;
	            final int height = b - t;

	            int previewWidth = width;
	            int previewHeight = height;
	            if (mPreviewSize != null) {
	                previewWidth = mPreviewSize.width;
	                previewHeight = mPreviewSize.height;
	            }

	            // Center the child SurfaceView within the parent.
	            if (width * previewHeight > height * previewWidth) {
	                final int scaledChildWidth = previewWidth * height / previewHeight;
	                child.layout((width - scaledChildWidth) / 2, 0,
	                        (width + scaledChildWidth) / 2, height);
	            } else {
	                final int scaledChildHeight = previewHeight * width / previewWidth;
	                child.layout(0, (height - scaledChildHeight) / 2,
	                        width, (height + scaledChildHeight) / 2);
	            }
	        }
	    }

	    public void surfaceCreated(SurfaceHolder holder) {
	        // The Surface has been created, acquire the camera and tell it where
	        // to draw.
	        try {
	            if (mCamera != null) {
	                mCamera.setPreviewDisplay(holder);
	            }
	        } catch (IOException exception) {
	        }
	    }

	    public void surfaceDestroyed(SurfaceHolder holder) {
	        // Surface will be destroyed when we return, so stop the preview.
	        if (mCamera != null) {
	            mCamera.stopPreview();
	        }
	    }
		
	    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
	        final double ASPECT_TOLERANCE = 0.1;
	        double targetRatio = (double) w / h;
	        if (sizes == null) return null;

	        Size optimalSize = null;
	        double minDiff = Double.MAX_VALUE;

	        int targetHeight = h;

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
	    
	    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
	        // Now that the size is known, set up the camera parameters and begin
	        // the preview.
	        Camera.Parameters parameters = mCamera.getParameters();
	        parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
	        requestLayout();

	        mCamera.setParameters(parameters);
	        mCamera.startPreview();
	    }
		
	}

}
