package Enchere;


/**
* Enchere/ArchivageHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from encheresIDL.idl
* mercredi 1 avril 2015 00 h 55 CEST
*/

abstract public class ArchivageHelper
{
  private static String  _id = "IDL:Enchere/Archivage:1.0";

  public static void insert (org.omg.CORBA.Any a, Enchere.Archivage that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static Enchere.Archivage extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (Enchere.ArchivageHelper.id (), "Archivage");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static Enchere.Archivage read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_ArchivageStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, Enchere.Archivage value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static Enchere.Archivage narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof Enchere.Archivage)
      return (Enchere.Archivage)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      Enchere._ArchivageStub stub = new Enchere._ArchivageStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static Enchere.Archivage unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof Enchere.Archivage)
      return (Enchere.Archivage)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      Enchere._ArchivageStub stub = new Enchere._ArchivageStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
