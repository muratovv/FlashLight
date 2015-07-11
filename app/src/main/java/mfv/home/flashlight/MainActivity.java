package mfv.home.flashlight;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity
{

	private TextView statusView;

	private boolean isOn;
	private FlashLight flashLight;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button turnButton = ((Button) findViewById(R.id.turnButton));
		statusView = ((TextView) findViewById(R.id.statusField));
		turnButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				turnNext();
			}
		});
		SurfaceView preview = ((SurfaceView) findViewById(R.id.surface));
		SurfaceHolder holder = preview.getHolder();
		flashLight = new FlashLight(getApplicationContext());
		holder.addCallback(flashLight);
		preview.setVisibility(View.VISIBLE);
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

	private void turnNext()
	{
		if(flashLight.isSupport())
		{
			if(isOn)
			{
				statusView.setText("Flash turnNext off");
				flashLight.turnOff();
				isOn = false;
			}
			else
			{
				statusView.setText("Flash turnNext on");
				flashLight.turnOn();
				isOn = true;
			}
		}
		else
		{
			statusView.setText("Camera not supported");
		}
	}
}
