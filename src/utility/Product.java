/**
 * 
 */
package utility;

import java.beans.PropertyChangeSupport;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Salim AHMED
 *
 */
public class Product extends MyAbstractModelObject{

	private Date date;
	private String nomProduit;
	private String catProduit;
	private String descProduit;
	private String prixProduit;
	
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	public Product() {
		date = new Date();
	
		nomProduit = "";
		catProduit = "";
		descProduit = "";
		prixProduit = "";
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		Date oldValue = this.date;
		this.date = date;
		propertyChangeSupport.firePropertyChange("date", oldValue, date);
	}

	/**
	 * @return the nomProduit
	 */
	public String getNomProduit() {
		return nomProduit;
	}

	/**
	 * @param nomProduit the nomProduit to set
	 */
	public void setNomProduit(String nomProduit) {
		String oldValue = this.nomProduit;
		this.nomProduit = nomProduit;
		propertyChangeSupport.firePropertyChange("nomProduit", oldValue, nomProduit);
	}

	/**
	 * @return the catProduit
	 */
	public String getCatProduit() {
		return catProduit;
	}

	/**
	 * @param catProduit the catProduit to set
	 */
	public void setCatProduit(String catProduit) {
		String oldValue = this.catProduit;
		this.catProduit = catProduit;
		propertyChangeSupport.firePropertyChange("catProduit", oldValue, catProduit);
	}

	/**
	 * @return the descProduit
	 */
	public String getDescProduit() {
		return descProduit;
	}

	/**
	 * @param descProduit the descProduit to set
	 */
	public void setDescProduit(String descProduit) {
		String oldValue = this.descProduit; 
		this.descProduit = descProduit;
		propertyChangeSupport.firePropertyChange("descProduit", oldValue, descProduit);
	}

	/**
	 * @return the prixProduit
	 */
	public String getPrixProduit() {
		return prixProduit;
	}

	/**
	 * @param prixProduit the prixProduit to set
	 */
	public void setPrixProduit(String prixProduit) {
		String oldValue = this.prixProduit;
		this.prixProduit = prixProduit;
		propertyChangeSupport.firePropertyChange("prixProduit", oldValue, prixProduit);
	}
	
	
}
