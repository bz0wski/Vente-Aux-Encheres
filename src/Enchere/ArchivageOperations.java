package Enchere;


/**
* Enchere/ArchivageOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from encheresIDL.idl
* mercredi 18 mars 2015 00 h 52 CET
*/

public interface ArchivageOperations 
{
  void archiverVenteAchevee (Enchere.Vente venteAcheve);
  void mettreAuxEnchere ();
  boolean archiverProduits (Enchere.Produit[] listeproduits);
  boolean archiverUtilisateurs (Enchere.Utilisateur[] listeUtilisateurs);
  Enchere.Produit[] chargerProduits ();
  Enchere.Utilisateur[] chargerUtilisateurs ();
  void statistiques ();
} // interface ArchivageOperations
