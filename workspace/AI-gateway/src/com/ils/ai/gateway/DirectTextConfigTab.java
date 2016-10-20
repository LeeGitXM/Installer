/**
 * Copyright 2016. ILS Automation. All rights reserved.
 */
package com.ils.ai.gateway;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.google.common.base.Preconditions;
import com.inductiveautomation.ignition.gateway.web.components.ConfigPanel;
import com.inductiveautomation.ignition.gateway.web.models.ConfigCategory;
import com.inductiveautomation.ignition.gateway.web.models.DefaultConfigTab;

/**
 * A configuration tab where the title string is directly settable. The
 * title does not rely on a resource bundle. We maintain both the menu location
 * and title privately.
 * 
 * TabName is menuLocation.getRight()
 * CategoryName is menuLoction.getLeft()
 * Pair.of(category.getName().toLowerCase(), name.toLowerCase())
 */
public class DirectTextConfigTab extends DefaultConfigTab  {
	private Pair<String,String> dtMenuLocation = null;
	private IModel<String> dtTitle = null;

	public DirectTextConfigTab(ConfigCategory category,String name,String titleKey,Class<? extends ConfigPanel>panelClass ){
        super(category,name,titleKey,panelClass);
        dtMenuLocation = Pair.of(category.getName(),name);
        dtTitle = new Model<String>(titleKey);
    }
	public DirectTextConfigTab(Pair<String,String>menuLocation,String titleKey,Class<? extends ConfigPanel>panelClass ){
        super(menuLocation,titleKey,panelClass);
        dtMenuLocation = menuLocation;
        dtTitle = new Model<String>(titleKey);
    }
	
	@Override
	public Pair<String,String> getMenuLocation() { return dtMenuLocation; }
	@Override
	public IModel<String> getTitle() { return dtTitle; } 
    
	/**
	 * When we set the title, also update the menu location
	 */
	public void setTitle(String title) {
		dtTitle = new Model<String>(title);
		dtMenuLocation = Pair.of(dtMenuLocation.getLeft(),dtTitle.getObject());
	}
	public static DirectTextBuilder tabBuilder() {
		return new DirectTextBuilder();
	}

	public static class DirectTextBuilder  {
		private String category;
		private String name;
		private String i18nKey;
		private Class<? extends ConfigPanel> panelClass;
		private List<String> terms;

		public DirectTextBuilder category(ConfigCategory category) {
			this.category = category.getName();
			return this;
		}

		public DirectTextBuilder name(String name) {
			this.name = name;
			return this;
		}

		public DirectTextBuilder i18n(String titleKey) {
			this.i18nKey = titleKey;
			return this;
		}

		public DirectTextBuilder page(Class<? extends ConfigPanel> panelClass) {
			this.panelClass = panelClass;
			return this;
		}

		public DirectTextBuilder terms(String... terms) {
			this.terms = Arrays.asList(terms);
			return this;
		}

		public DirectTextConfigTab build() {
			Preconditions.checkNotNull(this.category, "Missing category");
			Preconditions.checkNotNull(this.name, "Missing name");
			Preconditions.checkNotNull(this.i18nKey, "Missing i18n title key");
			Preconditions.checkNotNull(this.panelClass, "Missing panel class");
			DirectTextConfigTab tab = new DirectTextConfigTab(Pair.of(this.category, this.name), this.i18nKey, this.panelClass);
			if (this.terms != null) {
				tab.setSearchTerms(this.terms);
			}
			return tab;
		}
	}
}
