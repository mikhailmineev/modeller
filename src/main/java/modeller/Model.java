package modeller;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public interface Model<T extends ModelState> {
	void experiment(List<T> states);
	void init(T state);
	void finish();
	long initialTime();
	long currentTime();
	void planEvent(long time, Callable<?> event);
	void abortCondition(Supplier<Boolean> abort);
	String timeRenderer(long currentTime);
	
}
