/**
 * 
 */
package utility;

import ui.MainClientWindow;

/**
 * @author Salim AHMED
 *
 */
public interface NotificationSubject {
	public void notifyObserver();
	public void registerObserver(MainClientWindow p);
	public void unregisterObserver(MainClientWindow p);

}
