/*
 * Copyright (C) 2010 - 2012.
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
package org.jacp.demo.components;

import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import org.jacp.api.action.IAction;
import org.jacp.api.annotations.Component;
import org.jacp.demo.entity.Contact;
import org.jacp.demo.enums.BarChartAction;
import org.jacp.demo.main.Util;
import org.jacp.javafx.rcp.component.AFXComponent;
import org.jacp.javafx.rcp.util.FXUtil.MessageUtil;

/**
 * The chart view displays a faked consumer chart for each contact; The chart
 * data will be generated by the AnalyticsCallback component
 * 
 * @author Andy Moncsek Patrick Symmangk
 */
@Component(defaultExecutionTarget = "PmainContentBottom", id = "id003", name = "contactDemoChartView", active = true)
public class ContactChartViewComponent extends AFXComponent {
	private GridPane root;
	private CategoryAxis xAxis;
	private NumberAxis yAxis;
	private BarChart<String, Number> bc;
	private XYChart.Series<String, Number> series1;
	private XYChart.Series<String, Number> series2;
	private XYChart.Series<String, Number> series3;
	private XYChart.Series<String, Number> series4;
	public static final String[] YEARS = { "2006", "2007", "2008", "2009",
			"2010", "2011" };

	@Override
	public Node handleAction(final IAction<Event, Object> action) {
		System.out.println(action.getLastMessage());
		if (action.getLastMessage().equals(MessageUtil.INIT)) {
			this.root = this.createRoot();
			this.root.getChildren().add(this.createChart());
		}
		return null;
	}

	@Override
	public Node postHandleAction(final Node node,
			final IAction<Event, Object> action) {
		if (action.getLastMessage() instanceof BarChartAction) {
			if (BarChartAction.RESET.equals(action.getLastMessage())) {
				this.clearChartPane();
			}
		}
		if (action.getLastMessage() instanceof Contact) {
			final Contact contact = (Contact) action.getLastMessage();
			this.refreshChartPane(contact);
			final List<Data<String, Number>> data = contact.getDto()
					.getSeriesOneData();
			this.addData(this.series1, data);
			this.addData(this.series2, contact.getDto().getSeriesTwoData());
			this.addData(this.series3, contact.getDto().getSeriesThreeData());
			this.addData(this.series4, contact.getDto().getSeriesFourData());

		} 
		return this.root;
	}

	private GridPane createRoot() {
		final GridPane myRoot = new GridPane();
		myRoot.getStyleClass().addAll("dark", "bar-chart-root");
		myRoot.setAlignment(Pos.CENTER);
		GridPane.setHgrow(myRoot, Priority.ALWAYS);
		GridPane.setVgrow(myRoot, Priority.ALWAYS);
		System.out.println(myRoot);
		return myRoot;
	}

	protected BarChart<String, Number> createChart() {

		this.xAxis = new CategoryAxis();
		this.yAxis = new NumberAxis();
		this.yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(
				this.yAxis, "$", null));
		this.bc = new BarChart<String, Number>(this.xAxis, this.yAxis);
		this.bc.setAnimated(true);
		this.bc.setTitle(" ");

		this.xAxis.getStyleClass().add("jacp-bar");
		this.yAxis.getStyleClass().add("jacp-bar");
		this.xAxis.setLabel("Year");
		this.yAxis.setLabel("Price");

		this.series1 = new XYChart.Series<String, Number>();
		this.series1.setName("electronic");
		this.series2 = new XYChart.Series<String, Number>();
		this.series2.setName("clothes");
		this.series3 = new XYChart.Series<String, Number>();
		this.series3.setName("hardware");
		this.series4 = new XYChart.Series<String, Number>();
		this.series4.setName("books");

		GridPane.setHalignment(this.bc, HPos.CENTER);
		GridPane.setVgrow(this.bc, Priority.ALWAYS);
		GridPane.setHgrow(this.bc, Priority.ALWAYS);
		GridPane.setMargin(this.bc, new Insets(0, 6, 0, 0));
		return this.bc;
	}

	private void refreshChartPane(final Contact contact) {
		this.root.getChildren().clear();
		this.bc = this.createChart();
		this.root.getChildren().add(this.bc);
		this.bc.setTitle(contact.getFirstName() + " " + contact.getLastName()
				+ " consumer Chart");

		this.xAxis.setCategories(FXCollections
				.<String> observableArrayList(Arrays.asList(Util.YEARS)));
	}

	private void clearChartPane() {
		this.root.getChildren().clear();
		this.bc = this.createChart();
		this.root.getChildren().add(this.bc);
	}

	private void addData(final XYChart.Series<String, Number> series,
			final List<Data<String, Number>> data) {
		this.bc.getData().add(series);
		series.getData().addAll(data);
	}

}
