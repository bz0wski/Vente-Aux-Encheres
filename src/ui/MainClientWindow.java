package ui;

import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

import utility.MyAbstractModelObject;
import utility.NotificationHandler;
import utility.NotificationSubject;
import Enchere.Acheteur_Vendeur;
import Enchere.Produit;
import Enchere.SystemeEnchere;
import Enchere.Utilisateur;
import org.eclipse.swt.widgets.Label;

public class MainClientWindow extends MyAbstractModelObject{


	protected Shell shell;
	private Text text;

	//public volatile static boolean refresh = false;
	//public static Object lock = new Object();


	private TableViewer clientTableViewer;

	private  Table clientTable;

	List<Produit> listProduits = new ArrayList<>();

	private WritableList writableList;

	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	private final SystemeEnchere systemeEnchere;
	private final Acheteur_Vendeur acheteur_Vendeur;

	private final ProduitFilter filter = new ProduitFilter();

	private static final String[] clientColumnNames = {"Nom Produit","Catégorie","Description","Prix en cours","Fin de Vente"} ;

	private String builder =  new String("");

	private Utilisateur utilisateur = null;

	private boolean connexion = false;

	private Timer timer = new Timer();

	private static ExecutorService executor = Executors.newSingleThreadExecutor();
	private Text search;

	private Object lock = new Object();
	private boolean updateUI = false;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final Display display = Display.getDefault();
			Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
				@Override
				public void run() {
					try {

						// Intialisation de l'ORB
						org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, null);

						//String IOR = "IOR:000000000000001a49444c3a456e63686572652f4172636869766167653a312e30000000000000010000000000000086000102000000000e3137322e31372e3134352e333800d8e300000031afabcb00000000205c4eb50a00000001000000000000000100000008526f6f74504f410000000008000000010000000014000000000000020000000100000020000000000001000100000002050100010001002000010109000000010001010000000026000000020002";
						//org.omg.CORBA.Object distantEnchere = orb.string_to_object(IOR);

						// Casting de l'objet CORBA vers le type Enchere.SystemeEnchere
						//SystemeEnchere systeme = Enchere.SystemeEnchereHelper.narrow(distantEnchere);

						MainClientWindow window = new MainClientWindow();

						window.open();
					} catch (Exception e) {
						e.printStackTrace();
					}


				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	/**
	 * Initialise the MainClientWindow with the distant SystemeEnchere server object which will 
	 * process all distant calls to the server.
	 * @param systemeEnchere the distant reference to the server object.
	 * @param ior a reference to the serveur instance of this client, so that it can receive notifications.
	 */
	public MainClientWindow(SystemeEnchere systemeEnchere, Acheteur_Vendeur ior) {
		this.systemeEnchere = systemeEnchere;
		this.acheteur_Vendeur = ior;
		
	}

	public MainClientWindow() {
		acheteur_Vendeur = null;
		systemeEnchere = null;
	}
	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();

