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

import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;

import org.jacp.api.componentLayout.IBaseLayout;
import org.jacp.api.util.ToolbarPosition;

/**
 * A FX2ComponentLayout acts as an wrapper to the references of the main menu
 * and the defined bar entries; The menu and the bars are defined in the
 * workbench instance at application startup.
 * 
 * @author Andy Moncsek
 * 
 */
public class FX2ComponentLayout implements IBaseLayout<Node> {
	private final Map<ToolbarPosition, ToolBar> registeredToolBars;
	private final MenuBar menu;
	private final Pane glassPane;

	public FX2ComponentLayout(final MenuBar menu,
			final Map<ToolbarPosition, ToolBar> registeredToolBars,
			final Pane glassPane) {
		this.menu = menu;
		this.registeredToolBars = registeredToolBars;
		this.glassPane = glassPane;
	}

	@Override
	public ToolBar getRegisteredToolBar(ToolbarPosition position) {
		return this.registeredToolBars.get(position);
	}

	@Override
	public final MenuBar getMenu() {
		return this.menu;
	}

	public final Pane getGlassPane() {
		return glassPane;
	}

}