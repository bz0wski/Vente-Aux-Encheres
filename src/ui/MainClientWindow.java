package ui;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

import utility.EnchereSubject;
import Enchere.Produit;
import Enchere.SystemeEnchere;
import Enchere.SystemeEnchereHelper;
import Enchere.Utilisateur;

public class MainClientWindow {

	protected Shell shell;
	private Text text;

	private TableViewer clientTableViewer;

	private  Table clientTable;

	List<Produit> listProduits = new ArrayList<>();

	private WritableList writableList;

	private final SystemeEnchere systemeEnchere;

	private static final String[] clientColumnNames = {"Nom Produit","Catégorie","Description","Prix en cours","Fin de Vente"} ;

	private Produit produit;

	private Utilisateur utilisateur = null;
	
	private boolean connexion = false;

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
						
						String IOR = "IOR:000000000000001f49444c3a456e63686572652f53797374656d65456e63686572653a312e300000000000010000000000000086000102000000000e3137322e31372e3134352e333800c15300000031afabcb0000000020534fb97800000001000000000000000100000008526f6f74504f410000000008000000010000000014000000000000020000000100000020000000000001000100000002050100010001002000010109000000010001010000000026000000020002";
						org.omg.CORBA.Object distantEnchere = orb.string_to_object(IOR);
						
						// Casting de l'objet CORBA vers le type Enchere.SystemeEnchere
						SystemeEnchere systeme = Enchere.SystemeEnchereHelper.narrow(distantEnchere);
							
						MainClientWindow window = new MainClientWindow(systeme);
							
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
	 */
	
	public MainClientWindow(SystemeEnchere systemeEnchere) {
		this.systemeEnchere = systemeEnchere;
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		shell.setLayout(new FormLayout());

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




		//Produit p1 = new Produit("", "coffee", "alimentation", "fort et noir", 5.6f, "lundi 23 mars 2015 05:15", new Utilisateur("", "", "", ""));
		//Produit p2 = new Produit("", "milk", "alimentation", "fort et blanc", 2.6f, "lundi 23 mars 2015 05:10",new Utilisateur("", "", "", ""));

		//listProduits.add(p1);
		//listProduits.add(p2);

		listProduits = new ArrayList<>(Arrays.asList(systemeEnchere.tousLesProduits()));


		clientTableViewer = new TableViewer(composite, SWT.SINGLE|SWT.FULL_SELECTION|SWT.BORDER);
		createColumns(composite, clientTableViewer);
		clientTable = clientTableViewer.getTable();

		//Add a resize listener to resize the table when the user resizes the window
		composite.addControlListener(new CompositeResizeListener(composite));

		this.produit = new Produit("", "", "", "", 0.0, "", new Utilisateur("", "", "", ""));

		//writablelist to contain the table input, backed by an ArrayList
		writableList = new WritableList(listProduits, Produit.class);

		ObservableListContentProvider contentProvider =  new ObservableListContentProvider();

		//Setting the content provider for the table, to contain all of it's data
		clientTableViewer.setContentProvider(contentProvider);

		//Setting Input, Must be called after setting content provider
		//I just added this 
		clientTableViewer.setInput(writableList);


		clientTable.addListener(SWT.MouseDoubleClick, listener->{
			try {
				System.out.println("double click, Encherir");
				IStructuredSelection selection = (IStructuredSelection)clientTableViewer.getSelection();
				if (!selection.isEmpty()) {
					Produit produit = (Produit)selection.getFirstElement();
					EncherirWindow window = new EncherirWindow(shell, produit,systemeEnchere,utilisateur);
					window.open();
				}

			} catch (Exception e) {

				e.printStackTrace();
			}


		});


		/**I'm adding a modify listener to all the cell editors which gets fired once the Equation has been
		 * validated atleast once. After it is fired, every time an edit is made to any of the cells of the
		 * table, it replaces the value of equationValidationOk in the validationRegistry Map with false*/
		Listener listener = (event ->{

		}); 



		FormData fd_clientTable = new FormData();

		fd_clientTable.right = new FormAttachment(100);	
		fd_clientTable.top = new FormAttachment(0);
		fd_clientTable.left = new FormAttachment(0);
		fd_clientTable.bottom = new FormAttachment(100, -150);

		clientTable.setLayoutData(fd_clientTable);
		clientTable.setHeaderVisible(true);
		clientTable.setLinesVisible(true);



		Button btn1 = new Button(composite, SWT.NONE);
		btn1.setText("Proposer Prix");
		btn1.setEnabled(false);
		FormData fd_btn1 = new FormData();

		//fd_btn1.right = new FormAttachment(0,10);	
		fd_btn1.top = new FormAttachment(clientTable, 10, SWT.BOTTOM);
		fd_btn1.left = new FormAttachment(0,10);
		fd_btn1.bottom = new FormAttachment(100, -100);
		btn1.setLayoutData(fd_btn1);
		
		
/////////////////////////////////////////Encherir Listener//////////////////////////////////////////////////////////////////////////////////////////////////////////	

		btn1.addListener(SWT.Selection, l->{
			IStructuredSelection selection = (IStructuredSelection)clientTableViewer.getSelection();
			if (!selection.isEmpty()) {
				if (utilisateur == null)
					return;
				if (this.utilisateur.id.equals(""))
					return;

				Produit produit = (Produit)selection.getFirstElement();
				EncherirWindow window = new EncherirWindow(shell, produit,systemeEnchere,utilisateur);
				window.open();
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
						System.out.println("User id is empty");
					
						
						CreerCompte creerCompte = new CreerCompte(shell, systemeEnchere);
						utilisateur = (Utilisateur) creerCompte.open();
						
						//if user closes dialog before creating new account
						if (utilisateur == null) 
							return;
						
						
						if (!utilisateur.id.equals("")) {
							System.out.println("Utilisateur crée avec succès");
							shell.setText("Système Enchère "+utilisateur.nom);
							btn2.setText("Se Deconnecter");
							setConnexion(true);
							
							//enable buttons after the user has successfully connected
							btn1.setEnabled(true);
							btn3.setEnabled(true);
						}
					}else {
						System.out.println("Utilisateur trouvé et connecté");
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
			
			AjouterProduitWindow window = new AjouterProduitWindow(shell, systemeEnchere, utilisateur);
			Produit prod = (Produit) window.open();
			if (prod == null) 
				return;
			
			if (!prod.id.equals("")) {
				writableList.add(prod);
			}
			
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
		List newList = new ArrayList<>(Arrays.asList(systemeEnchere.tousLesProduits()));
		
	}

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
}
