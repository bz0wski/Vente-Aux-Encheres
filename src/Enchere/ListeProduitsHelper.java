package Enchere;


/**
* Enchere/ListeProduitsHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from encheresIDL.idl
* lundi 23 mars 2015 14 h 40 CET
*/

abstract public class ListeProduitsHelper
{
  private static String  _id = "IDL:Enchere/ListeProduits:1.0";

  public static void insert (org.omg.CORBA.Any a, Enchere.Produit[] that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static Enchere.Produit[] extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = Enchere.ProduitHelper.type ();
      __typeCode = org.omg.CORBA.ORB.init ().create_sequence_tc (0, __typeCode);
      __typeCode = org.omg.CORBA.ORB.init ().create_alias_tc (Enchere.ListeProduitsHelper.id (), "ListeProduits", __typeCode);
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static Enchere.Produit[] read (org.omg.CORBA.portable.InputStream istream)
  {
    Enchere.Produit value[] = null;
    int _len0 = istream.read_long ();
    value = new Enchere.Produit[_len0];
    for (int _o1 = 0;_o1 < value.length; ++_o1)
      value[_o1] = Enchere.ProduitHelper.read (istream);
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, Enchere.Produit[] value)
  {
    ostream.write_long (value.length);
    for (int _i0 = 0;_i0 < value.length; ++_i0)
      Enchere.ProduitHelper.write (ostream, value[_i0]);
  }

}
