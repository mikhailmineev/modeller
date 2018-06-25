package modeller;

import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public abstract class AbstractModel<T extends ModelState> implements Model<T> {
	private ModelComponents components;

	public AbstractModel(ModelComponents components) {
		this.components = components;
	}

	@Override
	public final void planEvent(long time, Callable<?> event) {
		components.getEvents().add(new Event(time, event));
		Collections.sort(components.getEvents());
	}

	@Override
	public long initialTime() {
		return 0L;
	}

	@Override
	public final void abortCondition(Supplier<Boolean> abort) {
		components.getAborts().add(abort);
	}

	@Override
	public String timeRenderer(long currentTime) {
		return Long.toString(currentTime);
	}

	public final void log(String format, Object... args) {
		System.out.println(String.format("MODEL\t " + components.renderCurrentTime() + "\t " + format, args));
	}

	@Override
	public final long currentTime() {
		return this.components.getCurrentTime();
	}

}
