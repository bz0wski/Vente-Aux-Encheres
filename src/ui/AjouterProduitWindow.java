package ui;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.DateAndTimeObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import utility.Product;
import Enchere.Acheteur_Vendeur;
import Enchere.Produit;
import Enchere.SystemeEnchere;
import Enchere.Utilisateur;

public class AjouterProduitWindow extends Dialog{

	protected Object result;
	protected Shell shell;

	private Text nomProduitText;
	private Text categorie;
	private Text prixText;
	private Text descriptionText;


	private Produit produit;

	private Product product = new Product();

	private final SystemeEnchere systemeEnchere;
	private final Utilisateur utilisateur;
	private final Acheteur_Vendeur acheteur_Vendeur;

	private boolean receiveNotifications = false;
	
	private IStatus nomStatus;
	private IStatus catStatus;
	private IStatus dateTimeStatus;
	private IStatus prixStatus;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param systeme
	 * @param user
	 */
	public AjouterProduitWindow(Shell parent, SystemeEnchere systeme, Utilisateur user, Acheteur_Vendeur ior) {
		super(parent);
		setText("Ajouter Nouveau Produit");
		this.systemeEnchere = systeme;
		this.utilisateur = user;
		this.acheteur_Vendeur = ior;
	}



	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		//Display display = getParent().getDisplay();
		Display display = Display.getDefault();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return produit;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		//SWT.DIALOG_TRIM | SWT.RESIZE
		//shell = new Shell(getParent(), getStyle());
		shell = new Shell(getParent(), SWT.APPLICATION_MODAL|SWT.DIALOG_TRIM);
		shell.setLocation(getParent().getLocation());
		shell.setSize(322, 250);
		//shell.setText(getText());
		shell.setLayout(new FormLayout());

		Composite composite = new Composite(shell, SWT.NONE);

