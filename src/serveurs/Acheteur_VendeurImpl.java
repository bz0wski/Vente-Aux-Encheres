/**
 * 
 */
package serveurs;

import utility.EnchereObserver;
import Enchere.Acheteur_VendeurPOA;
import Enchere.Produit;

/**
 * @author Salim AHMED
 *
 */
public class Acheteur_VendeurImpl extends Acheteur_VendeurPOA{


	@Override
	public Produit[] listeproduits() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void listeproduits(Produit[] newListeproduits) {
		// TODO Auto-generated method stub

	}

	@Override
	public void recevoirNotification(String message) {
		System.out.println("NOTIFICATION:  "+ message);
	}

}
