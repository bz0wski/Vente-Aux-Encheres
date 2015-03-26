/**
 * 
 */
package clients;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

import Enchere.Produit;
import Enchere.SystemeEnchere;
import Enchere.Utilisateur;

/**
 * @author Salim AHMED
 *
 */
public class Acheteur_Vendeur_Client {

	/**
	 * @param args
	 */
	public static SystemeEnchere systemeEnchere;

	public static void main(String[] args) {
		try {
			// Intialisation de l'ORB
			org.omg.CORBA.ORB orbClient = org.omg.CORBA.ORB.init(args, null);	

			// Utilisation service de nommage
			//********************************
			// Saisie du nom de l'objet (si utilisation du service de nommage)
			System.out.println("Quel objet SystemeEnchere - Corba voulez-vous contacter ?");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			String idObj = in.readLine();

			// Recuperation du naming service
			NamingContext nameRoot = NamingContextHelper.narrow(orbClient
					.resolve_initial_references("NameService"));

			// Construction du nom à rechercher
			// Chemin : ConvertisseurDevises/idObj
			NameComponent[] nameToFind = new NameComponent[2];
			nameToFind[0] = new NameComponent("SystemeEnchere",
					"Contexte");
			nameToFind[1] = new NameComponent(idObj, "Objet SystemeEnchere");

			// Recherche aupres du naming service
			org.omg.CORBA.Object distantSystemeEnchere = nameRoot.resolve(nameToFind);
			System.out.println("Objet '" + idObj
					+ "' trouvé auprès du service de noms. IOR de l'objet :");
			System.out.println(orbClient.object_to_string(distantSystemeEnchere));

			// Casting de l'objet CORBA vers le type Enchere.SystemeEnchere
			systemeEnchere = Enchere.SystemeEnchereHelper.narrow(distantSystemeEnchere);

		
			/*********************************************TESTING QUERIES*******************************************************************************************/			
			systemeEnchere.creerCompte("salim", "ahmed", "tripode");
			systemeEnchere.creerCompte("naweed", "ahmed", "koftown");
			Utilisateur[] users = systemeEnchere.tousLesUtilisateurs();
			for (int i = 0; i < users.length; i++) {
				System.out.println(users[i].nom);
			}
			Utilisateur userUtilisateur = new Utilisateur("yttft","jkhhhjk","ljjn","jhjkhj");
			
			
			systemeEnchere.publierProduit(users[0], "coffee", "alimentation", "fort et noir", 5.6f, "mercredi 19 mars 2015 01:18",systemeEnchere);
			systemeEnchere.publierProduit(userUtilisateur, "milk", "alimentation", "fort et blanc", 2.6f, "mercredi 19 mars 2015 01:19",systemeEnchere);

			/*Produit [] produits = systemeEnchere.tousLesProduits();
			for (int i = 0; i < produits.length; i++) {
				System.out.println(produits[i].nom);
			}
			
			
			if (IORServant != null) {
				//System.out.println("IORSERVANT not null:\n"+IORServant);
				org.omg.CORBA.Object acheteurbObject = orbClient
						.string_to_object(IORServant);
				Acheteur_Vendeur acheteur_Vendeur = Acheteur_VendeurHelper.narrow(acheteurbObject);
				systemeEnchere.demanderNotificationEnchereEnCours(users[0], produits[0], acheteur_Vendeur);
			}
			

			systemeEnchere.proposerPrix(7.3f, users[0], produits[0]);
			systemeEnchere.proposerPrix(7.8, users[0], produits[0]);
			systemeEnchere.proposerPrix(9.3f, users[0], produits[0]);
			
			if (IORServant != null) {
				//System.out.println("IORSERVANT not null:\n"+IORServant);
				org.omg.CORBA.Object acheteurbObject = orbClient
						.string_to_object(IORServant);
				Acheteur_Vendeur acheteur_Vendeur = Acheteur_VendeurHelper.narrow(acheteurbObject);
				systemeEnchere.demanderNotificationEnchereEnCours(users[0], produits[0], acheteur_Vendeur);
			}
			Utilisateur user = systemeEnchere.seConnecter("salim", "ahmed");
			if(user != null)
				System.out.println("Bonjour "+user.nom);
			*//****************************************************************************************************************************************//*			
			// Appel de l'interface graphique
			SystemeEnchereWindow clientWindow = new SystemeEnchereWindow();
			clientWindow.open();*/
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}



	}
}