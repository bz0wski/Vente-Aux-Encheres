package ui;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.eclipse.core.databinding.*;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;

import Enchere.Archivage;
import Enchere.SystemeEnchere;

public class SystemeEnchereWindow {

	protected Shell shell;
	private Text text;
	private Archivage archivage = null;
	private SystemeEnchere systemeEnchere = null;
	private StringBuilder builder = new StringBuilder();
	/**
	 * Launch the application.
	 * @param args
	 * @wbp.parser.entryPoint
	 */
	public SystemeEnchereWindow() {
	}
	public SystemeEnchereWindow(Archivage archivage, SystemeEnchere systemeEnchere) {
		this.archivage = archivage;
		this.systemeEnchere = systemeEnchere;
	}
	
	public static void main(String[] args) {
		try {
			SystemeEnchereWindow window = new SystemeEnchereWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		
		shell.addListener(SWT.Dispose, listener->{
			if (systemeEnchere == null || archivage == null){
				System.out.println("On quitting something was null");
				return;
			}
				
			
			archivage.archiverProduits(systemeEnchere.tousLesProduits());
			archivage.archiverUtilisateurs(systemeEnchere.tousLesUtilisateurs());
			archivage.archiverVenteEncours(systemeEnchere.tousLesVentesEncours());
			
			
		});
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 403);
		
		ByteBuffer titreBuffer = Charset.forName("UTF-8").encode("Application Serveur Système Enchère");
		String str = new String(titreBuffer.array());
		shell.setText(str);
		shell.setLayout(new FormLayout());
		
		Composite composite = new Composite(shell, SWT.NONE);
		FormLayout fl_composite = new FormLayout();
		fl_composite.marginHeight = 0;
		fl_composite.marginWidth = 0;
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100);
		fd_composite.right = new FormAttachment(100);
		fd_composite.top = new FormAttachment(0);
		fd_composite.left = new FormAttachment(0);
		composite.setLayoutData(fd_composite);
		
		composite.setLayout(fl_composite);
		
		text = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.LEFT | SWT.MULTI |SWT.READ_ONLY);
		
		text.setLayoutData(new FormData());
		FormData fd_text = new FormData();
		fd_text.bottom = new FormAttachment(100,-60);
		fd_text.right = new FormAttachment(100,-5);
		fd_text.top = new FormAttachment(0,5);
		fd_text.left = new FormAttachment(0,5);
		text.setLayoutData(fd_text);
		
		
		Button btnLancerServeur = new Button(composite, SWT.PUSH);
		btnLancerServeur.setText("Lancer serveur");
		FormData fd_btnLancerServeur = new FormData();
		
		fd_btnLancerServeur.top = new FormAttachment(text,5,SWT.BOTTOM);
		fd_btnLancerServeur.bottom = new FormAttachment(100, -10);
		fd_btnLancerServeur.right = new FormAttachment(text, 0, SWT.CENTER);
		
		btnLancerServeur.setLayoutData(fd_btnLancerServeur);
		btnLancerServeur.setText("Terminer");
		btnLancerServeur.setToolTipText("Lancer le Serveur");
		
		btnLancerServeur.addListener(SWT.Selection, listener->{
			System.out.println("push button");
		});
		
		DataBindingContext context = new DataBindingContext();
		
		IObservableValue widget = WidgetProperties.text(SWT.Modify).observe(text);
		IObservableValue modelValue = BeanProperties.value("errorLog").observe(builder.toString());
		context.bindValue(widget, modelValue);
		text.setText(builder.toString());
		
	}
	
	public void appendTo(String str) {
		builder.append(str);

	}
}
