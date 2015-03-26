package ui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

import utility.Proposition;
import Enchere.Produit;
import Enchere.SystemeEnchere;
import Enchere.Utilisateur;
import Enchere.pas;

public class EncherirWindow extends Dialog {

	protected Object result;
	protected Shell shell;

	private Text prixencours;
	private Text votrePrix;
	
	private final Produit produit;
	private final SystemeEnchere systemeEnchere;
	private final Utilisateur utilisateur;

	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	Proposition proposition =  new Proposition();
	
	private IStatus status;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public EncherirWindow(Shell parent, Produit produit, SystemeEnchere systemeEnchere, Utilisateur user) {
		super(parent);		
		this.produit = produit;

		setText(this.produit.nom);

		this.systemeEnchere = systemeEnchere;

		this.utilisateur = user;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		//shell = new Shell(getParent(), getStyle());
		shell = new Shell(getParent(), SWT.APPLICATION_MODAL|SWT.DIALOG_TRIM);
		shell.setSize(450, 300);
		//shell.setText(getText());
		shell.setLocation(getParent().getLocation());
		FormLayout layout = new FormLayout();
		layout.marginRight = 5;
		layout.marginBottom = 5;
		shell.setLayout(layout);


		// create the binding context
		DataBindingContext ctx = new DataBindingContext();

		Label lblPrixencours = new Label(shell, SWT.NONE);
		FormData fd_lblPrixencours = new FormData();
		fd_lblPrixencours.top = new FormAttachment(0, 10);
		fd_lblPrixencours.left = new FormAttachment(0, 10);
		lblPrixencours.setLayoutData(fd_lblPrixencours);
		lblPrixencours.setText("Prix en cours");

		prixencours = new Text(shell, SWT.BORDER | SWT.READ_ONLY);
		FormData fd_prixencours = new FormData();
		fd_prixencours.left = new FormAttachment(lblPrixencours, 40);
		fd_prixencours.top = new FormAttachment(0, 10);
		prixencours.setLayoutData(fd_prixencours);

		//modifier l'affichage du prix
		BigDecimal bd = new BigDecimal(Double.toString(produit.prix_depart));
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP); 
		prixencours.setText(bd.toString());

		Label lblVotrePrix = new Label(shell, SWT.NONE);
		FormData fd_lblVotrePrix = new FormData();
		fd_lblVotrePrix.top = new FormAttachment(lblPrixencours, 21);
		fd_lblVotrePrix.left = new FormAttachment(0, 10);
		lblVotrePrix.setLayoutData(fd_lblVotrePrix);
		lblVotrePrix.setText("Votre proposition");

		votrePrix = new Text(shell, SWT.BORDER);
		FormData fd_votrePrix = new FormData();
		fd_votrePrix.bottom = new FormAttachment(lblVotrePrix, 0, SWT.BOTTOM);
		fd_votrePrix.left = new FormAttachment(lblVotrePrix, 5, SWT.RIGHT);
		votrePrix.setLayoutData(fd_votrePrix);

		Button button = new Button(shell, SWT.PUSH);
		button.setText("Valider");
		button.setEnabled(false);
		FormData fd_button = new FormData();

		fd_button.right = new FormAttachment(votrePrix, 0, SWT.RIGHT);
		fd_button.top = new FormAttachment(votrePrix, 7, SWT.BOTTOM);
		button.setLayoutData(fd_button);

		button.addListener(SWT.Selection, listener->{
			if (status == ValidationStatus.ok()) {
				Double prix = Double.parseDouble(proposition.getProposition());
				
				systemeEnchere.proposerPrix(prix, utilisateur, produit);
				shell.close();
				shell.dispose();
			}
			

		});
	
		Listener l = listener->{
			System.out.println(status);
			if (status == ValidationStatus.OK_STATUS) {
				if(!proposition.getProposition().isEmpty())
					button.setEnabled(true);
			}
			else {
				button.setEnabled(false);
			}
		};
		
		votrePrix.addListener(SWT.Modify, l);
		
		
		/////////////////////////////////////////////Prix Validation////////////////////////////////////////////////////////////
		//Adding a validator so that only a the right sum will be accepted
		IValidator prixValidator = new IValidator() {
			@Override
			public IStatus validate(Object value) {
				if (value instanceof String) {
					String s = String.valueOf(value).trim();
					try {
					    Double t_prixDouble = Double.parseDouble(s);
					    
					    if (t_prixDouble < produit.prix_depart*pas.value) {					    	
					    	setStatus(ValidationStatus.error("Votre proposition est trop base"));
					    	ValidationStatus.error("Votre proposition est trop base");
						}
						
						status = ValidationStatus.ok();
						return ValidationStatus.ok();
					} catch (Exception e) {
						if (e instanceof NumberFormatException) {					
							setStatus(ValidationStatus.error("Prix Invalide"));
							return ValidationStatus.error("Prix Invalide");
						}
					}
					
				}

				status = ValidationStatus.error("Prix Invalide");
				return ValidationStatus.error("Prix Invalide");
			}
		}; 

		IObservableValue prixtarget = WidgetProperties.
				text(SWT.Modify).observe(votrePrix);
		IObservableValue prixmodel = BeanProperties.value(Proposition.class, "proposition").
				observe(proposition);
		UpdateValueStrategy prixstrategy = new UpdateValueStrategy();
		prixstrategy.setBeforeSetValidator(prixValidator);



		//Binding the validator to the value and update strategy
		Binding prixbinding = ctx.bindValue(prixtarget, prixmodel,prixstrategy,null);

		//Adding some decoration
		ControlDecorationSupport.create(prixbinding, SWT.RIGHT|SWT.TOP);	

		shell.pack();
	}

	/**
	 * @return the status
	 */
	public IStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(IStatus status) {
		this.status = status;
	}

	/**
	 * Adds basic JavaBeans support  
	 * @param propertyName : The name of the property to which to add the {@code PropertyChangeListener}.
	 * @param listener : The {@code PropertyChangeListener} to add
	 */
	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}
	/**
	 * Removes basic JavaBeans support  
	 * @param listener : The {@code PropertyChangeListener} to remove.
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

}
