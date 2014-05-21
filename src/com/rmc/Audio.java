package com.rmc;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class Audio extends MainActivity{
	
	private String fileName = null; 	
	private Context context = this; 
	private MediaRecorder audioRecorder = null; 
	private MediaPlayer audioPlayer = null;
	private RecordButton recordButton = null;
	private PlayButton playButton = null; 
	private Button sendButton = null;
	
	public Audio() {
        fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        fileName += "/audiorecordtest.3gp";
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundResource(R.drawable.bones);
        
		recordButton = new RecordButton(this);
		//recordButton.setTextSize(30);		
        
        ll.addView(recordButton,
            new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0));
        
        playButton = new PlayButton(this);
        //playButton.setTextSize(50);  
        
        
        ll.addView(playButton,
            new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0));
        
		
        sendButton = new Button(this);
        sendButton.setText("Send");
        //sendButton.setTextSize(30);
        
        ll.addView(sendButton,
                new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    0));       
        
        setContentView(ll);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();		
		
        sendButton.setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v)
        	{
        		File file = new File(fileName); 
        		int size = (int)file.length();
        		Log.wtf("Size of file is", Long.toString(size) + " bytes"); 
        		InputStream soundStream = null; 
        		byte[] data = new byte[size];
        		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        		int numRead;
        		
        		while(true)
        		{
	        		try {
	        			//soundStream = context.getAssets().open(fileName);
	        			soundStream = new BufferedInputStream(new FileInputStream(file));  
	        			break;
					} catch (Exception e) {
						Log.wtf("write", "Failed to open sound file");
					}      
        		}
        		
        		while(true)
        		{
	        		try {
	        			while ((numRead = soundStream.read(data, 0, data.length)) != -1) {
	        				  buffer.write(data, 0, numRead);
	        				}						
	        			break; 
					} catch (IOException e) {
						Log.wtf("write", "Failed to convert sound file to byte array"); 
					}
        		}
        		
        		try {
					buffer.flush();
				} catch (IOException e) {					
					Log.wtf("write", "Failed to flush buffer"); 
				}
        		
    		
				try {
					soundStream.close();
				} catch (IOException e) {
					Log.wtf("write", "Failed to close sound stream"); 
				} 
        		
        		writeFile(buffer.toByteArray(), size); 
            }
        });
        
	}
	
	@Override
    public void onPause() {
        super.onPause();
        if (audioRecorder != null) {
        	audioRecorder.release();
        	audioRecorder = null;
        }

        if (audioPlayer != null) {
        	audioPlayer.release();
        	audioPlayer = null;
        }
    }
	
	private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }
	
	private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }
	
	private void startPlaying() {
        audioPlayer = new MediaPlayer();
        try {
        	audioPlayer.setDataSource(fileName);
        	audioPlayer.prepare();
        	audioPlayer.start();
        } catch (IOException e) {
            Log.e("startPlaying()", "prepare() failed");
        }
    }
	
	private void stopPlaying() {
        audioPlayer.release();
        audioPlayer = null;
    }
	
	private void startRecording() {
        audioRecorder = new MediaRecorder();
        audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        audioRecorder.setOutputFile(fileName);
        audioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
        	audioRecorder.prepare();
        } catch (IOException e) {
            Log.e("startRecording()", "prepare() failed");
        }

        audioRecorder.start();
    }
	
	private void stopRecording() {
        audioRecorder.stop();
        audioRecorder.release();
        audioRecorder = null;
    }
	
	
	class RecordButton extends Button {
		public RecordButton(Context mContext) {
            super(mContext);
            setText("Start recording");
            setOnClickListener(clicker);
        }

		boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };
	}
	
	class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context mContext) {
            super(mContext);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }

}
