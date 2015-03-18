/**
 * 
 */
package utility;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimerTask;

import clients.Acheteur_Vendeur_Client;
import clients.SystemeEnchereClient;
import serveurs.Acheteur_VendeurImpl;
import Enchere.Produit;
import Enchere.Utilisateur;
import Enchere.Vente;

/**
 * Cette classe, comme son nom l'indique va mettre fin a une vente aux 
 * encheres d'un produit. Elle permettra eventuellement d'archiver la vente
 * 
 * @author Salim AHMED
 *
 */
public class TerminerVente extends TimerTask{

	private Vente _vente;
	//boolean acheteurtrouve = false;
	/**
	 * Constructeur permettant d'initialiser les valeurs pour une vente achevée
	 * avec un acheteur trouvé;
	 */
	public TerminerVente(Vente vente) {
		this._vente = vente;		
	}

	private void terminerVente() {
		if (Acheteur_Vendeur_Client.systemeEnchere != null) {
			DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT, Locale.FRANCE);
			Date now = new Date();
			_vente.finVente = df.format(now);
		}
		Utilisateur acheteur = Acheteur_Vendeur_Client.systemeEnchere.getAcheteurProduit(_vente.produitVendu);
		if(acheteur == null)
			System.err.println("Aucun acheteur trouvé, vente se termine.");
		else 
			_vente.acheteur = acheteur;
		
		//if (Acheteur_Vendeur_Client.systemeEnchere != null)
			//if (Acheteur_Vendeur_Client.systemeEnchere
	}
	@Override
	public void run() {
		//terminerVente();
		if (SystemeEnchereClient.archivage != null) {
			//eventuellement le code pour archiver la vente
			SystemeEnchereClient.archivage.archiverVenteAchevee(_vente);
		}


	}
}
