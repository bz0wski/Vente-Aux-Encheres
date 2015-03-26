package Enchere;


/**
* Enchere/Acheteur_VendeurPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from encheresIDL.idl
* lundi 23 mars 2015 14 h 40 CET
*/

public abstract class Acheteur_VendeurPOA extends org.omg.PortableServer.Servant
 implements Enchere.Acheteur_VendeurOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("_get_listeproduits", new java.lang.Integer (0));
    _methods.put ("_set_listeproduits", new java.lang.Integer (1));
    _methods.put ("recevoirNotification", new java.lang.Integer (2));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // Enchere/Acheteur_Vendeur/_get_listeproduits
       {
         Enchere.Produit $result[] = null;
         $result = this.listeproduits ();
         out = $rh.createReply();
         Enchere.ListeProduitsHelper.write (out, $result);
         break;
       }

       case 1:  // Enchere/Acheteur_Vendeur/_set_listeproduits
       {
         Enchere.Produit newListeproduits[] = Enchere.ListeProduitsHelper.read (in);
         this.listeproduits (newListeproduits);
         out = $rh.createReply();
         break;
       }

       case 2:  // Enchere/Acheteur_Vendeur/recevoirNotification
       {
         String message = in.read_string ();
         this.recevoirNotification (message);
         out = $rh.createReply();
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:Enchere/Acheteur_Vendeur:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public Acheteur_Vendeur _this() 
  {
    return Acheteur_VendeurHelper.narrow(
    super._this_object());
  }

  public Acheteur_Vendeur _this(org.omg.CORBA.ORB orb) 
  {
    return Acheteur_VendeurHelper.narrow(
    super._this_object(orb));
  }


} // class Acheteur_VendeurPOA
