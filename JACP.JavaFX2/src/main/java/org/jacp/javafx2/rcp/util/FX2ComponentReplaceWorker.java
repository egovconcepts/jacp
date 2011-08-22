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
package org.jacp.javafx2.rcp.util;


import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import org.jacp.api.component.IVComponent;
import org.jacp.api.componentLayout.Layout;




/**
 * Background Worker to execute components handle method to replace or add the
 * component
 * 
 * @author Andy Moncsek
 * 
 */
public class FX2ComponentReplaceWorker extends AFX2ComponentWorker<IVComponent<Node, EventHandler<ActionEvent>, ActionEvent, Object>> {

    private final Map<String, Node> targetComponents;
    private final IVComponent<Node, EventHandler<ActionEvent>, ActionEvent, Object> component;
    private final Map<Layout, Node> bars;
    private final MenuBar menu;
    private volatile BlockingQueue<Boolean> lock = new ArrayBlockingQueue<Boolean>(
            1);

    public FX2ComponentReplaceWorker(
            final Map<String, Node> targetComponents,
            final IVComponent<Node, EventHandler<ActionEvent>, ActionEvent, Object> component,
            final Map<Layout, Node> bars, final MenuBar menu) {
        this.targetComponents = targetComponents;
        this.component = component;
        this.bars = bars;
        this.menu = menu;
    }

    @Override
    protected IVComponent<Node, EventHandler<ActionEvent>, ActionEvent, Object> call() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected final void done() {
        try {
            final IVComponent<Node, EventHandler<ActionEvent>, ActionEvent, Object> component = this.get();
            component.setBlocked(false);
        } catch (final InterruptedException e) {
            System.out.println("Exception in Component REPLACE Worker, Thread interrupted:");
            e.printStackTrace();
            // TODO add to error queue and restart thread if
            // messages in
            // queue
        } catch (final ExecutionException e) {
            System.out.println("Exception in Component REPLACE Worker, Thread Excecution Exception:");
            e.printStackTrace();
            // TODO add to error queue and restart thread if
            // messages in
            // queue
        } catch (final Exception e) {
            System.out.println("Exception in Component REPLACE Worker, Thread Exception:");
            e.printStackTrace();
            // TODO add to error queue and restart thread if
            // messages in
            // queue
        } finally {
            component.setBlocked(false);
        }

    }
    
    private void removeComponentValue(
            final IVComponent<Node, EventHandler<ActionEvent>, ActionEvent, Object> component,
            final Node previousContainer) {
    // bar entries
    final Map<Layout, Node> componentBarEnries = component
                    .getBarEntries();
    // when global bars and local bars are defined
    // TODO handle menu bar entries

    if (previousContainer == null) {
            releaseLock();
    } else {
            final Node parent = previousContainer.getParent();
            if (parent != null) {
            	 Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                            	getChildren(parent).remove(component
                                                    .getRoot());
                            }
                    });
            }
            releaseLock();
    }

}
    
    /**
     * run in Main Thread
     */
    protected final void process(final List<ChunkDTO> chunks) {
            // process method runs in EventDispatchThread
            for (int i=0;i<chunks.size();i++) {
                    final ChunkDTO dto = chunks.get(i);
                    final Node parent = dto.getParent();
                    final IVComponent<Node, EventHandler<ActionEvent>, ActionEvent, Object> component = dto
                                    .getComponent();
                    // TODO decide if menu and bars are always handled or
                    // only at start
                    // time
                    // component.handleBarEntries(dto.getBars());
                    // component.handleMenuEntries(dto.getMenu());
                    final Node previousContainer = dto
                                    .getPreviousContainer();
                    final String currentTaget = dto.getCurrentTaget();
                    // remove old view
                    log(" //1.1.1.1.3// handle old component remove: "
                                    + component.getName());
                    if (parent != null && previousContainer != null) {
                            handleOldComponentRemove(parent,
                                            previousContainer);
                    }

                    final Node root = component.getRoot();
                    if (root != null) {
                            // add new view
                            log(" //1.1.1.1.4// handle new component insert: "
                                            + component.getName());
                            root.setVisible(true);
                            handleNewComponentValue(component,
                                            targetComponents, parent,
                                            currentTaget);
                    }

            }
            releaseLock();
    }

    /**
     * run in thread
     */
    private void waitOnLock() {
        try {
            lock.take();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * run in thread
     */
    private void releaseLock() {
        lock.add(true);
    }
}
