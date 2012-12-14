package com.thoughtcomplex.blit.ld21;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.util.Vector;
import javax.sound.sampled.Clip;

public class GameWindow extends JFrame implements KeyListener {

	long lastRenderStart = -1;
	long targetRenderTicks = 40; //change this as I get an idea of how fast the game should run. 40 seems quite smooth
	BufferStrategy buf = null;
	Shadowbox curShadowbox;
	Room curRoom = new Room(50,15);
	Color[] terrainColors = new Color[255];
	Image[] terrainImages = new Image[255];
	Mob player = new Mob();
	Camera camera = new Camera();
	Image playerLeft = null;
	Image playerRight = null;
	Image doorLeft = null;
	Image doorRight = null;
	Image idoorLeft = null;
	Image idoorRight = null;
	Image doorClosedRight = null;
	Image doorClosedLeft = null;
	
	int blockSize = 10;
	int coordSize = 8;
	int prevHeight = 0;
	
	double terminalVelocity = 3.0;
	double xVelocity = 1.5;
	double acceleration = 0.2;
	double drag = 0.05;
	
	boolean leftPressed = false;
	boolean rightPressed = false;
	boolean upPressed = false;
	boolean downPressed = false;
	boolean actionPressed = false;
	
	boolean onGround = false;
	boolean jetpackActive = false;
	int jumpCharge = 2;
	int maxJumpCharge = 2;
	int jetpackCharge = 10;
	int maxJetpackCharge = 40;
	boolean jumpLock = false;
	boolean jumping = false;
	boolean hasJetpack = true;
	boolean hasGun = false;
	int gunReload = 0;
	int maxGunReload = 8;
	String displayString = "Ready.";
	DialogueMessage curDialogue = null;
	//Font dialogueFont = null;
	public MessageQueue queue = new MessageQueue();
	Clip gunSound = null;
	Clip damageSound = null;
	Clip hurtSound = null;
	Clip boomSound = null;
	Clip plinkSound = null;
	Clip powerupSound = null;
	
	Vector<Bullet> bullets = new Vector<Bullet>();
	Vector<Bullet> trashBullets = new Vector<Bullet>();
	Vector<Bullet> newBullets = new Vector<Bullet>();

	public GameWindow() {
		setTitle("Starlight");
		setSize(new Dimension(1024,768));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addKeyListener(this);
		//curShadowbox = new StarfieldShadowbox();
		//curShadowbox = new PlasmaShadowbox();
		curShadowbox = new BlobShadowbox();
		terrainColors[0] = new Color(0,0,0,0);
		terrainColors[1] = Color.GRAY;
		
		curRoom = RoomLibrary.getRoom("spawn");
		hasJetpack=false; hasGun=true;
		queue = curRoom.openScript;
		
		lastRenderStart = System.currentTimeMillis();
		
		gunSound = ResourceBroker.loadSample("shoot.wav");
		damageSound = ResourceBroker.loadSample("damage.wav");
		hurtSound = ResourceBroker.loadSample("hurt.wav");
		boomSound = ResourceBroker.loadSample("boom.wav");
		plinkSound = ResourceBroker.loadSample("plink.wav");
		powerupSound = ResourceBroker.loadSample("powerup.wav");
		
		//setup player
		player.x = 2*coordSize;
		player.y = 6*coordSize;
		
		//dialogueFont = new Font("Courier",Font.BOLD,24);
		
		//starting soundsystem
		//MusicPlayer.startPlaying("Escape.mp3");
	}

