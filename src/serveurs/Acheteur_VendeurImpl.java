/**
 * 
 */
package serveurs;


import Enchere.Acheteur_VendeurPOA;

/**
 * @author Salim AHMED
 *
 */
public class Acheteur_VendeurImpl extends Acheteur_VendeurPOA{

	private String notification = new String();
	private String previous = "";
	
	@Override
	public void recevoirNotification(String message) {
		System.out.println("IN SERVER NOTIFICATION:  "+ message);
		
		notification("NOTIFICATION: "+message+"\n");
		
	//	synchronized(MainClientWindow.lock) {
	//		MainClientWindow.refresh = true;
	//		MainClientWindow.lock.notify();
	//	}
		
		
	}

	@Override
	public String notification() {
		if (!notification.equals(previous)) {
			previous = notification;
			return notification;
		}
		return "";
	}

	@Override
	public void notification(String newNotification) {
		this.notification = newNotification;		
	}

}
