/**
 * 
 */
package serveurs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Display;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.IOP.IOR;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import Enchere.Acheteur_Vendeur;
import Enchere.Acheteur_VendeurHelper;
import Enchere.ClientUpdate;
import Enchere.Produit;
import Enchere.SystemeEnchere;
import Enchere.Utilisateur;
import ui.MainClientWindow;
import ui.SystemeEnchereWindow;
import utility.EnchereObserver;
import utility.NotificationHandler;

/**
 * @author Salim AHMED
 *
 */
public class Acheteur_Vendeur_Serveur {

	/**
	 * @param args
	 */

	public static SystemeEnchere systemeEnchere;
	static String IORServant = null;
	private static Object lock = new Object();
	private static boolean updateUI = false;
	private static ExecutorService executor = Executors.newSingleThreadExecutor();
	
	private static ExecutorService executor1 = Executors.newSingleThreadExecutor();

	public static void main(String[] args) {
	
		/********************************************************************************************************************/
		/******************************** LANCEMENT DE LA PARTIE SERVEUR ********************************************************/			
		/********************************************************************************************************************/	
		
		server(args);
		
		/********************************************************************************************************************/			
		/******************************** LANCER LA PARTIE CLIENT, CE CLIENT VA CONSULTER ******************************/			
		/******************************** LE SERVEUR SYSTEME ENCHERE POUR LE PROCESSUS ENCHERE. ********************************/			
		/********************************************************************************************************************/			
	
		client(args);

		/****************************************************************************************************************************************/			
		/****************************************************************************************************************************************/			
		/****************************************************************************************************************************************/			
		/****************************************************************************************************************************************/			


	}
	
