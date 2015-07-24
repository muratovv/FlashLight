package mfv.home.flashlight;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity
{

	private static final String flashlightFragmentTag = "flashlightFragment";

	private boolean isOn;

	private TextView statusView;
	private FlashLightFragment flashLightFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button turnButton = ((Button) findViewById(R.id.turnButton));
		turnButton.setSoundEffectsEnabled(false);

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

		if(savedInstanceState == null || getSupportFragmentManager()
				.findFragmentByTag(flashlightFragmentTag) == null)
		{
			getSupportFragmentManager()
					.beginTransaction()
					.add(new FlashLightFragment(), flashlightFragmentTag)
					.commit();
			getSupportFragmentManager().executePendingTransactions();
		}

		flashLightFragment = ((FlashLightFragment) getSupportFragmentManager()
				.findFragmentByTag(flashlightFragmentTag));
		flashLightFragment.setHolder(preview.getHolder());

	}


	@Override
	protected void onDestroy()
	{
		super.onStop();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		statusView.setText("");
		if(!flashLightFragment.getFlashLight().isUsed())
		{
			flashLightFragment.getFlashLight().releaseCamera();
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		flashLightFragment.getFlashLight().openCamera();
	}

	private void playSound(int rawId)
	{
		MediaPlayer player = MediaPlayer.create(this, rawId);
		player.start();
	}

	private void turnNext()
	{
		if(flashLightFragment.getFlashLight().isSupport())
		{
			if(isOn)
			{
				statusView.setText("Flash turnNext off");
				playSound(R.raw.turn_on);
				flashLightFragment.getFlashLight().turnOff();
				isOn = false;
			}
			else
			{
				statusView.setText("Flash turnNext on");
				playSound(R.raw.turn_off);
				flashLightFragment.getFlashLight().turnOn();
				isOn = true;
			}
		}
	}
}
