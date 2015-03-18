/**
 * 
 */
package utility;


/**
 * @author Salim AHMED
 *
 */
public interface EnchereSubject {
	public void notifyObserver();
	public void registerObserver(EnchereObserver o);
	public void unregisterObserver(EnchereObserver o);
}
