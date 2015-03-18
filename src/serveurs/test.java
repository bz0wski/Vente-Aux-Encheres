package serveurs;

import java.awt.EventQueue;

import javax.swing.JFrame;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JButton;
import javax.swing.JTable;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import javax.swing.JTextArea;

public class test {

	private JFrame frame;
	private JTable table;
	private JTable table_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					test window = new test();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public test() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().setLayout(new MigLayout("", "[86px,grow][][grow]", "[29px,grow][][][][][][grow]"));
				
				table_1 = new JTable();
				frame.getContentPane().add(table_1, "cell 0 0 3 2,grow");
			
				
				JButton btnButton = new JButton("button");
				frame.getContentPane().add(btnButton, "cell 0 2,alignx center,aligny top");
			
			JButton btnButton_1 = new JButton("button2");
			frame.getContentPane().add(btnButton_1, "cell 1 2,alignx center");
			
			JButton btnButton_2 = new JButton("button3");
			frame.getContentPane().add(btnButton_2, "cell 2 2,alignx center,aligny center");
			
			JTextArea textArea = new JTextArea();
			frame.getContentPane().add(textArea, "cell 0 3 3 4,grow");
	}

}
