package com.virtualprodigy.studypro.Utils;

import android.content.Context;
import android.media.MediaPlayer;

public class alertSound {
	private static MediaPlayer mp = null;
	
	/*stops a song and starts a new on e*/
	public static void play(Context context, int resource){
		stop(context);
		mp= MediaPlayer.create( context, resource);
		mp.setLooping(false);
		mp.start();
		
	}
	/*stops music*/
	public static void stop (Context context){
		if(mp!= null){
			mp.stop();
			mp.release();
			mp = null;
		}
	}
}



