package frames;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.json.simple.JSONArray;

public class MainFrame {
	private final String VERSION = "1.1";

	private JFrame frame;
	private JLabel sugestedCost;
	private JLabel sugestedPrice;
	private DecimalFormat df;

	private int vat, tran, vemc;
	private double costD;
	private double profitPercent;
	protected double transportCharge;
	private String costS;
	private String priceS;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
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
	public MainFrame() {
		vat = 0;
		tran = 0;
		vemc = 0;

		
		costD = 0;
		transportCharge = 0;
		profitPercent = 0;
		
		
		costS = "";
		priceS = "";
		
		DecimalFormatSymbols symbols = new DecimalFormatSymbols( new Locale("en", "UK"));
		symbols.setDecimalSeparator('.');
		symbols.setGroupingSeparator(' ');

		df = new DecimalFormat("00,000.00", symbols);

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		int lblX = 10, lblY = 10; 
		int xOffset = 120, yOffset = 24;
		int lblW = 80, tfW = 260, rbW = 110;
		int lbltfH = 20;
		float sHigh = 2.1f, sW = 1.75f;

		String zero = "€ 0";
		String title = "Kalkulator Ceny v"+VERSION;
		Color color = new Color(255,5,10);
		Font fonts = new Font("Segoe UI Black", Font.PLAIN, 12);
		Font fonts_title = new Font("Segoe UI Black", Font.PLAIN, 16);

		frame = new JFrame();
		frame.getContentPane().setBackground(color);
		frame.setTitle(title );
		frame.setBounds(100, 10, 520, 260);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);


		Border b = BorderFactory.createLineBorder(Color.WHITE);
		TitledBorder border = BorderFactory.createTitledBorder(b, "KOSZT");

		sugestedCost = new JLabel();
		sugestedCost.setFont(fonts_title);
		sugestedCost.setBounds(lblX, lblY, (int) (lblW*sW), (int) (lbltfH*sHigh));
		sugestedCost.setBorder(border);
		sugestedCost.setText(zero);
		frame.getContentPane().add(sugestedCost);
		
		border = BorderFactory.createTitledBorder(b, "CENA");
		sugestedPrice = new JLabel();
		sugestedPrice.setFont(fonts_title);
		sugestedPrice.setBounds((int) (xOffset*1.25), lblY, (int) (lblW*sW), (int) (lbltfH*sHigh));
		sugestedPrice.setBorder(border);
		sugestedPrice.setText(zero);
		frame.getContentPane().add(sugestedPrice);
		
		String[] profits = {"5 %","10 %", "20 %","25 %","30 %","50 %","75 %","100 %","125 %","150 %","175 %","200 %"};
		JComboBox cbProfit = new JComboBox(profits);
//		
		int x = sugestedPrice.getX() + sugestedPrice.getWidth() + 10;
		int defaultProfit = 3;
		cbProfit.setBounds(x, lblY + 10, tfW/2, lbltfH);
		cbProfit.setSelectedIndex(defaultProfit);
		frame.getContentPane().add(cbProfit);
		profitPercent = removeSpecialChars(cbProfit.getItemAt(defaultProfit).toString());
		
		JLabel lblCost = new JLabel("NETTO");
		lblCost.setFont(fonts);
		lblY += (yOffset*2);
		lblCost.setBounds(lblX, lblY, lblW, lbltfH);
		frame.getContentPane().add(lblCost);
		
		JTextField tfCost = new JTextField();
		tfCost.setFont(fonts);
		tfCost.setText("0.00");
		tfCost.setBounds(xOffset, lblY, tfW, lbltfH);
		tfCost.requestFocusInWindow();
		SwingUtilities.invokeLater(new Runnable() {
		      public void run() {
		        tfCost.requestFocus();
		        tfCost.selectAll();
		      }
		    });
		frame.getContentPane().add(tfCost);
		
		JLabel lblVat = new JLabel("VAT");
		lblVat.setFont(fonts);
		lblY += yOffset;
		lblVat.setBounds(lblX, lblY, lblW, lbltfH);
		frame.getContentPane().add(lblVat);
		
		JCheckBox chbVAT = new JCheckBox();
		chbVAT.setSelected(Boolean.parseBoolean(""+vat));
		chbVAT.setBackground(color);
		chbVAT.setFont(fonts);
		chbVAT.setBounds(xOffset, lblY, tfW, lbltfH);
		frame.getContentPane().add(chbVAT);
		
		JLabel lblVemc = new JLabel("VEMC");
		lblVemc.setFont(fonts);
		lblY += yOffset;
		lblVemc.setBounds(lblX, lblY, lblW, lbltfH);
		frame.getContentPane().add(lblVemc);
		
		JCheckBox chbVemc = new JCheckBox();
		chbVemc.setSelected(Boolean.parseBoolean(""+vemc));
		chbVemc.setBackground(color);
		chbVemc.setFont(fonts);
		chbVemc.setBounds(xOffset, lblY, tfW, lbltfH);
		frame.getContentPane().add(chbVemc);

