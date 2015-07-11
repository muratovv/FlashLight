package mfv.home.flashlight;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;


public class FlashLight implements SurfaceHolder.Callback
{

	public static final String CAMERA_TAG = "cameraTag";
	private boolean isSupport;
	private boolean isCatced;
	private Camera camera;
	private Camera.Parameters parameters;

	public FlashLight(Context context)
	{
		checkSupportCamera(context);
	}

	public boolean isSupport()
	{
		return isSupport && camera != null
				&& parameters.getSupportedFlashModes()
				.contains(Camera.Parameters.FLASH_MODE_TORCH);
	}

	private void checkSupportCamera(Context context)
	{
		isSupport = context.getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
	}

	@Deprecated
	private void debug()
	{
		for(String s : camera.getParameters().getSupportedFlashModes())
		{
			Log.d(CAMERA_TAG, s);
		}

		Log.d(CAMERA_TAG, camera.getParameters().getFocusMode());
		for(String s : camera.getParameters().getSupportedFocusModes())
		{
			Log.d(CAMERA_TAG, s);
		}


	}

	public void openCamera()
	{
		if(camera == null && isSupport && !isCatced)
		{
			camera = Camera.open();
			parameters = camera.getParameters();
			isCatced = true;
			debug();
		}
	}

	public void releaseCamera()
	{
		if(camera != null)
		{
			camera.release();
			camera = null;
		}
	}

	public void turnOn()
	{
		if(isSupport())
		{
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
			camera.setParameters(parameters);
			camera.startPreview();
		}
	}

	public void turnOff()
	{
		if(isSupport())
		{
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			camera.setParameters(parameters);
			camera.stopPreview();
		}
	}


	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		try
		{
			if(isSupport() && holder != null)
			{
				Log.d(CAMERA_TAG, "surface created");
				holder.setFixedSize(0, 0);
				camera.setPreviewDisplay(holder);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		Log.d(CAMERA_TAG, "surface changed");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		if(isSupport())
		{
			Log.d(CAMERA_TAG, "surface destroyed");
			camera.stopPreview();
		}
	}
}
