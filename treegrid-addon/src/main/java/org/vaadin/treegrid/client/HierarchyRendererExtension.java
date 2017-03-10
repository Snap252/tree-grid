package org.vaadin.treegrid.client;

import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.renderers.TextRenderer;
import com.vaadin.client.widget.grid.RendererCellReference;

public class HierarchyRendererExtension extends HierarchyRenderer {
	public HierarchyRendererExtension() {
		super.setInnerRenderer(new TextRenderer());
	}

	@Override
	public void render(RendererCellReference cell, Object data, Widget widget) {
		super.render(cell, data, widget);
	}
}
