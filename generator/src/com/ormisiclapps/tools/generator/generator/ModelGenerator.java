package com.ormisiclapps.tools.generator.generator;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.ormisiclapps.flappydunkermadness.game.nodes.entity.EntityBodyPartNode;
import com.ormisiclapps.tools.generator.GeneratorLauncher;
import com.ormisiclapps.tools.generator.enumerations.OutputType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;

/**
 * Created by OrMisicL on 6/2/2016.
 * Will generate OBM files from JSON files
 */
public class ModelGenerator {

    public ModelGenerator()
    {

    }

    public void generateModel(String directoryPath)
    {
        GeneratorLauncher.getGeneratorInterface().output("Model generation started:", OutputType.OUTPUT_TYPE_NORMAL);
        GeneratorLauncher.getGeneratorInterface().output("------------------------------------------", OutputType.OUTPUT_TYPE_NORMAL);
        // Create the directory instance
        File directory = new File(directoryPath);
        // Create the files list array
        File[] filesList = directory.listFiles();
        GeneratorLauncher.getGeneratorInterface().output("Found " + filesList.length + " files:", OutputType.OUTPUT_TYPE_NORMAL);
        GeneratorLauncher.getGeneratorInterface().output("", OutputType.OUTPUT_TYPE_NORMAL);
        // Loop through all the files
        for (int i = 0; i < filesList.length; i++)
        {
            // Make sure that its a file
            if(!filesList[i].isFile())
                continue;

            // Get the part name
            String name = filesList[i].getName();
            GeneratorLauncher.getGeneratorInterface().output("Reading file: " + name, OutputType.OUTPUT_TYPE_NORMAL);
            // Create the body definition instance
            BodyDef bodyDef = new BodyDef();
            bodyDef.position.set(2, 2);
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            // Create a body instance
            Body body = GeneratorLauncher.getGeneratorWorld().addToWorld(bodyDef);
            // Create a fixture definition
            FixtureDef fixtureDef = new FixtureDef();
            // Create a polygon shape
            PolygonShape shape = new PolygonShape();
            // Set the body physics
            fixtureDef.shape = shape;
            fixtureDef.density = 0;
            fixtureDef.friction = 0;
            fixtureDef.restitution = 0;
            try
            {
                // Create the file
                File jsonFile = new File(filesList[i].getAbsolutePath());
                // Create the file input stream
                FileInputStream inputStream = new FileInputStream(jsonFile);
                // Create the data array
                byte[] data = new byte[(int)jsonFile.length()];
                // Read file
                inputStream.read(data);
                // Create the text string
                String json = new String(data, "UTF-8");
                // Create the body editor loader
                BodyEditorLoader bodyEditorLoader = new BodyEditorLoader(json);
                // Create the body
                bodyEditorLoader.attachFixture(body, filesList[i].getName().substring(0, filesList[i].getName().indexOf('.')), fixtureDef, 100);
                // Close the file
                inputStream.close();
            }
            catch(RuntimeException e2)
            {
                e2.printStackTrace();
                GeneratorLauncher.getGeneratorInterface().output("File generation failed: File is not a valid JSON format (" + name + ")", OutputType.OUTPUT_TYPE_ERROR);
                continue;
            }
            catch(Exception e3)
            {
                e3.printStackTrace();
                GeneratorLauncher.getGeneratorInterface().output("File generation failed: File is not a valid JSON format (" + name + ")", OutputType.OUTPUT_TYPE_ERROR);
                continue;
            }
            // Create the target directory
            File targetDirectory = new File(directoryPath + "\\" + "Generated\\");
            if(!targetDirectory.exists())
                targetDirectory.mkdir();

            // Get the file handle
            File file = new File(directoryPath + "\\" + "Generated\\" + swapExtension(name));
            GeneratorLauncher.getGeneratorInterface().output("Generating file: " + swapExtension(name), OutputType.OUTPUT_TYPE_NORMAL);
            // Open the file and create an object output stream
            FileOutputStream outStream;
            ObjectOutputStream objStream;
            try
            {
                outStream = new FileOutputStream(file);
                objStream = new ObjectOutputStream(outStream);
            }
            catch(IOException e)
            {
                e.printStackTrace();
                GeneratorLauncher.getGeneratorInterface().output("File generation failed: Failed to open file (" + swapExtension(name) + ")", OutputType.OUTPUT_TYPE_ERROR);
                continue;
            }
            // Get the fixtures iterator
            Iterator<Fixture> iterator = body.getFixtureList().iterator();
            // Loop through all the fixtures
            while(iterator.hasNext())
            {
                // Get the fixture
                Fixture fixture = iterator.next();
                // Get the fixture shape
                PolygonShape fixtureShape = (PolygonShape)(fixture.getShape());
                // Create the vertices array
                Vector2[] shapeVertices = new Vector2[fixtureShape.getVertexCount()];
                // Loop through the fixture vertices
                for(int j = 0; j < fixtureShape.getVertexCount(); j++)
                {
                    // Create the shape vertices instance
                    shapeVertices[j] = new Vector2();
                    // Get the shape vertices
                    fixtureShape.getVertex(j, shapeVertices[j]);
                }
                // Create the body part node
                EntityBodyPartNode node = new EntityBodyPartNode(shapeVertices, fixtureShape.getVertexCount());
                // Write the node
                try
                {
                    objStream.writeObject(node);
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                    GeneratorLauncher.getGeneratorInterface().output("File generation failed: Failed to write to file (" + swapExtension(name) + ")", OutputType.OUTPUT_TYPE_ERROR);
                    continue;
                }
                GeneratorLauncher.getGeneratorInterface().output("Successfully generated: " + swapExtension(name), OutputType.OUTPUT_TYPE_SUCCESS);
            }
            // Remove the test body
            //GeneratorLauncher.getGeneratorWorld().removeFromWorld(body);
            // Close the file
            try
            {
                objStream.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        GeneratorLauncher.getGeneratorInterface().output("", OutputType.OUTPUT_TYPE_NORMAL);
        GeneratorLauncher.getGeneratorInterface().output("Model generation terminated", OutputType.OUTPUT_TYPE_NORMAL);
    }

    public String swapExtension(String file)
    {
        // Remove the json extension
        String result = file.substring(0, file.lastIndexOf('.'));
        return result + ".obm";
    }
}
