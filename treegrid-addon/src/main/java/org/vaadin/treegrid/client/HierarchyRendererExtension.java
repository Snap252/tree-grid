package org.vaadin.treegrid.client;

import com.vaadin.client.renderers.TextRenderer;

public class HierarchyRendererExtension extends HierarchyRenderer {
	public HierarchyRendererExtension() {
		super.setInnerRenderer(new TextRenderer());
	}
}
