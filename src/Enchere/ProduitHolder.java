package Enchere;

/**
* Enchere/ProduitHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from encheresIDL.idl
* mercredi 1 avril 2015 16 h 31 CEST
*/

public final class ProduitHolder implements org.omg.CORBA.portable.Streamable
{
  public Enchere.Produit value = null;

  public ProduitHolder ()
  {
  }

  public ProduitHolder (Enchere.Produit initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = Enchere.ProduitHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    Enchere.ProduitHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return Enchere.ProduitHelper.type ();
  }

}
