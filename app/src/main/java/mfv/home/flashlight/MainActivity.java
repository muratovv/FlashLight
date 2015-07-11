package mfv.home.flashlight;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;


public class MainActivity extends Activity
{

	private Button turnButton;
	private TextView statusView;
	private SurfaceView preview;

	private boolean isOn;
	private FlashLight flashLight;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		turnButton = ((Button) findViewById(R.id.turnButton));
		statusView = ((TextView) findViewById(R.id.statusField));
		turnButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				turn();
			}
		});
		preview = ((SurfaceView) findViewById(R.id.surface));
		SurfaceHolder holder = preview.getHolder();
		flashLight = new FlashLight(getApplicationContext());
		holder.addCallback(flashLight);
		//		preview.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		flashLight.releaseCamera();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		flashLight.openCamera();
	}

	private void turn()
	{
		if(flashLight.isSupport)
		{
			if(isOn)
			{
				statusView.setText("Flash turn off");
				flashLight.turnOff();
				isOn = false;
			}
			else
			{
				statusView.setText("Flash turn on");
				flashLight.turnOn();
				isOn = true;
			}
		}
		else
		{
			statusView.setText("Camera not supported");
		}
	}

	private static class FlashLight implements SurfaceHolder.Callback
	{

		private boolean isSupport;
		private Camera camera;
		private Camera.Parameters parameters;

		public FlashLight(Context context)
		{
			checkSupportCamera(context);
		}

		public boolean isSupport()
		{
			return isSupport && camera != null;
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
				Log.d("camera", s);
			}

			Log.d("camera", camera.getParameters().getFocusMode());
			for(String s : camera.getParameters().getSupportedFocusModes())
			{
				Log.d("camera", s);
			}


		}

		public void openCamera()
		{
			if(camera == null && isSupport)
			{
				camera = Camera.open();
				parameters = camera.getParameters();
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

		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder)
		{
			if(isSupport() && holder != null)
			{ camera.stopPreview(); }
		}
	}

}
