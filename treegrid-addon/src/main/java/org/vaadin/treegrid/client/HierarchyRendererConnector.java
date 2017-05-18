package org.vaadin.treegrid.client;

import com.vaadin.client.ServerConnector;
import com.vaadin.client.connectors.AbstractRendererConnector;
import com.vaadin.client.renderers.Renderer;

/*
 * Copyright 2000-2014 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import com.vaadin.client.renderers.TextRenderer;
import com.vaadin.shared.ui.Connect;

import elemental.json.JsonValue;

/**
 * A connector for {@link TextRenderer}.
 *
 * @since 7.4
 * @author Vaadin Ltd
 */
@Connect(org.vaadin.treegrid.HierarchyRenderer.class)
public class HierarchyRendererConnector extends AbstractRendererConnector<Object> {

	@Override
	protected Renderer<Object> createRenderer() {
		HierarchyRendererExtension renderer = new HierarchyRendererExtension();
		ServerConnector parent = getParent();
		if (parent instanceof TreeGridConnector) {
			TreeGridConnector parentConnector = (TreeGridConnector) parent;
			parentConnector.addClickHandler(renderer);
		}
		return renderer;
	}

	@Override
	public String decode(JsonValue value) {
		return value.asString();
	}
}