	public void paint(Graphics g) {
		internalPaint();
	}
	
	
	public void internalPaint() {
		//calculate optimal blockSize
		blockSize = (int) (this.getHeight() / 128);
	
		if (buf==null) {
			createBufferStrategy(2);
			buf = getBufferStrategy();
			
			GraphicsEnvironment curEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice curDevice = curEnvironment.getDefaultScreenDevice();
			curDevice.setFullScreenWindow(this);
			
			if (curShadowbox!=null) curShadowbox.init();
			
			
			playerLeft = ResourceBroker.loadImage("player.png");
			playerRight = ResourceBroker.getFlipped(playerLeft);
			player.curImage = playerRight;
			player.isPlayer=true;
			
			doorRight = ResourceBroker.loadImage("door.png");
			doorLeft = ResourceBroker.getFlipped(doorRight);
			
			idoorRight = ResourceBroker.loadImage("infecteddoor.png");
			idoorLeft = ResourceBroker.getFlipped(idoorRight);
			
			doorClosedRight = ResourceBroker.loadImage("doorclosed.png");
			doorClosedLeft = ResourceBroker.getFlipped(doorClosedRight);
			
			//parse out tiles
			Image terrainImage = ResourceBroker.loadImage("tiles.png");
			int tilesHigh = terrainImage.getHeight(this)/coordSize;
			int tilesWide = terrainImage.getWidth(this)/coordSize;
			int curTile=1;
			for(int y=0; y<tilesHigh; y++) {
				for(int x=0; x<tilesWide; x++) {
					BufferedImage tileImage = new BufferedImage(coordSize,coordSize,BufferedImage.TYPE_4BYTE_ABGR);
					Graphics tg = tileImage.getGraphics();
					tg.drawImage(terrainImage,-x*coordSize,-y*coordSize,terrainImage.getWidth(this),terrainImage.getHeight(this),this);
					tg.dispose();
					terrainImages[curTile] = tileImage;
					curTile++;
				}
			}
			
		}
		
		Graphics2D g = (Graphics2D)buf.getDrawGraphics();
		long renderStart = System.currentTimeMillis();
		//camera.x = player.x-coordSize*9; if (camera.x<0) camera.x=0;
		//camera.y = player.y-coordSize*8; if (camera.y<0) camera.y=0;
		camera.follow(curRoom,player,this.getWidth(),this.getHeight(),blockSize,coordSize);
		displayString = "Camera: "+camera.x+", "+camera.y;
		if (camera.xScroll) displayString+=" xScroll";
		if (camera.yScroll) displayString+=" yScroll";
		
		//1: DRAW SHADOWBOX
		g.setColor(Color.BLACK);
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		
		if (curShadowbox!=null) curShadowbox.paint(g,this.getWidth(),this.getHeight());
		
		//2: DRAW BACKGROUND
		
		for(int y=0; y<curRoom.getHeight(); y++) {
			for(int x=0; x<curRoom.getWidth(); x++) {
				int blockID = curRoom.getTileID(x,y);
				if (blockID<0) blockID=-blockID;
				Color blockColor = terrainColors[blockID];
				if (terrainImages[blockID]!=null) {
					g.drawImage(terrainImages[blockID], x*blockSize*coordSize-(int)camera.x,y*blockSize*coordSize-(int)camera.y,blockSize*coordSize,blockSize*coordSize,this);
				} else {
					g.setColor(blockColor);
					g.fillRect(x*blockSize*coordSize,y*blockSize*coordSize,blockSize*coordSize,blockSize*coordSize);
				}
			}
		}
		
		//3: DRAW ACTIVE ELEMENTS
		//doors
		if (!curRoom.exits.isEmpty()) for(Door d : curRoom.exits) {
			Image doorImage = doorRight; if (!d.facingRight) doorImage = doorLeft;
			if (d.infected & (!d.flashing)) {
				doorImage = idoorRight; if (!d.facingRight) doorImage = idoorLeft;
			}
			g.drawImage(doorImage,d.x*blockSize*coordSize-(int)camera.x,d.y*blockSize*coordSize-(int)camera.y,16*blockSize,24*blockSize,this);
			
			d.flashing = false;
		}
		
		//bullets
		if (!bullets.isEmpty()) for(Bullet b : bullets) {
			g.setColor(b.color);
			int r = (int)(b.size/2);
			g.fillRect(((int)b.x-r)*blockSize-(int)camera.x,((int)b.y-r)*blockSize-(int)camera.y,blockSize*b.size,blockSize*b.size);
		}
		
		//player
		player.paint(g,blockSize,camera);
		//enemies
		for(Mob m : curRoom.monsters) {
			m.paint(g,blockSize,camera);
		}
		
		//4: DRAW FOREGROUND ELEMENTS
		
		//HUD, such as it is.
		g.setColor(Color.WHITE);
		g.drawString(displayString,8,10);
	
		//dialogue
		if (curDialogue!=null) {
			if (curDialogue.curDelay>=curDialogue.delay) curDialogue=null;
			else {
				g.setColor(Color.WHITE);
				g.setFont(ResourceBroker.dialogueFont);
				g.drawString(curDialogue.messageString,blockSize*coordSize*2,blockSize*coordSize*2);
			}
		}
	
		if (hasJetpack) {
			//jetpack bar
			g.setColor(getBarColor(jetpackCharge,maxJetpackCharge));
			
			int startX = this.getWidth() - (blockSize*4);
			int tickWidth = blockSize*3;
			int numTicks = jetpackCharge/4;
			for(int i=0; i<numTicks; i++) {
				g.fillRect(startX-((i+1)*(tickWidth+blockSize)),blockSize*4,tickWidth,tickWidth);
			}
		
		}
	
		//health bar
		{
			g.setColor(getBarColor(player.health,player.maxHealth));
			int startX = blockSize*3;
			int tickWidth = blockSize*2;
			int numTicks = player.health;
			for(int i=0; i<numTicks; i++) {
				g.fillRect(startX+((i+1)*(tickWidth+blockSize)),blockSize*4,tickWidth,tickWidth*3);
			}
		}
	
	
		buf.show();
		
		
		//5: DO GAME PHYSICS - NOTE: PUTTING THIS BEFORE THE RENDER DELAY CODE INCREASES LATENCY AND POTENTIALLY MAKES CONTROLS MUDDIER
		
		doGamePhysics();
		
	
		long renderTicks = renderStart-lastRenderStart;
		lastRenderStart = renderStart;
		Thread.yield();
		if (renderTicks < targetRenderTicks ) try {
			Thread.sleep(targetRenderTicks-renderTicks);
		} catch (InterruptedException ex) {}
		this.repaint();
	}
	
