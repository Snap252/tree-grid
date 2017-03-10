package org.vaadin.treegrid;

import com.vaadin.shared.communication.SharedState;
import com.vaadin.ui.Grid.AbstractRenderer;

public class HierarchyRenderer extends AbstractRenderer<String> {

	public HierarchyRenderer(String nullRepresentation) {
		super(String.class, nullRepresentation);
	}
	
	@Override
	protected SharedState getState() {
		return super.getState();
	}
}
