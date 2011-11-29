package org.jacp.javafx2.rcp.perspectives;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.componentLayout.Layout;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;
import org.jacp.javafx2.rcp.componentLayout.FX2PerspectiveLayout;
import org.jacp.javafx2.rcp.perspective.AFX2Perspective;

public class DemoFX2PerspectiveTwo extends AFX2Perspective{

	

	@Override
	public void handlePerspective(IAction<Event, Object> action,
			FX2PerspectiveLayout perspectiveLayout) {
		BorderPane layout = new BorderPane();
		final  IActionListener<EventHandler<Event>, Event, Object> listenerBottomOne = getActionListener();
		listenerBottomOne.getAction().addMessage("id01","oneButtonBottomTwo");
		Button bc=  new Button("Button Perspective 2");
        bc.setStyle("-fx-background-color: slateblue; -fx-text-fill: white;");
        bc.setOnMouseClicked((EventHandler<? super MouseEvent>) listenerBottomOne);
        Group top = new Group();
		layout.setTop(top);
		Group bottom = new Group();
		Pane left = new Pane();
		Pane right = new Pane();
		left.setMinSize(50, 240);
		right.setMinSize(50, 240);
		left.setMaxSize(50, 240);
		right.setMaxSize(50, 240);
		// bottom.getChildren().add(new Rectangle(1024, 50, Color.BLACK));
		layout.setBottom(bottom);
		layout.setCenter(bc);
		layout.setLeft(left);
		layout.setRight(right);
		perspectiveLayout.setRootComponent(layout);
		perspectiveLayout.registerTargetLayoutComponent("P1", bottom);
		perspectiveLayout.registerTargetLayoutComponent("P2", left);
		perspectiveLayout.registerTargetLayoutComponent("P3", right);
		perspectiveLayout.registerTargetLayoutComponent("P0", top);
		
	}
	
	@Override
	public void onStartPerspective(final FX2ComponentLayout layout) {
		System.out.println("run on start perspective two bar:"+layout.getMenu() +" bars:"+layout.getToolBar(Layout.NORTH));
		ToolBar north = layout.getToolBar(Layout.SOUTH);
		Button b1= new Button("p2");
		north.getItems().add(b1);
	}

	@Override
	public void onTearDownPerspective(final FX2ComponentLayout layout) {
		// TODO Auto-generated method stub
		
	}

}
