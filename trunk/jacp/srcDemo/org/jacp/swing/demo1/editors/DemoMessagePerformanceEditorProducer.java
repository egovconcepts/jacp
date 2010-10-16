package org.jacp.swing.demo1.editors;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.swing.rcp.component.ASwingComponent;

/**
 * This demo editor acts as message producer; this demo shows an good and a bad
 * variant of using components and their specific way of handling Variant A:
 * defines a while loop inside an "actionPerfomed"
 * 
 * @author Andy Moncsek
 * 
 */
public class DemoMessagePerformanceEditorProducer extends ASwingComponent {

	private final JPanel panel = new JPanel();
	final JButton button = new JButton("send 1000 messages");
	final JButton button2 = new JButton("send 1000 bg messages");
	final JButton button3 = new JButton("send 1000 stateless");
	final JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

	@Override
	public void handleMenuEntries(final Container meuneBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleBarEntries(final Container toolBar,
			final Container bottomBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public Container handleAction(final IAction<ActionEvent, Object> action) {

		if (action.getMessage() instanceof String) {
			if (action.getMessage().equals("begin")) {
				int p = 0;
				System.out.println("BEGIN_ACTION" + action.getMessage());
				final IActionListener<ActionListener, ActionEvent, Object> listener2 = getActionListener();
				listener2.getAction().setMessage("id09", "start");
				listener2.getListener()
						.actionPerformed(action.getActionEvent());
				while (p < 10000) {
					final IActionListener<ActionListener, ActionEvent, Object> listener3 = getActionListener();
					String val = "test" + p;
					listener3.getAction().setMessage("id09", val);
					System.out.println("Producer Val.: "+val);
					listener3.getListener().actionPerformed(
						listener3.getAction().getActionEvent());
					p++;

				}
				final IActionListener<ActionListener, ActionEvent, Object> listener3 = getActionListener();
				listener3.getAction().setMessage("id09", "stop");
				listener3.getListener()
						.actionPerformed(action.getActionEvent());
			} else if (action.getMessage().equals("begin1")) {
				int p = 0;
				System.out.println("BEGIN_ACTION2" + action.getMessage());
				final IActionListener<ActionListener, ActionEvent, Object> listener2 = getActionListener();
				listener2.getAction().setMessage("id11", "start");
				listener2.getListener()
						.actionPerformed(action.getActionEvent());
				while (p < 9999) {
					final IActionListener<ActionListener, ActionEvent, Object> listener3 = getActionListener();
					listener3.getAction().setMessage("id11", "test" + p);
					listener3.getListener().actionPerformed(
						listener3.getAction().getActionEvent());
					p++;

				}
				final IActionListener<ActionListener, ActionEvent, Object> listener3 = getActionListener();
				listener3.getAction().setMessage("id11", "stop");
				listener3.getListener()
						.actionPerformed(action.getActionEvent());
			} else if (action.getMessage().equals("begin2")) {
				int p = 0;
				System.out.println("BEGIN_ACTION3" + action.getMessage());
				final IActionListener<ActionListener, ActionEvent, Object> listener2 = getActionListener();
				listener2.getAction().setMessage("id12", "start");
				listener2.getListener()
						.actionPerformed(action.getActionEvent());
				while (p < 50) {
					final IActionListener<ActionListener, ActionEvent, Object> listener3 = getActionListener();
					listener3.getAction().setMessage("id12", p*1000);
					listener3.getListener().actionPerformed(
						listener3.getAction().getActionEvent());
					p++;

				}
				final IActionListener<ActionListener, ActionEvent, Object> listener3 = getActionListener();
				listener3.getAction().setMessage("id11", "stop");
				listener3.getListener()
						.actionPerformed(action.getActionEvent());
			}else {
				button.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(final ActionEvent e) {
						final IActionListener<ActionListener, ActionEvent, Object> listener2 = getActionListener();
						listener2.getAction().setMessage("id08", "begin");
						listener2.getListener().actionPerformed(e);

					}
				});
				final boolean isBlocked = isBlocked();
				button2.addActionListener(new ActionListener() {
					
		
					@Override
					public void actionPerformed(final ActionEvent e) {
						System.out.println("blocked"+isBlocked);
						final IActionListener<ActionListener, ActionEvent, Object> listener2 = getActionListener();
						listener2.getAction().setMessage("id08", "begin1");
						listener2.getListener().actionPerformed(e);

					}
				});
				button3.addActionListener(new ActionListener() {
					
					
					@Override
					public void actionPerformed(final ActionEvent e) {
						System.out.println("blocked"+isBlocked);
						final IActionListener<ActionListener, ActionEvent, Object> listener2 = getActionListener();
						listener2.getAction().setMessage("id08", "begin2");
						listener2.getListener().actionPerformed(e);

					}
				});

				pane.add(button);
				JSplitPane frame=  new JSplitPane(JSplitPane.VERTICAL_SPLIT);
				frame.add(button2);
				frame.add(button3);
				pane.add(frame);
				//pane.add(button3);

				panel.add(pane);
			} 
		}
		return panel;
	}

}
