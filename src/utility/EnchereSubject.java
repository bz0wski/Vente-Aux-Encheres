/**
 * 
 */
package utility;

import Enchere.Acheteur_Vendeur;
import Enchere.Produit;


/**
 * @author Salim AHMED
 *
 */
public interface EnchereSubject {
	public void notifyObserver(Produit p);
	public void registerObserver(Produit p, Acheteur_Vendeur o);
	public void unregisterObserver(Produit p, Acheteur_Vendeur o);
}
