/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [AFX2Workbench.java]
 * AHCP Project (http://jacp.googlecode.com)
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 *
 ************************************************************************/
package org.jacp.javafx.rcp.workbench;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.component.IRootComponent;
import org.jacp.api.componentLayout.IWorkbenchLayout;
import org.jacp.api.coordinator.IComponentDelegator;
import org.jacp.api.coordinator.IMessageDelegator;
import org.jacp.api.coordinator.IPerspectiveCoordinator;
import org.jacp.api.handler.IComponentHandler;
import org.jacp.api.launcher.Launcher;
import org.jacp.api.perspective.IPerspective;
import org.jacp.api.util.ToolbarPosition;
import org.jacp.api.workbench.IWorkbench;
import org.jacp.javafx.rcp.action.FXAction;
import org.jacp.javafx.rcp.action.FXActionListener;
import org.jacp.javafx.rcp.componentLayout.FX2ComponentLayout;
import org.jacp.javafx.rcp.componentLayout.FX2WorkbenchLayout;
import org.jacp.javafx.rcp.components.optionPane.JACPModalDialog;
import org.jacp.javafx.rcp.components.toolBar.JACPToolBar;
import org.jacp.javafx.rcp.coordinator.FX2ComponentDelegator;
import org.jacp.javafx.rcp.coordinator.FX2MessageDelegator;
import org.jacp.javafx.rcp.coordinator.FX2PerspectiveCoordinator;
import org.jacp.javafx.rcp.handler.FX2WorkbenchHandler;

/**
 * represents the basic JavaFX2 workbench instance; handles perspectives and
 * components;
 * 
 * @author Andy Moncsek
 */
public abstract class AFX2Workbench
		implements
		IWorkbench<Node, EventHandler<Event>, Event, Object>,
		IRootComponent<IPerspective<EventHandler<Event>, Event, Object>, IAction<Event, Object>> {

	private List<IPerspective<EventHandler<Event>, Event, Object>> perspectives;

	private IComponentHandler<IPerspective<EventHandler<Event>, Event, Object>, IAction<Event, Object>> componentHandler;
	private final IPerspectiveCoordinator<EventHandler<Event>, Event, Object> perspectiveCoordinator = new FX2PerspectiveCoordinator();
	private final IComponentDelegator<EventHandler<Event>, Event, Object> componentDelegator = new FX2ComponentDelegator();
	private final IMessageDelegator<EventHandler<Event>, Event, Object> messageDelegator = new FX2MessageDelegator();
	private final IWorkbenchLayout<Node> workbenchLayout = new FX2WorkbenchLayout();
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private Launcher<?> launcher;
	private Stage stage;
	private GridPane root;
	private Pane glassPane;
	private JACPModalDialog dimmer;

	/**
	 * JavaFX2 specific start sequence
	 * 
	 * @param stage
	 * @throws Exception
	 */
	public final void start(final Stage stage) throws Exception {
		this.stage = stage;
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				// TODO close all threads, use thread group to interrupt
				// ((FX2PerspectiveCoordinator)perspectiveCoordinator).interrupt();
				// ((FX2ComponentDelegator)componentDelegator).interrupt();
				// ((FX2MessageDelegator)messageDelegator).interrupt();
				System.exit(0);

			}
		});
		this.log("1: init workbench");
		// init user defined workspace
		this.handleInitialLayout(new FXAction("TODO", "init"),
				this.getWorkbenchLayout());
		this.setBasicLayout(stage);
		postHandle(new FX2ComponentLayout(this.getWorkbenchLayout().getMenu(),
				this.getWorkbenchLayout().getRegisteredToolbars(), glassPane));
		this.log("3: handle initialisation sequence");
		componentHandler = new FX2WorkbenchHandler(this.launcher,
				this.workbenchLayout, this.root, this.perspectives);
		this.perspectiveCoordinator.setComponentHandler(componentHandler);
		this.componentDelegator.setComponentHandler(componentHandler);
		this.messageDelegator.setComponentHandler(componentHandler);
		this.handleInitialisationSequence();
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	// TODO init method also defined in perspective!!!!
	public void init(Launcher<?> launcher) {
		this.launcher = launcher;

	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public final void initComponents(final IAction<Event, Object> action) {
		final List<IPerspective<EventHandler<Event>, Event, Object>> perspectivesTmp = this
				.getPerspectives();
		for (int i = 0; i < perspectivesTmp.size(); i++) {
			final IPerspective<EventHandler<Event>, Event, Object> perspective = perspectivesTmp
					.get(i);
			this.log("3.4.1: register component: " + perspective.getName());
			this.registerComponent(perspective);
			// TODO what if component removed an initialized later
			// again?
			this.log("3.4.2: create perspective menu");
			if (perspective.isActive()) {
				Platform.runLater(new Runnable() {
					@Override
					public final void run() {
						AFX2Workbench.this.componentHandler.initComponent(
								new FXAction(perspective.getId(), perspective
										.getId(), "init"), perspective);
					}
				}); // FX2 UTILS END
			}

		}
	}

	/**
	 * handles sequence for workbench size, menu bar, tool bar and perspective
	 * initialisation
	 */
	private void handleInitialisationSequence() {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				AFX2Workbench.this.stage.show();
				// start perspective Observer worker thread
				// TODO create status daemon which observes
				// thread component on
				// failure and restarts if needed!!
				((FX2PerspectiveCoordinator) AFX2Workbench.this.perspectiveCoordinator)
						.start();
				((FX2ComponentDelegator) AFX2Workbench.this.componentDelegator)
						.start();
				((FX2MessageDelegator) AFX2Workbench.this.messageDelegator)
						.start();
				// init toolbar instance
				AFX2Workbench.this.log("3.2: workbench tool bars");
				// initToolBars();
				// handle perspectives
				AFX2Workbench.this.log("3.3: workbench init perspectives");
				AFX2Workbench.this.initComponents(null);

			}

		});
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void handleInitialLayout(final IAction<Event, Object> action,
			final IWorkbenchLayout<Node> layout) {
		this.handleInitialLayout(action, layout, this.stage);
	}

	/**
	 * JavaFX2 specific initialization method to create a workbench instance
	 * 
	 * @param action
	 * @param layout
	 * @param stage
	 */
	public abstract void handleInitialLayout(
			final IAction<Event, Object> action,
			final IWorkbenchLayout<Node> layout, final Stage stage);

	/**
	 * Handle menu and bar entries created in @see
	 * {@link org.jacp.javafx.rcp.workbench.AFX2Workbench#handleInitialLayout(IAction, IWorkbenchLayout, Stage)}
	 * 
	 * @param layout
	 */
	public abstract void postHandle(final FX2ComponentLayout layout);

	@Override
	/**
	 * {@inheritDoc}
	 */
	public final void registerComponent(
			final IPerspective<EventHandler<Event>, Event, Object> perspective) {
		perspective.init(this.componentDelegator.getComponentDelegateQueue(),
				this.messageDelegator.getMessageDelegateQueue());
		this.perspectiveCoordinator.addPerspective(perspective);
		this.componentDelegator.addPerspective(perspective);
		this.messageDelegator.addPerspective(perspective);
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public final void unregisterComponent(
			final IPerspective<EventHandler<Event>, Event, Object> perspective) {
		this.perspectiveCoordinator.removePerspective(perspective);
		this.componentDelegator.removePerspective(perspective);
		this.messageDelegator.removePerspective(perspective);
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public final FX2WorkbenchLayout getWorkbenchLayout() {
		return (FX2WorkbenchLayout) this.workbenchLayout;
	}

	@Override
	public IComponentHandler<IPerspective<EventHandler<Event>, Event, Object>, IAction<Event, Object>> getComponentHandler() {
		return componentHandler;
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public final void setPerspectives(
			final List<IPerspective<EventHandler<Event>, Event, Object>> perspectives) {
		this.perspectives = perspectives;

	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public final List<IPerspective<EventHandler<Event>, Event, Object>> getPerspectives() {
		return this.perspectives;
	}

	@Override
	public final IActionListener<EventHandler<Event>, Event, Object> getActionListener() {
		return new FXActionListener(new FXAction("workbench"),
				perspectiveCoordinator.getMessageQueue());
	}

	/**
	 * set basic layout manager for workspace
	 * 
	 * @param stage
	 *            javafx.stage.Stage
	 */
	private void setBasicLayout(final Stage stage) {
		int x = this.getWorkbenchLayout().getWorkbenchSize().getX();
		int y = this.getWorkbenchLayout().getWorkbenchSize().getY();

		// the top most pane is a Stackpane
		
		BorderPane top = new BorderPane();
		StackPane absoluteRoot = new StackPane();

		final BorderPane baseLayoutPane = new BorderPane();
		// top most pane
		this.root = new GridPane();
		root.setId("root-pane");
		JACPModalDialog.initDialog(baseLayoutPane);
		dimmer = JACPModalDialog.getInstance();
		dimmer.setVisible(false);

		glassPane = this.getWorkbenchLayout().getGlassPane();
		glassPane.autosize();
		glassPane.setVisible(false);
		glassPane.setPrefSize(0, 0);

		GaussianBlur blur = new GaussianBlur();
		blur.setRadius(0);
		baseLayoutPane.setEffect(blur);

		stage.initStyle((StageStyle) this.getWorkbenchLayout().getStyle());
		// holds only the decorator (if set) and the menu bar.

		// TODO: handle the custom decorator

		// add the menu if needed
		if (this.getWorkbenchLayout().isMenuEnabled()) {
			top.setTop(this.getWorkbenchLayout().getMenu());
			this.getWorkbenchLayout().getMenu().setMenuDragEnabled(stage);
		}
		// add the toolbars in a specific order
		if (!this.getWorkbenchLayout().getRegisteredToolbars().isEmpty()) {

			final BorderPane toolbarPane = new BorderPane();
			baseLayoutPane.setCenter(toolbarPane);

			final Map<ToolbarPosition, JACPToolBar> registeredToolbars = this
					.getWorkbenchLayout().getRegisteredToolbars();
			final Iterator<Entry<ToolbarPosition, JACPToolBar>> it = registeredToolbars
					.entrySet().iterator();
			while (it.hasNext()) {
				final Entry<ToolbarPosition, JACPToolBar> entry = it.next();
				final ToolbarPosition position = entry.getKey();
				final JACPToolBar toolBar = entry.getValue();
				this.assignCorrectToolBarLayout(position, toolBar, toolbarPane);
			}

			// add root to the center
			toolbarPane.setCenter(root);

		} else {
			baseLayoutPane.setCenter(root);
		}
		absoluteRoot.getChildren().add(baseLayoutPane);
		absoluteRoot.setId("root");
		stage.setScene(new Scene(top, x, y));
		initCSS(stage.getScene());

		// new Layer for Menu Effects
		absoluteRoot.getChildren().add(glassPane);
		absoluteRoot.getChildren().add(dimmer);
		top.setCenter(absoluteRoot);
	}

	private void initCSS(Scene scene) {
		scene.getStylesheets().addAll(
				AFX2Workbench.class.getResource("/styles/jacp-styles.css")
						.toExternalForm(),
				// Workaround for CSS issue with HTML Editor
				com.sun.javafx.scene.web.skin.HTMLEditorSkin.class.getResource(
						"html-editor.css").toExternalForm());

	}

	/**
	 * set toolBars to correct position
	 * 
	 * @param layout
	 * @param bar
	 * @param pane
	 * @param x
	 * @param y
	 */
	private void assignCorrectToolBarLayout(ToolbarPosition position,
			ToolBar bar, BorderPane pane) {

		switch (position) {
		case NORTH:
			pane.setTop(bar);
			break;
		case SOUTH:
			pane.setBottom(bar);
			break;
		case EAST:
			bar.setOrientation(Orientation.VERTICAL);
			pane.setRight(bar);
			break;
		case WEST:
			bar.setOrientation(Orientation.VERTICAL);
			pane.setLeft(bar);
			break;
		}
	}

	private void log(final String message) {
		if (this.logger.isLoggable(Level.FINE)) {
			this.logger.fine(">> " + message);
		}
	}
}
