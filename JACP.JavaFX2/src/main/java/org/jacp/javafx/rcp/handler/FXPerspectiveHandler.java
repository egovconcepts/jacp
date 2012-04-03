/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [FX2PerspectiveHandler.java]
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
package org.jacp.javafx.rcp.handler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;

import org.jacp.api.action.IAction;
import org.jacp.api.component.ICallbackComponent;
import org.jacp.api.component.IStatelessCallabackComponent;
import org.jacp.api.component.ISubComponent;
import org.jacp.api.componentLayout.IPerspectiveLayout;
import org.jacp.api.handler.IComponentHandler;
import org.jacp.api.launcher.Launcher;
import org.jacp.javafx.rcp.component.AFXComponent;
import org.jacp.javafx.rcp.component.AStatefulCallbackComponent;
import org.jacp.javafx.rcp.component.AStatelessCallbackComponent;
import org.jacp.javafx.rcp.componentLayout.FXComponentLayout;
import org.jacp.javafx.rcp.scheduler.StatelessCallbackScheduler;
import org.jacp.javafx.rcp.util.FXComponentInitWorker;
import org.jacp.javafx.rcp.util.FXComponentReplaceWorker;
import org.jacp.javafx.rcp.util.StateComponentRunWorker;

/**
 * Handles initialization an reassignment of components in perspective
 * 
 * @author Andy moncsek
 * 
 */
