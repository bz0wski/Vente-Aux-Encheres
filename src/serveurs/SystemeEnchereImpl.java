package serveurs;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.UUID;
import java.util.stream.Collectors;

import utility.EnchereSubject;
import utility.TerminerVente;
import Enchere.Acheteur_Vendeur;
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

	Map<Utilisateur, String> user_IOR_ASSOC = new HashMap<>();
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

		this.lesProduits = Arrays.asList(newTousLesProduits);

	}

	@Override
	public Utilisateur[] tousLesUtilisateurs() {
		return  lesUtilisateurs.toArray(new Utilisateur[0]);
	}

	@Override
	public void tousLesUtilisateurs(Utilisateur[] newTousLesUtilisateurs) {
		this.lesUtilisateurs = Arrays.asList(newTousLesUtilisateurs);

	}

	@Override
	public Produit[] consulterListeProduits() {
		return tousLesProduits();
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
		if (prix < produit.prix_depart*pas.value) {
			System.out.println("Prix trop bas.");
		}
		//Ajouter le pas 
		prix *= pas.value;
		try {
			List<Double> listPropos = new ArrayList<>();
			Map<String, List<Double>> histoProposition = new HashMap<>();

			//Ajouter la nouvelle proposition pour le produit
			if (histo_prod_User_Prix.containsKey(produit.id)) {
				//System.out.println("Produt deja enregistré");
				histoProposition = histo_prod_User_Prix.get(produit.id);
				if (histoProposition.containsKey(user.id)) {
					//System.out.println("Utilisateur existe, ajoutant a son historique.");
					List<Double> props = histoProposition.get(user.id);
					
					props.stream().forEach(d->listPropos.add(d));
					
					listPropos.add(prix);
					//listPropos.stream().forEach(d->System.out.println("After adding prix: "+d));
					histoProposition.put(user.id, listPropos);
					histo_prod_User_Prix.put(produit.id, histoProposition);
				} else {
					//System.out.println("Premiere proposition de cet Utilisateur.");
					listPropos.add(prix);
					histoProposition.put(user.id, listPropos);
					histo_prod_User_Prix.put(produit.id, histoProposition);
				}
			} else {
				//System.out.println("Produt non existant.");
				listPropos.add(prix);
				histoProposition.put(user.id, listPropos);
				histo_prod_User_Prix.put(produit.id, histoProposition);

			}


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
			//System.out.println("Prix proposés ordonnés\n"+listPrix);

			if (listPrix.size() > 1) {
				//modifier le prix du produit
				produit.prix_depart = (listPrix.get(listPrix.size()-1));
				
				//prix modifié, il faut notifier les parties concernées
				notifyObserver(produit);
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return "";
	}


	/*
	 * This method is called when a user wishes to create a new account and be associated with the system.
	 * (non-Javadoc)
	 * @see Enchere.SystemeEnchereOperations#creerCompte(Enchere.Utilisateur)
	 */

	@Override
	public void creerCompte(String username, String userpswd, String userAdresse) {

		Utilisateur newUtilisateur = new Utilisateur();
		//Creation d'un identifiant unique pour les utilisateurs
		newUtilisateur.id = UUID.randomUUID().toString();
		newUtilisateur.nom = username;
		newUtilisateur.mdp = userpswd;
		newUtilisateur.adresse = userAdresse;

		lesUtilisateurs.add(newUtilisateur);

		//System.out.println("Utilisateur ajouté: \nNom: "+newUtilisateur.nom+"");
	}

	/**
	 * Chercher un match pour le nom d'utilisateur et le mdp, retourne le 1e match trouvé 
	 * (non-Javadoc)
	 * @see Enchere.SystemeEnchereOperations#seConnecter(java.lang.String, java.lang.String)
	 */
	@Override
	public Utilisateur seConnecter(String username, String userpswd) {
		Utilisateur newUser = null;
		newUser = lesUtilisateurs.stream().filter(u->u.nom.equals(username) && u.mdp.equals(userpswd)).findFirst().get();
		//Si utilisateur n'est pas dans la BD retourne null.
		if(newUser == null)
			return null;
		return newUser;

	}
	/**
	 * Cette methode permettra de publier un nouveau produit et le mettre en vente
	 * @see Enchere.SystemeEnchereOperations#publierProduit(Enchere.Utilisateur, java.lang.String, java.lang.String, java.lang.String, float, java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void publierProduit(Utilisateur vendeur, String nomProduit,
			String categorieProduit, String descProduit, double prixProduit,
			String dateProduit, SystemeEnchere ior) {
		Produit newProduit = new Produit();
		newProduit.vendeur = new Utilisateur("","","","");
		newProduit.id = (UUID.randomUUID().toString());
		newProduit.categorie = (categorieProduit);
		newProduit.description = (descProduit);
		newProduit.nom = (nomProduit);;
		newProduit.prix_depart = (prixProduit);;
		newProduit.date_fin = (dateProduit);

		
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT, Locale.FRANCE);
		try {
			Date endDate = df.parse(dateProduit);
			Date now = new Date();
			String nowstr = df.format(now);
			now = df.parse(nowstr);
			//now.setMinutes(now.getMinutes()+5);
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(now);
			int min = calendar.get(Calendar.MINUTE);
			min +=5;
			calendar.set(Calendar.MINUTE, min);
			now = calendar.getTime();
			
			//Calendar.get(Calendar.MINUTE).
			//Calendar.set(Calendar.DAY_OF_MONTH, int date).
			if (endDate.before(now)) {
				throw new IllegalStateException("Date fin ne peut pas etre inférieure, il doit etre au moins 5mins dans le futur");
				
			}
			//Créer une vente pour ce produit qui vient d'etre crée
			Vente vente = new Vente(UUID.randomUUID().toString(), newProduit, new Utilisateur(), nowstr, "");
			System.out.println("vente ID ----- "+vente.idVente);
			
			//Verifier la dateFin d'enchere avant d'ajouter ce produit
			lesProduits.add(newProduit);
			//Verifier la dateFin d'enchere avant d'ajouter la vente
			lesVentesEnCours.add(vente);

			//demarré un compteur qui mettra fin à la vente quand le temps s'acheve
			Timer timer = new Timer(nomProduit+"Timer",true );
			timer.schedule(new TerminerVente(vente,ior), endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
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
	/*public Vente getVente(String uuid) {
		return lesVentesEnCours.parallelStream().filter(v->v.idVente.equals(uuid)).findFirst().get();
	}*/
	

	
//	public boolean enleverVente(Vente v) {
//		if (lesVentesEnCours == null) {
//			System.out.println("List vente is null");
//			return false;
//			}
//		else {
//			System.out.println("List vente is NOT null");
//		}
		//System.out.println("Liste Ventes: "+lesVentesEnCours);
		//if (lesVentesEnCours.isEmpty()) 
		//	return false;
		/*
		for (Vente vente : lesVentesEnCours) {
			if (vente != null) {
				if (vente.idVente.equals(v.idVente)) {
					this.lesVentesEnCours.remove(v);
					return true;
				}
			}else {
				System.out.println("A vente is null");
			}
		}*/
	//	return true;
	//}

	/**
	 * L'utilisateur peut demander d'etre notifie lorsque le prix d'une enchere dans lequel il participe evolue
	 * @param user l'utilisateur qui sera notifié
	 * @param ior son IOR coté serveur 
	 * @see Enchere.SystemeEnchereOperations#demanderNotificationEnchereEnCours(Enchere.Utilisateur, Enchere.Acheteur_Vendeur)
	 */
	@Override
	public void demanderNotificationEnchereEnCours(Utilisateur user,
			Produit produit, Acheteur_Vendeur ior) {
		registerObserver(produit, ior);

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
			list.add(o);
			prod_UserNotifications.put(p.id, list);
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
				this.lesVentesEnCours.remove(vente);
				System.out.println("This vente removed "+venteID);
				return;
			}
		}else {
			System.out.println("A vente is null");
		}
	}
		
	}
}
