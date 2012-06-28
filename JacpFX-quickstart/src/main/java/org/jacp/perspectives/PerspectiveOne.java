/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [PerspectiveOne.java]
 * AHCP Project (http://jacp.googlecode.com)
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
package org.jacp.perspectives;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import org.jacp.api.action.IAction;
import org.jacp.api.annotations.OnStart;
import org.jacp.api.annotations.OnTearDown;
import org.jacp.api.annotations.Perspective;
import org.jacp.api.util.ToolbarPosition;
import org.jacp.javafx.rcp.componentLayout.FXComponentLayout;
import org.jacp.javafx.rcp.componentLayout.PerspectiveLayout;
import org.jacp.javafx.rcp.components.toolBar.JACPToolBar;
import org.jacp.javafx.rcp.controls.optionPane.JACPDialogButton;
import org.jacp.javafx.rcp.controls.optionPane.JACPDialogUtil;
import org.jacp.javafx.rcp.controls.optionPane.JACPModalDialog;
import org.jacp.javafx.rcp.controls.optionPane.JACPOptionPane;
import org.jacp.javafx.rcp.perspective.AFXPerspective;
import org.jacp.javafx.rcp.util.FXUtil.MessageUtil;

/**
 * A simple perspective defining a split pane
 * 
 * @author Andy Moncsek
 * 
 */
@Perspective(id = "id01", name = "perspectiveOne" ,viewLocation="/perspectiveOne.fxml" , resourceBundleLocation="bundles.languageBundle")
public class PerspectiveOne extends AFXPerspective {
	@FXML
	private GridPane gridPaneLeft;
	@FXML
	private GridPane gridPaneRight;


	@Override
	public void handlePerspective(final IAction<Event, Object> action,
			final PerspectiveLayout perspectiveLayout) {
		if (action.getLastMessage().equals(MessageUtil.INIT)) {
			
			GridPane.setVgrow(perspectiveLayout.getRootComponent(), Priority.ALWAYS);
			GridPane.setHgrow(perspectiveLayout.getRootComponent(), Priority.ALWAYS);

			// register left panel  
			perspectiveLayout.registerTargetLayoutComponent("Pleft", gridPaneLeft);
			// register main panel 
			perspectiveLayout.registerTargetLayoutComponent("PMain",
					gridPaneRight);
		}

	}

	@OnStart
	public void onStartPerspective(FXComponentLayout layout) {
		// define toolbars and menu entries
		JACPToolBar toolbar = layout.getRegisteredToolBar(ToolbarPosition.NORTH);
		Button pressMe = new Button("press me");
		pressMe.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// create a modal dialog
				JACPOptionPane dialog = JACPDialogUtil.createOptionPane("modal dialog", "Add some action");
				dialog.setDefaultButton(JACPDialogButton.NO);
				dialog.setDefaultCloseButtonOrientation(Pos.CENTER_RIGHT);
				dialog.setOnYesAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						JACPModalDialog.getInstance().hideModalMessage();
					}
				});
				JACPModalDialog.getInstance().showModalMessage(dialog);

			}
		});
		toolbar.addOnEnd(pressMe);

	}

	@OnTearDown
	public void onTearDownPerspective(FXComponentLayout arg0) {
		// define toolbars and menu entries when close perspective

	}

}