		createContents();
		updateEvery5SecondsOrSo();
		//refresh();
		shell.open();
		shell.layout();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	
	public void refresh(){
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				if(systemeEnchere == null)
					return;
				
				synchronized (lock) {
					
					while(!updateUI){
						try {
							lock.wait();
						} catch (InterruptedException e) {
							e.getMessage();
							e.printStackTrace();
							throw new NullPointerException();
						}
					}
				}
				if(acheteur_Vendeur == null)
					return;

				if(text == null)
					return;

				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						System.out.println("!!!!!!!refreshed!!!!!");
						update();
					}
				});

			}
		};

		try {
			executor.execute(runnable);
			//**Important to shut the executor once it's done *//*
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}finally{
			executor.shutdownNow();
		}
	}


	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		shell.setLayout(new FormLayout());

		//Add a dispose listener to unregister this instance and stop receiving notifications
		shell.addListener(SWT.Dispose, listener->{
			executor.shutdown();
			timer.cancel();
		});

		Composite composite = new Composite(shell, SWT.NONE);
		FormLayout fl_composite = new FormLayout();
		fl_composite.marginHeight = 2;
		fl_composite.marginWidth = 2;
		composite.setLayout(fl_composite);
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100);
		fd_composite.right = new FormAttachment(100);
		fd_composite.top = new FormAttachment(0);
		fd_composite.left = new FormAttachment(0);
		composite.setLayoutData(fd_composite);

		listProduits = new ArrayList<>(Arrays.asList(systemeEnchere.tousLesProduits()));

		/*FilteredTree filteredTree = new FilteredTree(composite, SWT.BORDER, new PatternFilter(), true);
		TreeViewer viewer = filteredTree.getViewer();*/
		

		
		//Added search function to the table//
		search = new Text(composite, SWT.BORDER | SWT.SEARCH);

		FormData fd_search = new FormData();
		
		fd_search.top = new FormAttachment(0);
		fd_search.right = new FormAttachment(100);
		search.setLayoutData(fd_search);

		//Listen for searches
		search.addListener(SWT.KeyUp, listener->{
			filter.setSearchText(search.getText());
			clientTableViewer.refresh(true);
		});

		clientTableViewer = new TableViewer(composite, SWT.SINGLE|SWT.FULL_SELECTION|SWT.BORDER);

		//adding sorter to the table
		clientTableViewer.setComparator(new ProduitComparator());


		//adding search filter to the table
		clientTableViewer.addFilter(filter);
		createColumns(composite, clientTableViewer);
		clientTable = clientTableViewer.getTable();

		//Add a resize listener to resize the table when the user resizes the window
		composite.addControlListener(new CompositeResizeListener(composite));



		///Produit p1 = new Produit("", "coffee", "alimentation", "fort et noir", 5.6, "jeudi 2 avril 2015 05:15",null,20.0);
		///Produit p2 = new Produit("", "milk", "alimentation", "fort et blanc", 2.6, "mercredi 1 avril 2015 05:10",null, 20.0);
		//listProduits.add(p1);
		//listProduits.add(p2);

		//writablelist to contain the table input, backed by an ArrayList
		writableList = new WritableList(listProduits, Produit.class);

		ObservableListContentProvider contentProvider =  new ObservableListContentProvider();

		//Setting the content provider for the table, to contain all of it's data
		clientTableViewer.setContentProvider(contentProvider);

		//Setting Input, Must be called after setting content provider
		//I just added this 
		clientTableViewer.setInput(writableList);


		FormData fd_clientTable = new FormData();
		fd_clientTable.right = new FormAttachment(100);	
		fd_clientTable.top = new FormAttachment(search, 2, SWT.BOTTOM);
		fd_clientTable.left = new FormAttachment(0);
		fd_clientTable.bottom = new FormAttachment(100, -150);
		clientTable.setLayoutData(fd_clientTable);
		clientTable.setHeaderVisible(true);
		clientTable.setLinesVisible(true);

		

		Button btn1 = new Button(composite, SWT.NONE);
		
		btn1.setText("Proposer Prix");
		btn1.setEnabled(false);
		FormData fd_btn1 = new FormData();

		fd_btn1.top = new FormAttachment(clientTable, 10, SWT.BOTTOM);
		fd_btn1.left = new FormAttachment(0,10);
		fd_btn1.bottom = new FormAttachment(100, -100);
		btn1.setLayoutData(fd_btn1);


		final Action sync = new Action() {
			@Override
			public void run() {					
				super.run();
				update();
			
			}
		};

		clientTableViewer.getTable().addMenuDetectListener(menu->{
			MenuManager menuManager = new MenuManager();
			menuManager.setRemoveAllWhenShown(true);
			menuManager.addMenuListener(listener->{

				sync.setText("Synchroniser \t Cmd+R");
				sync.setAccelerator(SWT.COMMAND | 'R' );
				menuManager.add(sync);

			});
			clientTableViewer.getTable().setMenu(menuManager.createContextMenu(clientTableViewer.getTable()));
		});

		/////////////////////////////////////////Encherir Listener//////////////////////////////////////////////////////////////////////////////////////////////////////////	

		btn1.addListener(SWT.Selection, l->{
			IStructuredSelection selection = (IStructuredSelection)clientTableViewer.getSelection();
			if (!selection.isEmpty()) {
				if (utilisateur == null)
					return;
				if (this.utilisateur.id.equals(""))
					return;

				Produit produit = (Produit)selection.getFirstElement();
				EncherirWindow window = new EncherirWindow(shell, produit,systemeEnchere,utilisateur, acheteur_Vendeur);
				window.open();

				update();
				System.out.println("Testing notifications In client UI: "+builder);
				clientTableViewer.refresh(true);
			}
		});

		clientTable.addListener(SWT.MouseDoubleClick, l->{
			try {
				if (!connexion || !btn1.isEnabled()) 
					return;
				IStructuredSelection selection = (IStructuredSelection)clientTableViewer.getSelection();
				if (!selection.isEmpty()) {
					Produit produit = (Produit)selection.getFirstElement();
					EncherirWindow window = new EncherirWindow(shell, produit,systemeEnchere,utilisateur,acheteur_Vendeur);
					window.open();

					update();
					clientTableViewer.refresh(produit);
				}

			} catch (Exception e) {

				e.printStackTrace();
			}


		});


		Button btn2 = new Button(composite, SWT.NONE);
		btn2.setText("Se Connecter");
		FormData fd_btn2 = new FormData();

		fd_btn2.right = new FormAttachment(clientTable,0,SWT.CENTER);	
		fd_btn2.top = new FormAttachment(clientTable, 10, SWT.BOTTOM);
		fd_btn2.left = new FormAttachment(clientTable,0, SWT.CENTER);
		fd_btn2.bottom = new FormAttachment(100, -100);
		btn2.setLayoutData(fd_btn2);


		Button btn3 = new Button(composite, SWT.NONE);
		btn3.setText("Ajouter Produit");
		btn3.setEnabled(false);

		/////////////////////////////////////////Se connecter Listener//////////////////////////////////////////////////////////////////////////////////////////////////////////	

		btn2.addListener(SWT.Selection, l->{
			if (!isConnexion()) {
				if (utilisateur == null || utilisateur.id.equals("")) {
					SeConnecterWindow window = new SeConnecterWindow(shell,utilisateur,systemeEnchere);
					this.utilisateur = (Utilisateur) window.open();

					if (this.utilisateur == null) 
						return;				

					if (this.utilisateur.id.equals("")) {
						//System.out.println("User id is empty");


						CreerCompte creerCompte = new CreerCompte(shell, systemeEnchere);
						utilisateur = (Utilisateur) creerCompte.open();

						//if user closes dialog before creating new account
						if (utilisateur == null) 
							return;


						if (!utilisateur.id.equals("")) {
							//System.out.println("Utilisateur crée avec succès");
							shell.setText("Système Enchère "+utilisateur.nom);
							btn2.setText("Se Deconnecter");
							setConnexion(true);

							//enable buttons after the user has successfully connected
							btn1.setEnabled(true);
							btn3.setEnabled(true);
						}
					}else {
						//System.out.println("Utilisateur trouvé et connecté");
						shell.setText("Système Enchère "+utilisateur.nom);
						btn2.setText("Se Deconnecter");
						setConnexion(true);
						//enable buttons after the user has successfully connected
						btn1.setEnabled(true);
						btn3.setEnabled(true);
					}


				}
			}
			else {
				if (!this.utilisateur.id.equals("")) {
					//disconnect user
					utilisateur = new Utilisateur("", "", "", "");
					setConnexion(false);
					btn2.setText("Se Connecter");

					shell.setText("Systeme Enchère");
					//disable buttons after the user has successfully disconnected
					btn1.setEnabled(false);
					btn3.setEnabled(false);
				}

			}

		});

		/////////////////////////////////////////Ajouter produit Listener//////////////////////////////////////////////////////////////////////////////////////////////////////////	


		btn3.addListener(SWT.Selection, l->{
			if(utilisateur == null)
				return;
			if (this.utilisateur.id.equals(""))
				return;

			AjouterProduitWindow window = new AjouterProduitWindow(shell, systemeEnchere, utilisateur, acheteur_Vendeur);
			Produit prod = (Produit) window.open();


			if (prod == null) 
				return;
			if (prod.id.equals(""))
				return;

			writableList.add(prod);
			//notify all clients of change

			//clientTableViewer.insert(produit, -1);
			//clientTableViewer.refresh();
		});


		FormData fd_btn3 = new FormData();

		fd_btn3.right = new FormAttachment(100,-10);	
		fd_btn3.top = new FormAttachment(clientTable, 10, SWT.BOTTOM);
		//fd_btn3.left = new FormAttachment();
		fd_btn3.bottom = new FormAttachment(100, -100);

		btn3.setLayoutData(fd_btn3);

		text = new Text(composite, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.MULTI);

		FormData fd_text = new FormData();

		fd_text.right = new FormAttachment(100);	
		fd_text.top = new FormAttachment(btn2,10,SWT.BOTTOM);
		fd_text.left = new FormAttachment(0);
		fd_text.bottom = new FormAttachment(100,-1);

		text.setLayoutData(fd_text);

		DataBindingContext context = new DataBindingContext();
		IObservableValue widget = WidgetProperties.text(SWT.Modify).observe(text);
		
		


		IObservableValue modelValue = BeanProperties.value("builder").observe(this);
		Binding binding = context.bindValue(widget, modelValue);
		binding.updateModelToTarget();
		/*
		IObservableValue target = WidgetProperties.text(SWT.DefaultSelection).observe(text);
		IObservableValue model = BeanProperties.value(Proposition.class, "proposition").observe(proposition);


		DataBindingContext dbc = new DataBindingContext();
		dbc.bindValue(model, target);
		new UpdateValueStrategy().setBeforeSetValidator(new IValidator() {

			@Override
			public IStatus validate(Object value) {
				if (value instanceof String) {
					System.out.println("observing "+(String)value);

				}
				return ValidationStatus.ok();
			}
		});*/

		//equationTableViewer.getTable().addListener(SWT.Selection, listener);
	}

	/**
	 * @return the connexion
	 */
	public boolean isConnexion() {
		return connexion;
	}

	/**
	 * @param connexion the connexion to set
	 */
	public void setConnexion(boolean connexion) {
		this.connexion = connexion;
	}
	/**
	 * Creates table columns for the table.
	 * @param parent : Parent composite on which to attach new column.
	 * @param viewer : {@link TableViewer} on which to attach new column.
	 */
	private void createColumns(final Composite parent, final TableViewer viewer){


		//first column, text field for the product name
		TableViewerColumn nomProduitColumn = createTableViewerColumn(clientColumnNames[0], 100);
		nomProduitColumn.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				Produit produit = (Produit) element;
				return produit.nom;
			}
		});


		//second column for the categorie of the product
		TableViewerColumn categorieColumn = createTableViewerColumn(clientColumnNames[1], 100);
		categorieColumn.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				Produit produit = (Produit) element;
				return produit.categorie;
			}
		});


		//third column  for the description of the product
		TableViewerColumn descriptionColumn = createTableViewerColumn(clientColumnNames[2], 100);
		descriptionColumn.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				Produit produit = (Produit) element;

				return produit.description;
			}

		});

		//fourth column for the price of the product
		TableViewerColumn prixColumn = createTableViewerColumn(clientColumnNames[3], 100);
		prixColumn.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				Produit produit = (Produit) element;
				BigDecimal bd = new BigDecimal(Double.toString(produit.prix_depart));
				bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);       

				return bd.toString();
			}
		});


		TableViewerColumn finVenteColumn = createTableViewerColumn(clientColumnNames[4], 100);
		finVenteColumn.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				Produit produit = (Produit) element;
				return produit.date_fin;
			}
		});
	}

	/**
	 * Returns a newly created {@link TableViewerColumn}
	 * @param title : Title of the column.
	 * @param width : WIdth of the column.
	 * @param colNumber : Index of the column.
	 * @return Newly created Table column.
	 */
	private TableViewerColumn createTableViewerColumn(String title, int width){
		final TableViewerColumn tableViewerCol =  new TableViewerColumn(clientTableViewer,SWT.NONE);
		final TableColumn column = tableViewerCol.getColumn();

		column.setText(title);
		column.setWidth(width);
		column.setResizable(true);
		column.setMoveable(true);
		column.pack();

		return tableViewerCol;
	}


	private void sync() {
		if(writableList == null)
			return;
		if(systemeEnchere == null)
			return;

		List<Produit> newList = new ArrayList<>(Arrays.asList(systemeEnchere.tousLesProduits()));

			listProduits.clear();
			listProduits.addAll(newList);
			clientTableViewer.refresh();
			System.out.println("replacing list");
			//writableList.clear();
			//writableList.addAll(newList);
		



	}

	public void recevoirNotification(String notification) {

		builder += notification;

	}

	/*	*//**
	 * @return the builder
	 *//*
	public String getBuilder() {
		return builder.toString();
	}
	  *//**
	  * @param builder the builder to set
	  *//*
	public void setBuilder(String builder) {
		String oldValue = this.builder;
		this.builder = (builder);
		propertyChangeSupport.firePropertyChange("builder", oldValue, builder);
	}*/

	public Table getTable() {
		return clientTable;
	}



	private class CompositeResizeListener implements ControlListener{
		private Table eqnTable;
		private Composite composite;

		public CompositeResizeListener(Composite composite){
			eqnTable = getTable();
			this.composite = composite;
		}

		@Override
		public void controlResized(ControlEvent e) {
			Rectangle compositeArea = composite.getClientArea();
			Point tableSize = eqnTable.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			//	ScrollBar vBar = eqns_Table.getVerticalBar();
			//	int width = compositeArea.width - eqns_Table.computeTrim(0, 0, 0, 0).width - vBar.getSize().x;

			if(tableSize.y > compositeArea.height + eqnTable.getHeaderHeight()){
				// Subtract the scrollbar width from the total column width
				// if a vertical scrollbar will be required
				//	Point vBarSize = vBar.getSize();
				//	width -= vBarSize.x;	
			}

			Point oldSize = eqnTable.getSize();
			if(oldSize.x > compositeArea.width){
				// table is getting smaller so make the columns 
				// smaller first and then resize the table to
				// match the client area width

				for(TableColumn col:eqnTable.getColumns())
					if (col.getWidth() < compositeArea.width/clientColumnNames.length)
						col.setWidth(col.getWidth());
					else
					{  col.setWidth(compositeArea.width/clientColumnNames.length); } 

				eqnTable.setSize(compositeArea.width, compositeArea.height);
			}
			else{
				// table is getting bigger so make the table 
				// bigger first and then make the columns wider
				// to match the client area width
				eqnTable.setSize(compositeArea.width, compositeArea.height);

				for(TableColumn col:eqnTable.getColumns()){
					if (col.getWidth() > compositeArea.width/clientColumnNames.length)
						col.setWidth(col.getWidth());
					else
						col.setWidth(compositeArea.width/clientColumnNames.length); 
				}
			}
		}

		@Override
		public void controlMoved(ControlEvent e) {			
		}
	}

	class ProduitComparator extends ViewerComparator{

		DateFormat format = DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.SHORT,  Locale.FRANCE);
		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			Produit p1 = (Produit)e1;
			Produit p2 = (Produit)e1;

			try {
				return format.parse(p1.date_fin).compareTo(format.parse(p2.date_fin));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return 0;
		}
	}

	private class ProduitFilter extends ViewerFilter{

		private String searchString;

		public void setSearchText(String s) {
			// ensure that the value can be used for matching 
			this.searchString = ".*" + s + ".*";
		}

		@Override
		public boolean select(Viewer viewer, Object parentElement,
				Object element) {
			if (searchString == null || searchString.isEmpty()) {
				return true;
			}
			Produit produit = (Produit)element;
			if (produit.nom.matches(searchString)) {
				return true;
			}
			if (produit.categorie.matches(searchString)) {
				return true;
			}
			return false;
		}

	}


	public String getBuilder(){
		return builder;
	}

	public void setBuilder(String newValue) {
		String oldValue = this.builder;
		this.builder = newValue;
		propertyChangeSupport.firePropertyChange("builder", oldValue, newValue);
	}

	public void update() {
		if(acheteur_Vendeur == null)
			return;

		if(text.isDisposed())
			return;

		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				sync(); 
				builder += (acheteur_Vendeur.notification());

				text.setText(builder);
			}
		});
	}

	private void updateEvery5SecondsOrSo(){

		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				update();
			}
		};
		timer.scheduleAtFixedRate(task, new Date(), 5000);

	}
}
