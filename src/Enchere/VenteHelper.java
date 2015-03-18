package Enchere;


/**
* Enchere/VenteHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from encheresIDL.idl
* mercredi 18 mars 2015 00 h 52 CET
*/

abstract public class VenteHelper
{
  private static String  _id = "IDL:Enchere/Vente:1.0";

  public static void insert (org.omg.CORBA.Any a, Enchere.Vente that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static Enchere.Vente extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      synchronized (org.omg.CORBA.TypeCode.class)
      {
        if (__typeCode == null)
        {
          if (__active)
          {
            return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );
          }
          __active = true;
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [5];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[0] = new org.omg.CORBA.StructMember (
            "idVente",
            _tcOf_members0,
            null);
          _tcOf_members0 = Enchere.ProduitHelper.type ();
          _members0[1] = new org.omg.CORBA.StructMember (
            "produitVendu",
            _tcOf_members0,
            null);
          _tcOf_members0 = Enchere.UtilisateurHelper.type ();
          _members0[2] = new org.omg.CORBA.StructMember (
            "acheteur",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[3] = new org.omg.CORBA.StructMember (
            "debutVente",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[4] = new org.omg.CORBA.StructMember (
            "finVente",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (Enchere.VenteHelper.id (), "Vente", _members0);
          __active = false;
        }
      }
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static Enchere.Vente read (org.omg.CORBA.portable.InputStream istream)
  {
    Enchere.Vente value = new Enchere.Vente ();
    value.idVente = istream.read_string ();
    value.produitVendu = Enchere.ProduitHelper.read (istream);
    value.acheteur = Enchere.UtilisateurHelper.read (istream);
    value.debutVente = istream.read_string ();
    value.finVente = istream.read_string ();
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, Enchere.Vente value)
  {
    ostream.write_string (value.idVente);
    Enchere.ProduitHelper.write (ostream, value.produitVendu);
    Enchere.UtilisateurHelper.write (ostream, value.acheteur);
    ostream.write_string (value.debutVente);
    ostream.write_string (value.finVente);
  }

}