package Enchere;


/**
* Enchere/ListeVentesHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from encheresIDL.idl
* lundi 23 mars 2015 14 h 40 CET
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
