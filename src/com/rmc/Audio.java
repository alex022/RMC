package com.rmc;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder.AudioSource;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Audio extends MainActivity {
	private static final int RECORDER_SAMPLERATE = 44100;
	private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
	private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
	private static final int BPS = 16;
	private AudioRecord recorder = null;
	private MediaPlayer player = new MediaPlayer(); 
	private Thread recordingThread = null;
	private boolean isRecording = false;
	String filePath;
	Button playButton;
	
	public static final short FORMAT_PCM = 1;
	byte bData[];
	
	int bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
	            RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING); 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.audio);
	
	    setButtonHandlers();
	    enableButtons(false);
	}
	
	private void setButtonHandlers() {
	    ((Button) findViewById(R.id.start)).setOnClickListener(btnClick);
	    ((Button) findViewById(R.id.stop)).setOnClickListener(btnClick);
	    playButton = (Button)findViewById(R.id.play); 
	    playButton.setOnClickListener(btnClick); 
	  //  playButton.setEnabled(false); 
	}
	
	private void enableButton(int id, boolean isEnable) {
	    ((Button) findViewById(id)).setEnabled(isEnable);
	}
	
	private void enableButtons(boolean isRecording) {
	    enableButton(R.id.start, !isRecording);
	    enableButton(R.id.stop, isRecording);
	}
	
	int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
	int BytesPerElement = 2; // 2 bytes in 16bit format
	
	private void startRecording() {
										
	    recorder = new AudioRecord(AudioSource.MIC, RECORDER_SAMPLERATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
	    
	    recorder.startRecording();
	    isRecording = true;
	    recordingThread = new Thread(new Runnable() {
	        public void run() {
	            writeAudioDataToFile();
	        }
	    }, "AudioRecorder Thread");
	    recordingThread.start();
	}
	
	    //convert short to byte
	private byte[] short2byte(short[] sData) {
	    int shortArrsize = sData.length;
	    byte[] bytes = new byte[shortArrsize * 2];
	    for (int i = 0; i < shortArrsize; i++) {
	        bytes[i * 2] = (byte) (sData[i] & 0x00FF);
	        bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
	        sData[i] = 0;
	    }
	    return bytes;	
	}
	
	private void writeAudioDataToFile() {
	    // Write the output audio in byte
	
	    filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
		filePath+= "/audioFile.wav";
		
	    short sData[] = new short[BufferElements2Rec];
	
	    FileOutputStream os = null;
	    try {
	        os = new FileOutputStream(filePath);
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	
	    while (isRecording) {
	        // gets the voice output from microphone to byte format
	    	
	        recorder.read(sData, 0, BufferElements2Rec);
	        System.out.println("Short writing to file" + sData.toString());
	        try {
	            // // writes the data to file from buffer
	            // // stores the voice buffer
	            bData = short2byte(sData);
	            
	            write(os); 
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    try {	    	
	        os.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
			
	private void stopRecording() {
	    // stops the recording activity
	    if (null != recorder) {
	        isRecording = false;
	        recorder.stop();
	        recorder.release();
	        recorder = null;
	        recordingThread = null;
	    }
	}
	
	private View.OnClickListener btnClick = new View.OnClickListener() {
	    public void onClick(View v) {
	        switch (v.getId()) {
	        case R.id.start: {
	            enableButtons(true);
	            startRecording();
	            break;
	        }
	        case R.id.stop: {
	            enableButtons(false);
	            stopRecording();
	            playButton.setEnabled(true);
	            break;
	        }
	        case R.id.play: {
	        	try {
	        		Toast.makeText(currentActivity, "Sending audio", Toast.LENGTH_LONG).show(); 
					playAudio();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	break;
	        }
	        }
	    }
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}
			
	public void playAudio() throws IOException
	{
		/*try {
			player.reset(); 
	        player.setDataSource(filePath);
	        player.prepare();
	        player.start();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }*/
		
		int bytesRead;
		FileInputStream is = new FileInputStream(filePath);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		while ((bytesRead = is.read(b)) != -1) {
		   bos.write(b, 0, bytesRead);
		}
		byte[] bytes = bos.toByteArray();
		
		
		is.close();
		
		for(int i = 0; i < 44; i++)
			Log.wtf("Audio", Integer.toHexString(bytes[i]));
		
		//Thread writeThread = new Thread(new writeThread("*AUDIO*", bytes, bytes.length, null, null)); 
		//writeThread.start(); 		
	}
	
	public void write(OutputStream out) throws IOException {
        /* RIFF header */
	
		writeId(out, "RIFF");
		writeInt(out, 36 + bData.length);
        writeId(out, "WAVE");
        
        /* fmt chunk */
        writeId(out, "fmt ");
        writeInt(out, 16);
        writeShort(out, FORMAT_PCM);
        writeShort(out, (short)1);
        writeInt(out, RECORDER_SAMPLERATE);
        writeInt(out, 1 * RECORDER_SAMPLERATE * BPS / 8);
        writeShort(out, (short)(1 * BPS / 8));
        writeShort(out, (short)BPS);

        /* data chunk */
        writeId(out, "data");
        writeInt(out, bData.length); 
        
		out.write(bData, 0, bData.length); 
	}
	
	
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
	private static void writeId(OutputStream out, String id) throws IOException {
        for (int i = 0; i < id.length(); i++) out.write(id.charAt(i));
	}
        
        private static void writeInt(OutputStream out, int val) throws IOException {
            out.write(val >> 0);
            out.write(val >> 8);
            out.write(val >> 16);
            out.write(val >> 24);
    }
        
        private static void writeShort(OutputStream out, short val) throws IOException {
            out.write(val >> 0);
            out.write(val >> 8);
        }
}
