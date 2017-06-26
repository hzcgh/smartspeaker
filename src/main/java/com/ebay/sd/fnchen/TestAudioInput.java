package com.ebay.sd.fnchen;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Peter on 6/24/2017.
 */
public class TestAudioInput {
    private static AtomicBoolean stopped = new AtomicBoolean(false);

    public static void main(String[] args) throws LineUnavailableException {
        final Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(5000);
                    stopped.set(true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
        TargetDataLine line;
        DataLine.Info info = new DataLine.Info(TargetDataLine.class,
                format); // format is an AudioFormat object
        if (!AudioSystem.isLineSupported(info)) {
            // Handle the error ...

            System.out.println("Not Supported");
        }
        // Obtain and open the line.
        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);

            // Assume that the TargetDataLine, line, has already
            // been obtained and opened.
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int numBytesRead;
            byte[] data = new byte[line.getBufferSize() / 5];

            // Begin audio capture.
            line.start();

            // Here, stopped is a global boolean set by another thread.
            while (!stopped.get()) {
                // Read the next chunk of data from the TargetDataLine.
                numBytesRead = line.read(data, 0, data.length);
                // Save this chunk of data.
                out.write(data, 0, numBytesRead);
            }

            System.out.println("length: " + out.size());

        } catch (LineUnavailableException ex) {
            // Handle the error ...
            ex.printStackTrace();
        }



/*        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        for (Mixer.Info info: mixerInfos) {
            Mixer m = AudioSystem.getMixer(info);
            Line.Info[] lineInfos = m.getSourceLineInfo();
            for (Line.Info lineInfo : lineInfos) {
                System.out.println(info.getName() + "---" + lineInfo);
                Line line = m.getLine(lineInfo);
                System.out.println("\t-----" + line);
            }
            lineInfos = m.getTargetLineInfo();
            for (Line.Info lineInfo : lineInfos) {
                System.out.println(m + "---" + lineInfo);
                Line line = m.getLine(lineInfo);
                System.out.println("\t-----" + line);

            }
        }*/
    }
}
