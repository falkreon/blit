package com.thoughtcomplex.blit.ld21;

public class ConditionMessage extends Message {
	String flagName = "";
	MessageQueue trueScript = new MessageQueue();
	MessageQueue falseScript = new MessageQueue();

	public ConditionMessage(String theCondition) { super("If "+theCondition); flagName = theCondition; }
	public ConditionMessage(String theCondition, Message trueMessage, Message falseMessage) {
		super("If "+theCondition);
		flagName = theCondition; trueScript.insert(trueMessage); falseScript.insert(falseMessage);
	}
	public ConditionMessage(String theCondition, MessageQueue trueQueue, MessageQueue falseQueue) {
		super("If "+theCondition);
		flagName = theCondition;
		trueScript=trueQueue; falseScript=falseQueue;
	}
	
	public void execute(Mob player, Room rm, GameWindow gw) {
		if (GameFlags.getFlag(flagName)) {
			if (trueScript!=null) gw.queue.insertImmediate(trueScript);
		} else {
			if (falseScript!=null) gw.queue.insertImmediate(falseScript);
		}
	}

}