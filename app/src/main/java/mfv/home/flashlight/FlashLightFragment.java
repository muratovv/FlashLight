package mfv.home.flashlight;

import android.app.Fragment;
import android.view.SurfaceHolder;

/**
 * Created by mfv on 14.07.15.
 */
public class FlashLightFragment extends Fragment
{
	private static FlashLightFragment instance = null;
	FlashLight flashLight;
	SurfaceHolder holder;

	public FlashLightFragment()
	{
		setRetainInstance(true);
	}

	public static FlashLightFragment getInstance()
	{
		if(instance == null)
			instance = new FlashLightFragment();
		return instance;
	}

	public FlashLight getFlashLight()
	{
		return flashLight;
	}

	public void setHolder(SurfaceHolder holder)
	{
		this.holder = holder;
		if(flashLight == null)
			flashLight = new FlashLight(holder);
		else
			flashLight.setNewSurfaceHolder(holder);
	}

}
