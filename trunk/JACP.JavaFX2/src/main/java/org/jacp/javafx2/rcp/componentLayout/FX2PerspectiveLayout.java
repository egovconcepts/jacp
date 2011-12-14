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
package org.jacp.javafx2.rcp.componentLayout;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javafx.scene.Node;
import org.jacp.api.componentLayout.IPerspectiveLayout;

/**
 * Configuration handler for perspective components, used in handle method for
 * configuration and registration of layout 'leaves' where subcomponents can
 * live in. Create your own complex layout, return the root node and register
 * parts of your layout that can handle subcomponents
 * 
 * @author Andy Moncsek
 */
public class FX2PerspectiveLayout implements IPerspectiveLayout<Node, Node> {

	private Node rootComponent;
	private final Map<String, Node> targetComponents = new ConcurrentHashMap<String, Node>();

	@Override
	public void registerRootComponent(Node comp) {
		this.rootComponent = comp;
	}

	@Override
	public Node getRootComponent() {
		return this.rootComponent;
	}

	@Override
	public Map<String, Node> getTargetLayoutComponents() {
		return this.targetComponents;
	}

	@Override
	public void registerTargetLayoutComponent(String id, Node target) {
		this.targetComponents.put(id, target);
	}

}