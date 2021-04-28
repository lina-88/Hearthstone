package view;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class warning  {
	JPanel panel;
	JLabel text;


public warning(String s){
	Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
	panel = new JPanel();
	text = new JLabel(s);	
	text.setFont(new Font("Al Bayan", Font.BOLD, 7*screenSize.width/350));
	panel.setLayout(new BorderLayout());
	panel.add(text,BorderLayout.NORTH);

	
}


public JPanel getPanel() {
	return panel;
}


public JLabel getText() {
	return text;
}

}