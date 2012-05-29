/*
 * Copyright (C) 2010,2011.
 * AHCP Project (http://code.google.com/p/jacp)
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
package org.jacp.demo.perspectives;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.annotations.OnStart;
import org.jacp.api.annotations.OnTearDown;
import org.jacp.api.annotations.Perspective;
import org.jacp.api.util.ToolbarPosition;
import org.jacp.javafx.rcp.componentLayout.FXComponentLayout;
import org.jacp.javafx.rcp.componentLayout.PerspectiveLayout;
import org.jacp.javafx.rcp.components.toolBar.JACPToolBar;
import org.jacp.javafx.rcp.perspective.AFXPerspective;
import org.jacp.javafx.rcp.util.FXUtil.MessageUtil;

/**
 * Contact perspective; here you define the basic layout for your application
 * view and declare targets for your UI components.
 * 
 * @author Andy Moncsek
 * 
 */
@Perspective(id = "id01", name = "contactPerspective" ,viewLocation="/perspective2.fxml" )
public class ContactPerspective extends AFXPerspective {

	private String topId = "PmainContentTop";
	private String bottomId = "PmainContentBottom";
	
	@FXML
	private GridPane gridPane1;
	@FXML
	private GridPane gridPane2;
	@FXML
	private GridPane gridPane3;

	@OnStart
	/**
	 * create buttons in tool bars; menu entries  
	 */
	public void onStartPerspective(final FXComponentLayout layout) {
		System.out.println("Perspective Post");
		// create button in toolbar; button should switch top and bottom id's
		final JACPToolBar north = layout
				.getRegisteredToolBar(ToolbarPosition.NORTH);

		final Button custom = new Button("switch");
		custom.setTooltip(new Tooltip("Switch Components"));
		custom.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(final ActionEvent e) {
				final IActionListener<EventHandler<Event>, Event, Object> listener = ContactPerspective.this
						.getActionListener("switch");
				listener.getAction().setMessage("switch");
				listener.performAction(null);

			}
		});
		north.addOnEnd(custom);
	}

	@OnTearDown
	public void onTearDownPerspective(final FXComponentLayout layout) {
	}

	@Override
	public void handlePerspective(final IAction<Event, Object> action,
			final PerspectiveLayout perspectiveLayout) {
		if (action.getLastMessage().equals(MessageUtil.INIT)) {
			this.createPerspectiveLayout(perspectiveLayout);
		} else if (action.getLastMessage().equals("switch")) {
			final String tmp = this.topId;
			this.topId = this.bottomId;
			this.bottomId = tmp;
			this.createPerspectiveLayout(perspectiveLayout);
		}
	}

	private void createPerspectiveLayout(
			final PerspectiveLayout perspectiveLayout) {


		GridPane.setVgrow(perspectiveLayout.getRootComponent(), Priority.ALWAYS);
		GridPane.setHgrow(perspectiveLayout.getRootComponent(), Priority.ALWAYS);
		perspectiveLayout.registerTargetLayoutComponent("PleftMenu", gridPane1);
		// register main content Top
		perspectiveLayout.registerTargetLayoutComponent(this.topId,
				gridPane2);
		// register main content Bottom
		perspectiveLayout.registerTargetLayoutComponent(this.bottomId,
				gridPane3);
	}

	
}
