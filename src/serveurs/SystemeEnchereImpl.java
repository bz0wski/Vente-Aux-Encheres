package serveurs;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.UUID;
import java.util.stream.Collectors;

import utility.EnchereSubject;
import utility.TerminerVente;
import Enchere.Acheteur_Vendeur;
import Enchere.ClientUpdate;
import Enchere.Produit;
import Enchere.ProduitExistePas;
import Enchere.SystemeEnchere;
import Enchere.SystemeEncherePOA;
import Enchere.Utilisateur;
import Enchere.Vente;
import Enchere.pas;

public class SystemeEnchereImpl extends SystemeEncherePOA implements EnchereSubject{
	private List<Utilisateur> lesUtilisateurs = new ArrayList<>();
	private List<Produit> lesProduits = new ArrayList<>();
	private List<Vente> lesVentesEnCours = new ArrayList<>();
	private Map<String, List<Acheteur_Vendeur>> prod_UserNotifications = new HashMap<>();
	private Map<String, Map<String, List<Double>>> histo_prod_User_Prix = new HashMap<>();
	private List<ClientUpdate>whenToUpdate = new ArrayList<>();

	//Map<Utilisateur, String> user_IOR_ASSOC = new HashMap<>();
	/**
	 * Get the list of all the produits.
	 * @see Enchere.SystemeEnchereOperations#tousLesProduits()
	 */
	@Override
	public Produit[] tousLesProduits() {

		return lesProduits.toArray(new Produit[0]);

	}
	/**
	 * Set the list of produits.
	 * (non-Javadoc)
	 * @see Enchere.SystemeEnchereOperations#tousLesProduits(Enchere.Produit[])
	 */
	@Override

	public void tousLesProduits(Produit[] newTousLesProduits) {

		this.lesProduits = new ArrayList<>(Arrays.asList(newTousLesProduits));

	}

	@Override
	public Utilisateur[] tousLesUtilisateurs() {
		return lesUtilisateurs.toArray(new Utilisateur[0]);
	}

	@Override
	public void tousLesUtilisateurs(Utilisateur[] newTousLesUtilisateurs) {
		this.lesUtilisateurs = new ArrayList<>(Arrays.asList(newTousLesUtilisateurs));

	}

	@Override
	public Produit[] consulterListeProduits() {
		return tousLesProduits();
	}

	@Override
	public Vente[] tousLesVentesEncours() {
		System.out.println("LES VENTES EN COURS:");
		return lesVentesEnCours.toArray(new Vente[0]);
	}
	@Override
	public void tousLesVentesEncours(Vente[] newTousLesVentesEncours) {
		lesVentesEnCours = new ArrayList<Vente>(Arrays.asList(newTousLesVentesEncours));	
	}

	@Override
	public Produit[] rechercherProduit(String critere) throws ProduitExistePas {
		//L'utilisateur presente son critere de recherche, qui peut etre le nom ou la catégorie
		//du produit

		//Filtrage de la liste de produits pour sortir avec les produits qui repondent au critere de recherche
		List<Produit> prods = lesProduits.parallelStream()
				.filter(p->p.nom.equals(critere) || 
						p.categorie.equals(critere))
						.collect(Collectors.toCollection(ArrayList::new));
		return prods.toArray(new Produit[0]);
	}



	/**
	 * Cette methode permet d'accepter une nouvelle proposition pour un produit en cour d'encheres
	 * @param prix le nouveau prix proposé
	 * @param user l'utilisateur qui propose le prix
	 * @param produit le produit pour lequel il propose un prix 
	 * 
	 * @see Enchere.SystemeEnchereOperations#proposerPrix(float, Enchere.Utilisateur, Enchere.Produit)
	 */


