package modeller;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class Executor implements Callable<Result> {

	private Model<ModelState> model;
	private ModelState state;
	private ModelComponents components;

	public Executor(ModelComponents components, Model<ModelState> model, ModelState state) {
		this.components = components;
		this.model = model;
		this.state = state;
	}

	public final Result step() {
		for (Supplier<Boolean> abort : components.getAborts()) {
			if (abort.get()) {
				System.out.println("Finished by abort condition at " + components.renderCurrentTime());
				return Result.END;
			}
		}
		Event event = components.getEvents().poll();
		if (event == null) {
			System.out.println("No more events " + components.renderCurrentTime());
			return Result.END;
		}
		if (components.getCurrentTime() > event.getTime()) {
			throw new RuntimeException("Event time (" + components.renderTime(event.getTime()) + ") lower than current "
					+ components.renderCurrentTime());
		}
		try {
			components.setCurrentTime(event.getTime());
			event.getEvent().call();
			return Result.OK;
		} catch (Exception e) {
			throw new RuntimeException("Exception in event occured", e);
		}
	}

	public void finish() {
		components.getFinish().run();
	}

	@Override
	public Result call() throws Exception {
		try {
			System.out.println("Starting model at " + components.renderCurrentTime());
			model.init(state);
			try {
				Result innerResult = Result.OK;
				while (innerResult != Result.END) {
					innerResult = step();
				}
			} finally {
				finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Result.FAIL;
		}
		return Result.END;
	}
}
