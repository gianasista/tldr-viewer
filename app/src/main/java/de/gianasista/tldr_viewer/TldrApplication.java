package de.gianasista.tldr_viewer;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

/*
 * This is the source code of tldr-viewer for Android.
 * It is licensed under GNU GPL v. 3 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Vera Henneberger, 2015-2016.
 */
public class TldrApplication extends Application 
{
	private static Context appContext;
	private static ConnectivityManager connectivityManager;

	@Override
	public void onCreate() 
	{
		super.onCreate();
		TldrApplication.appContext = getApplicationContext();
		TldrApplication.connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

	}
	
	public static Context getAppContext()
	{
		return appContext;
	}

	public static ConnectivityManager getConnectivityManager()
	{
		return connectivityManager;
	}

}
