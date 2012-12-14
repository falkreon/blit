package com.thoughtcomplex.blit.ld21;

import java.util.*;

import com.thoughtcomplex.blit.ld21.GameWindow;

public class MessageQueue {
	Vector<Message> queue = new Vector<Message>(); 
	Message curMessage = null;
	
	public MessageQueue() {}

	public void nextTick(Mob player, Room rm, GameWindow gw) {
		if (curMessage==null) pullMessageFromQueue(player,rm,gw);
		else {
			curMessage.curDelay++; if (curMessage.curDelay>=curMessage.delay) pullMessageFromQueue(player,rm,gw);
		}
	}
	
	protected void pullMessageFromQueue(Mob player, Room rm, GameWindow gw) {
		if (!queue.isEmpty()) {
			curMessage = queue.remove(0);
			curMessage.execute(player,rm,gw);
		}
	}
	
	public void insert(Message m) {
		m.curDelay = 0; queue.add(m);
	}
	
	public void insert(MessageQueue ms) {
		queue.addAll(ms.queue);
	}
	
	public void insertImmediate(MessageQueue ms) {
		queue.addAll(0,ms.queue);
	}
	
	public void insertDelay(int seconds) {
		Message delayMsg = new Message("Delay");
		delayMsg.delay = seconds*100/4;
		insert(delayMsg);
	}
	
	public void clear() {
		queue.removeAllElements(); curMessage=null;
	}
	
}