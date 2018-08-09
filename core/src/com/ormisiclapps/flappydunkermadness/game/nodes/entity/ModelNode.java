package com.ormisiclapps.flappydunkermadness.game.nodes.entity;

import com.badlogic.gdx.math.Vector2;

public class ModelNode
{
	public Vector2 size;

	public float mass;
	public float friction;
	public float restitution;

	public Vector2[] fixturePosition;
	public float[] fixtureRadius;

	public int movementsCount;

	public ModelNode(int fixturesCount)
	{
		// Create vectors
		size = new Vector2();
		// Create arrays
		fixturePosition = new Vector2[fixturesCount];
		fixtureRadius = new float[fixturesCount];
	}

	public void setNode(Vector2 size, float mass, float friction, float restitution, int movementsCount)
	{
		this.size.set(size);
		this.mass = mass;
		this.friction = friction;
		this.restitution = restitution;
		this.movementsCount = movementsCount;
	}

	public void addFixture(int id, Vector2 position, float radius)
	{
		fixturePosition[id] = new Vector2(position);
		fixtureRadius[id] = radius;
	}
}