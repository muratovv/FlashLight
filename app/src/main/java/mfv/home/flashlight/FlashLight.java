package mfv.home.flashlight;

import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;



public class FlashLight
{

	public static final String FLASH_TAG = "flash";

	private boolean isUsed = false;
	private boolean isSupport = false;

	private Camera.Parameters parameters;
	private Camera camera;
	private SurfaceHolder holder;
	private HolderHelper holderHelper;

	public FlashLight(SurfaceHolder holder)
	{
		this.holder = holder;
	}

	public boolean isUsed()
	{
		return isUsed;
	}

	public boolean isSupport()
	{
		return isSupport;
	}

	public void openCamera()
	{
		if(camera == null)
		{
			try
			{
				holderHelper = new HolderHelper();
				holder.addCallback(holderHelper);
				camera = Camera.open();
				parameters = camera.getParameters();
				isSupport = parameters
						.getSupportedFlashModes()
						.contains(Camera.Parameters.FLASH_MODE_TORCH);
				camera.setPreviewDisplay(holder);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void setNewSurfaceHolder(SurfaceHolder holder)
	{
		if(camera != null)
		{
			releaseCamera();
			this.holder = holder;
			openCamera();
		}
	}

	public void releaseCamera()
	{
		try
		{
			holder.removeCallback(holderHelper);
			camera.setPreviewDisplay(null);
			camera.release();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		holderHelper = null;
		camera = null;
		isSupport = false;
		isUsed = false;
	}

	public void turnOn()
	{
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				if(isSupport())
				{
					parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
					camera.setParameters(parameters);
					camera.startPreview();
					isUsed = true;
					Log.d(FLASH_TAG, "turn on");
				}
			}
		}).start();
	}

	public void turnOff()
	{
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				if(isSupport())
				{
					parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
					camera.setParameters(parameters);
					camera.stopPreview();
					isUsed = false;
					Log.d(FLASH_TAG, "turn off");
				}
			}
		}).start();
	}


	private class HolderHelper implements SurfaceHolder.Callback
	{
		@Override
		public void surfaceCreated(SurfaceHolder holder)
		{
			try
			{
				FlashLight.this.holder = holder;
				if(isSupport() && holder != null)
				{
					holder.setFixedSize(0, 0);
					camera.setPreviewDisplay(holder);
					Log.d(FLASH_TAG, "surface created");
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
			Log.d(FLASH_TAG, "surface changed");
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder)
		{
			if(isSupport())
			{
				Log.d(FLASH_TAG, "surface destroyed");
			}
		}
	}
}
