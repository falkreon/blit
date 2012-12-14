package com.thoughtcomplex.blit.ld21;
public class RoomLibrary {
	private RoomLibrary() throws InstantiationException { throw new InstantiationException("The RoomLibrary class contains static helper methods and cannot be instantiated."); }
	
	public static Room getRoom(String roomName) {
		String name = roomName.toLowerCase();
		
		if (name=="lights") {
			Room curRoom = new Room(20,10);
			curRoom.setTileWalls(4);
			curRoom.setTileRect(1,1,18,5, 4);
			return curRoom;
		}
		
		if (name=="spawn") {
			Room curRoom = new Room(20,10);
			curRoom.setTileWalls(3);
			
			Door d = new Door(18,6,"crux"); d.facingRight=false; curRoom.exits.add(d);
			
			MessageQueue queue = curRoom.openScript;
			
			MessageQueue dialogue = new MessageQueue();
			dialogue.insert(new FlagSetMessage("demo"));
			dialogue.insert(new DialogueMessage("System: Welcome to the Escape demo, where you will most certainly survive.",null));
			dialogue.insertDelay(1);
			dialogue.insert(new DialogueMessage("Sam: I don't like that tone of voice... are you sure this demo is going to be safe?",null));
			dialogue.insertDelay(1);
			dialogue.insert(new DialogueMessage("System: Of course. There is less than a 78% chance that you will die. Those are great odds.",null));
			dialogue.insertDelay(1);
			dialogue.insert(new DialogueMessage("Sam: Okay.... what?",null));
			dialogue.insertDelay(1);
			dialogue.insert(new DialogueMessage("System: Since the Interplanetary Trade Treaty, forming workers' safety organizations is forbidden.",null));
			dialogue.insertDelay(1);
			dialogue.insert(new DialogueMessage("System: It hurts small business. This has been confirmed in legislatures galaxy-wide.",null));
			dialogue.insertDelay(1);
			dialogue.insert(new DialogueMessage("Sam: So I could complain to OSHA, but they're 140,000 light-years away. Got it.",null));
			dialogue.insertDelay(1);
			dialogue.insert(new DialogueMessage("Sam: Is this demo really necessary?",null));
			dialogue.insertDelay(1);
			dialogue.insert(new DialogueMessage("System: Absolutely. We need to test the effectiveness of your suit armor.",null));
			dialogue.insertDelay(1);
			dialogue.insert(new DialogueMessage("Sam: ...",null));
			
			MessageQueue review = new MessageQueue();
			review.insert(new DialogueMessage("System: Please proceed to the right to see additional test chambers.",null));
			
			queue.insert(new ConditionMessage("demo",review,dialogue));
			return curRoom;
		}
		
		/*if (name=="spawn") {
			Room curRoom = new Room(50,15);
			curRoom.setTileWalls(3);
		
			//platforms
			
			curRoom.setTileRect(1,9,3,1,1);
			curRoom.setTileRect(1,10,10,4,2);
			curRoom.setTileRect(8,1,3,7,1);
			curRoom.setTileRect(12,1,1,13,-6);
			curRoom.setTileRect(13,5,5,12,4);
			curRoom.setTileRect(18,5,5,1,1);
			curRoom.setTileRect(19,8,5,1,1);
			curRoom.setTileRect(18,11,5,1,1);
			curRoom.setTileRect(24,1,1,11,1);
			
			curRoom.buildLine(26,14,30,7, 2);
			curRoom.setTileRect(31,6,4,4,1);
			curRoom.setTileRect(30,8,1,6,-6);
			curRoom.setTileRect(39,7,1,7,1);
			curRoom.setTileRect(41,1,1,11,1);
			curRoom.setTileRect(42,10,8,1,3);
			
			Door d = new Door(48,11,"crux"); d.facingRight=false;
			curRoom.exits.add(d);
			
			MessageQueue queue = curRoom.openScript;
			
			MessageQueue dialogue = new MessageQueue();
			dialogue.insert(new FlagSetMessage("spawn"));
			dialogue.insertDelay(2);
			dialogue.insert(new DialogueMessage("System: There is an electrical storm headed this way.",null));
			dialogue.insertDelay(2);
			dialogue.insert(new DialogueMessage("Sam: Argh! I'm not done locking these systems down. How much time do we have?",null));
			dialogue.insertDelay(2);
			dialogue.insert(new DialogueMessage("System: Approximately 48 hours. Essential personnel have already been evacuated.",null));
			dialogue.insertDelay(2);
			dialogue.insert(new DialogueMessage("Sam: Yikes! I haven't been under this much pressure since the Ludum Dare!",null));
			dialogue.insertDelay(2);
			dialogue.insert(new DialogueMessage("Sam: Wait, so when the big guy found out he evacuated HIMSELF first?!?",null));
			dialogue.insertDelay(2);
			dialogue.insert(new DialogueMessage("System: I am not allowed to question leadership of Level A employees.",null));
			dialogue.insertDelay(2);
			dialogue.insert(new DialogueMessage("System: Level C personnel shall now evacuate through the door to your right.",null));
			
			MessageQueue review = new MessageQueue();
			
			review.insertDelay(2);
			review.insert(new DialogueMessage("System: Please move in an orderly fashion through the door to your right.",null));
			review.insertDelay(2);
			review.insert(new DialogueMessage("Sam: What about you?",null));
			review.insertDelay(2);
			review.insert(new DialogueMessage("System: I have electrical shielding. You do not. Get moving.",null));
			
			queue.insert(new ConditionMessage("spawn",review,dialogue));
			
			return curRoom;
		}*/
		
		if (name=="crux") {
			Room curRoom = new Room(20,20);
			curRoom.setTileWalls(1);
			curRoom.buildLine(1,14,4,18,1);
			Door d = new Door(0,11,"spawn"); d.facingRight=true;
			curRoom.exits.add(d);
			d = new Door(18,4,"ascent"); d.facingRight=false; d.targetY=1;
			curRoom.exits.add(d);
			d = new Door(18,16,"darkening"); d.facingRight=false; d.targetY=-1;
			curRoom.exits.add(d);
			
			curRoom.buildLadder(8,7,12);
			curRoom.setTileRect(9,8,1,7, 1);
			curRoom.setTileRect(9,7,10,1, 1);
			
			curRoom.setTileRect(12,1,3,6, 14);
			
			return curRoom;
		}
		
		if (name=="ascent") {
			Room curRoom = new Room(20,90);
			curRoom.setTileWalls(2);
			
			curRoom.setTileRect(1,85,2,1 ,2);
			curRoom.setTileRect(8,83,4,1 ,2);
			curRoom.setTileRect(8,76,4,1 ,2);
			curRoom.buildLadder(11,76,7);
			
			
			curRoom.setTileRect(1,72,3,1, 2);
			curRoom.setTileRect(8,68,4,1, 2);
			curRoom.setTileRect(16,64,3,1,2);
			curRoom.setTileRect(8,60,4,1, 2);
			curRoom.setTileRect(1,56,3,1, 2);
			curRoom.setTileRect(8,52,4,1, 2);
			curRoom.setTileRect(16,48,3,1,2);
			curRoom.setTileRect(8,44,4,1, 2);
			curRoom.setTileRect(1,40,3,1, 2);
			
			
			Door d = new Door(0,86,"crux"); d.facingRight=true; d.targetY=-1;
			curRoom.exits.add(d);
			
			d = new Door(18,86,"violence"); d.facingRight=false; d.targetY=1;
			curRoom.exits.add(d);
			
			d = new Door(0,53,"jetpack"); d.facingRight=true; d.targetY=1;
			curRoom.exits.add(d);
			
			return curRoom;
		}
		
		if (name=="violence") {
			Room curRoom = new Room(40,20);
			curRoom.setTileWalls(2);
		
			curRoom.setTileID(35,18, -15);
		
			Door d = new Door(0,16,"ascent"); d.facingRight=true; d.targetY=1;
			curRoom.exits.add(d);
		
			return curRoom;
		}
		
		if (name=="darkening") {
			Room curRoom = new Room(20,20);
			curRoom.setTileWalls(4);
			
			MessageQueue queue = curRoom.openScript;
			
			MessageQueue dialogue = new MessageQueue();
			dialogue.insertDelay(2);
			dialogue.insert(new DialogueMessage("System: Something appears to be wrong with the next door in our evacuation route.",null));
			dialogue.insertDelay(2);
			dialogue.insert(new DialogueMessage("Sam: No kidding! It looks alive!",null));
			dialogue.insertDelay(2);
			dialogue.insert(new DialogueMessage("Sam: There's got to be something nearby I can use to get through there.",null));
			dialogue.insertDelay(2);
			dialogue.insert(new DialogueMessage("Sam: I mean, we have contingencies for this sort of thing, right?",null));
			dialogue.insertDelay(2);
			dialogue.insert(new DialogueMessage("System: We do not have a contingency for strange alien goo clinging to doors, no.",null));
			
			queue.insert(dialogue);
			
			
			Door d = new Door(0,1,"crux"); d.facingRight=true; d.targetY=1;
			curRoom.exits.add(d);
			d = new Door(18,16,"barrier"); d.facingRight=false; d.targetY=1; d.infected=true;
			curRoom.exits.add(d);
			d = new Door(0,16,"armory"); d.facingRight=true; d.targetY=1;
			curRoom.exits.add(d);
			
			return curRoom;
		}
		
		if (name=="barrier") {
			Room curRoom = new Room(20,20);
			curRoom.setTileWalls(4);
			
			
			
			
			Door d = new Door(0,16,"darkening"); d.facingRight=true; d.targetY=1;
			curRoom.exits.add(d);
			
			
			
			return curRoom;
		}
		
		if (name=="armory") {
			Room curRoom = new Room(50,20);
			curRoom.setTileWalls(4);
			
			curRoom.setTileRect(2,16,3,1, 5);
			curRoom.setTileRect(2,15,3,1, 4);
			
			curRoom.setTileRect(1,17,6,2, 4);
			
			curRoom.setTileID(3,14, -13);
			
			curRoom.setTileRect(10,17,4,2,  14);
			curRoom.setTileRect(10,15,2,2,  14);
			curRoom.setTileRect(10,13,1,2,  14);
			curRoom.setTileRect(9,11,1,6,  14);
			
			Door d = new Door(48,16,"darkening"); d.facingRight=false; d.targetY=1;
			curRoom.exits.add(d);
			
			
			//testEnemy.width=8; testEnemy.height=8;
			for(int i=0; i<6; i++) {
				RavenMob testEnemy = new RavenMob();
				//testEnemy.curImage = ResourceBroker.loadImage("hvitravnur.png");
				testEnemy.x = 21*8+(int)(Math.random()*(80)); testEnemy.y = 8*8;
				curRoom.monsters.add(testEnemy);
			}
			return curRoom;
		}
		
		if (name=="jetpack") {
			//miniboss room
		}
		
		
		Room r = new Room(20,18); r.setTileWalls(4);
		return r;
	}

}