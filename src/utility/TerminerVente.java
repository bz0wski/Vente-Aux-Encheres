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
import serveurs.Acheteur_Vendeur_Serveur;
import Enchere.Acheteur_Vendeur;
import Enchere.Produit;
import Enchere.SystemeEnchere;
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
	private SystemeEnchere _systemeEnchere;
	//boolean acheteurtrouve = false;
	/**
	 * Constructeur permettant d'initialiser les valeurs pour une vente achevée
	 * avec un acheteur trouvé;
	 */
	public TerminerVente(Vente vente,SystemeEnchere systemeEnchere) {
		this._vente = vente;
		this._systemeEnchere = systemeEnchere;
	}

	private void terminerVente() {
		System.out.println("vente ID:"+_vente.idVente);
	
		if (_systemeEnchere != null) {
			
			
			DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT, Locale.FRANCE);
			Date now = new Date();
			_vente.finVente = df.format(now);
			Utilisateur acheteur = _systemeEnchere.getAcheteurProduit(_vente.produitVendu);
			
			if(acheteur.id.equals(""))
				System.err.println("Aucun acheteur trouvé, vente se termine.");
			else 
				_vente.acheteur = acheteur;
			
			//Remove this Vente from the venteEnCours List
			 //_systemeEnchere.supprimerVente(_vente);
			_systemeEnchere.enleverVente(_vente.idVente);
		}else {
			System.out.println("it was null");
		}
		
		
	}
	
	@Override
	public void run() {
		System.out.println("running terminer vente...");
		terminerVente();
		if (SystemeEnchereClient.archivage != null) {
			//eventuellement le code pour archiver la vente
			SystemeEnchereClient.archivage.archiverVenteAchevee(_vente);
		}else {
			System.out.println("Archivage is null.");
		}
		


	}
}
