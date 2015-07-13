package mfv.home.flashlight;

import android.view.SurfaceHolder;

/**
 * Created by mfv on 14.07.15.
 */
public class FlashLightFragment extends android.support.v4.app.Fragment
{
	FlashLight flashLight;
	SurfaceHolder holder;

	public FlashLightFragment()
	{
		setRetainInstance(true);
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
