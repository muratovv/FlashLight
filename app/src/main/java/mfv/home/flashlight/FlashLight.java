package mfv.home.flashlight;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

import mfv.home.flashlight.Exceptions.CameraBusyException;


public class FlashLight
{

	public static final String CAMERA_TAG = "cameraTag";
	private boolean isSupport = false;

	private Camera camera;
	private Camera.Parameters parameters;

	public FlashLight(Context context, SurfaceHolder holder) throws CameraBusyException
	{
		holder.addCallback(new HolderHelper());
		prepareCamera(context);
	}

	public boolean isSupport()
	{
		return isSupport
				&& parameters.getSupportedFlashModes()
				.contains(Camera.Parameters.FLASH_MODE_TORCH);
	}


	public void openCamera()
	{
		if(isSupport())
		{
			try
			{
				camera.reconnect();
			}
			catch(IOException e)
			{
				Log.d(CAMERA_TAG, "reconnect failed");
			}
			parameters = camera.getParameters();

			Log.d(CAMERA_TAG, "camera open");
		}
	}

	public void releaseCamera()
	{
		if(camera != null)
		{
			camera.release();

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

	private void prepareCamera(Context context) throws CameraBusyException
	{
		if(context.getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
		{
			try
			{
				camera = Camera.open();
				parameters = camera.getParameters();
				isSupport = parameters.getSupportedFlashModes()
						.contains(Camera.Parameters.FLASH_MODE_TORCH);
			}
			catch(Exception e)
			{
				throw new CameraBusyException();
			}
		}
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
