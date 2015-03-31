package serveurs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Display;
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

import ui.SystemeEnchereWindow;
import utility.NotificationHandler;
import utility.TerminerVente;
import Enchere.Acheteur_Vendeur;
import Enchere.Acheteur_VendeurHelper;
import Enchere.Archivage;
import Enchere.Produit;
import Enchere.SystemeEnchere;
import Enchere.SystemeEnchereHelper;
import Enchere.Utilisateur;
import Enchere.Vente;

public class SystemeEnchere_Serveur {

	public static Archivage archivage;
	//private static boolean advance = false;
	private static String IORServant = null;
	public static AtomicInteger clients = new AtomicInteger();
	CountDownLatch latch = new CountDownLatch(1);

	//private static final Object lock = new Object();
	private static ExecutorService executor = Executors.newSingleThreadExecutor();
	public static void main(String[] args) {

		/********************************************************************************************************************/			
		/******************************** LANCER LA PARTIE CLIENT D'ABORD, CE CLIENT VA CONSULTER ******************************/			
		/******************************** LE SERVEUR D'ARCHIVAGE POUR INITIALISER LES DONNEES. ********************************/			
		/********************************************************************************************************************/			

		enchereClientToArchive(args);

	}



/*	public static void advance(boolean what) {
		synchronized (lock) {
			advance = what;
			lock.notifyAll();

		}
	}*/

	private static void enchereServer(String [] args) {
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
			// RUN ORB on another thread

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
				//**Important to shut the executor once it's done *//*
				executor.shutdown();

			}finally{
				executor.shutdownNow();
			}

		} catch (InvalidName | ServantAlreadyActive | WrongPolicy | AdapterInactive | IOException | NotFound | CannotProceed | org.omg.CosNaming.NamingContextPackage.InvalidName | ServantNotActive e) {
			e.getMessage();		
			e.printStackTrace();
		}
	}


/*	public static void shutdown() {
		 executor.shutdown();
	}
	*/
	private static void enchereClientToArchive(String [] args) {

		try {
			// Intialisation de l'ORB
			org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, null);	

			// Utilisation service de nommage
			//********************************
			// Saisie du nom de l'objet (si utilisation du service de nommage)
			System.out.println("Quel objet Serveur Archivage voulez-vous contacter ?");
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
			System.out.println("\nLancement du serveur Systeme Enchere");
			/********************************************************************************************************************/
			/******************************** LANCEMENT DE LA PARTIE SERVEUR ********************************************************/			
			/********************************************************************************************************************/	
			enchereServer(args);

			if (IORServant != null) {
				//System.out.println("IORSERVANT not null:\n"+IORServant);
				org.omg.CORBA.Object systemeEnchereObj = orb.string_to_object(IORServant);
				SystemeEnchere systemeEnchere = SystemeEnchereHelper.narrow(systemeEnchereObj);

				//Passer une reference de systemeEnchereImpl pour les appels aux structs de données à stocker


				archivage = Enchere.ArchivageHelper.narrow(distantArchivage);
				


				//Recuperer les données stockées dans la bd, initialiser le systeme
				//quand tout s'est bien passé, lancer le serveur
				//Lancement du serveur


				Produit[] produits = archivage.chargerProduits();
				Utilisateur[] utilisateurs = archivage.chargerUtilisateurs();
				Vente[] ventes = archivage.chargerVentesEncours();

				systemeEnchere.tousLesProduits(produits);
				systemeEnchere.tousLesUtilisateurs(utilisateurs);
				systemeEnchere.tousLesVentesEncours(ventes);

				System.out.println("Les Produits");
				for (int i = 0; i < produits.length; i++) {
					System.out.println(produits[i].nom);
				}

				System.out.println("Les Utilisateurs");
				for (int i = 0; i < utilisateurs.length; i++) {
					System.out.println(utilisateurs[i].nom);
				}
				
				//Continuer les Ventes qui ne sont pas encore terminé, enlève ceux qui ont
				//expiré depuis le dernier lancement du système.
				DateFormat format = DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.SHORT,  Locale.FRANCE);
				System.out.println("Les Ventes en cours");
				for (int i = 0; i < ventes.length; i++) {
					
					Date now = new Date();
					Date dateFin = format.parse(ventes[i].dateAchevee);
					if (dateFin instanceof Date) {
						if (now.before(dateFin)) {
							systemeEnchere.enleverVente(ventes[i].idVente);
						} else {
							String nomProduit = ventes[i].produitVendu.nom;

							Timer timer = new Timer(nomProduit + "Timer", true);
							timer.schedule(new TerminerVente(ventes[i],
									systemeEnchere), dateFin);
						}
					}
					
				}

				final Display display = Display.getDefault();
				Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
					@Override
					public void run() {
						try {
							// Appel de l'interface graphique
							SystemeEnchereWindow systemeEnchereWindow = new SystemeEnchereWindow(archivage,systemeEnchere);
							systemeEnchereWindow.open();
						} catch (Exception e) {
							e.printStackTrace();
						}


					}
				});

			}

			
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}finally{
			executor.shutdown();
		}

	}

}
