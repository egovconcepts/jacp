/*
 * Copyright (C) 2010,2011.
 * AHCP Project (http://code.google.com/p/jacp/)
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
package org.jacp.api.coordinator;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.jacp.api.action.IAction;
import org.jacp.api.component.IComponent;
import org.jacp.api.handler.IComponentHandler;

/**
 * Defines a basic observer for component messages; handles the message and
 * delegate to responsible component.
 * 
 * @author Andy Moncsek
 * @param <L>
 *            defines the action listener type
 * @param <A>
 *            defines the basic action type
 * @param <M>
 *            defines the basic message type
 */
public interface ICoordinator<L, A, M> {

	/**
	 * Handles message to specific component addressed by the id.
	 * 
	 * @param id
	 * @param action
	 */
	void handleMessage(final String id, final IAction<A, M> action);



	/**
	 * Returns a specific, observed perspective or component by id.
	 * 
	 * @param id
	 * @return the corresponding component, perspective or null when nothing
	 *         found
	 */
	<P extends IComponent<L, A, M>> P getObserveableById(final String id,
			final List<P> perspectives);

	/**
	 * Handle a message to an active component.
	 * 
	 * @param component
	 * @param action
	 */
	<P extends IComponent<L, A, M>> void handleActive(final P component,
			final IAction<A, M> action);

	/**
	 * Handle a message to an inactive component.
	 * 
	 * @param component
	 * @param action
	 */
	<P extends IComponent<L, A, M>> void handleInActive(final P component,
			final IAction<A, M> action);
	/**
	 * Returns the message queue of coordinator.
	 * @return
	 */
	BlockingQueue<IAction<A, M>> getMessageQueue();
	
	
	/**
	 * returns associated componentHandler
	 * @return
	 */
    <P extends IComponent<L,A,M>>IComponentHandler<P, IAction<A,M>> getComponentHandler();
	/**
	 * set associated componentHandler
	 * @return
	 */
    <P extends IComponent<L,A,M>> void setComponentHandler(final IComponentHandler<P, IAction<A,M>> handler);
    
 
}
