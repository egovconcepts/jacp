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
package org.jacp.swing.rcp.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.component.IBGComponent;
import org.jacp.api.component.IStateLessBGComponent;
import org.jacp.api.coordinator.ICoordinator;
import org.jacp.api.coordinator.IStatelessComponentCoordinator;
import org.jacp.api.perspective.IPerspective;
import org.jacp.impl.Launcher;
import org.jacp.swing.rcp.action.SwingAction;
import org.jacp.swing.rcp.action.SwingActionListener;
import org.jacp.swing.rcp.coordinator.StatelessComponentCoordinator;

/**
 * Represents a state less background component
 * 
 * @author Andy Moncsek
 * 
 */
public abstract class AStatelessComponent implements
	IStateLessBGComponent<ActionListener, ActionEvent, Object> {

    private String id;
    private String target = "";
    private String name;
    private volatile String handleComponentTarget;
    private volatile boolean active = true;
    private boolean isActived = false;
    private volatile AtomicBoolean blocked = new AtomicBoolean(false);
    private ICoordinator<ActionListener, ActionEvent, Object> componentObserver;
    private IPerspective<ActionListener, ActionEvent, Object> parentPerspective;
    private final BlockingQueue<IAction<ActionEvent, Object>> incomingActions = new ArrayBlockingQueue<IAction<ActionEvent, Object>>(
	    20);
    private IStatelessComponentCoordinator<ActionListener, ActionEvent, Object> coordinator;
    private Launcher<?> launcher;

    @Override
    public String getExecutionTarget() {
	return target;
    }

    @Override
    public void setExecutionTarget(final String target) {
	this.target = target;
    }

    @Override
    public void setParentPerspective(
	    final IPerspective<ActionListener, ActionEvent, Object> perspective) {
	parentPerspective = perspective;
    }

    @Override
    public IPerspective<ActionListener, ActionEvent, Object> getParentPerspective() {
	return parentPerspective;
    }

    @Override
    public boolean hasIncomingMessage() {
	return !incomingActions.isEmpty();
    }

    private synchronized IStatelessComponentCoordinator<ActionListener, ActionEvent, Object> getCooridinator() {
	if (coordinator == null) {
	    if (launcher == null) {
		throw new UnsupportedOperationException("no di launcher set");
	    }
	    coordinator = new StatelessComponentCoordinator(this, launcher);
	}
	return coordinator;
    }

    public void addMessage(final IAction<ActionEvent, Object> message) {
	getCooridinator().incomingMessage(message);
    }

    @Override
    public void putIncomingMessage(final IAction<ActionEvent, Object> action) {
	try {
	    incomingActions.put(action);
	} catch (final InterruptedException e) {
	    e.printStackTrace();
	}

    }

    @Override
    public IAction<ActionEvent, Object> getNextIncomingMessage() {
	if (hasIncomingMessage()) {
	    try {
		return incomingActions.take();
	    } catch (final InterruptedException e) {
		e.printStackTrace();
	    }
	}
	return null;
    }

    @Override
    public boolean isBlocked() {
	return blocked.get();
    }

    @Override
    public void setBlocked(final boolean blocked) {
	this.blocked.set(blocked);
    }

    @Override
    public IActionListener<ActionListener, ActionEvent, Object> getActionListener() {
	return new SwingActionListener(new SwingAction(id), componentObserver);
    }

    @Override
    public String getId() {
	if (id == null) {
	    throw new UnsupportedOperationException("No id set");
	}
	return id;
    }

    @Override
    public void setId(final String id) {
	this.id = id;
    }

    @Override
    public boolean isActive() {
	return active;
    }

    @Override
    public void setActive(final boolean active) {
	this.active = active;
    }

    @Override
    public String getName() {
	if (name == null) {
	    throw new UnsupportedOperationException("No name set");
	}
	return name;
    }

    @Override
    public void setName(final String name) {
	this.name = name;
    }

    @Override
    public void setObserver(
	    final ICoordinator<ActionListener, ActionEvent, Object> observer) {
	componentObserver = observer;
    }

    @Override
    public String getHandleTargetAndClear() {
	return handleComponentTarget;
    }

    @Override
    public void setHandleTarget(final String componentTargetId) {
	handleComponentTarget = componentTargetId;
    }

    @Override
    protected Object clone() {
	try {
	    final AStatelessComponent comp = (AStatelessComponent) super
		    .clone();
	    comp.setId(id);
	    comp.setActive(active);
	    comp.setName(name);
	    comp.setExecutionTarget(target);
	    comp.setHandleTarget(handleComponentTarget);
	    comp.setObserver(componentObserver);
	    comp.setParentPerspective(parentPerspective);
	    return comp;
	} catch (final CloneNotSupportedException e) {
	    e.printStackTrace();
	}

	return null;
    }

    /**
     * init cloned instance with values of blueprint
     * 
     * @param comp
     * @return
     */
    public synchronized IBGComponent<ActionListener, ActionEvent, Object> init(
	    final IBGComponent<ActionListener, ActionEvent, Object> comp) {
	comp.setId(id);
	comp.setActive(active);
	comp.setName(name);
	comp.setExecutionTarget(target);
	comp.setHandleTarget(handleComponentTarget);
	comp.setObserver(componentObserver);
	comp.setParentPerspective(parentPerspective);
	return comp;
    }

    @Override
    public <C> C handle(final IAction<ActionEvent, Object> action) {
	return (C) handleAction(action);
    }

    public abstract Object handleAction(IAction<ActionEvent, Object> action);

    @Override
    public void setLauncher(final Launcher<?> launcher) {
	this.launcher = launcher;
    }

    @Override
    public boolean isActived() {
	return isActived;
    }

    @Override
    public void setActived(final boolean isActived) {
	this.isActived = isActived;
    }

}