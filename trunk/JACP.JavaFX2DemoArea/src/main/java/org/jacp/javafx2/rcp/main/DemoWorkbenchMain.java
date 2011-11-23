package org.jacp.javafx2.rcp.main;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.jacp.api.launcher.Launcher;
import org.jacp.api.workbench.IWorkbench;
import org.jacp.javafx2.rcp.launcher.SpringLauncher;
import org.jacp.javafx2.rcp.workbench.AFX2Workbench;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DemoWorkbenchMain extends Application {
	final Launcher<ClassPathXmlApplicationContext> launcher = new SpringLauncher(
			"main.xml");

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Application.launch(args);

	}

	@Override
	public void start(Stage arg0) throws Exception {
		final IWorkbench< Node, EventHandler<Event>, Event, Object> workbench = (IWorkbench< Node, EventHandler<Event>, Event, Object>) launcher
				.getContext().getBean("workbench");
		workbench.init(launcher);
		((AFX2Workbench) workbench).start(arg0);
	}

}