	@Override
	public String proposerPrix(double prix, Utilisateur user, Produit produit) {
		if (prix < produit.prix_depart) {
			System.out.println("Prix trop bas.");
			return "Prix trop bas.";
		}
		//Ajouter le pas 
		produit.prixMaxPropse = prix;
		try {
			List<Double> listPropos = new ArrayList<>();
			Map<String, List<Double>> histoProposition = new HashMap<>();

			//Ajouter la nouvelle proposition pour le produit
			if (histo_prod_User_Prix.containsKey(produit.id)) {
				//System.out.println("Produt deja enregistré");
				histoProposition = histo_prod_User_Prix.get(produit.id);
				if (histoProposition.containsKey(user.id)) {
					System.out.println("Utilisateur existe, ajoutant a son historique.");
					List<Double> props = histoProposition.get(user.id);

					props.stream().forEach(d->listPropos.add(d));

					listPropos.add(prix);

					histoProposition.put(user.id, listPropos);
					histo_prod_User_Prix.put(produit.id, histoProposition);
				} else {
					System.out.println("Premiere proposition de cet Utilisateur.");
					listPropos.add(prix);
					histoProposition.put(user.id, listPropos);
					histo_prod_User_Prix.put(produit.id, histoProposition);
				}
			} else {
				System.out.println("Produt non existant.");
				listPropos.add(prix);
				histoProposition.put(user.id, listPropos);
				histo_prod_User_Prix.put(produit.id, histoProposition);

			}

			/*
			System.out.println("Les produits");
			for (java.util.Map.Entry<String, Map<String, List<Double>>>   x : histo_prod_User_Prix.entrySet()) {
				System.out.println("Produit: "+x.getKey());
				for (java.util.Map.Entry<String, List<Double>>  g : x.getValue().entrySet()) {
					System.out.println("\tUtilisateur:" +g.getKey()+"\n");
					System.out.println("\t\tPropositions:");
					for (Double db : g.getValue()) {
						System.out.println("\t\t\t"+db);
					}

				}
			}

			 */
			histoProposition = histo_prod_User_Prix.get(produit.id);

			List<Double> listPrix = new ArrayList<>();
			for (java.util.Map.Entry<String, List<Double>> entry : histoProposition.entrySet()) {

				for (Double db : entry.getValue()) {

					listPrix.add(db);
				}
			}

			//Calculer au meme temps le prix en cours pour un produit(modifier Produit.prix_depart)

			//Ordonner la liste des prix 	
			listPrix.sort((n1,n2)->n1.compareTo(n2));

			System.out.println("Prix proposés ordonnés\n"+listPrix);

			if (listPrix.size() > 1) {
				System.out.println("Plus de deux prix proposé, choisissant le max.");

				//modifier le prix du produit
				Double newprix = (listPrix.get(listPrix.size()-2));
				Double highest = listPrix.get(listPrix.size()-1);

				newprix *= pas.value;
				if (newprix>highest) 
					produit.prix_depart = highest;
				else 
					produit.prix_depart = newprix;



				System.out.println("PRIX MODIFIE "+produit.prix_depart);

				//Replace the produit with the new one with the new price 
				for (int i = 0; i < lesProduits.size(); i++) {
					if (produit.id.equals(lesProduits.get(i).id)) {
						lesProduits.remove(i);
						lesProduits.add(i, produit);
						System.out.println("replaced the product");
					}
				}
				//prix modifié, il faut notifier les parties concernées
				notifyObserver(produit);
			}
		} catch (Exception e) {
			e.getMessage();
		}
		updateClients();
		return "Prix modifié";
	}

	public void updateClients() {
		for (ClientUpdate cl : whenToUpdate) {
			cl.updateUI = true;
		}
	}
	
	public void doneUpdatingClient(Acheteur_Vendeur ior) {
		for (ClientUpdate cl : whenToUpdate) {
			if (cl.ac.equals(ior)) {
				cl.updateUI = false;
			}
		}
	}
	
	public ClientUpdate[] getUIUpdateStatus() {
		return this.whenToUpdate.toArray(new ClientUpdate[0]);
	}
	/*
	 * This method is called when a user wishes to create a new account and be associated with the system.
	 * (non-Javadoc)
	 * @see Enchere.SystemeEnchereOperations#creerCompte(Enchere.Utilisateur)
	 */

	@Override
	public boolean creerCompte(String username, String userpswd, String userAdresse) {

		Utilisateur newUtilisateur = new Utilisateur();
		//Creation d'un identifiant unique pour les utilisateurs
		newUtilisateur.id = UUID.randomUUID().toString();
		newUtilisateur.nom = username;
		newUtilisateur.mdp = userpswd;
		newUtilisateur.adresse = userAdresse;

		addUser(newUtilisateur);

		return true;
		//System.out.println("Utilisateur ajouté: \nNom: "+newUtilisateur.nom+"");
	}