	public Color getBarColor(int current, int max) {
		double percentage = 1; if (max!=0) percentage = (double)current/(double)max;
		if (percentage==1.0) return Color.GREEN;
		if (percentage>0.6) return Color.YELLOW;
		if (percentage>0.4) return Color.ORANGE;
		return Color.RED;
	}

	
	public int blockAtFeet(Mob m) {
		if (m.y+m.height-1>(curRoom.getHeight()*coordSize)-1) return 0;
		
		int tilesWide = (int)Math.ceil(m.width/(double)coordSize); if (tilesWide<1) tilesWide=1;
		if ((int)m.x%coordSize!=0) tilesWide++;
		
		int block = 0;
		for(int i=0; i<tilesWide; i++) {
			int id = curRoom.getTileID((int)(m.x/coordSize)+i,(int)((m.y+m.height-1)/coordSize));
			if (id<0) return id; //prioritize special (negative) blocks
			if (id>0) block=id;
		}
		return block;
	}
	
	public void doGamePhysics() {
		//scripts
		queue.nextTick(player,curRoom,this);
		
		//monsters
		for(Mob m : curRoom.monsters) {
			m.vy+=0.2;
			if (!curRoom.canMoveDown(m)) if (m.vy>0) m.vy=0;
			
			double xTicks = Math.abs(m.vx);
			double yTicks = Math.abs(m.vy);
			int ticks = (int)Math.ceil(Math.max(xTicks,yTicks));
			if (ticks==0) ticks=1;
			double xScale = xTicks/ticks;
			double yScale = yTicks/ticks;
			for(int i=0; i<ticks; i++) {
				double xdist = Math.abs(m.vx*xScale); if (xdist>1) xdist=1;
				double ydist = Math.abs(m.vy*yScale); if (ydist>1) ydist=1;
				if (m.vx<0)
					if (curRoom.canMoveLeft(m)) m.x-=xdist; else { m.vx=0; m.x = (int)m.x; }
				if (m.vx>0)
					if (curRoom.canMoveRight(m)) m.x+=xdist; else { m.vx=0; m.x = (int)m.x; }
					
				if (m.vy<0)
					if (curRoom.canMoveUp(m)) m.y-=ydist; else { m.vy=0; m.y = (int)m.y; }
				if (m.vy>0)
					if (curRoom.canMoveDown(m)) m.y+=ydist; else { m.vy=0;  }
					
			}
			
			//collisions?
			if (m.collides(player)) {
				m.onCollide(player);
			}
			
			//bullets?
			
			m.ai(curRoom,player);
		}
		
		
		//gravity
		player.ai(curRoom,player);
		player.vy+=0.2;
		
		onGround = !curRoom.canMoveDown(player);
		
		//inertia
		double xTicks = Math.abs(player.vx);
		double yTicks = Math.abs(player.vy);
		int ticks = (int)Math.ceil(Math.max(xTicks,yTicks));
		if (ticks==0) ticks=1;
		double xScale = xTicks/ticks;
		double yScale = yTicks/ticks;
		for(int i=0; i<ticks; i++) {
			double xdist = Math.abs(player.vx*xScale); if (xdist>1) xdist=1;
			double ydist = Math.abs(player.vy*yScale); if (ydist>1) ydist=1;
			if (player.vx<0)
				if (curRoom.canMoveLeft(player)) player.x-=xdist; else { player.vx=0; player.x = (int)player.x; }
			if (player.vx>0)
				if (curRoom.canMoveRight(player)) player.x+=xdist; else { player.vx=0; player.x = (int)player.x; }
				
			if (player.vy<0)
				if (curRoom.canMoveUp(player)) player.y-=ydist; else { player.vy=0; player.y = (int)player.y; }
			if (player.vy>0)
				if (curRoom.canMoveDown(player)) player.y+=ydist; else { player.vy=0;  }
				
		}
	
		//drag
		if (Math.abs(player.vx)<drag) player.vx=0;
		if (Math.abs(player.vy)<drag) player.vy=0;
		
		if (player.vx<0) player.vx+=drag;
		if (player.vx>0) player.vx-=drag;
		if (player.vy<0) player.vy+=drag;
		if (player.vy>0) player.vy-=drag;

		onGround = !curRoom.canMoveDown(player);

		
		//controls
		if (player.health>0) {
			if (leftPressed) { player.vx-=acceleration; player.curImage = playerLeft; }
			if (rightPressed) { player.vx+=acceleration; player.curImage = playerRight; }
			if (upPressed) {
				//find out if we're standing on a ladder
				if (blockAtFeet(player)==-6) {
					player.vy = -1;
				} else {
				
					if (jetpackActive & hasJetpack) {
						if (jetpackCharge>0) {
							if (!jumpLock) {
								player.vy -= 0.5; jetpackCharge--;
							}
						}
					} else {
						if (jumping&jumpCharge>0) { player.vy = -8.5; jumpCharge--; }
						if (!jumpLock) {
							if (onGround) {
								player.vy = -8.5;
								jumpLock=true;
								jumping=true;
								jetpackActive = false;
							}
						}
						if ((!jumpLock) & (!onGround)) jetpackActive = true;
					}
				}
			} else { jumpLock = false; }
			if (actionPressed & hasGun) {
				if (gunReload<=0) {
					int bvel = 4; if (player.curImage==playerLeft) bvel=-4;
					bullets.add(new Bullet((int)player.x,(int)player.y+9,bvel,0));
					ResourceBroker.playSound(gunSound);
					
					explode((int)player.x+(bvel*2),(int)player.y+9,4,1,3,Color.GRAY,Color.WHITE);
					gunReload = maxGunReload;
				}
			}
		} else {
			//TODO: SET PLAYER IMAGE TO DEAD-IMAGE
			player.width=16; player.height=8; player.curImage = ResourceBroker.loadImage("playerdead.png");
			//player.curImage=null;
			explode((int)player.x+8,(int)player.y+4, 4, 1, 10, Color.RED, new Color(128,32,32));
		}
		
		//bullets
		for(Bullet b : bullets) {
			b.x+=b.vx; b.y+=b.vy;
			if (b.lifetime>0) {
				b.lifetime--;
				if (b.lifetime==0) trashBullets.add(b);
			}
			
			//bullets have left the building
			if (b.x>curRoom.width*coordSize) trashBullets.add(b);
			if (b.y>curRoom.height*coordSize) trashBullets.add(b);
			if (b.x<0) trashBullets.add(b);
			if (b.y<0) trashBullets.add(b);
			
			//collisions?
			for(Door d : curRoom.exits) {
				if (d.infected) {
					if (d.contains((int)b.x,(int)b.y)) {
						d.flashing = true;
						//ResourceBroker.playSound(damageSound);
						d.resistance--;
						trashBullets.add(b);
						if (d.resistance==0) {
							d.infected=false; ResourceBroker.playSound(boomSound);
							explode(d.x*8 + 8,d.y*8 + 12, 40,10,30,Color.RED,Color.ORANGE);
						}
						else ResourceBroker.playSound(hurtSound);
					}
				}
			}
			for(Mob m : curRoom.monsters) {
				if (m.contains((int)b.x,(int)b.y) & b.solid) {
					trashBullets.add(b);
					ResourceBroker.playSound(hurtSound);
					m.health--;
					if (m.health<=0) {
						m.onDie(curRoom,player,this);
						curRoom.trashMonsters.add(m);
					}
				}
			}
			
			int struckBlock = curRoom.getTileID((int)(b.x/coordSize),(int)(b.y/coordSize));
			if ((struckBlock>0) & b.solid) {
				if (struckBlock==14) {
					curRoom.setTileID((int)(b.x/coordSize),(int)(b.y/coordSize),0);
					ResourceBroker.playSound(hurtSound);
					
					//take out up to four surrounding destructable blocks
					struckBlock = curRoom.getTileID((int)(b.x/coordSize)-1,(int)(b.y/coordSize));
					if (struckBlock==14) curRoom.setTileID((int)(b.x/coordSize)-1,(int)(b.y/coordSize),0);
					
					struckBlock = curRoom.getTileID((int)(b.x/coordSize)+1,(int)(b.y/coordSize));
					if (struckBlock==14) curRoom.setTileID((int)(b.x/coordSize)+1,(int)(b.y/coordSize),0);
					
					struckBlock = curRoom.getTileID((int)(b.x/coordSize),(int)(b.y/coordSize)+1);
					if (struckBlock==14) curRoom.setTileID((int)(b.x/coordSize),(int)(b.y/coordSize)+1,0);
					
					struckBlock = curRoom.getTileID((int)(b.x/coordSize),(int)(b.y/coordSize)-1);
					if (struckBlock==14) curRoom.setTileID((int)(b.x/coordSize),(int)(b.y/coordSize)-1,0);
					
					
					 explode((int)b.x, (int)b.y, 64, 1, 40, Color.RED, Color.GRAY);
					//set off non-solid particles for the explosion
					/*
					for(int i=0; i<64; i++) {
						double vx = (Math.random()*4.0)-2.0; if (vx<0.5) vx-=1.4;
						double vy = (Math.random()*4.0)-2.0; if (vy<0.5) vy-=1.4;
					
						Bullet bill = new Bullet(b.x,b.y,vx,vy);
						bill.solid=false; bill.color = Color.RED; bill.size=1; bill.lifetime=40;
						newBullets.add(bill);
					}*/
				} else {
					ResourceBroker.playSound(plinkSound);
				}
				trashBullets.add(b);
			}
			
		}
		for(Bullet b : trashBullets) {
			bullets.remove(b);
		}
		trashBullets.clear();
		
		for(Bullet b : newBullets) {
			bullets.add(b);
		}
		newBullets.clear();
		
		for(Mob m : curRoom.trashMonsters) {
			curRoom.monsters.remove(m);
		}
		
		if (gunReload>0) gunReload--;
	
		//Limits - screen
		if (player.x<0) { player.x=0; if (player.vx<0) player.vx=0; }
		if (player.x>curRoom.getWidth()*coordSize) { player.x=curRoom.getWidth()*coordSize-1; if (player.vx>0) player.vx=0; }
		if (player.y<0) { player.y=0; if (player.vy<0) player.vy=0; }
		if (player.y>curRoom.getHeight()*coordSize) { player.y=curRoom.getHeight()*coordSize-1; if (player.vy>0) player.vy=0; }
		
		
		//jetpack recharge
		if (hasJetpack) {
			if (onGround & (player.vy==0)) {
				jumpLock = false;
				jetpackActive = false;
				jetpackCharge = Math.min(jetpackCharge+2,maxJetpackCharge);
			}
		}
		if (onGround & (player.vy>=0)) { jumping=false; jumpCharge=maxJumpCharge; }

		//displayString = "Jump Charge: "+jumpCharge+" VY: "+player.vy+" OnGround: "+onGround;
		
		//Limits - terminal velocity
		if (player.vx>xVelocity) player.vx=xVelocity; if (player.vx<-xVelocity) player.vx=-xVelocity;
		if (player.vy>terminalVelocity) player.vy=terminalVelocity; if (player.vy<-terminalVelocity) player.vy=-terminalVelocity;
		
		//Item - Gun
		if (blockAtFeet(player)==-13) {
			curRoom.removeGuns();
			if (!hasGun) {
				hasGun=true;
				ResourceBroker.playSound(powerupSound);
				queue.insert(new DialogueMessage("Sam: YES! A gun! This is exactly what I need!",null));
				queue.insertDelay(2);
				queue.insert(new DialogueMessage("Sam: The trigger seems to be rigged to an ENTER key... whatever that means.",null));
			} else {
				queue.insert(new DialogueMessage("Sam: I seem to have a gun already... and I really like the one I've got!",null));
			}
		}
		
		if (blockAtFeet(player)==-15) {
			curRoom.removeJumpBoost();
			if (maxJumpCharge<=2) {
				maxJumpCharge=6;
				ResourceBroker.playSound(powerupSound);
				queue.insert(new DialogueMessage("Sam: Hey, these synthetic myelin jump boosters fit my suit!",null));
				queue.insertDelay(2);
				queue.insert(new DialogueMessage("Sam: I hope I don't have to use these too much though. They look kind of scary.",null));
			} else {
				queue.insert(new DialogueMessage("Sam: I already have jump boosters, but thanks anyway.",null));
			}
		}
		
		if (blockAtFeet(player)==-16) {
			curRoom.removeJetpack();
			if (!hasJetpack) {
				hasJetpack=true;
				ResourceBroker.playSound(powerupSound);
				queue.insert(new DialogueMessage("Sam: This is a jetpack! Holy smokes, I can fly!",null));
				queue.insertDelay(2);
				queue.insert(new DialogueMessage("System: please do not mount the vertical propulsion device upside-down.",null));
				queue.insertDelay(1);
				queue.insert(new DialogueMessage("Sam: I knew that.",null));
			} else {
				queue.insert(new DialogueMessage("Sam: There isn't any way I can use two jetpacks at once, is there?",null));
				queue.insertDelay(2);
				queue.insert(new DialogueMessage("System: Negative. You'll have to find additional induction coils for a more sustained boost.",null));
				queue.insertDelay(2);
				queue.insert(new DialogueMessage("Sam: There's an induction coil in this one. Can't I just rip it out or something?",null));
				queue.insertDelay(2);
				queue.insert(new DialogueMessage("System: Opening the vertical propulsion device would void its warranty...",null));
				queue.insertDelay(1);
				queue.insert(new DialogueMessage("Sam: So?",null));
				queue.insertDelay(1);
				queue.insert(new DialogueMessage("System: ...and your contract.",null));
			}
		}
		
		
		//portal
		if (!curRoom.exits.isEmpty()) for(Door d : curRoom.exits) {
			if ((d.x*coordSize<=player.x) & (d.y*coordSize<=player.y)) {
				if ((d.x*coordSize+16>=player.x+player.width-1) & (d.y*coordSize+24>=player.y+player.height-1)) {
					if ((!d.infected) & (!d.locked)) {
						//displayString = "DOOR: "+d.targetRoom;
						curDialogue = null;
						queue.clear();
						curRoom = RoomLibrary.getRoom(d.targetRoom);
						if (d.targetX>0) player.x = curRoom.width*coordSize-(coordSize*2);
						if (d.targetX<0) player.x = coordSize*2;
						
						if (d.targetY>0) player.y = curRoom.height*coordSize-(coordSize*3);
						if (d.targetY<0) player.y = coordSize*2;
						
						queue = curRoom.openScript;
						
						return;
					}
				}
			}
		}
	}
	
