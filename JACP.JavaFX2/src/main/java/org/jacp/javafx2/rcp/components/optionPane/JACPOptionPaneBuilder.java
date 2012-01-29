package org.jacp.javafx2.rcp.components.optionPane;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

// TODO: Auto-generated Javadoc
/**
 * The Class JACPOptionPaneBuilder.
 */
public class JACPOptionPaneBuilder {

	/** The on ok action. */
	private EventHandler<ActionEvent> onOkAction;
	
	/** The on cancel action. */
	private EventHandler<ActionEvent> onCancelAction;
	
	/** The on yes action. */
	private EventHandler<ActionEvent> onYesAction;
	
	/** The on no action. */
	private EventHandler<ActionEvent> onNoAction;

	/** The title. */
	private String title;
	
	/** The content. */
	private String content;

	/** The default button. */
	private JACPDialogButton defaultButton;

	/**
	 * Gets the on ok action.
	 *
	 * @return the on ok action
	 */
	private EventHandler<ActionEvent> getOnOkAction() {
		return onOkAction;
	}

	/**
	 * Sets the on ok action.
	 *
	 * @param onOkAction the on ok action
	 * @return the jACP option pane builder
	 */
	public JACPOptionPaneBuilder setOnOkAction(
			EventHandler<ActionEvent> onOkAction) {
		this.onOkAction = onOkAction;
		return this;
	}

	/**
	 * Gets the on cancel action.
	 *
	 * @return the on cancel action
	 */
	private EventHandler<ActionEvent> getOnCancelAction() {
		return onCancelAction;
	}

	/**
	 * Sets the on cancel action.
	 *
	 * @param onCancelAction the on cancel action
	 * @return the jACP option pane builder
	 */
	public JACPOptionPaneBuilder setOnCancelAction(
			EventHandler<ActionEvent> onCancelAction) {
		this.onCancelAction = onCancelAction;
		return this;
	}

	/**
	 * Gets the on yes action.
	 *
	 * @return the on yes action
	 */
	private EventHandler<ActionEvent> getOnYesAction() {
		return onYesAction;
	}

	/**
	 * Sets the on yes action.
	 *
	 * @param onYesAction the on yes action
	 * @return the jACP option pane builder
	 */
	public JACPOptionPaneBuilder setOnYesAction(
			EventHandler<ActionEvent> onYesAction) {
		this.onYesAction = onYesAction;
		return this;
	}

	/**
	 * Gets the on no action.
	 *
	 * @return the on no action
	 */
	private EventHandler<ActionEvent> getOnNoAction() {
		return onNoAction;
	}

	/**
	 * Sets the on no action.
	 *
	 * @param onNoAction the on no action
	 * @return the jACP option pane builder
	 */
	public JACPOptionPaneBuilder setOnNoAction(
			EventHandler<ActionEvent> onNoAction) {
		this.onNoAction = onNoAction;
		return this;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	private String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the title
	 * @return the jACP option pane builder
	 */
	public JACPOptionPaneBuilder setTitle(String title) {
		this.title = title;
		return this;
	}

	/**
	 * Gets the content.
	 *
	 * @return the content
	 */
	private String getContent() {
		return content;
	}

	/**
	 * Sets the content.
	 *
	 * @param content the content
	 * @return the jACP option pane builder
	 */
	public JACPOptionPaneBuilder setContent(String content) {
		this.content = content;
		return this;
	}

	/**
	 * Gets the default button.
	 *
	 * @return the default button
	 */
	private JACPDialogButton getDefaultButton() {
		return defaultButton;
	}

	/**
	 * Sets the default button.
	 *
	 * @param defaultButton the default button
	 * @return the jACP option pane builder
	 */
	public JACPOptionPaneBuilder setDefaultButton(
			JACPDialogButton defaultButton) {
		this.defaultButton = defaultButton;
		return this;
	}

	/**
	 * Builds the.
	 *
	 * @return the jACP option pane
	 */
	public JACPOptionPane build() {
		// build OptionPane!
		JACPOptionPane pane = JACPDialogUtil.createOptionPane(getTitle(),
				getContent(), getDefaultButton());
		if (getOnCancelAction() != null)
			pane.setOnCancelAction(getOnCancelAction());
		if (getOnOkAction() != null)
			pane.setOnOkAction(getOnOkAction());
		if (getOnYesAction() != null)
			pane.setOnYesAction(getOnYesAction());
		if (getOnNoAction() != null)
			pane.setOnNoAction(getOnNoAction());
		return pane;
	}

}
