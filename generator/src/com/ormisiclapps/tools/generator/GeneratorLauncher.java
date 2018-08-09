package com.ormisiclapps.tools.generator;

import com.ormisiclapps.tools.generator.generator.ModelGenerator;
import com.ormisiclapps.tools.generator.ui.GeneratorInterface;

public class GeneratorLauncher {
	private static GeneratorWorld generatorWorld;
	private static ModelGenerator modelGenerator;
    private static GeneratorInterface generatorInterface;

	public static void main (String[] arg) {
		// Create the generator world instance
		generatorWorld = new GeneratorWorld();
		// Setup the world
		generatorWorld.setup();
		// Create the model generator instance
		modelGenerator = new ModelGenerator();
        // Create the interface
        generatorInterface = new GeneratorInterface();
	}

    public static GeneratorWorld getGeneratorWorld() { return generatorWorld; }
    public static GeneratorInterface getGeneratorInterface() { return generatorInterface; }
    public static ModelGenerator getModelGenerator() { return modelGenerator; }
}
