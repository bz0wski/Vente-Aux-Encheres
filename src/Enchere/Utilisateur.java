package Enchere;


/**
* Enchere/Utilisateur.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from encheresIDL.idl
* mercredi 18 mars 2015 00 h 52 CET
*/

public final class Utilisateur implements org.omg.CORBA.portable.IDLEntity
{
  public String id = null;
  public String nom = null;
  public String mdp = null;
  public String adresse = null;

  public Utilisateur ()
  {
  } // ctor

  public Utilisateur (String _id, String _nom, String _mdp, String _adresse)
  {
    id = _id;
    nom = _nom;
    mdp = _mdp;
    adresse = _adresse;
  } // ctor

} // class Utilisateur