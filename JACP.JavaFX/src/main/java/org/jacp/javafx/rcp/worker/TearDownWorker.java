/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [TearDownWorker.java]
 * AHCP Project (http://jacp.googlecode.com/)
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
package org.jacp.javafx.rcp.worker;

import java.util.concurrent.Callable;

import javafx.event.Event;
import javafx.event.EventHandler;

import org.jacp.api.annotations.OnTearDown;
import org.jacp.api.component.ICallbackComponent;
import org.jacp.javafx.rcp.util.FXUtil;

/**
 * This worker handles TearDown annotated methods for state- and stateless components. This type of components handle their live cycle always aoutside application thread.
 * @author Andy Moncsek
 *
 */
public class TearDownWorker implements Callable<Boolean>{
	private final ICallbackComponent<EventHandler<Event>, Event, Object> component;
	public TearDownWorker(final ICallbackComponent<EventHandler<Event>, Event, Object> component) {
		this.component = component;
	}
	@Override
	public Boolean call() throws Exception {
		synchronized (component) {
			// run teardown
			FXUtil.invokeHandleMethodsByAnnotation(OnTearDown.class,
					component);
		}
		return true;
	}

}