		FormLayout fl_composite = new FormLayout();
		fl_composite.marginHeight = 5;
		fl_composite.marginWidth = 5;
		composite.setLayout(fl_composite);
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100);
		fd_composite.right = new FormAttachment(100);
		fd_composite.top = new FormAttachment(0);
		fd_composite.left = new FormAttachment(0);
		composite.setLayoutData(fd_composite);



		Label lblNomProduit = new Label(composite, SWT.NONE);
		FormData fd_lblNomProduit = new FormData();
		fd_lblNomProduit.top = new FormAttachment(0);
		fd_lblNomProduit.left = new FormAttachment(0);
		lblNomProduit.setLayoutData(fd_lblNomProduit);
		lblNomProduit.setText("Nom Produit");

		nomProduitText = new Text(composite, SWT.BORDER);
		FormData fd_text = new FormData();
		fd_text.right = new FormAttachment(lblNomProduit, 100);
		fd_text.top = new FormAttachment(lblNomProduit, 6);
		fd_text.left = new FormAttachment(lblNomProduit, 0, SWT.LEFT);

		nomProduitText.setLayoutData(fd_text);

		Label lblCategorie = new Label(composite, SWT.NONE);
		FormData fd_lblCategorie = new FormData();
		fd_lblCategorie.bottom = new FormAttachment(lblNomProduit, 0, SWT.BOTTOM);
		fd_lblCategorie.left = new FormAttachment(nomProduitText, 15);
		lblCategorie.setLayoutData(fd_lblCategorie);
		lblCategorie.setText("Categorie");

		categorie = new Text(composite, SWT.BORDER);

		FormData fd_categorie = new FormData();
		fd_categorie.top = new FormAttachment(lblCategorie, 6);
		fd_categorie.right = new FormAttachment(lblCategorie, 100);
		fd_categorie.left = new FormAttachment(lblCategorie,0,SWT.LEFT);
		categorie.setLayoutData(fd_categorie);

		Label lblPrix = new Label(composite, SWT.NONE);
		FormData fd_lblPrix = new FormData();
		fd_lblPrix.bottom = new FormAttachment(lblNomProduit, 0, SWT.BOTTOM);
		fd_lblPrix.left = new FormAttachment(categorie, 10, SWT.RIGHT);
		lblPrix.setLayoutData(fd_lblPrix);
		lblPrix.setText("Prix");

		prixText = new Text(composite, SWT.BORDER);
		FormData fd_prixText = new FormData();
		fd_prixText.top = new FormAttachment(lblPrix, 6);
		fd_prixText.left = new FormAttachment(lblPrix, 0,SWT.LEFT);
		fd_prixText.right = new FormAttachment(100, -3);
		prixText.setLayoutData(fd_prixText);

		Label lblDateFin = new Label(composite, SWT.NONE);
		FormData fd_lblDateFin = new FormData();
		fd_lblDateFin.top = new FormAttachment(nomProduitText, 2);
		fd_lblDateFin.left = new FormAttachment(0);
		lblDateFin.setLayoutData(fd_lblDateFin);
		lblDateFin.setText("Date Fin");

		// Date Selection as a drop-down
		DateTime dateFin = new DateTime(composite, SWT.DATE | SWT.DROP_DOWN);
		FormData fd_dateFin = new FormData();
		fd_dateFin.left = new FormAttachment(lblDateFin,0,SWT.LEFT);
		fd_dateFin.top = new FormAttachment(lblDateFin, 6);

		dateFin.setLayoutData(fd_dateFin);

		//Time Selection right next to it
		DateTime time = new DateTime(composite, SWT.TIME|SWT.SHORT);
		FormData fd_time = new FormData();
		fd_time.left = new FormAttachment(dateFin,10,SWT.RIGHT);
		fd_time.top = new FormAttachment(lblDateFin, 6);
		time.setLayoutData(fd_time);

		//Calendar calendar = Calendar.getInstance();
		DateFormat format = DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.SHORT,  Locale.FRANCE);

		/*dateFin.addListener(SWT.Selection, listener->{
			calendar.set(Calendar.DAY_OF_MONTH, dateFin.getDay());
			calendar.set(Calendar.MONTH, dateFin.getMonth());
			calendar.set(Calendar.YEAR, dateFin.getYear());

			String dateString  = format.format(calendar.getTime());

			System.out.println("Data "+dateString);
		});

		time.addListener(SWT.Selection, listener->{
			calendar.set(Calendar.HOUR_OF_DAY, time.getHours());
			calendar.set(Calendar.MINUTE, time.getMinutes());

			String date = format.format(calendar.getTime());

			System.out.println("Time "+date);
		});*/

		Label lblDescription = new Label(composite, SWT.NONE);
		FormData fd_lblDescription = new FormData();
		fd_lblDescription.top = new FormAttachment(lblDateFin, 34);
		fd_lblDescription.left = new FormAttachment(lblNomProduit, 0, SWT.LEFT);
		lblDescription.setLayoutData(fd_lblDescription);
		lblDescription.setText("Description");

		descriptionText = new Text(composite, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);

		FormData fd_descriptionText =  new FormData();
		fd_descriptionText.left = new FormAttachment(lblDescription, 0, SWT.LEFT);
		fd_descriptionText.top = new FormAttachment(lblDescription, 6, SWT.BOTTOM);
		fd_descriptionText.right = new FormAttachment(100, 0);
		fd_descriptionText.bottom = new FormAttachment(100, -30);

		descriptionText.setLayoutData(fd_descriptionText);

		Button btnValider = new Button(composite, SWT.NONE);
		FormData fd_btnValider = new FormData();
		fd_btnValider.bottom = new FormAttachment(100, -2);
		fd_btnValider.right = new FormAttachment(descriptionText, 0, SWT.RIGHT);
		fd_btnValider.top = new FormAttachment(descriptionText, 6, SWT.BOTTOM);
		btnValider.setLayoutData(fd_btnValider);
		btnValider.setText("Valider");
		btnValider.setEnabled(false);

		///////////////////////// Valider Listener///////////////////////////////////////////////////////////////////////////
		btnValider.addListener(SWT.Selection, listener->{

			Double prix = Double.valueOf(product.getPrixProduit());
			String date = format.format(product.getDate());

			produit = systemeEnchere.publierProduit(utilisateur, product.getNomProduit(), product.getCatProduit(),
					product.getDescProduit(), prix, date , systemeEnchere);
			if (receiveNotifications) {
				if(systemeEnchere.demanderNotificationEnchereEnCours(utilisateur, produit, acheteur_Vendeur))
					System.out.println("DEMANDER NOTIFICATIONS");
			}
			
			shell.close();
			shell.dispose();

		});

		Listener l = listener->{
			if (nomStatus == ValidationStatus.OK_STATUS && catStatus == ValidationStatus.OK_STATUS 
					&& prixStatus == ValidationStatus.OK_STATUS && dateTimeStatus == ValidationStatus.OK_STATUS) {
				btnValider.setEnabled(true);
			}
			else {
				btnValider.setEnabled(false);
			}
		};

		//only enable button if status is OK
		nomProduitText.addListener(SWT.Modify, l);
		categorie.addListener(SWT.Modify, l);
		prixText.addListener(SWT.Modify, l);
		//descriptionText.addListener(SWT.Modify, l);

		dateFin.addListener(SWT.Selection, l);
		time.addListener(SWT.Selection, l);
		// create the binding context
		DataBindingContext ctx = new DataBindingContext();

		IObservableValue datetarget = WidgetProperties.
				selection().observe(dateFin);

		//use the same validation strategy for the time too
		IObservableValue timetarget = WidgetProperties.
				selection().observe(time);

		IObservableValue model = BeanProperties.value(Product.class, "date").
				observe(product);

		//Adding a validator so that only a proper date atleast five minutes into the future can be used.
		IValidator dateValidator = new IValidator() {
			@Override
			public IStatus validate(Object value) {
				try {										
					Date dateEnd = (Date)value;

					if (dateEnd.before(new Date())) {
						dateTimeStatus = ValidationStatus.error("Date fin ne peut pas etre inférieure, il doit etre au moins 5mins dans le futur");
						return ValidationStatus.error("Date fin ne peut pas etre inférieure, il doit etre au moins 5mins dans le futur");					
					}

					//Date dateEnd = product.getDate();

					Date now = new Date();
					//String nowstr = format.format(now);
					//now = format.parse(nowstr);


					Calendar calendar = Calendar.getInstance();
					calendar.setTime(now);
					calendar.add(Calendar.MINUTE, 5);
					now = calendar.getTime();

					//System.out.println("Date i set "+format.format(dateEnd)+"\n\nNow + five mins : "+format.format(now));

					if (dateEnd.after(now)) {
					
						dateTimeStatus = (ValidationStatus.ok());
						//product.setDate(dateEnd); 
						return ValidationStatus.ok();
					}
					else {
						//System.out.println("\nNOT AFTER\n");
						dateTimeStatus = ValidationStatus.error("Date fin ne peut pas etre inférieure, il doit etre au moins 5mins dans le futur");

						return ValidationStatus.error("Date fin ne peut pas etre inférieure, il doit etre au moins 5mins dans le futur");

					}


				} catch (Exception e) {
					e.getMessage();
					e.printStackTrace();
				}

				dateTimeStatus = ValidationStatus.error("Date Invalide");
				return ValidationStatus.error("Date Invalide");
			}
		};


		UpdateValueStrategy strategy = new UpdateValueStrategy();
		strategy.setAfterGetValidator(dateValidator);

		//Binding the validator to the value and update strategy
		Binding binding = ctx.bindValue(new DateAndTimeObservableValue(datetarget, timetarget), model,strategy,null);

		//Adding some decoration
		ControlDecorationSupport.create(binding, SWT.RIGHT|SWT.TOP);

		
		IObservableValue descTarget = WidgetProperties.text(SWT.Modify).observe(descriptionText);
		IObservableValue descModel = BeanProperties.value(Product.class, "descProduit").observe(product);
		
		Binding descBinding = ctx.bindValue(descTarget, descModel);

		//IObservableValue timemodel = BeanProperties.value(Product.class, "date").
		//		observe(product);
		//UpdateValueStrategy timestrategy = new UpdateValueStrategy();
		//strategy.setBeforeSetValidator(dateValidator);


		//Binding the validator to the value and update strategy
		//Binding datetimebinding = ctx.bindValue(timetarget, timemodel,timestrategy,null);

		//Adding some decoration
		//ControlDecorationSupport.create(datetimebinding, SWT.RIGHT|SWT.TOP);
		//DateTime calendar = new DateTime(composite, SWT.CALENDAR);

		//DateTime date = new DateTime(composite, SWT.DATE);



		/////////////////Adding a validator so that only a the right sum will be accepted//////////////////////////////////////////////////////////////////
		IValidator prixValidator = new IValidator() {
			@Override
			public IStatus validate(Object value) {
				if (value instanceof String) {
					String s = String.valueOf(value).trim();
					try {
						Double.parseDouble(s);
						prixStatus = ValidationStatus.ok();
						return ValidationStatus.ok();
					} catch (Exception e) {
						if (e instanceof NumberFormatException) {
							prixStatus = (ValidationStatus.error("Prix Invalide"));
							return ValidationStatus.error("Prix Invalide");
						}
					}

				}

				prixStatus = ValidationStatus.error("Prix Invalide");
				return ValidationStatus.error("Prix Invalide");
			}
		};

		IObservableValue prixtarget = WidgetProperties.
				text(SWT.Modify).observe(prixText);
		IObservableValue prixmodel = BeanProperties.value(Product.class, "prixProduit").
				observe(product);
		UpdateValueStrategy prixstrategy = new UpdateValueStrategy();
		prixstrategy.setBeforeSetValidator(prixValidator);



		//Binding the validator to the value and update strategy
		Binding prixbinding = ctx.bindValue(prixtarget, prixmodel,prixstrategy,null);

		//Adding some decoration
		ControlDecorationSupport.create(prixbinding, SWT.RIGHT|SWT.TOP);	

		///////////////////////////////////////////////Catégorie Validation////////////////////////////////////////////////////////////


		//Adding a validator so that only a proper word can be used as a categorie
		IValidator catValidator = new IValidator() {
			@Override
			public IStatus validate(Object value) {
				if (value instanceof String) {
					String s = String.valueOf(value).trim();

					if (s.matches("\\w+")) {
						catStatus = ValidationStatus.ok();
						return ValidationStatus.ok();
					}
				}
				catStatus = ValidationStatus.error("Nom Invalide");
				return ValidationStatus.error("Nom Invalide");
			}
		};

		IObservableValue cattarget = WidgetProperties.
				text(SWT.Modify).observe(categorie);
		IObservableValue catmodel = BeanProperties.value(Product.class, "catProduit").
				observe(product);

		UpdateValueStrategy catstrategy = new UpdateValueStrategy();
		catstrategy.setBeforeSetValidator(catValidator);



		//Binding the validator to the value and update strategy
		Binding catbinding = ctx.bindValue(cattarget, catmodel,catstrategy,null);

		//Adding some decoration
		ControlDecorationSupport.create(catbinding, SWT.RIGHT|SWT.TOP);




		//Adding a validator so that only a proper word can be used as a name
		IValidator nomValidator = new IValidator() {
			@Override
			public IStatus validate(Object value) {
				if (value instanceof String) {
					String s = String.valueOf(value).trim();

					if (s.matches("\\w+")) {
						nomStatus = ValidationStatus.ok();
						return ValidationStatus.ok();
					}
				}
				nomStatus = ValidationStatus.error("Nom Invalide");
				return ValidationStatus.error("Nom Invalide");
			}
		};

		IObservableValue nomtarget = WidgetProperties.
				text(SWT.Modify).observe(nomProduitText);
		
		IObservableValue nomModel = BeanProperties.value(Product.class, "nomProduit").
				observe(product);

		UpdateValueStrategy nomStrategy = new UpdateValueStrategy();
		nomStrategy.setBeforeSetValidator(nomValidator);



		//Binding the validator to the value and update strategy
		Binding nomBinding = ctx.bindValue(nomtarget, nomModel,nomStrategy,null);

		//Adding some decoration
		ControlDecorationSupport.create(nomBinding, SWT.RIGHT|SWT.TOP);
		
		Button btnNotifications = new Button(composite, SWT.CHECK);
		FormData fd_btnNotifications = new FormData();
		fd_btnNotifications.top = new FormAttachment(dateFin, 0, SWT.TOP);
		fd_btnNotifications.right = new FormAttachment(100, -23);
		btnNotifications.setLayoutData(fd_btnNotifications);
		btnNotifications.setText("Notifications");
		
		btnNotifications.addListener(SWT.Selection, listener->{
			receiveNotifications =  btnNotifications.getSelection();
		});
		//shell.pack();
	}
}