	public void explode(int x, int y, int numParticles, int particleSize, int particleDuration, Color a, Color b) {
		for(int i=0; i<numParticles; i++) {
			//double vx = (Math.random()*4.0)-2.0; if (Math.abs(vx)<0.1) vx=0.5;
			//double vy = (Math.random()*4.0)-2.0; if (Math.abs(vy)<0.1) vy=0.5;
		
			//it'd be better if we arranged this stuff radially
			double angle = Math.random()*6.28;
			double speed = Math.random()*2.0 + 0.1;
			Bullet bill = new Bullet(x,y,vectorX(speed,angle),vectorY(speed,angle));
			if (Math.random()>0.5) bill.color=a; else bill.color=b;
			bill.solid=false; bill.size=particleSize; bill.lifetime=particleDuration;
			newBullets.add(bill);
		}
	}
	
	
	//****CONVERT POLAR TO CARTESIAN COORDINATES
	
	public double vectorX(double r, double theta) {
		return -r*Math.sin(theta);
	}
	
	public double vectorY(double r, double theta) {
		return -r*Math.cos(theta);
	}
	
	//****END CONVERT POLAR TO CARTESIAN COORDINATES
	
	public void showMessage(DialogueMessage dm) {
		curDialogue = dm;
	}
	
	
	public void keyPressed(KeyEvent k) {
		if (k.getKeyCode()==KeyEvent.VK_ESCAPE) {
			GraphicsEnvironment curEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice curDevice = curEnvironment.getDefaultScreenDevice();
			curDevice.setFullScreenWindow(null);
			this.setVisible(false);
			System.exit(0);
		}
		
		if (k.getKeyCode()==KeyEvent.VK_ENTER) actionPressed=true;
		
		if (k.getKeyCode()==KeyEvent.VK_A) leftPressed=true;
		if (k.getKeyCode()==KeyEvent.VK_D) rightPressed=true;
		if (k.getKeyCode()==KeyEvent.VK_W) upPressed=true;
		if (k.getKeyCode()==KeyEvent.VK_S) downPressed=true;
		
		
	}
	public void keyReleased(KeyEvent k) {
		if (k.getKeyCode()==KeyEvent.VK_ENTER) actionPressed=false;
	
		if (k.getKeyCode()==KeyEvent.VK_A) leftPressed=false;
		if (k.getKeyCode()==KeyEvent.VK_D) rightPressed=false;
		if (k.getKeyCode()==KeyEvent.VK_W) upPressed=false;
		if (k.getKeyCode()==KeyEvent.VK_S) downPressed=false;
	
	}
	
	public void keyTyped(KeyEvent k) {}
}