public class FXPerspectiveHandler
		implements
		IComponentHandler<ISubComponent<EventHandler<Event>, Event, Object>, IAction<Event, Object>> {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	public static int MAX_INCTANCE_COUNT;
	private final FXComponentLayout layout;
	private final Launcher<?> launcher;
	private StatelessCallbackScheduler scheduler;
	private final IPerspectiveLayout<Node, Node> perspectiveLayout;
	private final BlockingQueue<ISubComponent<EventHandler<Event>, Event, Object>> componentDelegateQueue;
	private final ExecutorService executor = Executors
			.newFixedThreadPool(MAX_INCTANCE_COUNT);

	static {
		final Runtime runtime = Runtime.getRuntime();
		final int nrOfProcessors = runtime.availableProcessors();
		MAX_INCTANCE_COUNT = nrOfProcessors + (nrOfProcessors / 2);
	}

	public FXPerspectiveHandler(
			final Launcher<?> launcher,
			final FXComponentLayout layout,
			final IPerspectiveLayout<Node, Node> perspectiveLayout,
			final BlockingQueue<ISubComponent<EventHandler<Event>, Event, Object>> componentDelegateQueue) {
		this.layout = layout;
		this.launcher = launcher;
		this.perspectiveLayout = perspectiveLayout;
		this.componentDelegateQueue = componentDelegateQueue;
		scheduler = new StatelessCallbackScheduler(this.launcher);
	}

	@Override
	public final void initComponent(final IAction<Event, Object> action,
			final ISubComponent<EventHandler<Event>, Event, Object> component) {
		if (Platform.isFxApplicationThread()) {
			handleInit(action, component);
		} else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					handleInit(action, component);
				}
			});
		}

	}

	@Override
	public final void handleAndReplaceComponent(IAction<Event, Object> action,
			ISubComponent<EventHandler<Event>, Event, Object> component) {
		if (component.isBlocked() && component.isStarted()) {
			this.putMessageToQueue(component, action);
			this.log("ADD TO QUEUE:::" + component.getName());
		} else {
			this.executeComponentReplaceThread(this.perspectiveLayout,
					component, action, this.layout);

		}
		this.log("DONE EXECUTE REPLACE:::" + component.getName());
	}

	/**
	 * start component replace thread, be aware that all actions are in
	 * components message box!
	 * 
	 * @param layout
	 * @param component
	 */
	private final void executeComponentReplaceThread(
			final IPerspectiveLayout<? extends Node, Node> perspectiveLayout,
			final ISubComponent<EventHandler<Event>, Event, Object> component,
			final IAction<Event, Object> action, final FXComponentLayout layout) {
		if (component instanceof AFXComponent) {
			this.log("CREATE NEW THREAD:::" + component.getName());
			this.putMessageToQueue(component, action);
			this.runFXComponent(perspectiveLayout, component, layout);

		} else if (component instanceof AStatefulCallbackComponent) {
			this.log("CREATE NEW THREAD:::" + component.getName());
			this.putMessageToQueue(component, action);
			this.runStateComponent(action, ((AStatefulCallbackComponent) component));
		} else if (component instanceof AStatelessCallbackComponent) {
			this.log("RUN STATELESS COMPONENTS:::" + component.getName());
			runStatelessCallbackComponent(
					((AStatelessCallbackComponent) component), action);
		}

	}
	/**
	 * run at startup method in perspective
	 * 
	 * @param component
	 */
	private void runComponentOnStartupSequence(final AFXComponent component) {
		component.onStartComponent(this.layout);
	}

	/**
	 * handle state less callback component
	 * @param component
	 * @param action
	 */
	private final void runStatelessCallbackComponent(
			IStatelessCallabackComponent<EventHandler<Event>, Event, Object> component,
			final IAction<Event, Object> action) {
		
		scheduler.incomingMessage(action,component);
	}

	/**
	 * run component in background thread
	 * 
	 * @param layout
	 * @param component
	 */
	private final void runFXComponent(
			final IPerspectiveLayout<? extends Node, Node> perspectiveLayout,
			final ISubComponent<EventHandler<Event>, Event, Object> component,
			final FXComponentLayout layout) {
		this.executor.execute(new FXComponentReplaceWorker(perspectiveLayout
				.getTargetLayoutComponents(), this.componentDelegateQueue,
				((AFXComponent) component), layout));
	}

	/**
	 * run background components thread
	 * 
	 * @param action
	 * @param component
	 */
	private final void runStateComponent(
			final IAction<Event, Object> action,
			final ICallbackComponent<EventHandler<Event>, Event, Object> component) {
		this.executor.execute(new StateComponentRunWorker(this.componentDelegateQueue,
				component));
	}

	/**
	 * Execute component initialization.
	 * 
	 * @param action
	 * @param component
	 */
	private void handleInit(final IAction<Event, Object> action,
			final ISubComponent<EventHandler<Event>, Event, Object> component) {
		if (component instanceof AFXComponent) {
			this.log("COMPONENT EXECUTE INIT:::" + component.getName());
			this.runComponentOnStartupSequence(((AFXComponent) component));
			final FXComponentInitWorker tmp = new FXComponentInitWorker(
					this.perspectiveLayout.getTargetLayoutComponents(),
					((AFXComponent) component), this.layout, action);
			this.executor.execute(tmp);
		}// if END
		else if (component instanceof AStatefulCallbackComponent) {
			this.log("BACKGROUND COMPONENT EXECUTE INIT:::"
					+ component.getName());
			this.putMessageToQueue(component, action);
			this.runStateComponent(action, ((AStatefulCallbackComponent) component));
		}// else if END
		else if (component instanceof AStatelessCallbackComponent) {
			this.log("SATELESS BACKGROUND COMPONENT EXECUTE INIT:::"
					+ component.getName());
			runStatelessCallbackComponent(
					((AStatelessCallbackComponent) component), action);
		}// else if END

	}

	/**
	 * set component blocked and add message to queue
	 * 
	 * @param component
	 * @param action
	 */
	private final void putMessageToQueue(
			final ISubComponent<EventHandler<Event>, Event, Object> component,
			final IAction<Event, Object> action) {
		component.putIncomingMessage(action);
	}

	private void log(final String message) {
		if (this.logger.isLoggable(Level.FINE)) {
			this.logger.fine(">> " + message);
		}
	}
}
