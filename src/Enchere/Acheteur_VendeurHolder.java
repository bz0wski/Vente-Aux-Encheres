package Enchere;

/**
* Enchere/Acheteur_VendeurHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from encheresIDL.idl
* mercredi 1 avril 2015 00 h 55 CEST
*/

public final class Acheteur_VendeurHolder implements org.omg.CORBA.portable.Streamable
{
  public Enchere.Acheteur_Vendeur value = null;

  public Acheteur_VendeurHolder ()
  {
  }

  public Acheteur_VendeurHolder (Enchere.Acheteur_Vendeur initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = Enchere.Acheteur_VendeurHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    Enchere.Acheteur_VendeurHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return Enchere.Acheteur_VendeurHelper.type ();
  }

}