	private void addUser(Utilisateur user) {
		synchronized (lesUtilisateurs) {
			lesUtilisateurs.add(user);
		}
	}

	private void addProduit(Produit produit) {
		synchronized (lesProduits) {
			lesProduits.add(produit);
		}
	}
	/**
	 * Chercher un match pour le nom d'utilisateur et le mdp, retourne le 1e match trouvé 
	 * (non-Javadoc)
	 * @see Enchere.SystemeEnchereOperations#seConnecter(java.lang.String, java.lang.String)
	 */
	@Override
	public Utilisateur seConnecter(String username, String userpswd) {
		if (lesUtilisateurs.isEmpty()) {
			return new Utilisateur("", "", "", "");
		}

		Optional<Utilisateur> newUser = Optional.ofNullable(null);
		newUser = lesUtilisateurs.stream().filter(u-> u.nom.equals(username) && u.mdp.equals(userpswd)).findFirst();

		//Si utilisateur n'est pas dans la BD retourne null.
		if(!newUser.isPresent())
			return new Utilisateur("", "", "", "");
		return newUser.get();

		/*	for (Utilisateur u : lesUtilisateurs) {
		if (u.nom.equals(username) && u.mdp.equals(userpswd)) {
			return u;
		}
	}*/

	}
	/**
	 * Cette methode permettra de publier un nouveau produit et le mettre en vente
	 * @see Enchere.SystemeEnchereOperations#publierProduit(Enchere.Utilisateur, java.lang.String, java.lang.String, java.lang.String, float, java.lang.String)
	 */
	@Override
	public Produit publierProduit(Utilisateur vendeur, String nomProduit,
			String categorieProduit, String descProduit, double prixProduit,
			String dateProduit, SystemeEnchere ior) {

		Produit newProduit = new Produit();
		newProduit.vendeur = new Utilisateur("","","","");
		newProduit.id = (UUID.randomUUID().toString());
		newProduit.categorie = (categorieProduit);
		newProduit.description = (descProduit);
		newProduit.nom = (nomProduit);
		newProduit.prix_depart = (prixProduit);;
		newProduit.date_fin = (dateProduit);


		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT, Locale.FRANCE);
		try {
			//Date fin de l'enchere
			Date endDate = df.parse(dateProduit);

			Date now = new Date();
			String nowstr = df.format(now);
			now = df.parse(nowstr);

			//System.out.println("Product details: "+dateProduit+"\n"+nomProduit);
			/*Calendar calendar = Calendar.getInstance();
			calendar.setTime(now);
			int min = calendar.get(Calendar.MINUTE);
			min +=5;
			calendar.set(Calendar.MINUTE, min);
			now = calendar.getTime();


			if (endDate.before(now)) {
				throw new IllegalStateException("Date fin ne peut pas etre inférieure, il doit etre au moins 5mins dans le futur");

			}*/
			//Créer une vente pour ce produit qui vient d'etre crée
			Vente vente = new Vente(UUID.randomUUID().toString(), newProduit, new Utilisateur("","","",""), df.format(now),"", df.format(endDate));
			//System.out.println("vente ID ----- "+vente.idVente);

			//Verifier la dateFin d'enchere avant d'ajouter ce produit
			addProduit(newProduit);

			//Verifier la dateFin d'enchere avant d'ajouter la vente
			lesVentesEnCours.add(vente);

			//demarré un compteur qui mettra fin à la vente quand le temps s'acheve
			Timer timer = new Timer(nomProduit+"Timer",true );
			timer.schedule(new TerminerVente(vente,ior), endDate);


		} catch (ParseException e) {
			e.printStackTrace();
		}
		updateClients();
		return newProduit;
	}

	/**
	 * Cette methode permettra de recuperer l'utilisateur qui a proposé le meilleur
	 *  prix pour un produit.
	 * @param produit Le produit por lequel on cherche les acheteurs.
	 */
	//you cannot reference a non-final variable in an anonymous class' method
	private double max; private String remporte;

	public Utilisateur getAcheteurProduit(Produit produit) {
		Map<String, List<Double>> map = new  HashMap<String, List<Double>>();
		map = histo_prod_User_Prix.get(produit.id);
		if (map != null) {
			//S'il n'y a eu aucune proposition de prix pour ce produit
			if (map.isEmpty()) {
				System.out.println("aucune proposition de prix pour ce produit");
				return new Utilisateur("", "", "", "");
			}

			//initialize the variables to be sure the right op is carried out
			remporte = ""; max = 0;

			map.forEach((u,p)->{ p.stream().forEach(prix-> {  if(prix>max) 
			{max = prix; remporte = u;}}); });

			Utilisateur user = getUtilisateur(remporte);
			if(user != null)
				System.out.println("Max price: "+max+"\nUsername: "+user.nom);
			return user;

		}else 
			return new Utilisateur("", "", "", "");
	}
	/**
	 * Cette methode permet de recuperer un utilisateur a partir de son ID
	 * @param id l'identifiant de l'utilisateur
	 * @return Utilisateur
	 */
	private Utilisateur getUtilisateur(String id) {
		Utilisateur user = lesUtilisateurs.stream().filter(u->u.id.equals(id)).findFirst().get();
		return user;
	}


	/**
	 * L'utilisateur peut demander d'etre notifie lorsque le prix d'une enchere dans lequel il participe evolue
	 * @param user l'utilisateur qui sera notifié
	 * @param ior son IOR coté serveur 
	 * @see Enchere.SystemeEnchereOperations#demanderNotificationEnchereEnCours(Enchere.Utilisateur, Enchere.Acheteur_Vendeur)
	 */
	@Override
	public boolean demanderNotificationEnchereEnCours(Utilisateur user,
			Produit produit, Acheteur_Vendeur ior) {
		registerObserver(produit, ior);
		return true;
	}

	@Override
	public void notifyObserver(Produit p) {
		if (prod_UserNotifications.containsKey(p.id)){
			List<Acheteur_Vendeur> list = new ArrayList<>();
			list = prod_UserNotifications.get(p.id);
			for (Acheteur_Vendeur acheteur_Vendeur : list) {
				acheteur_Vendeur.recevoirNotification("Le prix de "+p.nom+" a evolué, nouveau prix est "+p.prix_depart);
			}
		}

	}

	@Override
	public void registerObserver(Produit p, Acheteur_Vendeur o) {
		if (prod_UserNotifications.containsKey(p.id)) {
			List<Acheteur_Vendeur> list = new ArrayList<>();
			list = prod_UserNotifications.get(p.id);
			if (!list.contains(o)) {
				list.add(o);
				prod_UserNotifications.put(p.id, list);
			}else {
				System.out.println("didnt re add user");
			}			
		} else {
			List<Acheteur_Vendeur> list = new ArrayList<>();
			list.add(o);
			prod_UserNotifications.put(p.id, list);
		}

	}

	@Override
	public void unregisterObserver(Produit p, Acheteur_Vendeur o) {
		if (prod_UserNotifications.containsKey(p.id)) {
			List<Acheteur_Vendeur> list = new ArrayList<>();
			list = prod_UserNotifications.get(p.id);
			if (list.contains(o)) {
				list.remove(o);
			}
			prod_UserNotifications.put(p.id, list);
		}

	}
	
	
	@Override
	public void supprimerVente(Vente vente) {

	}

	@Override
	public void enleverVente(String venteID) {
		if (lesVentesEnCours == null) {
			System.out.println("List vente is null");
			return;
		}
		else {
			System.out.println("List vente is NOT null");
		}
		if (lesVentesEnCours.isEmpty()) 
			return ;

		for (Vente vente : lesVentesEnCours) {
			if (vente != null) {
				if (vente.idVente.equals(venteID)) {
					//remove the product associated with this sale
					for (Produit produit : lesProduits) {
						if (produit != null) {
							if (produit.id.equals(vente.produitVendu.id)) {
								//from the list of products in the system
								lesProduits.remove(produit);
								if (histo_prod_User_Prix.containsKey(produit.id)) {
									//and from the mapping of products and user price propositions
									histo_prod_User_Prix.remove(produit.id);
								}
							}
						}
					}

					//Now remove the sale itself
					this.lesVentesEnCours.remove(vente);
					System.out.println("This vente removed "+venteID);
					return;
				}
			}else {
				System.out.println("A vente is null");
			}
		}

	}
	
	private Produit findProduit(String id){
		Optional<Produit> optional = Optional.empty();
		optional = lesProduits.stream().filter(p->p.id.equals(id)).findFirst();
		if(optional.isPresent())
			return optional.get();
		return null;
	}
	
	
	private Utilisateur findUser(String id){
		Optional<Utilisateur> optional = Optional.empty();
		optional = lesUtilisateurs.stream().filter(u->u.id.equals(id)).findFirst();
		if(optional.isPresent())
			return optional.get();
		return null;
	}
	
	
	@Override
	public String statistiques() {
		StringBuilder builder = new StringBuilder();
		int userMaxProp = 0;
		int prodMaxProp = 0;
		String prodId = "";
		String UserId = "";

		double totalDePropositions = 0;
		
		String userwithMaxProp = "";
		String produitwithMinprix = "";
		int maxProd = 0;
		int maxUser = 0;
		
		double maxProp = 0;
		double minPrix = 500000000;
		double minProdPrix = 0;

		builder.append("Le produit avec les plus de propositions.\n");
		for (java.util.Map.Entry<String, Map<String, List<Double>>>   x : histo_prod_User_Prix.entrySet()) {
			//pour chaque produit...
			for (java.util.Map.Entry<String, List<Double>>  g : x.getValue().entrySet()) {
				//sommer tous les propositions de tous les utilisateurs
				prodMaxProp+=g.getValue().size();
				
				//Récupérer le nombre de propositions pour chaque utilisateur
				userMaxProp = g.getValue().size();
				
				if (userMaxProp>maxUser) {
					maxUser = userMaxProp;
					UserId = g.getKey();
					userMaxProp = 0;
				}
					for (Double db : g.getValue()) {
						if (db>maxProp) {
							maxProp = db;
							
							userwithMaxProp = g.getKey();
						}
						
						totalDePropositions+=db;
					}

			}
			if (prodMaxProp>maxProd) {
				maxProd = prodMaxProp;
				prodId = x.getKey();
				prodMaxProp = 0;
			}
		}
		Produit produit = findProduit(prodId);
		if (produit != null) {
			builder.append("Nom du produit: "+produit.nom +" \tNombre de propositions: "+maxProd+"\n");
		}else {
			builder.append("Aucun résultat.\n");
		}
		maxProd = 0;	

		builder.append("Utilisateur avec les plus de propositions.\n");
		Utilisateur user = findUser(UserId);
		if (user != null) {
			builder.append("Nom de l'utilisateur: "+user.nom +" \tNombre de propositions: "+maxUser+"\n");
		} else {
			builder.append("Aucun résultat.\n");
		}
		maxUser = 0;

		builder.append("Utilisateur avec la plus grande proposition.\n");
		Utilisateur user2 = findUser(userwithMaxProp);
		if (user2 != null) {
			builder.append("Nom de l'utilisateur: "+user2.nom +" \tMontant de proposition: "+maxProp+"\n");
		} else {
			builder.append("Aucun résultat.\n");
		}
		
		
		builder.append("Le produit le plus cher\n");
		maxProp = 0; prodId = "";
		for (Produit p : lesProduits) {
			if (p.prix_depart>maxProd) {
				prodId = p.id;
				maxProp = p.prix_depart;
			}
			if (p.prix_depart<minPrix) {
				minPrix = p.prix_depart;
				produitwithMinprix = p.id;
			}
		}
		produit = null;
		produit = findProduit(prodId);
		if (produit != null) {
			builder.append("Nom du produit: "+produit.nom +" \tPrix: "+maxProp+"\n");
		}else {
			builder.append("Aucun résultat.\n");
		}

		
		builder.append("Le produit le moins cher\n");
		
		produit = null;
		produit = findProduit(produitwithMinprix);
		
		if (produit != null && minPrix != 500000000) {
			builder.append("Nom du produit: "+produit.nom +" \tPrix: "+minPrix+"\n");
		}else {
			builder.append("Aucun résultat.\n");
		}
		
		builder.append("Montant Total de tous les propositions.\n");
		builder.append(totalDePropositions+"\n");

		return builder.toString();
	}
	
	@Override
	public boolean addClient(ClientUpdate client) {
		if (whenToUpdate.contains(client)) {
			return false;
		}
		System.out.println("Adding client");
		return whenToUpdate.add(client);
	}

}
