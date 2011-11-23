/*
 * Copyright (C) 2010,2011.
 * AHCP Project
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
 */
package org.jacp.javafx2.rcp.action;

import javafx.event.Event;
import javafx.event.EventHandler;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.coordinator.ICoordinator;

/**
 * This class represents the JACP FX2 Event listener... this class can be
 * assigned to components, it reacts on actions and notifies other components in
 * JACP
 * 
 * @author Andy Moncsek
 */
public class FX2ActionListener implements EventHandler<Event>,
		IActionListener<EventHandler<Event>, Event, Object> {
	private IAction<Event, Object> action;
	private final ICoordinator<EventHandler<Event>, Event, Object> coordinator;

	public FX2ActionListener(final IAction<Event, Object> action,
			final ICoordinator<EventHandler<Event>, Event, Object> coordinator) {
		this.action = action;
		this.coordinator = coordinator;
	}

	@Override
	public void notifyComponents(IAction<Event, Object> action) {
		this.coordinator.handle(action);
	}

	@Override
	public void setAction(IAction<Event, Object> action) {
		this.action = action;
	}

	@Override
	public IAction<Event, Object> getAction() {
		return this.action;
	}

	@Override
	@SuppressWarnings("unchecked")
	public EventHandler<Event> getListener() {
		return this;
	}

	@Override
	public void handle(Event t) {
		this.action.setActionEvent(t);
		this.notifyComponents(this.action);
	}

	@Override
	public void performAction(Event arg0) {
		this.handle(arg0);
	}

}