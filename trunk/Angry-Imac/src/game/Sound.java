package game;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Sound extends Thread{

	private AudioFormat format;

	private byte[] samples;
	InputStream finalStream;

	public Sound(String filename) {
		try {
			AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filename));
			format = stream.getFormat();
			samples = getSamples(stream);
		} catch (Exception e) {
		}
		finalStream = new ByteArrayInputStream(this.getSamples());
	}

	public byte[] getSamples() {
		return samples;
	}

	public byte[] getSamples(AudioInputStream stream) {
		int length = (int) (stream.getFrameLength() * format.getFrameSize());
		byte[] samples = new byte[length];
		DataInputStream in = new DataInputStream(stream);
		try {
			in.readFully(samples);
		} catch (Exception e) {
		}
		return samples;
	}

	public void play(InputStream source) {
		int bufferSize = format.getFrameSize() * Math.round(format.getSampleRate() / 10);
		byte[] buffer = new byte[bufferSize];
		SourceDataLine line;
		try {
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(format, bufferSize);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			return;
		}
		line.start();
		try {
			int numBytesRead = 0;
			while (numBytesRead != -1) {
				numBytesRead = source.read(buffer, 0, buffer.length);
				if (numBytesRead != -1)
					line.write(buffer, 0, numBytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		line.drain();
		line.close();
	}
	
	public void loop(){
		this.play(finalStream);
		try {
			finalStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finalStream = new ByteArrayInputStream(this.getSamples());
		this.loop();
	}
	
	public void run(){
		this.loop();
	}
}