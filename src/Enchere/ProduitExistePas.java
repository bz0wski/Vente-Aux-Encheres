package Enchere;


/**
* Enchere/ProduitExistePas.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from encheresIDL.idl
* mercredi 1 avril 2015 00 h 55 CEST
*/

public final class ProduitExistePas extends org.omg.CORBA.UserException
{
  public String raison = null;

  public ProduitExistePas ()
  {
    super(ProduitExistePasHelper.id());
  } // ctor

  public ProduitExistePas (String _raison)
  {
    super(ProduitExistePasHelper.id());
    raison = _raison;
  } // ctor


  public ProduitExistePas (String $reason, String _raison)
  {
    super(ProduitExistePasHelper.id() + "  " + $reason);
    raison = _raison;
  } // ctor

} // class ProduitExistePas
