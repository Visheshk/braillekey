package com.braille.type;

import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;

public class BrailleTyperActivity2 extends Activity implements TextToSpeech.OnInitListener {

	View placeholder = null;
	TextToSpeech tts;       
	int enterno=0; 
	int senti=0;
	int total1=0,total2=0,total=0;
	String printtotal;
	char array[]=new char[78];
	char sentence[]= new char[100];
	private GestureDetector gestureDetector; 

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		arrayinit();
		tts = new TextToSpeech(this, this);
		final BrailleGestureHandler activityWideGestureListener = new BrailleGestureHandler(this);
		gestureDetector = new GestureDetector(this, activityWideGestureListener);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event))
			return true;
		else
			return false;
	} 

	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{ 
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) { 
			TextView tv2 = new TextView(this);
			tv2 = (TextView) findViewById(R.id.textView1);
			TextView tv3 = new TextView(this);
			tv3 = (TextView) findViewById(R.id.textView2);
			tv2.setText(" ");
			tv3.setText(" ");
			TextView tv = new TextView(this);
			tv=(TextView) findViewById(R.id.textView3);
			tv.setText("");
			for(int i=0; i<senti;i++)
				tv.append(String.valueOf(sentence[i]));
			speakOut(3);
			return true;
		} else {
			return super.onKeyDown(keyCode, event); 
		}
	}

	public void arrayinit(){
		array[10]='a';
		array[30]='b';
		array[11]='c';
		array[13]='d';
		array[12]='e';
		array[31]='f';
		array[33]='g';
		array[32]='h';
		array[21]='i';
		array[23]='j';
		array[50]='k';
		array[70]='l';
		array[51]='m';
		array[53]='n';
		array[52]='o';
		array[71]='p';
		array[73]='q';
		array[72]='r';
		array[61]='s';
		array[63]='t';
		array[54]='u';
		array[74]='v';
		array[27]='w';
		array[55]='x';
		array[57]='y';
		array[56]='z';
	}

	public void convertToBits(int totalprint){
		switch (totalprint){
		case 1:
			printtotal = "1";
			break;
		case 2:
			printtotal = "2";
			break;
		case 3:
			printtotal = "1 " + "2";
			break;
		case 4:
			printtotal = "3";
			break;
		case 5:
			printtotal = "1 " + "3";
			break;
		case 6:
			printtotal = "2 " + "3";
			break;
		case 7:
			printtotal = "1 " + "2 " + "3";
			break;
		default:
			printtotal = "Invalid";
		}
	}

	public void leftadd(int i){
		enterno=0;
		if(total1%2*i-total1%i==i)
			return;
		total1+=i;
		convertToBits(total1);
		TextView tv = new TextView(this);
		tv=(TextView) findViewById(R.id.textView1);
		tv.setText(printtotal);
		speakOut(1);
	}


	public void rightadd(int i){
		enterno=0;
		if(total2%2*i-total2%i==i)
			return;
		total2+=i;
		convertToBits(total2);
		TextView tv = new TextView(this);
		tv=(TextView) findViewById(R.id.textView2);
		tv.setText(printtotal);
		speakOut(2);
	}

	public void fnenter(){
		enterno++;
		TextView tv = new TextView(this);
		tv=(TextView) findViewById(R.id.textView3);
		TextView tv2 = new TextView(this);
		tv2 = (TextView) findViewById(R.id.textView1);
		TextView tv3 = new TextView(this);
		tv3 = (TextView) findViewById(R.id.textView2);
		tv2.setText(" ");
		tv3.setText(" ");
		if(enterno==1)
		{
			int total= total1*10+total2;
			total1=0;
			total2=0;			
			if(array[total]!='\u0000')
			{	
				sentence[senti++]=array[total];
				tv.setText(String.valueOf(array[total]));		//array[total];
			}
			else 
				tv.setText("No such character");	
		}
		if(enterno==2)
		{
			sentence[senti++]= ' ';
			tv.setText("Space") ;
		}
		speakOut(3);
	}

	public void fndelete(){

		TextView tv2 = new TextView(this);
		tv2 = (TextView) findViewById(R.id.textView1);
		TextView tv3 = new TextView(this);
		tv3 = (TextView) findViewById(R.id.textView2);
		tv2.setText(" ");
		tv3.setText(" ");

		TextView tv = new TextView(this);
		tv=(TextView) findViewById(R.id.textView3);


		if((total1!=0)||(total2!=0))
		{
			total1=0;
			total2=0;
			tv.setText("Current letter cleared");
		}
		else
		{
			if(senti>0){
				if(sentence[--senti]!=' ')
					tv.setText("Deleted the letter "+sentence[senti]);
				else
					tv.setText("Deleted Space");
				sentence[senti]='\u0000';
			}
			else
				tv.setText("No more text present ");
			speakOut(3);
		}


		/* full sentence display
    	 tv.setText("");
    	for(int i=0; i<senti;i++)
    	tv.append(String.valueOf(sentence[i]));*/
	}
	@Override

	public void onDestroy() {
		// Don't forget to shutdown tts!
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}

	@Override
	public void onInit(int status) {

		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.UK);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} else { speakOut(1);
			}

		} else {
			Log.e("TTS", "Initialization Failed!");
		}

	}

	private void speakOut(int number) {
		TextView tv = new TextView(this);
		switch (number){
		case 1:
			tv=(TextView) findViewById(R.id.textView1);
			String text = tv.getText().toString();
			tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
			break;
		case 2:
			tv=(TextView) findViewById(R.id.textView2);
			String text2 = tv.getText().toString();
			tts.speak(text2, TextToSpeech.QUEUE_FLUSH, null);
			break;
		case 3:
			tv=(TextView) findViewById(R.id.textView3);
			String text3 = tv.getText().toString();
			tts.speak(text3, TextToSpeech.QUEUE_FLUSH, null);
			break;
		}

	}
	/**
	 * Integration with BrailleGestureListener
	 */

	public void swipeUp(Boolean right){
		if(!right){
			leftadd(1);
		} else {
			rightadd(1);
		}
	}

	public void singleTap(Boolean right){
		if(!right){
			leftadd(2);
		} else {
			rightadd(2);
		}
	}

	public void swipeDown(Boolean right){
		if(!right){
			leftadd(4);
		} else {
			rightadd(4);
		}
	}

	public void swipeLeft(){
		fndelete();
	}

	public void swipeRight(){
		fnenter();
	}	
}
