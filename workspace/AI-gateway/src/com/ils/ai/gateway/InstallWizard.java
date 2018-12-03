package com.ils.ai.gateway;

import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;

import com.ils.ai.gateway.model.InstallerData;
import com.ils.ai.gateway.model.InstallerDataHandler;
import com.ils.ai.gateway.model.PersistenceHandler;
import com.ils.ai.gateway.model.PropertyItem;
import com.ils.ai.gateway.panel.BasicInstallerPanel;
import com.ils.ai.gateway.panel.FinalPage;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;
import com.inductiveautomation.ignition.gateway.web.components.wizard.GatewayWizard;
import com.inductiveautomation.ignition.gateway.web.components.wizard.GatewayWizardModel;
import com.inductiveautomation.ignition.gateway.web.pages.IConfigPage;

/**
 * Created by travis.cox on 2/17/2016.
 */
public class InstallWizard extends GatewayWizard {
	private static final long serialVersionUID = 7625405250885635937L;

	public InstallWizard(String id, IConfigPage configPage, Model<InstallerData> dataModel){
		super(id, configPage, dataModel);
		
        BasicInstallerPanel step = InstallerDataHandler.getInstance().getNextPanel(0,null,dataModel);

		GatewayWizardModel wizardModel = new GatewayWizardModel(step);
		// Set the page timeout to 5 minutes
		this.getApplication().getRequestCycleSettings().setTimeout(Duration.minutes(5));
		this.init(wizardModel);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onFinish(IModel iModel, IConfigPage iConfigPage) {
		Model<InstallerData> dataModel = (Model<InstallerData>)iModel;
		recordCompletion(dataModel.getObject());
		iConfigPage.setConfigPanel(new FinalPage(dataModel));
		new Thread(new PageRunner()).start();
	}
	
	private void recordCompletion(InstallerData data) {
		PersistenceHandler dbHandler = PersistenceHandler.getInstance();
		InstallerDataHandler dataHandler = InstallerDataHandler.getInstance();
		List<PropertyItem> properties = dataHandler.getProperties(data);

		// For starters get the product name
		String productName = "";
		for(PropertyItem prop:properties) {
			if(prop.getName().equalsIgnoreCase("product")) {
				productName  = prop.getValue();
				break;
			};
		}
		if( !productName.isEmpty() ) {
			for(PropertyItem prop:properties) {
				if(prop.getName().equalsIgnoreCase("product")) continue;
				dbHandler.setProductProperty(productName, prop.getName(), prop.getValue());
			}
		}
		else {
			error("Product name is missing from configured properties. No properties update possible.");
		}
	}
	
	/**
	 * Delay for long enough to display the page, then delete the module.
	 * @author chuckc
	 *
	 */
	public class PageRunner implements Runnable {
		public void run() {
			try {
				Thread.sleep(10000);    // 10 secs
			}
			catch(InterruptedException ignore) {}
			//System.out.println("InstallWizard: uninstalling installer module");

			try {
				GatewayContext context = ApplicationInstallerGatewayHook.getInstance().getContext();
				context.getModuleManager().uninstallModule(InstallerConstants.MODULE_ID);
			} 
			catch (Exception ignored) {}
		}
	}
}
