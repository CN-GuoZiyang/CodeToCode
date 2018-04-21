import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.awt.Desktop;

public class CodeToCode extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem menuItem;
	private JButton button;
	private JTextField textField;
	private JLabel label_1;
	private JComboBox<String> comboBox;
	private JLabel label;
	private JScrollPane scrollPane;
	private HashMap<String, String> all = new HashMap<String, String>(5);
	private MyTable table;
	private DefaultTableModel dtm;
	private String[][] elements = new String[5][2];
	private String[] colnames = {"编码名", "编码"};
	private DefaultTableCellRenderer render;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CodeToCode frame = new CodeToCode();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CodeToCode() {
		all.put("8421BCD码", "");
		all.put("二进制码", "");
		all.put("十进制码", "");
		all.put("标准格雷码", "");
		all.put("余三码", "");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 470, 324);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("编码转换 by Guo Ziyang");
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menu = new JMenu();
		menu.setText("帮助");
		menuBar.add(menu);	
		menuItem = new JMenuItem("关于");
		menuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new About();
			}
		});
		menu.add(menuItem);
	
		textField = new JTextField();
		textField.setBounds(19, 41, 207, 37);
		contentPane.add(textField);
		textField.setColumns(10);
		
		label = new JLabel("请在此输入要转换的编码");
		label.setBounds(51, 25, 150, 16);
		contentPane.add(label);
		
		comboBox = new JComboBox<String>();
		comboBox.setBounds(230, 42, 124, 37);
		comboBox.addItem("8421BCD码");
		comboBox.addItem("二进制码");
		comboBox.addItem("十进制码");
		comboBox.addItem("标准格雷码");
		comboBox.addItem("余三码");
		comboBox.setSelectedItem("8421BCD码");
		contentPane.add(comboBox);
		
		label_1 = new JLabel("这是");
		label_1.setBounds(276, 25, 33, 16);
		contentPane.add(label_1);
		
		for(int i = 0; i < 5; i ++){
			elements[i][1] = "";
		}
		elements[0][0] = "8421BCD码";
		elements[1][0] = "二进制码";
		elements[2][0] = "十进制码";
		elements[3][0] = "标准格雷码";
		elements[4][0] = "余三码";
		dtm = new DefaultTableModel(elements, colnames);
		render = new DefaultTableCellRenderer();
		render.setHorizontalAlignment(SwingConstants.CENTER);
		table = new MyTable(dtm);
		table.getColumn("编码名").setCellRenderer(render);
		table.getColumn("编码").setCellRenderer(render);
		table.getTableHeader().setFont(new Font("Dialog", 0, 11));
		table.setRowHeight(28);
		table.setFont(new Font("Dialog", 0, 14));

		scrollPane = new JScrollPane();
		scrollPane.setBounds(19, 90, 432, 160);
		scrollPane.setViewportView(table);
		contentPane.add(scrollPane);
		
		button = new JButton("转换");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String temp8421 = changeTo8421(textField.getText());
				changeToEveryone(temp8421);
				changeToBoard();
			}
		});
		button.setBounds(359, 46, 93, 29);
		contentPane.add(button);
	}
	
	public String changeTo8421(String raw) {
		int choice = comboBox.getSelectedIndex();
		String temp8421 = "";
		if(choice == 0) {
			temp8421 = new String(raw);
		}else if(choice == 1) {
			int tenNumber = -1;
			try {
				tenNumber = Integer.parseInt(raw, 2);
			}catch(Exception e) {
				System.out.print(e);
			}
			String tenNumberStr = String.valueOf(tenNumber);
			for(int i = 0; i < tenNumberStr.length(); i++) {
				int temp = tenNumberStr.charAt(i) - '0';
				temp8421 += String.format("%04d", Integer.parseInt(Integer.toBinaryString(temp)));
			}
		}else if(choice == 2){
			for(int i = 0; i < raw.length(); i ++) {
				int temp = raw.charAt(i) - '0';
				temp8421 += String.format("%04d", Integer.parseInt(Integer.toBinaryString(temp)));
			}
		}else if(choice == 3){
			temp8421 = String.valueOf(raw.charAt(0));
			for(int i = 0; i < raw.length() - 1; i ++){
				if(temp8421.charAt(i) == raw.charAt(i + 1)){
					temp8421 += "0";
				}else{
					temp8421 += "1";
				}
			}
		}else if(choice == 4){
			int rawlength = raw.length();
			int num = rawlength / 4;
			String[] result = new String[num];
			int i = 0;
			while(rawlength >= 4){
				result[i] = raw.substring(0, 4);
				i ++;
				raw = raw.substring(4);
				rawlength = raw.length();
			}
			//for(String res:result){
			//	System.out.println(res);
			//}
			for(String res:result){
				int k = 3;
				StringBuffer sb = new StringBuffer(res);
				while(k > 0){
					if(sb.charAt(3) == '1'){
						sb.setCharAt(3, '0');
					}else{
						sb.setCharAt(3, '1');
						if(sb.charAt(2) == '1'){
							sb.setCharAt(2, '0');
						}else{
							sb.setCharAt(2, '1');
							if(sb.charAt(1) == '1'){
								sb.setCharAt(1, '0');
							}else{
								sb.setCharAt(1, '1');
								if(sb.charAt(0) == '1'){
									sb.setCharAt(0, '0');
								}
							}
						}
					}
					System.out.println(sb.toString());
					k --;
				}
				temp8421 += sb.toString();
			}
		}
		//System.out.println(temp8421);
		return temp8421;
	}
	
	public void changeToEveryone(String temp8421) {
		all.put("8421BCD码", temp8421);
		String raw = new String(temp8421);
		int rawlength = raw.length();
		int num = rawlength / 4;
		String[] result = new String[num];
		int i = 0;
		while(rawlength >= 4){
			result[i] = raw.substring(0, 4);
			i ++;
			raw = raw.substring(4);
			rawlength = raw.length();
		}
		String tenNumberStr = "";
		for(String res:result){
			tenNumberStr += String.valueOf(Integer.parseInt(String.valueOf(res.charAt(0))) * 8 + Integer.parseInt(String.valueOf(res.charAt(1))) * 4 + Integer.parseInt(String.valueOf(res.charAt(2))) * 2 + Integer.parseInt(String.valueOf(res.charAt(3))));
		}
		all.put("十进制码", tenNumberStr);
		int tenNumber = Integer.parseInt(tenNumberStr);
		String twoNumber = Integer.toBinaryString(tenNumber);
		all.put("二进制码", twoNumber);
		String gray = String.valueOf(twoNumber.charAt(0));
		for(i = 0; i < twoNumber.length() - 1; i ++){
			if(twoNumber.charAt(i) == twoNumber.charAt(i + 1)){
				gray += "0";
			}else{
				gray += "1";
			}
		}
		all.put("标准格雷码", gray);
		String yuthree = "";
		for(String res:result){
			int k = 3;
			StringBuffer sb = new StringBuffer(res);
			while(k > 0){
				if(sb.charAt(3) == '0'){
					sb.setCharAt(3, '1');
				}else{
					sb.setCharAt(3, '0');
					if(sb.charAt(2) == '0'){
						sb.setCharAt(2, '1');
					}else{
						sb.setCharAt(2, '0');
						if(sb.charAt(1) == '0'){
							sb.setCharAt(1, '1');
						}else{
							sb.setCharAt(1, '0');
							if(sb.charAt(0) == '0'){
								sb.setCharAt(0, '1');
							}
						}
					}
				}
				k --;
			}
			yuthree += sb.toString();
		}
		all.put("余三码", yuthree);
	}
	
	public void changeToBoard() {
		elements[0][1] = all.get("8421BCD码");
		elements[1][1] = all.get("二进制码");
		elements[2][1] = all.get("十进制码");
		elements[3][1] = all.get("标准格雷码");
		elements[4][1] = all.get("余三码");
		dtm = new DefaultTableModel(elements, colnames);
		//System.out.println(all.get("8421BCD码") + "--" + all.get("二进制码") + "--" + all.get("十进制码") + "--" + all.get("标准格雷码") + "--" + all.get("余三码"));
		table.setModel(dtm);
		table.getColumn("编码名").setCellRenderer(render);
		table.getColumn("编码").setCellRenderer(render);
	}

	class About extends JFrame{
		private static final long serialVersionUID = 1L;
		public About() {
			setTitle("关于");
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			setSize(300, 230);
			setLocationRelativeTo(null);
			
			JPanel aboutPane = new JPanel();
			aboutPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			setContentPane(aboutPane);
			aboutPane.setLayout(null);
			
			JLabel aboutlabel = new JLabel();
			String aboutstr = "<html><body><center>一个简单的编码转换器。<br>可以实现各种编码转换<br>欢迎贡献你的代码帮助我提升<br>如果你觉得我做的好<br>请打开我的GitHub给我一个star<br><br>My Gmail：guoziyang0033@gmail.com<br>My Github：<br>https://github.com/CN-GuoZiyang</center></body></html>";
			aboutlabel.setText(aboutstr);
			aboutlabel.setBounds(30, 8, 310, 160);
			aboutPane.add(aboutlabel);
			
			JButton opengithub = new JButton("打开github");
			opengithub.setBounds(10, 170, 110, 29);
			opengithub.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Desktop desktop = Desktop.getDesktop();  
					try {
						desktop.browse(new URI("https://github.com/CN-GuoZiyang"));
					}catch(Exception e1) {
						System.out.print(e1);
					}
				}
			});
			aboutPane.add(opengithub);
			
			JButton yes = new JButton("确定");
			yes.setBounds(175, 170, 110, 29);
			yes.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
				}
			});
			aboutPane.add(yes);
			
			setVisible(true);
		}
	}
	
	class MyTable extends JTable{
		private static final long serialVersionUID = 1L;

		public MyTable(DefaultTableModel dlm){
			super(dlm);
		}

		public boolean isCellEditable(int row, int column) {
     		return false;
		}
	}
}
