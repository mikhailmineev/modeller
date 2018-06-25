package modeller;

import java.util.ArrayList;
import java.util.List;

import modeller.impl.BarberModel;
import modeller.impl.BarberModelState;

public class Modeller {

	public static void main(String... args) throws Exception {
		BarberModel barberModel = new BarberModel(null);
		List<BarberModelState> states = new ArrayList<>();
		barberModel.experiment(states);

		states.stream().forEach((state) -> {
			ModelComponents components = new ModelComponents();
			Model model = new BarberModel(components);
			components.setCurrentTime(model.initialTime());
			components.setFinish(model::finish);
			components.setTimeRenderer(model::timeRenderer);
			Executor executor = new Executor(components, model, state);
			try {
				executor.call();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

	}
}
