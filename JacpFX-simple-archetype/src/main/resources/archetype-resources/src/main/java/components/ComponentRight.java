#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.components;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.annotations.Component;
import org.jacp.javafx.rcp.component.AFXComponent;
import org.jacp.javafx.rcp.util.FXUtil.MessageUtil;

/**
 * Simple UI component, creates a ScrollPane with a Button and a label. The
 * Button will trigger a message to the StatelessCallback.
 * 
 * @author Andy Moncsek
 * 
 */
@Component(defaultExecutionTarget = "PMain", id = "id002", name = "componentRight", active = true)
public class ComponentRight extends AFXComponent {
	private ScrollPane pane;
	private Label rightLabel;

	@Override
	public Node handleAction(IAction<Event, Object> action) {
		// runs in worker thread
		if (action.getLastMessage().equals(MessageUtil.INIT)) {
			return createInitialLayout();
		}
		return null;
	}

	@Override
	public Node postHandleAction(Node arg0, IAction<Event, Object> action) {
		// runs in FX application thread
		if (action.getLastMessage().equals(MessageUtil.INIT)) {
			this.pane = (ScrollPane) arg0;
		} else {
			rightLabel.setText(action.getLastMessage().toString());
		}
		return this.pane;
	}
	
	@SuppressWarnings("unchecked")
	private ScrollPane createInitialLayout() {
		final ScrollPane pane = new ScrollPane();
		pane.setFitToHeight(true);
		pane.setFitToWidth(true);
		GridPane.setHgrow(pane, Priority.ALWAYS);
		GridPane.setVgrow(pane, Priority.ALWAYS);
		final VBox box = new VBox();
		rightLabel = new Label("");

		final Button right = new Button("right");
		// create message to StatelessCallback component
		IActionListener<EventHandler<Event>, Event, Object> listener = getActionListener(
				"id01.id004", "hello stateless component");
		right.setOnMouseClicked((EventHandler<? super MouseEvent>) listener);
		VBox.setMargin(right, new Insets(4, 2, 4, 5));
		box.getChildren().addAll(right, rightLabel);
		pane.setContent(box);
		return pane;
	}


}