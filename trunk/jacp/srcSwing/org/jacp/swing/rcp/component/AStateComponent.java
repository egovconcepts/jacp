package org.jacp.swing.rcp.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.base.IPerspective;
import org.jacp.api.base.ISubComponent;
import org.jacp.api.observers.IObserver;
import org.jacp.swing.rcp.action.SwingAction;
import org.jacp.swing.rcp.action.SwingActionListener;

public abstract class AStateComponent implements
		ISubComponent<ActionListener, ActionEvent, Object> {

	private String id;
	private String target;
	private String name;
	private IObserver<ActionListener, ActionEvent, Object> componentObserver;
	private final BlockingQueue<IAction<ActionEvent, Object>> incomingActions = new ArrayBlockingQueue<IAction<ActionEvent, Object>>(
			20);
	private volatile boolean active = false;
	private IPerspective<ActionListener, ActionEvent, Object> parentPerspective;
	private volatile boolean blocked;

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
	public String getName() {
		if (name == null) {
			throw new UnsupportedOperationException("No name set");
		}
		return name;
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
	public void setId(final String id) {
		this.id = id;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public void setObserver(
			final IObserver<ActionListener, ActionEvent, Object> observer) {
		this.componentObserver = observer;

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
	public IPerspective<ActionListener, ActionEvent, Object> getParentPerspective() {
		return parentPerspective;
	}

	@Override
	public String getTarget() {
		return target;
	}

	@Override
	public boolean hasIncomingMessage() {
		return !incomingActions.isEmpty();
	}

	@Override
	public boolean isBlocked() {
		return blocked;
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
	public void setBlocked(final boolean blocked) {
		this.blocked = blocked;
	}

	@Override
	public void setParentPerspective(
			final IPerspective<ActionListener, ActionEvent, Object> perspective) {
		parentPerspective = perspective;

	}

	@Override
	public void setTarget(final String target) {
		this.target = target;
	}

	@Override
	public <C> C handle(final IAction<ActionEvent, Object> action) {
		return (C) handleAction(action);
	}

	public abstract Object handleAction(IAction<ActionEvent, Object> action);

}