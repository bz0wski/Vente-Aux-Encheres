package Enchere;


/**
* Enchere/SystemeEnchereHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from encheresIDL.idl
* jeudi 19 mars 2015 01 h 47 CET
*/

abstract public class SystemeEnchereHelper
{
  private static String  _id = "IDL:Enchere/SystemeEnchere:1.0";

  public static void insert (org.omg.CORBA.Any a, Enchere.SystemeEnchere that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static Enchere.SystemeEnchere extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (Enchere.SystemeEnchereHelper.id (), "SystemeEnchere");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static Enchere.SystemeEnchere read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_SystemeEnchereStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, Enchere.SystemeEnchere value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static Enchere.SystemeEnchere narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof Enchere.SystemeEnchere)
      return (Enchere.SystemeEnchere)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      Enchere._SystemeEnchereStub stub = new Enchere._SystemeEnchereStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static Enchere.SystemeEnchere unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof Enchere.SystemeEnchere)
      return (Enchere.SystemeEnchere)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      Enchere._SystemeEnchereStub stub = new Enchere._SystemeEnchereStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
