package Enchere;


/**
* Enchere/ListeUtilisateursHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from encheresIDL.idl
* mercredi 18 mars 2015 00 h 52 CET
*/

public final class ListeUtilisateursHolder implements org.omg.CORBA.portable.Streamable
{
  public Enchere.Utilisateur value[] = null;

  public ListeUtilisateursHolder ()
  {
  }

  public ListeUtilisateursHolder (Enchere.Utilisateur[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = Enchere.ListeUtilisateursHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    Enchere.ListeUtilisateursHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return Enchere.ListeUtilisateursHelper.type ();
  }

}