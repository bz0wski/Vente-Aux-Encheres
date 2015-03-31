package Enchere;


/**
* Enchere/_SystemeEnchereStub.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from encheresIDL.idl
* mercredi 1 avril 2015 00 h 55 CEST
*/

public class _SystemeEnchereStub extends org.omg.CORBA.portable.ObjectImpl implements Enchere.SystemeEnchere
{

  public Enchere.Produit[] tousLesProduits ()
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("_get_tousLesProduits", true);
                $in = _invoke ($out);
                Enchere.Produit $result[] = Enchere.ListeProduitsHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return tousLesProduits (        );
            } finally {
                _releaseReply ($in);
            }
  } // tousLesProduits

  public void tousLesProduits (Enchere.Produit[] newTousLesProduits)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("_set_tousLesProduits", true);
                Enchere.ListeProduitsHelper.write ($out, newTousLesProduits);
                $in = _invoke ($out);
                return;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                tousLesProduits (newTousLesProduits        );
            } finally {
                _releaseReply ($in);
            }
  } // tousLesProduits

  public Enchere.Utilisateur[] tousLesUtilisateurs ()
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("_get_tousLesUtilisateurs", true);
                $in = _invoke ($out);
                Enchere.Utilisateur $result[] = Enchere.ListeUtilisateursHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return tousLesUtilisateurs (        );
            } finally {
                _releaseReply ($in);
            }
  } // tousLesUtilisateurs

  public void tousLesUtilisateurs (Enchere.Utilisateur[] newTousLesUtilisateurs)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("_set_tousLesUtilisateurs", true);
                Enchere.ListeUtilisateursHelper.write ($out, newTousLesUtilisateurs);
                $in = _invoke ($out);
                return;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                tousLesUtilisateurs (newTousLesUtilisateurs        );
            } finally {
                _releaseReply ($in);
            }
  } // tousLesUtilisateurs

  public Enchere.Vente[] tousLesVentesEncours ()
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("_get_tousLesVentesEncours", true);
                $in = _invoke ($out);
                Enchere.Vente $result[] = Enchere.ListeVentesHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return tousLesVentesEncours (        );
            } finally {
                _releaseReply ($in);
            }
  } // tousLesVentesEncours

  public void tousLesVentesEncours (Enchere.Vente[] newTousLesVentesEncours)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("_set_tousLesVentesEncours", true);
                Enchere.ListeVentesHelper.write ($out, newTousLesVentesEncours);
                $in = _invoke ($out);
                return;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                tousLesVentesEncours (newTousLesVentesEncours        );
            } finally {
                _releaseReply ($in);
            }
  } // tousLesVentesEncours

  public Enchere.Produit[] consulterListeProduits ()
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("consulterListeProduits", true);
                $in = _invoke ($out);
                Enchere.Produit $result[] = Enchere.ListeProduitsHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return consulterListeProduits (        );
            } finally {
                _releaseReply ($in);
            }
  } // consulterListeProduits

  public Enchere.Produit[] rechercherProduit (String critere) throws Enchere.ProduitExistePas
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("rechercherProduit", true);
                $out.write_string (critere);
                $in = _invoke ($out);
                Enchere.Produit $result[] = Enchere.ListeProduitsHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                if (_id.equals ("IDL:Enchere/ProduitExistePas:1.0"))
                    throw Enchere.ProduitExistePasHelper.read ($in);
                else
                    throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return rechercherProduit (critere        );
            } finally {
                _releaseReply ($in);
            }
  } // rechercherProduit


  /* attribut HashMap qui permet de stocker pour chaque produit la correspondance entre les acheteurs et les prix proposés*/
  public boolean demanderNotificationEnchereEnCours (Enchere.Utilisateur user, Enchere.Produit produit, Enchere.Acheteur_Vendeur acheteur_VendeurIOR)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("demanderNotificationEnchereEnCours", true);
                Enchere.UtilisateurHelper.write ($out, user);
                Enchere.ProduitHelper.write ($out, produit);
                Enchere.Acheteur_VendeurHelper.write ($out, acheteur_VendeurIOR);
                $in = _invoke ($out);
                boolean $result = $in.read_boolean ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return demanderNotificationEnchereEnCours (user, produit, acheteur_VendeurIOR        );
            } finally {
                _releaseReply ($in);
            }
  } // demanderNotificationEnchereEnCours

  public boolean creerCompte (String username, String userpswd, String userAdresse)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("creerCompte", true);
                $out.write_string (username);
                $out.write_string (userpswd);
                $out.write_string (userAdresse);
                $in = _invoke ($out);
                boolean $result = $in.read_boolean ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return creerCompte (username, userpswd, userAdresse        );
            } finally {
                _releaseReply ($in);
            }
  } // creerCompte

  public Enchere.Utilisateur seConnecter (String username, String userpswd)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("seConnecter", true);
                $out.write_string (username);
                $out.write_string (userpswd);
                $in = _invoke ($out);
                Enchere.Utilisateur $result = Enchere.UtilisateurHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return seConnecter (username, userpswd        );
            } finally {
                _releaseReply ($in);
            }
  } // seConnecter

  public Enchere.Utilisateur getAcheteurProduit (Enchere.Produit produit)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("getAcheteurProduit", true);
                Enchere.ProduitHelper.write ($out, produit);
                $in = _invoke ($out);
                Enchere.Utilisateur $result = Enchere.UtilisateurHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return getAcheteurProduit (produit        );
            } finally {
                _releaseReply ($in);
            }
  } // getAcheteurProduit

  public Enchere.Produit publierProduit (Enchere.Utilisateur vendeur, String nomProduit, String categorieProduit, String descProduit, double prixProduit, String dateProduit, Enchere.SystemeEnchere ior)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("publierProduit", true);
                Enchere.UtilisateurHelper.write ($out, vendeur);
                $out.write_string (nomProduit);
                $out.write_string (categorieProduit);
                $out.write_string (descProduit);
                $out.write_double (prixProduit);
                $out.write_string (dateProduit);
                Enchere.SystemeEnchereHelper.write ($out, ior);
                $in = _invoke ($out);
                Enchere.Produit $result = Enchere.ProduitHelper.read ($in);
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return publierProduit (vendeur, nomProduit, categorieProduit, descProduit, prixProduit, dateProduit, ior        );
            } finally {
                _releaseReply ($in);
            }
  } // publierProduit


  /* instanciation d'un objet GestionEnchere à la publication d'un nouveau produit*/
  public String proposerPrix (double prix, Enchere.Utilisateur user, Enchere.Produit produit)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("proposerPrix", true);
                $out.write_double (prix);
                Enchere.UtilisateurHelper.write ($out, user);
                Enchere.ProduitHelper.write ($out, produit);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return proposerPrix (prix, user, produit        );
            } finally {
                _releaseReply ($in);
            }
  } // proposerPrix


  /*Cette methode permettra d'enlever une vente terminée de la liste des ventes en cours */
  public void supprimerVente (Enchere.Vente vente)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("supprimerVente", true);
                Enchere.VenteHelper.write ($out, vente);
                $in = _invoke ($out);
                return;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                supprimerVente (vente        );
            } finally {
                _releaseReply ($in);
            }
  } // supprimerVente

  public void enleverVente (String venteID)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("enleverVente", true);
                $out.write_string (venteID);
                $in = _invoke ($out);
                return;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                enleverVente (venteID        );
            } finally {
                _releaseReply ($in);
            }
  } // enleverVente

  public String statistiques ()
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("statistiques", true);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return statistiques (        );
            } finally {
                _releaseReply ($in);
            }
  } // statistiques

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:Enchere/SystemeEnchere:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }

  private void readObject (java.io.ObjectInputStream s) throws java.io.IOException
  {
     String str = s.readUTF ();
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     org.omg.CORBA.Object obj = orb.string_to_object (str);
     org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate ();
     _set_delegate (delegate);
   } finally {
     orb.destroy() ;
   }
  }

  private void writeObject (java.io.ObjectOutputStream s) throws java.io.IOException
  {
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     String str = orb.object_to_string (this);
     s.writeUTF (str);
   } finally {
     orb.destroy() ;
   }
  }
} // class _SystemeEnchereStub
