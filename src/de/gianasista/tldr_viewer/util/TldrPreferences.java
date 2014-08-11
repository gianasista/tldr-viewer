package de.gianasista.tldr_viewer.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import de.gianasista.tldr_viewer.TldrApplication;

/**
 * @author vera
 */
public class TldrPreferences {

	private static final String TLDR_PREFS_NAME = "tldr-prefs";
	private static final String KEY_PLATFORM = "KEY_PLATFORM";
	private static final String DEFAULT_PLATFORM = "common";
	
	public static final String[] possibleChoices = new String[] {"Common", "OSX", "Linux", "SunOS"};
	
	public static String getCurrentPlatformFile()
	{
		String platformPrefString = getCurrentPlatformFromPreferences();
		return platformPrefString+".txt";
	}

	public static String getCurrentPlatformFromPreferences() {
		return getPrefs().getString(KEY_PLATFORM, DEFAULT_PLATFORM);
	}
	
	@SuppressLint("DefaultLocale")
	public static void setCurrentPlatform(String platform)
	{
		SharedPreferences.Editor editor = getPrefs().edit();
		editor.putString(KEY_PLATFORM, platform.toLowerCase());
		editor.commit();
	}

	private static SharedPreferences getPrefs() 
	{
		return TldrApplication.getAppContext().getSharedPreferences(TLDR_PREFS_NAME, Context.MODE_PRIVATE);
	}
	
	public static int getIndexOfCurrentPlaformChoice()
	{
		String platformFromPrefs = getCurrentPlatformFromPreferences();
		for(int i=0; i<possibleChoices.length; i++)
		{
			String possibleChoice = possibleChoices[i];
			if(platformFromPrefs.equalsIgnoreCase(possibleChoice))
			{
				return i;
			}
				
		}
		
		return -1;
	}
}