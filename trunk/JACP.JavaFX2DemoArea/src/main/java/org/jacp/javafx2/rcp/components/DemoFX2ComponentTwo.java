package org.jacp.javafx2.rcp.components;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.javafx2.rcp.component.AFX2Component;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;

public class DemoFX2ComponentTwo extends AFX2Component {
	VBox vbox = null;
	Button start = new Button("start");
	Button bc = new Button("message 2");
	Button move = new Button("move");
	boolean flag =false;
	int c=0;
	

	@Override
	public Node handleAction(IAction<Event, Object> action) {
/*		System.out
				.println("message to component two "+action.getLastMessage()+" in thread"+ Thread.currentThread());*/
		if(action.getLastMessage().equals("start")) {

			for(int i=0;i<100000;i++){
				EventHandler<? super MouseEvent> listener = getListener("id001", "ping");
				listener.handle(null);
				
			}

		}else if(action.getLastMessage().equals("stop")) {
			flag =false;
		} else if(action.getLastMessage().equals("move")) {
			System.out.println("should move");
			String target = this.getExecutionTarget();
			if(target.equals("P0")) {
				this.setExecutionTarget("P1");
			} else {
				this.setExecutionTarget("P0");
			}
		}
		return  null;
	}

	public Node createDemoButtonBar(IAction<Event, Object> action) {
		if(vbox==null) {
			vbox = new VBox();
			vbox.setPadding(new Insets(0, 0, 0, 0));
			vbox.setSpacing(10);
		}

		vbox.getChildren().clear();
		vbox.getChildren().add(createBar(action));
		return vbox;
	}
	
	private HBox createBar(IAction<Event, Object> action) {
		HBox toolbar = new HBox();
		toolbar.setPrefSize(1024, 50);		
		
		bc.setOnMouseClicked(getListener("id001", "DemoFX2ComponentTwo"));

		Button button2 = new Button("me");
		button2.setOnMouseClicked(getListener(null, "me"));

		start.setOnMouseClicked(getListener(null, "start"));

		toolbar.getChildren().add(bc);
		toolbar.getChildren().add(start);
		toolbar.getChildren().add(button2);
		toolbar.getChildren().add(move);
		
		move.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event arg0) {
				if(true){
					move();
				}

			}
		});
		return toolbar;
	}
	
	public final void move() {
		EventHandler<? super MouseEvent> listener = getListener("id001", "move");
		listener.handle(null);
		EventHandler<? super MouseEvent> listener2 = getListener(null,"move");
		listener2.handle(null);
	}
	
	private EventHandler<? super MouseEvent> getListener(final String id, final String message) {
		final IActionListener<EventHandler<Event>, Event, Object> listener = getActionListener();
		if(id!=null) {
			listener.getAction()
			.addMessage(id, message);
		}else {
			listener.getAction().setMessage(message);
		}

		
		return (EventHandler<? super MouseEvent>) listener;
	}

	@Override
	public Node postHandleAction(Node node, IAction<Event, Object> action) {
		if(vbox==null) {
			return createDemoButtonBar(action);
		} 
		if(action.getLastMessage().equals("me")) {
			bc.setStyle("-fx-background-color: red; -fx-text-fill: white;");
		} else  {

			bc.setStyle("-fx-background-color: slateblue; -fx-text-fill: white;");
		}
		return vbox;
	}

	@Override
	public void onStartComponent(final FX2ComponentLayout layout) {

		
	}

	@Override
	public void onTearDownComponent(final FX2ComponentLayout layout) {
		
	}



}