	private static void server(String [] args) {
		// Intialisation de l'ORB
		// ************************
		org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, null);

	
		// Creation de servant
		Acheteur_VendeurImpl utilisateur = new Acheteur_VendeurImpl();
		try {
			// Gestion du POA
			// ****************
			// Recuperation du Root POA
			POA rootPOA = POAHelper.narrow(orb
					.resolve_initial_references("RootPOA"));
			

			// Activer le servant au sein du POA
			rootPOA.activate_object(utilisateur);

			// Activer le POA manager
			rootPOA.the_POAManager().activate();

			// Affichage des référecences d'objets CORBA
			
			IORServant = orb.object_to_string(rootPOA.servant_to_reference(utilisateur));

			// Enregistrement dans le service de nommage
			// *******************************************
			// Recuperation du naming service
			NamingContext nameRoot = org.omg.CosNaming.NamingContextHelper
					.narrow(orb.resolve_initial_references("NameService"));
			// Creation d'un contexte "Systeme d'Enchere"
			NameComponent[] contexteUtilisateur = new NameComponent[1];
			contexteUtilisateur[0] = new NameComponent("Utilisateur", "Contexte");

			NameComponent[] objetUtilisateur = new NameComponent[2];
			objetUtilisateur[0] = contexteUtilisateur[0];

			System.out.println("Sous quel nom voulez-vous enregistrer l'objet Corba - Acheteur_Vendeur?");
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String nomObj01 = in.readLine();


			objetUtilisateur[1] = new NameComponent(nomObj01,"Objet Utilisateur"); // id, kind

			// Enregistrement de l'ensemble aupres du service de noms
			nameRoot.rebind_context(contexteUtilisateur, nameRoot.new_context());
			nameRoot.rebind(objetUtilisateur, rootPOA.servant_to_reference(utilisateur));

			System.out.println("==> Nom '" + nomObj01 + "' est enregistre dans le service de noms.");
			IORServant = orb.object_to_string(rootPOA.servant_to_reference(utilisateur));
			System.out.println("L'objet possede la reference suivante :");
			System.out.println(IORServant);

			// Lancement de l'ORB (et mise en attente de requetes)
			// ***************************************************
			//lancment du client associé
			
			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					
					orb.run();
				}
			};
			try {
				executor.execute(runnable);
				//**Important to shut the executor once it's done *//*
			} catch (Exception e) {
				e.getMessage();
			}finally{
				executor.shutdownNow();
			}
			
			
			
		} catch (InvalidName | ServantAlreadyActive | WrongPolicy | AdapterInactive | IOException | NotFound | CannotProceed | org.omg.CosNaming.NamingContextPackage.InvalidName | ServantNotActive e) {
			e.getMessage();		
			e.printStackTrace();
		}
	}
	
	
	private static void client(String [] args) {
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
		/*	systemeEnchere.creerCompte("salim", "ahmed", "tripode");
			systemeEnchere.creerCompte("naweed", "ahmed", "koftown");
			Utilisateur[] users = systemeEnchere.tousLesUtilisateurs();
			for (int i = 0; i < users.length; i++) {
				System.out.println(users[i].nom);
			}
			Utilisateur userUtilisateur = new Utilisateur("yttft","jkhhhjk","ljjn","jhjkhj");
			
			if (IORServant != null) {
				
			}
			systemeEnchere.publierProduit(users[0], "coffee", "alimentation", "fort et noir", 5.6f, "dimanche 29 mars 2015 05:15",systemeEnchere);
			systemeEnchere.publierProduit(userUtilisateur, "milk", "alimentation", "fort et blanc", 2.6f, "dimanche 29 mars 2015 05:10",systemeEnchere);

			Produit [] produits = systemeEnchere.tousLesProduits();
			for (int i = 0; i < produits.length; i++) {
				System.out.println(produits[i].nom);
			}
			
			*/
			
			
			final Display display = Display.getDefault();
			Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
				@Override
				public void run() {
					try {
						if (IORServant != null) {
							org.omg.CORBA.Object acheteurbObject = orbClient
									.string_to_object(IORServant);
							Acheteur_Vendeur acheteur_Vendeur = Acheteur_VendeurHelper.narrow(acheteurbObject);
						
							MainClientWindow window = new MainClientWindow(systemeEnchere, acheteur_Vendeur);
							ClientUpdate client = new ClientUpdate(acheteur_Vendeur, false);
							systemeEnchere.addClient(client);
							//Add this instance of MainClientWindow to the list of all clients opened atm.
							//NotificationHandler.addElement(window);
						
							window.open();
						}
							
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						executor.shutdown();
					}


				}
			});
			

	/*		systemeEnchere.proposerPrix(17.3f, users[0], produits[0]);
			systemeEnchere.proposerPrix(25.8, users[0], produits[0]);
			systemeEnchere.proposerPrix(35.3f, users[0], produits[0]);
			systemeEnchere.proposerPrix(56.3f, users[0], produits[0]);
			
			if (IORServant != null) {
				//System.out.println("IORSERVANT not null:\n"+IORServant);
				org.omg.CORBA.Object acheteurbObject = orbClient
						.string_to_object(IORServant);
				Acheteur_Vendeur acheteur_Vendeur = Acheteur_VendeurHelper.narrow(acheteurbObject);
				systemeEnchere.demanderNotificationEnchereEnCours(users[0], produits[0], acheteur_Vendeur);
			}
		*/	
			
			//Utilisateur user = systemeEnchere.seConnecter("salim", "ahmed");
	/*		if(user != null)
				System.out.println("Bonjour "+user.nom);
			List<Produit> prods = Arrays.asList(systemeEnchere.rechercherProduit("milk"));
			List<Produit> prods1 = Arrays.asList(systemeEnchere.rechercherProduit("alimentation"));
			prods.stream().forEach(p->System.out.println("1e recherche: "+p.nom));
			prods1.stream().forEach(p->System.out.println("2e recherche: "+p.nom));*/
			/****************************************************************************************************************************************//*			
			*
			*/
			// Appel de l'interface graphique
			
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}



	}

}
