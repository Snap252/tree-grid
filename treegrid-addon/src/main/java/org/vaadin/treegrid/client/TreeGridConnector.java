package org.vaadin.treegrid.client;

import java.util.Collection;

import com.google.gwt.dom.client.Element;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.connectors.GridConnector;
import com.vaadin.client.renderers.ClickableRenderer;
import com.vaadin.client.widget.grid.EventCellReference;
import com.vaadin.client.widget.grid.GridEventHandler;
import com.vaadin.client.widget.grid.events.GridClickEvent;
import com.vaadin.client.widgets.Grid;
import com.vaadin.shared.ui.Connect;

import elemental.json.JsonObject;

@Connect(org.vaadin.treegrid.TreeGrid.class)
public class TreeGridConnector extends GridConnector {

    private String hierarchyColumnId;

    private final ClickableRenderer.RendererClickHandler<JsonObject> rch = new ClickableRenderer.RendererClickHandler<JsonObject>() {
        @Override
        public void onClick(ClickableRenderer.RendererClickEvent<JsonObject> event) {
            NavigationExtensionConnector navigation = getNavigationExtensionConnector();
            if (navigation != null) {
                navigation.toggleCollapse(getRowKey(event.getRow()));
            }

            event.stopPropagation();
            event.preventDefault();
        }
    };

    public HandlerRegistration addClickHandler(ClickableRenderer<?, ?> hr) {
    	return hr.addClickHandler(rch);
    }

    @Override
    public TreeGrid getWidget() {
        return (TreeGrid) super.getWidget();
    }

    @Override
    public TreeGridState getState() {
        return (TreeGridState) super.getState();
    }

    private HierarchyRenderer hierarchyRenderer;

    private HierarchyRenderer getHierarchyRenderer() {
        if (hierarchyRenderer == null) {
            hierarchyRenderer = new HierarchyRenderer();
        }
        return hierarchyRenderer;
    }

    // Expander click event handling

    private HandlerRegistration expanderClickHandlerRegistration;

    @Override
    protected void init() {
        super.init();
        expanderClickHandlerRegistration = getHierarchyRenderer()
                .addClickHandler(rch);

        replaceMemberFields();
    }

    @Override
    public void onUnregister() {
        super.onUnregister();

        expanderClickHandlerRegistration.removeHandler();
    }

    private NavigationExtensionConnector getNavigationExtensionConnector() {
        for (ServerConnector c : getChildren()) {
            if (c instanceof NavigationExtensionConnector) {
                return (NavigationExtensionConnector) c;
            }
        }
        return null;
    }

    /**
     * Replaces the following members
     * <ul>
     * <li>{@link com.vaadin.client.widgets.Grid.CellFocusEventHandler} as an element of the {@link
     * Grid#browserEventHandlers} list -> {@link CellFocusEventHandler}</li>
     * </ul>
     */
    private void replaceMemberFields() {
        // Swap Grid's CellFocusEventHandler to this custom one
        // The handler is identical to the original one except for the child widget check
        replaceCellFocusEventHandler(getWidget(), new CellFocusEventHandler());
    }

    private native void replaceCellFocusEventHandler(Grid grid, GridEventHandler eventHandler)/*-{
        var browserEventHandlers = grid.@com.vaadin.client.widgets.Grid::browserEventHandlers;

        // FocusEventHandler is initially 5th in the list of browser event handlers
        browserEventHandlers.@java.util.List::set(*)(5, eventHandler);
    }-*/;


	private native EventCellReference getEventCell(Grid grid)/*-{
        return grid.@com.vaadin.client.widgets.Grid::eventCell;
    }-*/;

    /**
     * Class to replace {@link com.vaadin.client.widgets.Grid.CellFocusEventHandler}.
     * The only difference is that it handles events originated from widgets in hierarchy cells.
     */
    private class CellFocusEventHandler implements GridEventHandler<JsonObject> {
        @Override
        public void onEvent(Grid.GridEvent<JsonObject> event) {
            Element target = Element.as(event.getDomEvent().getEventTarget());
            boolean elementInChildWidget = getWidget().isElementInChildWidget(target);

            // Ignore if event was handled by keyboard navigation handler
            if (event.isHandled() && !elementInChildWidget) {
                return;
            }

            // Ignore target in child widget but handle hierarchy widget
            if (elementInChildWidget &&
                    !HierarchyRenderer.isElementInHierarchyWidget(target)) {
                return;
            }

            Collection<String> navigation = getNavigationEvents(getWidget());
            if (navigation.contains(event.getDomEvent().getType())) {
                handleNavigationEvent(getWidget(), event);
            }
        }

        private native Collection<String> getNavigationEvents(Grid grid)/*-{
            return grid.@com.vaadin.client.widgets.Grid::cellFocusHandler
                .@com.vaadin.client.widgets.Grid.CellFocusHandler::getNavigationEvents()();
        }-*/;

        private native void handleNavigationEvent(Grid grid, Grid.GridEvent<JsonObject> event)/*-{
            grid.@com.vaadin.client.widgets.Grid::cellFocusHandler
                .@com.vaadin.client.widgets.Grid.CellFocusHandler::handleNavigationEvent(*)(
                    event.@com.vaadin.client.widgets.Grid.GridEvent::getDomEvent()(),
                    event.@com.vaadin.client.widgets.Grid.GridEvent::getCell()())
        }-*/;
    }
}
