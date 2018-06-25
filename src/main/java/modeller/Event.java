package modeller;

import java.util.concurrent.Callable;

public final class Event implements Comparable<Event> {
	private long time;
	private Callable<?> event;

	public Event(long time, Callable<?> event) {
		this.time = time;
		this.event = event;
	}

	@Override
	public int compareTo(Event o) {
		return Long.compare(time, o.getTime());
	}

	public long getTime() {
		return time;
	}

	public Callable<?> getEvent() {
		return event;
	}

}
