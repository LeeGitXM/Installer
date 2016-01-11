/**
 *   (c) 2016  ILS Automation. All rights reserved.
 *  
 */
package com.ils.mb.common.notification;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.ils.mb.common.MasterBuilderProperties;
import com.inductiveautomation.ignition.client.gateway_interface.PushNotificationListener;
import com.inductiveautomation.ignition.client.model.ClientContext;
import com.inductiveautomation.ignition.common.gateway.messages.PushNotification;
import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;


/**
 *  The handler is used to convert notifications from running tests into UI actions.
 *  
 *  The moduleId used within the calls refers to the module that has the handler for the 
 *  method that is invoked.
 */
public class NotificationHandler implements PushNotificationListener {
	private static String TAG = "NotificationHandler";
	private final LoggerEx log;
	private final ClientContext context;
	private final Set<JTextField> receivers;
	/**
	 * The handler
	 */
	public NotificationHandler(ClientContext ctx) {
		this.log = LogUtil.getLogger(getClass().getPackage().getName());
		this.context = ctx;
		this.receivers = new HashSet<>();
	}

	/**
	 * Receive notification from the gateway. The payload contain a key-value pair which the
	 * listener (the main dialog) uses. We check the moduleId to make sure that we don't trigger
	 * on updates from other modules.
	 * 
	 * A few special messages are filtered out and acted upon directly. Generally, these are in 
	 * response to UI commands from the test script.
	 * 
	 * In general, the notification contains a type, blockUUID and name.
	 *   1) The module ID
	 *   2) The key 
	 *   3) The message - usually a string
	 */
	@Override
	public void receiveNotification(PushNotification notice) {
		String moduleId = notice.getModuleId();
		if( moduleId.equals(MasterBuilderProperties.MODULE_ID)) {
			final String key = notice.getMessageType();
			final Serializable payload = (Serializable)notice.getMessage();
			if( payload==null ) return;
			log.infof("%s.receiveNotification: key=%s,value=%s",TAG,key,payload.toString());
			try {
				// When we update the field, do it on the UI thread
				// Payload is the text of the message.
				if( key.equalsIgnoreCase(MasterBuilderProperties.FAIL_NOTIFICATION) ) {
					Updater updater = new Updater(receivers,payload.toString(),false);
					SwingUtilities.invokeLater(updater);
				}	
				else if( key.equalsIgnoreCase(MasterBuilderProperties.SUCCESS_NOTIFICATION) ) {
					Updater updater = new Updater(receivers,payload.toString(),true);
					SwingUtilities.invokeLater(updater);
				}
				else {
					log.infof("%s.receiveNotification: Unrecognized notification %s,ignored",TAG,key);
				}
			}
			catch(Exception ex) {
				log.warn(TAG+".receiveNotification: Exception "+ex.getLocalizedMessage(),ex);
			}
		} 
	}
	public synchronized void clear() {
		receivers.clear();
	}
	public synchronized void registerStatusReceiver(JTextField receiver) {
		receivers.add(receiver);
	}
	
	private class Updater implements Runnable {
		private final Set<JTextField> fields;
		private final String status;
		private boolean pass;
		
		private Updater(Set<JTextField> components,String text,boolean success) {
			this.fields = components;
			this.status = text;
			this.pass = success;
		}
		
		public void run() {
			for(JTextField field:fields) {
				field.setText(status);
				if(pass) {
					field.setBackground(new Color(250,255,200));
				}
				else {
					field.setBackground(new Color(255,230,230));
				}
			}
		}
	}
}
