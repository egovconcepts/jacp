package org.jacp.project.JACP.UnitTests.workbench;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.Map;

import javax.swing.JMenu;

import org.jacp.api.componentLayout.Layout;
import org.jacp.project.JACP.Util.util.OSXBottomBarPanel;
import org.jacp.project.JACP.Util.util.OSXToolBar;
import org.jacp.swing.rcp.action.SwingAction;
import org.jacp.swing.rcp.componentLayout.SwingWorkbenchLayout;
import org.jacp.swing.rcp.workbench.ASwingWorkbench;

/**
 * 
 * @author ady
 * 
 */
public class UnitTestWorkBench extends ASwingWorkbench {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2573222804878663322L;

	public UnitTestWorkBench() {
		super("UnitTestWorkbench");
	}

	@Override
	public void handleBarEntries(Map<Layout, Container> bars) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleInitialLayout(SwingAction action,
			SwingWorkbenchLayout layout) {
		layout.setLayoutManager(new BorderLayout());
		layout.registerToolBar(Layout.NORTH, new OSXToolBar());
		layout.registerToolBar(Layout.SOUTH, new OSXBottomBarPanel());
		layout.setWorkbenchXYSize(1024, 600);

	}

	@Override
	public JMenu handleMenuEntries(JMenu menuBar) {
		// TODO Auto-generated method stub
		return null;
	}

}
