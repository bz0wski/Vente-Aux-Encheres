package Enchere;

/**
* Enchere/UtilisateurHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from encheresIDL.idl
* mercredi 18 mars 2015 00 h 52 CET
*/

public final class UtilisateurHolder implements org.omg.CORBA.portable.Streamable
{
  public Enchere.Utilisateur value = null;

  public UtilisateurHolder ()
  {
  }

  public UtilisateurHolder (Enchere.Utilisateur initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = Enchere.UtilisateurHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    Enchere.UtilisateurHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return Enchere.UtilisateurHelper.type ();
  }

}