package org.jacp.javafx2.rcp.demo;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.layout.Region;

import org.jacp.api.action.IAction;
import org.jacp.api.componentLayout.IWorkbenchLayout;
import org.jacp.javafx2.rcp.workbench.AFX2Workbench;

/**
 * Test workbench for jacp javafx2
 * 
 * @author Andy Moncsek
 * 
 */
public class DemoFX2Workbench extends AFX2Workbench {

	public DemoFX2Workbench() {
		super("UnitTestWorkbench");

	}

	@Override
	public void handleInitialLayout(IAction<ActionEvent, Object> action,
			IWorkbenchLayout<Region, Node> layout) {
		System.out.println(action.getLastMessage());
		layout.setWorkbenchXYSize(1024, 400);
		System.out.println(action.getLastMessage());
	}
	


}
