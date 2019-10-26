package miner.view.gui;

import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;

public class TimerThread extends Thread {

	@Inject IEventBroker broker;
	
	public TimerThread() {

	}
	
	@Override
	public void run() {
		int counter = 999;
		int secondsValue = 0;
		do {
			if(!Thread.interrupted()) {
				secondsValue++;
				broker.send(GuiEvents.TIMER_EVENT, secondsValue);
			} else {
				broker.send(GuiEvents.TIMER_EVENT, 0);
				return;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				return;
			}
		} while (counter >= secondsValue);
	}
}
