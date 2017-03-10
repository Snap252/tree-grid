package org.vaadin.treegrid;

import com.vaadin.ui.Grid;

import org.vaadin.treegrid.client.NavigationExtensionConnector;

public class NavigationExtension extends Grid.AbstractGridExtension {

    private <T extends Grid & ExpansionTogglable> NavigationExtension(final T grid) {
        super(grid);

        registerRpc(new NavigationExtensionConnector.NodeCollapseRpc() {
            @Override
            public void toggleCollapse(String rowKey) {
                Object itemId = getItemId(rowKey);
                grid.toggleExpansion(itemId);
            }
        });
    }

    static <T extends Grid & ExpansionTogglable> NavigationExtension extend(T grid) {
        return new NavigationExtension(grid);
    }
}
