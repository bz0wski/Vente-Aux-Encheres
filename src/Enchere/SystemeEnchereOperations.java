package Enchere;


/**
* Enchere/SystemeEnchereOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from encheresIDL.idl
* mercredi 1 avril 2015 16 h 31 CEST
*/

public interface SystemeEnchereOperations 
{
  Enchere.Produit[] tousLesProduits ();
  void tousLesProduits (Enchere.Produit[] newTousLesProduits);
  Enchere.Utilisateur[] tousLesUtilisateurs ();
  void tousLesUtilisateurs (Enchere.Utilisateur[] newTousLesUtilisateurs);
  Enchere.Vente[] tousLesVentesEncours ();
  void tousLesVentesEncours (Enchere.Vente[] newTousLesVentesEncours);
  Enchere.Produit[] consulterListeProduits ();
  Enchere.Produit[] rechercherProduit (String critere) throws Enchere.ProduitExistePas;

  /* attribut HashMap qui permet de stocker pour chaque produit la correspondance entre les acheteurs et les prix proposés*/
  boolean demanderNotificationEnchereEnCours (Enchere.Utilisateur user, Enchere.Produit produit, Enchere.Acheteur_Vendeur acheteur_VendeurIOR);
  boolean creerCompte (String username, String userpswd, String userAdresse);
  Enchere.Utilisateur seConnecter (String username, String userpswd);
  Enchere.Utilisateur getAcheteurProduit (Enchere.Produit produit);
  Enchere.Produit publierProduit (Enchere.Utilisateur vendeur, String nomProduit, String categorieProduit, String descProduit, double prixProduit, String dateProduit, Enchere.SystemeEnchere ior);

  /* instanciation d'un objet GestionEnchere à la publication d'un nouveau produit*/
  void doneUpdatingClient (Enchere.Acheteur_Vendeur ior);
  Enchere.ClientUpdate[] getUIUpdateStatus ();
  boolean addClient (Enchere.ClientUpdate client);
  String proposerPrix (double prix, Enchere.Utilisateur user, Enchere.Produit produit);

  /*Cette methode permettra d'enlever une vente terminée de la liste des ventes en cours */
  void supprimerVente (Enchere.Vente vente);
  void enleverVente (String venteID);
  String statistiques ();
} // interface SystemeEnchereOperations
