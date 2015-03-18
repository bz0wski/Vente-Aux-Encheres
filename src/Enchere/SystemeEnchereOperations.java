package Enchere;


/**
* Enchere/SystemeEnchereOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from encheresIDL.idl
* mercredi 18 mars 2015 00 h 52 CET
*/

public interface SystemeEnchereOperations 
{
  Enchere.Produit[] tousLesProduits ();
  void tousLesProduits (Enchere.Produit[] newTousLesProduits);
  Enchere.Utilisateur[] tousLesUtilisateurs ();
  void tousLesUtilisateurs (Enchere.Utilisateur[] newTousLesUtilisateurs);
  Enchere.Produit[] consulterListeProduits ();
  Enchere.Produit[] rechercherProduit (String critere) throws Enchere.ProduitExistePas;

  /* attribut HashMap qui permet de stocker pour chaque produit la correspondance entre les acheteurs et les prix proposés*/
  void demanderNotificationEnchereEnCours (Enchere.Utilisateur user, Enchere.Produit produit, Enchere.Acheteur_Vendeur ior);
  void creerCompte (String username, String userpswd, String userAdresse);
  Enchere.Utilisateur seConnecter (String username, String userpswd);
  Enchere.Utilisateur getAcheteurProduit (Enchere.Produit produit);
  void publierProduit (Enchere.Utilisateur vendeur, String nomProduit, String categorieProduit, String descProduit, float prixProduit, String dateProduit);

  /* instanciation d'un objet GestionEnchere à la publication d'un nouveau produit*/
  String proposerPrix (float prix, Enchere.Utilisateur user, Enchere.Produit produit);
} // interface SystemeEnchereOperations