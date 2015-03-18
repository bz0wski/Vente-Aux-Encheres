package Enchere;

import java.util.ArrayList;
import java.util.List;

import utility.EnchereObserver;
import utility.EnchereSubject;


/**
 * Enchere/Produit.java .
 * Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from encheresIDL.idl
 * mercredi 18 mars 2015 00 h 52 CET
 */

public final class Produit implements org.omg.CORBA.portable.IDLEntity,EnchereSubject
{
	public String id = null;
	public String nom = null;
	public String categorie = null;
	public String description = null;
	public float prix_depart = (float)0;

	private List<EnchereObserver> listPartiesConcernees = new ArrayList<>();
	private static final long serialVersionUID = 1L;

	/*Cet attribut plus le pas sera le prix en cours */
	public String date_fin = null;
	public Enchere.Utilisateur vendeur = null;

	public Produit ()
	{
	} // ctor

	public Produit (String _id, String _nom, String _categorie, String _description, float _prix_depart, String _date_fin, Enchere.Utilisateur _vendeur)
	{
		id = _id;
		nom = _nom;
		categorie = _categorie;
		description = _description;
		prix_depart = _prix_depart;
		date_fin = _date_fin;
		vendeur = _vendeur;
	} // ctor

	@Override
	public void notifyObserver() {
		for (EnchereObserver enchereObserver : listPartiesConcernees) {
			enchereObserver.update("Le prix de "+nom+" a evolué, nouveau prix est "+prix_depart);
		}

	}

	@Override
	public void registerObserver(EnchereObserver o) {
		listPartiesConcernees.add(o);

	}

	@Override
	public void unregisterObserver(EnchereObserver o) {
		listPartiesConcernees.add(o);

	}

} // class Produit
