package serveurs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

public class SystemeEnchere_Serveur {

	public static void main(String[] args) {
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
			System.out.println("running..");
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

}