		JLabel transport = new JLabel("Transport");
		transport.setFont(fonts);
		lblY += yOffset;
		transport.setBounds(lblX, lblY, lblW, lbltfH);
		frame.getContentPane().add(transport);
		
		JCheckBox chbTransport = new JCheckBox();
		chbTransport.setSelected(Boolean.parseBoolean(""+tran));
		chbTransport.setBackground(color);
		chbTransport.setFont(fonts);
		chbTransport.setBounds(xOffset, lblY, 30, lbltfH);
		frame.getContentPane().add(chbTransport);
		
		String[] tSupNames = {"DT","TyreLeader","PWT","TyreCall","Mr.Tyres","NASZ VAN"};
		String[] tSupTrans = {"2.50","10.00","4.00","4.50","5.00","0.5"};
		transportCharge = Double.parseDouble(tSupTrans[0]);
		
		JComboBox cbTransCharges = new JComboBox(tSupNames);
		cbTransCharges.setBounds((int) (xOffset*1.4), lblY, tfW/2, lbltfH);
		cbTransCharges.setSelectedIndex(0);
		frame.getContentPane().add(cbTransCharges);
		

		JLabel lblTransportCharges = new JLabel("€ "+df.format(transportCharge));
		lblTransportCharges.setFont(fonts);
		lblTransportCharges.setBounds((int) (cbTransCharges.getX() + cbTransCharges.getWidth() + xOffset), lblY, lblW, lbltfH);
		frame.getContentPane().add(lblTransportCharges);
		
		tfCost.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
			  }
			  public void removeUpdate(DocumentEvent e) {
				  if(tfCost.getText() != null && !tfCost.getText().isEmpty())
					  costS = tfCost.getText();
				  recalculateCost();
			  }
			  public void insertUpdate(DocumentEvent e) {
				  if(tfCost.getText() != null && !tfCost.getText().isEmpty())
					  costS = tfCost.getText();
				  recalculateCost();
			  }
		});
		
		chbVAT.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(chbVAT.isSelected())	vat = 1;
				else vat = 0;
				recalculateCost();
		    }
		});

		chbTransport.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(chbTransport.isSelected())	tran = 1;
				else tran = 0;

				recalculateCost();
			}
		});
		
		chbVemc.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(chbVemc.isSelected())	vemc = 1;
				else vemc = 0;
				recalculateCost();
			}
		});
		
		
		cbProfit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbProfit ){
					JComboBox cb = (JComboBox) a.getSource();
					profitPercent = removeSpecialChars(cb.getSelectedItem().toString());
				}
				recalculateCost();
			}
		});

		cbTransCharges.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbTransCharges ){
					JComboBox cb = (JComboBox) a.getSource();
					for (int i=0; i<tSupNames.length; i++) {
						if(tSupNames[i].equals(cb.getSelectedItem().toString())){
							transportCharge = Double.parseDouble(tSupTrans[i]);
							lblTransportCharges.setText("€ " + df.format(transportCharge));
						}
					}
				}		
				recalculateCost();
			}
		});
	}

	
	protected void recalculateCost() {
//System.out.println("rc\ntf "+costS+"\nvat "+vat+"\ntrans "+tran+"\nvemc "+vemc+"\n ");
		double c = 0, p = 0, recyclingCharge = 3.44;
		if(costS != null && !costS.isEmpty()) {
			costD = isDouble(costS);
			c = costD;
		}
		p = c + (c * profitPercent) / 100;
		
		if(vat == 1) {
			c = c * 1.23;
			p = p *1.23;
		}
		if(tran == 1) {
			c = c + transportCharge;
			p = p + transportCharge;
		}
		if(vemc == 1) {
			c = c + recyclingCharge;
			p = p + recyclingCharge;
		}
		
		setCostLBL(df.format(c));
		setPriceLBL(df.format(p));
	}
	
	protected void setPriceLBL(String p) {
		sugestedPrice.setText("€ " + p);	}
	
	protected void setCostLBL(String c) {
		sugestedCost.setText("€ " + c);
	}
	

	// HELPER METHODS
	private double isDouble(String st) {
		if(st.contains(",")){
			st = st.replace(",",".");
		}
		st = removeZerosAndSpaces(st);
		try{
			return Double.parseDouble(st);
		} catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, "isDouble - Nie prawidlowa cena");
		}
		return 0;
	}
	
	private String removeZerosAndSpaces(String st) {
		st = st.replaceAll("\\s", "");
		st = st.replaceAll("^0+", "");		
		return st;
	}
	
	private int removeSpecialChars(String str) {
		str = str.replaceAll("\\D+","");
		return isInt(str);
	}

	public int isInt(String st) {
		try{
			return Integer.parseInt(st);
		} catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, "isIInt - To nie cyfra");
		}
		return 0;
	}
}
