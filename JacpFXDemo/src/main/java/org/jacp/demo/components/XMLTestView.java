package org.jacp.demo.components;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import org.jacp.api.action.IAction;
import org.jacp.api.annotations.DeclarativeComponent;
import org.jacp.api.annotations.OnStart;
import org.jacp.javafx.rcp.component.AFXMLComponent;
import org.jacp.javafx.rcp.componentLayout.FXComponentLayout;
import org.jacp.javafx.rcp.util.FXUtil;
import org.jacp.javafx.rcp.util.FXUtil.MessageUtil;

@DeclarativeComponent(defaultExecutionTarget = "PmainContentTop", id = "id006", name = "XMLTestView", active = false, uiDescriptionFile = "/AdoptionForm.fxml")
public class XMLTestView extends AFXMLComponent {
	@FXML
	private GridPane grid;
	@FXML
	private TextField field1;
	
	private String[] targets ={"PmainContentBottom","PmainContentTop"};
	
	private int count =0;

	@FXML
	private void handleSubmit(ActionEvent event) {

		System.out.println("action : " + grid);
		grid.setGridLinesVisible(!grid.isGridLinesVisible());
		this.getActionListener("stop").performAction(null);

	}
	
	@FXML
	private void change(ActionEvent event) {

		System.out.println("action : " + grid);
		grid.setGridLinesVisible(!grid.isGridLinesVisible());
		this.getActionListener("change").performAction(null);

	}

	@Override
	public Node handleAction(IAction<Event, Object> action) {
		System.out.println("handle : " + grid + " " + action.getLastMessage() + " thread" + Thread.currentThread());
		if (action.getLastMessage().equals("stop"))
			this.setActive(false);
		if (action.getLastMessage().equals("change")){
			this.setExecutionTarget(targets[count]);
			count = count==0?1:0;
		}
		return null;
	}

	@Override
	public void postHandleAction(Node xmlParent, Node handleNode, IAction<Event, Object> action) {
		// TODO Auto-generated method stub
		System.out.println("postHandle : " + grid + " " + action.getLastMessage() + " thread" + Thread.currentThread());
		if (!action.getLastMessage().equals(MessageUtil.INIT) && !action.getLastMessage().equals("stop"))
			field1.setText(action.getLastMessage().toString());

	}

	@OnStart
	public void start(FXComponentLayout layout, URL url, ResourceBundle resourceBundle) {
		System.out.println("STRAT" + grid + "  " + layout + " url: " + url.getPath());
	}

	// @OnStart
	// public void start(FXComponentLayout layout) {
	// System.out.println("STRAT" + grid+"  "+ layout);
	// }

	// @OnStart
	// public void start(URL url, ResourceBundle resourceBundle) {
	// System.out.println("STRAT" + grid+"  "+ " url: "+ url.getPath());
	// }

	// @OnStart
	// public void start(URL url) {
	// System.out.println("STRAT" + grid+"  ");
	// }
}