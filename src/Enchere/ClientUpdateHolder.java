package Enchere;

/**
* Enchere/ClientUpdateHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from encheresIDL.idl
* mercredi 1 avril 2015 16 h 31 CEST
*/

public final class ClientUpdateHolder implements org.omg.CORBA.portable.Streamable
{
  public Enchere.ClientUpdate value = null;

  public ClientUpdateHolder ()
  {
  }

  public ClientUpdateHolder (Enchere.ClientUpdate initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = Enchere.ClientUpdateHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    Enchere.ClientUpdateHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return Enchere.ClientUpdateHelper.type ();
  }

}
