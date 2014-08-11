/**
 * 
 */
package de.gianasista.tldr_viewer;

import android.app.Application;
import android.content.Context;

/**
 * @author vera
 *
 */
public class TldrApplication extends Application 
{
	private static Context appContext;
	
	@Override
	public void onCreate() 
	{
		super.onCreate();
		TldrApplication.appContext = getApplicationContext();
	}
	
	public static Context getAppContext()
	{
		return appContext;
	}
}
