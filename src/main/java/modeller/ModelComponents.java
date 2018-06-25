package modeller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ModelComponents {
	private LinkedList<Event> events = new LinkedList<>();
	private List<Supplier<Boolean>> aborts = new ArrayList<>();
	private long currentTime;
	private Function<Long, String> timeRenderer = (time) -> Long.toString(time);
	private Runnable finish = () -> {
	};

	public LinkedList<Event> getEvents() {
		return events;
	}

	public List<Supplier<Boolean>> getAborts() {
		return aborts;
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public void setTimeRenderer(Function<Long, String> timeTransformer) {
		this.timeRenderer = timeTransformer;
	}

	public String renderTime(Long time) {
		return timeRenderer.apply(time);
	}

	public String renderCurrentTime() {
		return renderTime(currentTime);
	}

	public void setCurrentTime(long newTime) {
		if (this.currentTime > newTime) {
			throw new RuntimeException(
					"Event time (" + renderTime(newTime) + ") lower than current " + renderCurrentTime());
		}
		this.currentTime = newTime;
	}

	public Runnable getFinish() {
		return finish;
	}

	public void setFinish(Runnable finish) {
		this.finish = finish;
	}
}
