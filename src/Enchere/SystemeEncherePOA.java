package Enchere;


/**
* Enchere/SystemeEncherePOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from encheresIDL.idl
* mercredi 18 mars 2015 00 h 52 CET
*/

public abstract class SystemeEncherePOA extends org.omg.PortableServer.Servant
 implements Enchere.SystemeEnchereOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("_get_tousLesProduits", new java.lang.Integer (0));
    _methods.put ("_set_tousLesProduits", new java.lang.Integer (1));
    _methods.put ("_get_tousLesUtilisateurs", new java.lang.Integer (2));
    _methods.put ("_set_tousLesUtilisateurs", new java.lang.Integer (3));
    _methods.put ("consulterListeProduits", new java.lang.Integer (4));
    _methods.put ("rechercherProduit", new java.lang.Integer (5));
    _methods.put ("demanderNotificationEnchereEnCours", new java.lang.Integer (6));
    _methods.put ("creerCompte", new java.lang.Integer (7));
    _methods.put ("seConnecter", new java.lang.Integer (8));
    _methods.put ("getAcheteurProduit", new java.lang.Integer (9));
    _methods.put ("publierProduit", new java.lang.Integer (10));
    _methods.put ("proposerPrix", new java.lang.Integer (11));
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
       case 0:  // Enchere/SystemeEnchere/_get_tousLesProduits
       {
         Enchere.Produit $result[] = null;
         $result = this.tousLesProduits ();
         out = $rh.createReply();
         Enchere.ListeProduitsHelper.write (out, $result);
         break;
       }

       case 1:  // Enchere/SystemeEnchere/_set_tousLesProduits
       {
         Enchere.Produit newTousLesProduits[] = Enchere.ListeProduitsHelper.read (in);
         this.tousLesProduits (newTousLesProduits);
         out = $rh.createReply();
         break;
       }

       case 2:  // Enchere/SystemeEnchere/_get_tousLesUtilisateurs
       {
         Enchere.Utilisateur $result[] = null;
         $result = this.tousLesUtilisateurs ();
         out = $rh.createReply();
         Enchere.ListeUtilisateursHelper.write (out, $result);
         break;
       }

       case 3:  // Enchere/SystemeEnchere/_set_tousLesUtilisateurs
       {
         Enchere.Utilisateur newTousLesUtilisateurs[] = Enchere.ListeUtilisateursHelper.read (in);
         this.tousLesUtilisateurs (newTousLesUtilisateurs);
         out = $rh.createReply();
         break;
       }

       case 4:  // Enchere/SystemeEnchere/consulterListeProduits
       {
         Enchere.Produit $result[] = null;
         $result = this.consulterListeProduits ();
         out = $rh.createReply();
         Enchere.ListeProduitsHelper.write (out, $result);
         break;
       }

       case 5:  // Enchere/SystemeEnchere/rechercherProduit
       {
         try {
           String critere = in.read_string ();
           Enchere.Produit $result[] = null;
           $result = this.rechercherProduit (critere);
           out = $rh.createReply();
           Enchere.ListeProduitsHelper.write (out, $result);
         } catch (Enchere.ProduitExistePas $ex) {
           out = $rh.createExceptionReply ();
           Enchere.ProduitExistePasHelper.write (out, $ex);
         }
         break;
       }


  /* attribut HashMap qui permet de stocker pour chaque produit la correspondance entre les acheteurs et les prix proposés*/
       case 6:  // Enchere/SystemeEnchere/demanderNotificationEnchereEnCours
       {
         Enchere.Utilisateur user = Enchere.UtilisateurHelper.read (in);
         Enchere.Produit produit = Enchere.ProduitHelper.read (in);
         Enchere.Acheteur_Vendeur ior = Enchere.Acheteur_VendeurHelper.read (in);
         this.demanderNotificationEnchereEnCours (user, produit, ior);
         out = $rh.createReply();
         break;
       }

       case 7:  // Enchere/SystemeEnchere/creerCompte
       {
         String username = in.read_string ();
         String userpswd = in.read_string ();
         String userAdresse = in.read_string ();
         this.creerCompte (username, userpswd, userAdresse);
         out = $rh.createReply();
         break;
       }

       case 8:  // Enchere/SystemeEnchere/seConnecter
       {
         String username = in.read_string ();
         String userpswd = in.read_string ();
         Enchere.Utilisateur $result = null;
         $result = this.seConnecter (username, userpswd);
         out = $rh.createReply();
         Enchere.UtilisateurHelper.write (out, $result);
         break;
       }

       case 9:  // Enchere/SystemeEnchere/getAcheteurProduit
       {
         Enchere.Produit produit = Enchere.ProduitHelper.read (in);
         Enchere.Utilisateur $result = null;
         $result = this.getAcheteurProduit (produit);
         out = $rh.createReply();
         Enchere.UtilisateurHelper.write (out, $result);
         break;
       }

       case 10:  // Enchere/SystemeEnchere/publierProduit
       {
         Enchere.Utilisateur vendeur = Enchere.UtilisateurHelper.read (in);
         String nomProduit = in.read_string ();
         String categorieProduit = in.read_string ();
         String descProduit = in.read_string ();
         float prixProduit = in.read_float ();
         String dateProduit = in.read_string ();
         this.publierProduit (vendeur, nomProduit, categorieProduit, descProduit, prixProduit, dateProduit);
         out = $rh.createReply();
         break;
       }


  /* instanciation d'un objet GestionEnchere à la publication d'un nouveau produit*/
       case 11:  // Enchere/SystemeEnchere/proposerPrix
       {
         float prix = in.read_float ();
         Enchere.Utilisateur user = Enchere.UtilisateurHelper.read (in);
         Enchere.Produit produit = Enchere.ProduitHelper.read (in);
         String $result = null;
         $result = this.proposerPrix (prix, user, produit);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:Enchere/SystemeEnchere:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public SystemeEnchere _this() 
  {
    return SystemeEnchereHelper.narrow(
    super._this_object());
  }

  public SystemeEnchere _this(org.omg.CORBA.ORB orb) 
  {
    return SystemeEnchereHelper.narrow(
    super._this_object(orb));
  }


} // class SystemeEncherePOA