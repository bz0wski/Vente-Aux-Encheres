package Enchere;


/**
* Enchere/ListeClientsHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from encheresIDL.idl
* mercredi 1 avril 2015 16 h 19 CEST
*/

public final class ListeClientsHolder implements org.omg.CORBA.portable.Streamable
{
  public Enchere.ClientUpdate value[] = null;

  public ListeClientsHolder ()
  {
  }

  public ListeClientsHolder (Enchere.ClientUpdate[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = Enchere.ListeClientsHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    Enchere.ListeClientsHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return Enchere.ListeClientsHelper.type ();
  }

}