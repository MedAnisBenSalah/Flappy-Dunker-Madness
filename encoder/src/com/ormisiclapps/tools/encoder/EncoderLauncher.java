package com.ormisiclapps.tools.encoder;

import com.ormisiclapps.tools.encoder.encoder.FileEncoder;
import com.ormisiclapps.tools.encoder.ui.EncoderInterface;

public class EncoderLauncher {
	private static FileEncoder encoder;
    private static EncoderInterface encoderInterface;

	public static void main (String[] arg) {
		// Create the file encoder instance
        encoder = new FileEncoder();
        // Create the interface
        encoderInterface = new EncoderInterface();
	}

    public static EncoderInterface getInterface() { return encoderInterface; }
    public static FileEncoder getEncoder() { return encoder; }
}
