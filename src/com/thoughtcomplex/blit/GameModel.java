package com.thoughtcomplex.blit;
public class GameModel {

	/*
		GameModel - A closed-system interactive simulation. It is assumed that a player-controlled actor will move out of and into each closed system in turn, creating
		a series of discrete environments for puzzles and obstacles. Generally speaking, action will be viewed from the side, so much of the simulation
		will involve gravity and the actions of rigid bodies. We don't worry -too- much about drawing details HERE, just what's relevant to provide a robust and relevant
		simulation.
		
		
		Pieces of the simulation:
		
			The Tile Map, which greatly simplifies and speeds up collisions. Much as one would expect, the tilemap consists of a bitmask representing evenly spaced
			axis-aligned squares, which collectively cover the entire "playable surface" and either obstruct or permit objects depending on their value. But what may be
			unexpected is that these squares may be at any logical resolution - whatever suits the room best.
			
			The Polygon Map is a kind of smooth overlay over the Tile Map. Collisions against this map work a little differently from other maps. Each simulation object can
			be thought of as a vertical line running down the exact center of its bounding box and occupying its full height. This way objects can "walk" up slopes without
			hanging on by just the upslope corner of the bounding box, which usually just looks like the object is in the air.
			
			The Simulation Objects are a rigid body simulation. All simulation objects have an axis-aligned bounding box which is used for primary collisions, and many also have a secondary "collision map" which is used for fine-grained collision confirmations. The actual image for the object may hang over the edges of this
			bounding box or map, and while offsets ARE stored in the model it's not really germane to the simulation. Objects can be solid or non-solid with respect to
			each other, they can be affected by gravity (or other field-effect forces such as wind or magnetism), they can have their own AI and scripting, and many will
			have special rules that are conducive to designing (and solving!) puzzles.
	*/

	/*
		Escape-Specific Pieces of the Simulation
		
		Items: FINDING FLUX AND USING IT DECREASES THE EFFECTIVENESS OF CORVUS CORAX VARIUS MORPHA
			{1*} JUMP JETS				increases jump height
			{1*} JETPACK				allows limited flight
			{1*} LASER
			{1*} MISSILE
			[~4$} ????					even higher jump (requires jump jets)
			{~6$} ????					add'l activation time for jetpack, probably in the form of cooling technology
			
			{#} or {~#}=found in specific quantities in the world {*}=mandatory for win {$}=optional, bought with flux
	*/



}