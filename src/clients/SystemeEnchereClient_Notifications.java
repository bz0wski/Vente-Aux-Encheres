/**
 * 
 */
package clients;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

import Enchere.Acheteur_Vendeur;

/**
 * @author Salim AHMED
 *
 */
public class SystemeEnchereClient_Notifications {

	/**
	 * @param args
	 */
	public static Acheteur_Vendeur client = null;
	
	public static void main(String[] args) {
		try {
			// Intialisation de l'ORB
			org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, null);	

			// Utilisation service de nommage
			//********************************
			// Saisie du nom de l'objet (si utilisation du service de nommage)
			System.out.println("Quel objet Corba voulez-vous contacter ?");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			String idObj = in.readLine();

			// Recuperation du naming service
			NamingContext nameRoot = NamingContextHelper.narrow(orb.resolve_initial_references("NameService"));

			// Construction du nom à rechercher
			// Chemin : Acheteur_Vendeur_Client/idObj
			NameComponent[] nameToFind = new NameComponent[2];
			nameToFind[0] = new NameComponent("Utilisateur","Contexte");
			nameToFind[1] = new NameComponent(idObj, "Objet Utilisateur");

			// Recherche aupres du naming service
			org.omg.CORBA.Object distantUtilisateur = nameRoot.resolve(nameToFind);
			System.out.println("Objet '" + idObj
					+ "' trouvé auprès du service de noms. IOR de l'objet :");
			System.out.println(orb.object_to_string(distantUtilisateur));

			// Casting de l'objet CORBA vers le type Convertisseur.Euro
			client = Enchere.Acheteur_VendeurHelper.narrow(distantUtilisateur);

			// Appel de l'interface graphique
			//ClientWindow clientWindow = new ClientWindow();
			//clientWindow.open();
			System.out.println("Systeme Enchere execute en tant que client..");
/**************************************************TESTING QUERIES***************************************************************************/
			client.recevoirNotification("testing notification.");
			
/****************************************************************************************************************************************/
			
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}

	}

}
