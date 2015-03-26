/**
 * 
 */
package utility;

import ui.SystemeEnchereWindow;

/**
 * @author Salim AHMED
 *
 */
public interface UpdateEnchereServerUI {
		public void notifyObserver(SystemeEnchereWindow cw);
		public void registerObserver(SystemeEnchereWindow cw);
		public void unregisterObserver(SystemeEnchereWindow cw);

}
