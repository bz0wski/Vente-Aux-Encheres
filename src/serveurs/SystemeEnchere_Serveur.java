package serveurs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import clients.ClientWindow;
import Enchere.Archivage;

public class SystemeEnchere_Serveur {

	public static Archivage archivage;
	
	public static void main(String[] args) {
		/********************************************************************************************************************/			
		/******************************** LANCER LA PARTIE CLIENT D'ABORD, CE CLIENT VA CONSULTER ******************************/			
		/******************************** LE SERVEUR D'ARCHIVAGE POUR INITIALISER LES DONNEES. ********************************/			
		/********************************************************************************************************************/			
		enchereClientToArchive(args);
		/********************************************************************************************************************/
		/******************************** LANCEMENT DE LA PARTIE SERVEUR ********************************************************/			
		/********************************************************************************************************************/			
		
		// Intialisation de l'ORB
		// ************************
		org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, null);

		// Gestion du POA
		// ****************
		// Recuperation du Root POA
		try {
			POA rootPOA = POAHelper.narrow(orb
					.resolve_initial_references("RootPOA"));
			// Creation des servants
			SystemeEnchereImpl systemeEnchere = new SystemeEnchereImpl();

			// Activer le servant au sein du POA
			rootPOA.activate_object(systemeEnchere);
		
			// Activer le POA manager
			rootPOA.the_POAManager().activate();
			
			// Affichage des référecences d'objets CORBA
			String IORServant = null;
			IORServant = orb.object_to_string(rootPOA.servant_to_reference(systemeEnchere));
		
			// Enregistrement dans le service de nommage
			// *******************************************
			// Recuperation du naming service
			NamingContext nameRoot = org.omg.CosNaming.NamingContextHelper
					.narrow(orb.resolve_initial_references("NameService"));
			// Creation d'un contexte "Systeme d'Enchere"
			NameComponent[] contexteSystemeEnchere = new NameComponent[1];
			contexteSystemeEnchere[0] = new NameComponent("SystemeEnchere", "Contexte");
			
			NameComponent[] objetSystemeEnchere = new NameComponent[2];
			objetSystemeEnchere[0] = contexteSystemeEnchere[0];
			
			System.out.println("Sous quel nom voulez-vous enregistrer l'objet Corba systeme Enchere ?");
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String nomObj01 = in.readLine();


			objetSystemeEnchere[1] = new NameComponent(nomObj01,"Objet SystemeEnchere"); // id, kind

			System.out.println("Enregistrement NS...");
			// Enregistrement de l'ensemble aupres du service de noms
			nameRoot.rebind_context(contexteSystemeEnchere, nameRoot.new_context());
			nameRoot.rebind(objetSystemeEnchere, rootPOA.servant_to_reference(systemeEnchere));

			System.out.println("==> Nom '" + nomObj01 + "' est enregistre dans le service de noms.");
			IORServant = orb.object_to_string(rootPOA.servant_to_reference(systemeEnchere));
			System.out.println("L'objet possede la reference suivante :");
			System.out.println(IORServant);

			// Lancement de l'ORB (et mise en attente de requetes)
			// ***************************************************
			orb.run();
		} catch (InvalidName | ServantAlreadyActive | WrongPolicy | AdapterInactive | IOException | NotFound | CannotProceed | org.omg.CosNaming.NamingContextPackage.InvalidName | ServantNotActive e) {
			e.getMessage();		
			e.printStackTrace();
		}


	}
	
	private static void enchereClientToArchive(String [] args) {
		
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
			NamingContext nameRoot = NamingContextHelper.narrow(orb
					.resolve_initial_references("NameService"));

			// Construction du nom à rechercher
			// Chemin : ConvertisseurDevises/idObj
			NameComponent[] nameToFind = new NameComponent[2];
			nameToFind[0] = new NameComponent("Archivage",
					"Contexte");
			nameToFind[1] = new NameComponent(idObj, "Objet Archivage");

			// Recherche aupres du naming service
			org.omg.CORBA.Object distantArchivage = nameRoot.resolve(nameToFind);
			System.out.println("Objet '" + idObj
					+ "' trouvé auprès du service de noms. IOR de l'objet :");
			System.out.println(orb.object_to_string(distantArchivage));

			// Casting de l'objet CORBA vers le type Convertisseur.Euro
			archivage = Enchere.ArchivageHelper.narrow(distantArchivage);

			//Recuperer les données stockées dans la bd
			
			// Appel de l'interface graphique
			ClientWindow clientWindow = new ClientWindow();
			clientWindow.open();
			System.out.println("Systeme Enchere execute en tant que client..");
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}

	}

}
