package mfv.home.flashlight;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;


public class FlashLight
{

	public static final String CAMERA_TAG = "cameraTag";
	private boolean isSupport = false;

	private Camera camera;
	private Camera.Parameters parameters;

	public FlashLight(Context context, SurfaceHolder holder)
	{
		holder.addCallback(new HolderHelper());
		checkSupportCamera(context);
	}

	public boolean isSupport()
	{
		return isSupport && camera != null
				&& parameters.getSupportedFlashModes()
				.contains(Camera.Parameters.FLASH_MODE_TORCH);
	}


	public void openCamera()
	{
		if(camera == null && isSupport)
		{
			camera = Camera.open();
			parameters = camera.getParameters();

			Log.d(CAMERA_TAG, "camera open");
		}
	}

	public void releaseCamera()
	{
		if(camera != null)
		{
			camera.release();
			camera = null;

			Log.d(CAMERA_TAG, "camera release");
		}
	}

	public void turnOn()
	{
		if(isSupport())
		{
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
			camera.setParameters(parameters);
			camera.startPreview();

			Log.d(CAMERA_TAG, "turn on");
		}
	}

	public void turnOff()
	{
		if(isSupport())
		{
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			camera.setParameters(parameters);

			Log.d(CAMERA_TAG, "turn off");
		}
	}

	private void checkSupportCamera(Context context)
	{
		isSupport = context.getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
	}


	private class HolderHelper implements SurfaceHolder.Callback
	{
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
}
