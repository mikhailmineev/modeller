package modeller.impl;

import java.time.LocalTime;
import java.util.List;

import modeller.AbstractModel;
import modeller.ModelComponents;

public class BarberModel extends AbstractModel<BarberModelState> {

	public BarberModel(ModelComponents components) {
		super(components);
	}

	private int queue;

	private int barberDelay = 10;

	private BarberState barberState = BarberState.FREE;

	@Override
	public void init(BarberModelState state) {
		barberDelay = state.barberDelay;
		planEvent(currentTime() + comeDelay(), () -> {
			clientCome();
			barberBusy();
			return null;
		});
		abortCondition(() -> {
			return currentTime() > LocalTime.of(19, 0).toSecondOfDay() / 60;
		});
	}

	@Override
	public long initialTime() {
		return LocalTime.of(10, 0).toSecondOfDay() / 60;
	}

	@Override
	public String timeRenderer(long currentTime) {
		return LocalTime.ofSecondOfDay(currentTime * 60).toString();
	}

	@Override
	public void finish() {
		log("Queue %d", queue);
	}

	private void clientCome() {
		//log("Client came");
		queue++;
		planEvent(currentTime() + comeDelay(), () -> {
			clientCome();
			barberBusy();
			return null;
		});
	}

	private void barberBusy() {
		if (queue > 0 && barberState == BarberState.FREE) {
			//log("Making barber busy");
			queue--;
			barberState = BarberState.BUSY;
			planEvent(currentTime() + barberDelay, () -> {
				barberFree();
				return null;
			});
		} else {
			//log("Can`t make barber busy. Queue %d", queue);
		}
	}

	private void barberFree() {
		//log("Barber free, queue %d", queue);
		barberState = BarberState.FREE;
		planEvent(currentTime(), () -> {
			barberBusy();
			return null;
		});
	}

	private int comeDelay() {
		return (int) (Math.random() * 30);
	}

	enum BarberState {
		FREE, BUSY
	}

	@Override
	public void experiment(List<BarberModelState> states) {
		for (int i = 0; i < 5; i++) {
			BarberModelState state = new BarberModelState();
			state.barberDelay = i * 10;
			states.add(state);
		}
	}
}
