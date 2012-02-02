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
package org.jacp.javafx2.rcp.coordinator;

import javafx.event.Event;
import org.jacp.api.action.IAction;
import org.jacp.api.action.IDelegateDTO;

/**
 * DTO interface to transfer components to desired target
 * @author Andy Moncsek
 *
 */
public class DelegateDTO implements IDelegateDTO<Event, Object>{
	private final String target;
	private final IAction<Event, Object> action;
	
	
	public DelegateDTO(final String target,
			IAction<Event, Object> action) {
		this.target = target;
		this.action = action;
	}

	@Override
	public String getTarget() {
		return target;
	}

	@Override
	public IAction<Event, Object> getAction() {
		return action;
	}
}
