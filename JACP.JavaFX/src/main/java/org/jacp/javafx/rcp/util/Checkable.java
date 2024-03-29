/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [AFXSubComponent.java]
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
package org.jacp.javafx.rcp.util;

/**
 * A checkable component allows to check component states and to avoid manual manipulation in components.
 * 
 * @author Andy Moncsek
 * 
 */

public abstract class Checkable {
	protected volatile boolean started = false;
	
	protected final void checkPolicy(final Object member, final String name) {
		if (member != null || this.started) {
			throw new UnsupportedOperationException(name);
		}
	}
}
