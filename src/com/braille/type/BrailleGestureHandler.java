package com.braille.type;

import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;;


public class BrailleGestureHandler extends SimpleOnGestureListener {

	private Context mCtx = null;
	private BrailleTyperActivity2 brailleActivity; 
	private int width;
	private int SWIPE_MIN_DISTANCE = 50;
	private int SWIPE_THRESHOLD_VELOCITY = 50;

	BrailleGestureHandler(Context ctx){
		mCtx = ctx;
		brailleActivity = (BrailleTyperActivity2) mCtx;
		WindowManager wm = (WindowManager) mCtx.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		width = display.getWidth();
	}

	@Override
	public boolean onDown(MotionEvent e){
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){

		double x1=e1.getX(), x2=e2.getX(), y1=e1.getY(), y2=e2.getY();
		Double radius = Math.pow(Math.pow(x1-x2,2)+Math.pow(y1-y2,2),.5);
		Double tan = (y2-y1)/(x2-x1);
		Boolean right = x1>width/2;

		Log.i("Swipe detected", "Radius "+radius.toString()+", Tan "+tan.toString());
		
		if(radius<SWIPE_MIN_DISTANCE)	return false;

		if(tan<-2 || tan>2){
			if(y2>y1)	brailleActivity.swipeUp(right);
			else 		brailleActivity.swipeDown(right);
		} else {
			if(x2>x1)	brailleActivity.swipeRight();
			else 		brailleActivity.swipeLeft();
		}
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e){
		brailleActivity.singleTap(e.getX()<width/2);
		Log.i("Single tap","Single tap confirmed.");
		return true;
	}
}
