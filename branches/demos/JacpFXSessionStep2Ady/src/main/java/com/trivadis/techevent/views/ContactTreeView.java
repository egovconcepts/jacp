/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [ContactTreeView.java]
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
package com.trivadis.techevent.views;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import com.trivadis.techevent.entity.Contact;


public class ContactTreeView extends ScrollPane {
	final ObservableList<Contact> contactList;
	private ListView<Contact> categoryListView;

	public ContactTreeView() {
		this.contactList = null;

	}

	public ContactTreeView(final ObservableList<Contact> contactList) {
		this.contactList = contactList;

		final GridPane gridPane = new GridPane();
		gridPane.getStyleClass().addAll("dark", "dark-border");
		this.getStyleClass().addAll("dark-scrollpane");
		this.setFitToHeight(true);
		this.setFitToWidth(true);
		GridPane.setHgrow(this, Priority.ALWAYS);
		GridPane.setVgrow(this, Priority.ALWAYS);
		this.setContent(gridPane);

		gridPane.setPadding(new Insets(5));
		gridPane.setHgap(10);
		gridPane.setVgap(10);

		// the label
		final Label categoryLbl = new Label("Category");
		categoryLbl.getStyleClass().addAll("light-label", "list-label");
		GridPane.setHalignment(categoryLbl, HPos.CENTER);
		gridPane.add(categoryLbl, 0, 0);

		this.categoryListView = this.createList();
		GridPane.setHgrow(categoryListView, Priority.ALWAYS);
		GridPane.setVgrow(categoryListView, Priority.ALWAYS);
		gridPane.add(categoryListView, 0, 1);
		GridPane.setMargin(categoryListView, new Insets(0, 10, 10, 10));
	}

	private ListView<Contact> createList() {
		final ListView<Contact> categoryListView = new ListView<Contact>(
				this.contactList);
		return categoryListView;
	}

	public void configureProgressBar(final Contact contact) {
		if (contact.getProgress() == null) {
			final ProgressBar progressBar = new ProgressBar();
			progressBar.getStyleClass().add("jacp-progress-bar");
			contact.setProgress(progressBar);
		}
		if (contact.getContacts().isEmpty()) {
			contact.getProgress().setVisible(false);
		} else {
			contact.getProgress().setVisible(true);
		}
	}

	public ListView<Contact> getCategoryListView() {
		return categoryListView;
	}

}