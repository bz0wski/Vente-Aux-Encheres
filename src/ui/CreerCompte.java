package ui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import utility.Person;
import Enchere.SystemeEnchere;
import Enchere.Utilisateur;

public class CreerCompte extends Dialog {

	protected Object result;
	protected Shell shell;

	private Text nomText;
	private Text mdpText;
	private Text coordonnees;

	private final SystemeEnchere systemeEnchere;
	private Utilisateur utilisateur;

	private Person person = new Person();

	private IStatus status;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public CreerCompte(Shell parent, SystemeEnchere systeme) {
		super(parent);
		setText("SWT Dialog");
		this.systemeEnchere = systeme;
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
		return utilisateur;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		//shell = new Shell(getParent(), getStyle()|SWT.DIALOG_TRIM);
		shell = new Shell(getParent(), SWT.APPLICATION_MODAL|SWT.DIALOG_TRIM);
		shell.setSize(239, 138);
		shell.setText(getText());
		shell.setLocation(getParent().getLocation());
		FormLayout layout = new FormLayout();
		layout.marginBottom = 5;
		shell.setLayout(layout);


		// create the binding context
		DataBindingContext ctx = new DataBindingContext();

		Label lblNom = new Label(shell, SWT.NONE);
		FormData fd_lblNom = new FormData();
		fd_lblNom.right = new FormAttachment(0, 70);
		fd_lblNom.top = new FormAttachment(0, 12);
		fd_lblNom.left = new FormAttachment(0, 10);
		lblNom.setLayoutData(fd_lblNom);
		lblNom.setText("Nom");

		nomText = new Text(shell, SWT.BORDER);
		FormData fd_text = new FormData();
		fd_text.right = new FormAttachment(100, -10);
		fd_text.left = new FormAttachment(lblNom, 10);
		fd_text.top = new FormAttachment(lblNom, -3, SWT.TOP);
		nomText.setLayoutData(fd_text);

		Label lblMotDePasse = new Label(shell, SWT.NONE);
		FormData fd_lblMotDePasse = new FormData();
		fd_lblMotDePasse.right = new FormAttachment(0, 86);
		fd_lblMotDePasse.top = new FormAttachment(lblNom, 12);
		fd_lblMotDePasse.left = new FormAttachment(0, 10);
		lblMotDePasse.setLayoutData(fd_lblMotDePasse);
		lblMotDePasse.setText("Mot de passe");

		mdpText = new Text(shell, SWT.BORDER | SWT.PASSWORD);
		FormData fd_mdpText = new FormData();
		fd_mdpText.right = new FormAttachment(100, -10);
		fd_mdpText.top = new FormAttachment(nomText, 6);
		fd_mdpText.left = new FormAttachment(lblNom, 20);
		mdpText.setLayoutData(fd_mdpText);

		Label lblCoordonnees = new Label(shell, SWT.NONE);
		FormData fd_lblCoordonnees = new FormData();
		fd_lblCoordonnees.right = new FormAttachment(0, 86);
		fd_lblCoordonnees.top = new FormAttachment(lblMotDePasse, 10);
		fd_lblCoordonnees.left = new FormAttachment(0, 10);
		lblCoordonnees.setLayoutData(fd_lblCoordonnees);
		lblCoordonnees.setText("Coordonnées");

		coordonnees = new Text(shell, SWT.BORDER | SWT.PASSWORD);
		FormData fd_Coordonnees = new FormData();
		fd_Coordonnees.right = new FormAttachment(100, -10);
		fd_Coordonnees.top = new FormAttachment(mdpText, 6);
		fd_Coordonnees.left = new FormAttachment(lblMotDePasse, 10);
		coordonnees.setLayoutData(fd_Coordonnees);


		Button button = new Button(shell, SWT.PUSH);
		button.setText("Se Connecter");
		button.setEnabled(false);

		FormData fd_button = new FormData();

		fd_button.right = new FormAttachment(coordonnees, 0, SWT.RIGHT);
		fd_button.top = new FormAttachment(coordonnees, 7, SWT.BOTTOM);
		button.setLayoutData(fd_button);

		button.addListener(SWT.Selection, listener->{
			if (status == ValidationStatus.ok()) {

				systemeEnchere.creerCompte(person.getName(), person.getPswd(),person.getAdresse());

				utilisateur = systemeEnchere.seConnecter(person.getName(), person.getPswd());

				shell.close();
				shell.dispose();
			}

		});

		Listener l = listener->{
			System.out.println(status);
			if (status == ValidationStatus.OK_STATUS) {
				if(!person.getName().isEmpty() && !person.getPswd().isEmpty() && !person.getPswd().isEmpty())
					button.setEnabled(true);
			}
			else {
				button.setEnabled(false);
			}
		};

		//only enable button if status is OK
		nomText.addListener(SWT.Modify, l);
		mdpText.addListener(SWT.Modify, l);
		coordonnees.addListener(SWT.Modify, l);
		//shell.pack();
		///////////////

		///////////////////////////////////////////////Nom Validation////////////////////////////////////////////////////////////

		IObservableValue target = WidgetProperties.
				text(SWT.Modify).observe(nomText);

		IObservableValue model = BeanProperties.value(Person.class, "name").
				observe(person);

		//Adding a validator so that only a proper word can be used as a name
		IValidator nomValidator = new IValidator() {
			@Override
			public IStatus validate(Object value) {
				if (value instanceof String) {
					String s = String.valueOf(value).trim();

					if (s.matches("\\w+")) {
						//check if a name is already taken
						Optional<Utilisateur> user = Optional.empty();
						List<Utilisateur> userList = Arrays.asList(systemeEnchere.tousLesUtilisateurs());
						user = userList.stream().filter(u->u.nom.equals(s)).findFirst();

						if(user.isPresent()){			        		  
							status = ValidationStatus.error("Nom déjà pris, veuillez choisir un autre nom.");
							return status;					
						}

						status = ValidationStatus.ok();
						return ValidationStatus.ok();
					}
				}

				status = ValidationStatus.error("Nom Invalide");
				return ValidationStatus.error("Nom Invalide");
			}
		};



		UpdateValueStrategy strategy = new UpdateValueStrategy();
		strategy.setBeforeSetValidator(nomValidator);

		//Binding the validator to the value and update strategy
		Binding binding = ctx.bindValue(target, model,strategy,null);

		//Adding some decoration
		ControlDecorationSupport.create(binding, SWT.RIGHT|SWT.TOP);


		/////////////////////////////////////////////MDP Validation////////////////////////////////////////////////////////////

		IObservableValue mdptarget = WidgetProperties.
				text(SWT.Modify).observe(mdpText);
		IObservableValue mdpmodel = BeanProperties.value(Person.class, "pswd").
				observe(person);

		//Adding a validator so that only a proper word can be used as a name
		IValidator mdpValidator = new IValidator() {
			@Override
			public IStatus validate(Object value) {
				if (value instanceof String) {
					String s = String.valueOf(value).trim();

					if (s.matches("\\w+")) {
						if (s.length()<3) {
							setStatus(ValidationStatus.error("Mdp doit etre au moins 3 carateres"));
							return ValidationStatus.error("Mdp doit etre au moins 3 carateres");
						}

						setStatus(ValidationStatus.ok());
						return ValidationStatus.ok();
					}
				}

				if (!button.isDisposed()) {
					button.setEnabled(false);
				}
				setStatus(ValidationStatus.error("Mot de passe Invalide"));
				return ValidationStatus.error("Mot de passe Invalide");
			}
		};

		UpdateValueStrategy mdpstrategy = new UpdateValueStrategy();
		mdpstrategy.setBeforeSetValidator(mdpValidator);


		//Binding the validator to the value and update strategy
		Binding mdpbinding = ctx.bindValue(mdptarget, mdpmodel,mdpstrategy,null);

		//Adding some decoration
		ControlDecorationSupport.create(mdpbinding, SWT.RIGHT|SWT.TOP);	


		/////////////////////////////////////////////Adresse Validation////////////////////////////////////////////////////////////

		IObservableValue adrtarget = WidgetProperties.
				text(SWT.Modify).observe(coordonnees);
		IObservableValue adrmodel = BeanProperties.value(Person.class, "adresse").
				observe(person);

		//Adding a validator so that only a proper word can be used as a name
		IValidator adrValidator = new IValidator() {
			@Override
			public IStatus validate(Object value) {
				if (value instanceof String) {
					String s = String.valueOf(value).trim();

					if (s.matches("\\w+")) {						
						status = ValidationStatus.ok();
						return ValidationStatus.ok();
					}
				}

				setStatus(ValidationStatus.error("Mot de passe Invalide"));
				return ValidationStatus.error("Adresse Invalide");
			}
		};


		UpdateValueStrategy adrstrategy = new UpdateValueStrategy();
		strategy.setBeforeSetValidator(adrValidator);


		//Binding the validator to the value and update strategy
		Binding adrbinding = ctx.bindValue(adrtarget, adrmodel,adrstrategy,null);

		//Adding some decoration
		ControlDecorationSupport.create(adrbinding, SWT.RIGHT|SWT.TOP);
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

}
