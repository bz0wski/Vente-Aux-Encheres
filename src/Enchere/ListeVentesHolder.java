package Enchere;


/**
* Enchere/ListeVentesHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from encheresIDL.idl
* mercredi 1 avril 2015 00 h 55 CEST
*/

public final class ListeVentesHolder implements org.omg.CORBA.portable.Streamable
{
  public Enchere.Vente value[] = null;

  public ListeVentesHolder ()
  {
  }

  public ListeVentesHolder (Enchere.Vente[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = Enchere.ListeVentesHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    Enchere.ListeVentesHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return Enchere.ListeVentesHelper.type ();
  }

}
