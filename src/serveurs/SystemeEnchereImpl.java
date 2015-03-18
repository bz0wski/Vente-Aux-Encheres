package serveurs;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.UUID;
import java.util.stream.Collectors;

import utility.TerminerVente;
import Enchere.Acheteur_Vendeur;
import Enchere.Produit;
import Enchere.ProduitExistePas;
import Enchere.SystemeEncherePOA;
import Enchere.Utilisateur;
import Enchere.Vente;

public class SystemeEnchereImpl extends SystemeEncherePOA{
	private List<Utilisateur> lesUtilisateurs = new ArrayList<>();
	private List<Produit> lesProduits = new ArrayList<>();
	private List<Vente> lesVentesEnCours = new ArrayList<>();
	private Map<Produit, Map<Utilisateur, Float>> prod_User_Prix = new HashMap<>();
	Map<Utilisateur, String> user_IOR_ASSOC = new HashMap<>();
	/**
	 * Get the list of all the produits.
	 * @see Enchere.SystemeEnchereOperations#tousLesProduits()
	 */
	@Override
	public Produit[] tousLesProduits() {
		System.out.println("Size "+lesProduits.size());
		
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
	public String proposerPrix(float prix, Utilisateur user, Produit produit) {
		try {
			//Calculer au meme temps le prix en cours pour un produit(modifier Produit.prix_depart)
			Map<Utilisateur, Float> proposition = new HashMap<>();
			proposition.put(user, prix);
			//Ajouter la nouvelle proposition pour le produit
			prod_User_Prix.put(produit, proposition);
			proposition = prod_User_Prix.get(produit);
			
			List<Float> prixList = new ArrayList<>();
			proposition.forEach((u,p)->{ prixList.add(p);});
			
			//Ordonner la liste des prix 	
			prixList.sort((n1,n2)->n1.compareTo(n2));
			if (prixList.size() > 1) {
				//modifier le prix du produit
				produit.prix_depart = (prixList.get(prixList.size()-1));
				//prix modifié, il faut notifier les parties concernées
				produit.notifyObserver();
				
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return "hhh";
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
		
		System.out.println("Utilisateur ajouté: \nNom: "+newUtilisateur.nom+"");
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
	@Override
	public void publierProduit(Utilisateur vendeur, String nomProduit,
			String categorieProduit, String descProduit, float prixProduit,
			String dateProduit) {
		Produit newProduit = new Produit();
		newProduit.vendeur = new Utilisateur("","","","");
		newProduit.id = (UUID.randomUUID().toString());
		newProduit.categorie = (categorieProduit);
		newProduit.description = (descProduit);
		newProduit.nom = (nomProduit);;
		newProduit.prix_depart = (prixProduit);;
		newProduit.date_fin = (dateProduit);
		
		lesProduits.add(newProduit);
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT, Locale.FRANCE);
		try {
			Date endDate = df.parse(dateProduit);
			Date now = new Date();
			String nowstr = df.format(now);
			now = df.parse(nowstr);
			
			//Créer une vente pour ce produit qui vient d'etre crée
			Vente vente = new Vente(UUID.randomUUID().toString(), newProduit, new Utilisateur(), nowstr, "");
			lesVentesEnCours.add(vente);
			
			//demarré un compteur qui mettra fin à la vente quand le temps s'acheve
			Timer timer = new Timer(nomProduit+"Timer",true );
			timer.schedule(new TerminerVente(vente), endDate);
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
	private float max; private Utilisateur remporte;
	public Utilisateur getAcheteurProduit(Produit produit) {
		Map<Utilisateur, Float> map = prod_User_Prix.get(produit);
		
		//initialize the variables to be sure the right op is carried out
		remporte = null; max = 0;
		
		map.forEach((u,p)->{ if(p>max) 
								{max = p; remporte = u;} });
		if(remporte != null)
			System.out.println("Max price: "+max+"\nUsername: "+remporte.nom);
		return remporte;
		
	}
	
	public Vente getVente(String uuid) {
		return lesVentesEnCours.parallelStream().filter(v->v.idVente.equals(uuid)).findFirst().get();
	}
	
	public void enleverVente(Vente v) {
		this.lesVentesEnCours.remove(v);
	}
	
	/**
	 * L'utilisateur peut demander d'etre notifie lorsque le prix d'une enchere dans lequel il participe evolue
	 * @param user l'utilisateur qui sera notifié
	 * @param ior son IOR coté serveur 
	 * @see Enchere.SystemeEnchereOperations#demanderNotificationEnchereEnCours(Enchere.Utilisateur, Enchere.Acheteur_Vendeur)
	 */
	@Override
	public void demanderNotificationEnchereEnCours(Utilisateur user,
			Produit produit, Acheteur_Vendeur ior) {
	//	produit.registerObserver(user);
		
	}
}
