package Enchere;


/**
* Enchere/ListeProduitsHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from encheresIDL.idl
* mercredi 1 avril 2015 00 h 55 CEST
*/

public final class ListeProduitsHolder implements org.omg.CORBA.portable.Streamable
{
  public Enchere.Produit value[] = null;

  public ListeProduitsHolder ()
  {
  }

  public ListeProduitsHolder (Enchere.Produit[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = Enchere.ListeProduitsHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    Enchere.ListeProduitsHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return Enchere.ListeProduitsHelper.type ();
  }

}
