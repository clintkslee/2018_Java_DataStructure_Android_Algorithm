import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import javax.swing.JOptionPane;
import java.io.FileWriter;
import org.json.simple.*;
import java.io.FileReader;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileNotFoundException;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;
import javax.swing.border.LineBorder;


public class MyGraph {

	public static void main (String[] args) {
		new MyFrame();
	}
}
class MyFrame extends JFrame {
	private JMenuBar mb;
	private JToolBar toolBar;
	private Container contentPane;
	private JTextArea jta1;
	private JScrollPane jsp1, jsp2; 
	protected JPanel jp1, jp2, jp3, jp2c, jp3c;
	private JSplitPane split, split2;
	private JButton applyButton, changeButton;
	protected JTextField nodeText, nodeXPos, nodeYPos, nodeWidth, nodeHeight, nodeColor;
	protected Node root;
	private MyFrame myJFrame;
	protected Node focusedNode;
	private String tempPath;
	static final long serialVersionUID = 42L;
	public MyFrame() {
		myJFrame = this;
		tempPath=null;
		setTitle("타이틀문자열");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = getContentPane();
		root = new Node();
		createMenu();
		createToolBar();
		createSplitPane();
		toolBar.setBackground(new Color(0xffffff));
		mb.setBackground(new Color(0xffffff));
		mb.setBorder(new LineBorder(new Color(0xF0F3F3).darker()));
		jp2c.setBackground(new Color(0xfdfd96).brighter());
		jp3.setBackground(new Color(0xfdfd96).brighter());
		jp3c.setBackground(new Color(0xfdfd96).brighter());
		jp1.setBackground(new Color(0xfdfd96).brighter());
		jta1.setBorder(new LineBorder(new Color(0xffffff)));
		jsp1.setBorder(new LineBorder(new Color(0xffffff)));
		jp2c.setBorder(new LineBorder(new Color(0xfdfd96).brighter()));
		jsp2.setBorder(new LineBorder(new Color(0xfdfd96).brighter()));
		jp2.setBackground(new Color(0xffffff));

		setSize(1440, 900);
		setLocationRelativeTo(null);
		setVisible(true);
		
	}	
	private void createMenu() {
		mb = new JMenuBar();
		mb.add(new JMenu("파일"));
		mb.add(new JMenu("닫기"));
		closeActionListener closeAL=new closeActionListener();
		mb.getMenu(1).addMouseListener(closeAL);
		mb.add(new JMenu("적용"));
		applyActionListener applyAL=new applyActionListener();
		mb.getMenu(2).addMouseListener(applyAL);
		mb.add(new JMenu("변경"));
		changeActionListener changeAL=new changeActionListener();
		mb.getMenu(3).addMouseListener(changeAL);
		mb.getMenu(0).add(new JMenuItem("새로만들기"));
		mb.getMenu(0).add(new JMenuItem("열기"));
		mb.getMenu(0).addSeparator();
		mb.getMenu(0).add(new JMenuItem("저장"));
		mb.getMenu(0).add(new JMenuItem("다른 이름으로 저장"));
		newActionListener newAL=new newActionListener();
		mb.getMenu(0).getItem(0).addActionListener(newAL);
		openActionListener openAL=new openActionListener();
		mb.getMenu(0).getItem(1).addActionListener(openAL);
		saveActionListener saveAL=new saveActionListener();
		mb.getMenu(0).getItem(3).addActionListener(saveAL);
		saveAsActionListener saveAsAL=new saveAsActionListener();
		mb.getMenu(0).getItem(4).addActionListener(saveAsAL);
		setJMenuBar(mb);	
	}
	
	private void createToolBar() {
		toolBar = new JToolBar("ToolBar");
		toolBar.setFloatable(false);
		toolBar.setBackground(Color.LIGHT_GRAY);
		
		toolBar.add(new JButton("New"));
		newActionListener newAL=new newActionListener();
		toolBar.getComponentAtIndex(0).addMouseListener(newAL);
		toolBar.getComponentAtIndex(0).setBackground(new Color(0xffffff));
		toolBar.add(new JButton("Open"));
		openActionListener openAL=new openActionListener();
		toolBar.getComponentAtIndex(1).addMouseListener(openAL);
		toolBar.getComponentAtIndex(1).setBackground(new Color(0xffffff));
		toolBar.add(new JButton("Save"));
		saveActionListener saveAL=new saveActionListener();
		toolBar.getComponentAtIndex(2).addMouseListener(saveAL);
		toolBar.getComponentAtIndex(2).setBackground(new Color(0xffffff));
		toolBar.add(new JButton("Save As"));
		saveAsActionListener saveAsAL=new saveAsActionListener();
		toolBar.getComponentAtIndex(3).addMouseListener(saveAsAL);
		toolBar.getComponentAtIndex(3).setBackground(new Color(0xffffff));
		toolBar.add(new JButton("Close"));
		closeActionListener closeAL=new closeActionListener();
		toolBar.getComponentAtIndex(4).addMouseListener(closeAL);
		toolBar.getComponentAtIndex(4).setBackground(new Color(0xffffff));
		toolBar.add(new JButton("Apply"));
		applyActionListener applyAL=new applyActionListener();
		toolBar.getComponentAtIndex(5).addMouseListener(applyAL);
		toolBar.getComponentAtIndex(5).setBackground(new Color(0xffffff));
		toolBar.add(new JButton("Change"));
		changeActionListener changeAL=new changeActionListener();
		toolBar.getComponentAtIndex(6).addMouseListener(changeAL);
		toolBar.getComponentAtIndex(6).setBackground(new Color(0xffffff));
		
		contentPane.add(toolBar, BorderLayout.NORTH);
	}
	private void createSplitPane() {
		jta1 = new JTextArea();
		jsp1 = new JScrollPane(jta1);
		jsp1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jp2 = new JPanel();
		jp2c = new JPanel();
		jp2c.setPreferredSize(new Dimension(1440,900));
		jp2c.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(focusedNode!=null) {
					nodeText.setText("");
					nodeXPos.setText("");
					nodeYPos.setText("");
					nodeWidth.setText("");
					nodeHeight.setText("");
					nodeColor.setText("");
					nodeColor.setBackground(new Color(0xFF,0xFF,0xFF));
					jp2c.setFocusable(true);
					jp2c.requestFocus();
					focusedNode.setColor(focusedNode.getColor());
					focusedNode=null;
				}
			}
		});
		jp2c.setLayout(null);
		jp2.setLayout(new BorderLayout(0,0));
		jsp2 = new JScrollPane(jp2c, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);		
		jp2.add(new JLabel("Mind Map Pane",SwingConstants.CENTER), BorderLayout.NORTH);
		jp2.add(jsp2, BorderLayout.CENTER);
		jsp2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		jp3 = new JPanel();
		jp1 = new JPanel();
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, jp1, jp2);
		split2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, split, jp3);
		jp1.setLayout(new BorderLayout(0,0));
		jp1.add(new JLabel("Text Editor Pane",SwingConstants.CENTER), BorderLayout.NORTH);
		jp1.add(jsp1, BorderLayout.CENTER);
		

		applyButton = new JButton("적용");
		applyActionListener applyAL=new applyActionListener();
		applyButton.addActionListener(applyAL);
		
		jp1.add(applyButton, BorderLayout.SOUTH);
		
		jp3.setLayout(new BorderLayout(0,50));
		jp3.add(new JLabel("Attribute Pane",SwingConstants.CENTER), BorderLayout.NORTH);
		jp3c = new JPanel();
		jp3c.setLayout(new GridLayout(6, 2, 0, 50));
		jp3c.add(new JLabel("TEXT",SwingConstants.CENTER));
		nodeText = new JTextField("");
		nodeText.setEditable(false);
		nodeText.setHorizontalAlignment(JTextField.CENTER);
		jp3c.add(nodeText);
		jp3c.add(new JLabel("X",SwingConstants.CENTER));
		nodeXPos = new JTextField("");
		nodeXPos.setHorizontalAlignment(JTextField.CENTER);
		jp3c.add(nodeXPos);
		jp3c.add(new JLabel("Y",SwingConstants.CENTER));
		nodeYPos = new JTextField("");
		nodeYPos.setHorizontalAlignment(JTextField.CENTER);
		jp3c.add(nodeYPos);
		jp3c.add(new JLabel("W",SwingConstants.CENTER));
		nodeWidth = new JTextField("");
		nodeWidth.setHorizontalAlignment(JTextField.CENTER);
		jp3c.add(nodeWidth);
		jp3c.add(new JLabel("H",SwingConstants.CENTER));
		nodeHeight = new JTextField("");
		nodeHeight.setHorizontalAlignment(JTextField.CENTER);
		jp3c.add(nodeHeight);
		jp3c.add(new JLabel("COLOR",SwingConstants.CENTER));
		nodeColor = new JTextField("");
		//nodeColor.setBackground(new Color(00,0xbf,0xff));
		nodeColor.setHorizontalAlignment(JTextField.CENTER);
		nodeColor.setFocusable(false);
		nodeColor.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(focusedNode!=null) {
					JColorChooser chooser = new JColorChooser();
					Color selectedColor = chooser.showDialog(null, "Color", Color.CYAN);
					if(selectedColor !=null)
						focusedNode.setColor(selectedColor.getRGB());
				}
			}
		});
		jp3c.add(nodeColor);
		jp3.add(jp3c, BorderLayout.CENTER);
		changeButton = new JButton("변경");
		changeActionListener changeAL=new changeActionListener();
		changeButton.addActionListener(changeAL);
		
		jp3.add(changeButton, BorderLayout.SOUTH);
		
		split2.setDividerLocation(1140);
		split.setDividerLocation(300);
		contentPane.add(split2);
	}

	private void addTree (int ct,boolean opened) {
		int lineLength = 200;
		double forChildAngle = Math.PI/12;
		double forRootAngle = ((Math.PI)*2)/root.getChild().getNOC();
		double forRootAngleTemp = 0;
		double foroddAngleTemp;
		double forevenAngleTemp;
		LinePanel LPanel;
		int cnt;
		Node[] arr = new Node[ct+1];
		Node cur;
		int front=0, rear=0;
		arr[rear]=root.getChild();
		jp2c.add(arr[rear]);
		arr[rear].setPaneSize();
		rear++;
		cur=root.getChild();
		while(true) {
			if(cur.getSibbling()==null) {
				break;
			}
			else {
				cur=cur.getSibbling();
				arr[rear]=cur;
				jp2c.add(arr[rear]);
				arr[rear].setPaneSize();
				rear++;
			}
		}
		while(true) {
			if(rear==ct||front==ct)
				break;
			if(arr[front].getChild()==null) {
				front++;
				continue;
			}
			arr[rear]=arr[front].getChild();
			foroddAngleTemp=arr[front].getmyAngle();
			forevenAngleTemp=arr[front].getmyAngle()+Math.PI/12;
			if(opened==false) {
				if(arr[rear].getLevel()==1) {
					arr[rear].setX(arr[front].mygetX()+(int)(lineLength*Math.sin(forRootAngleTemp)));
					arr[rear].setY(arr[front].mygetY()-(int)(lineLength*Math.cos(forRootAngleTemp)));
					arr[rear].setmyAngle(forRootAngleTemp);
					forRootAngleTemp+=forRootAngle;
					arr[rear].setLocation(arr[rear].mygetX(),arr[rear].mygetY());
				}
				else {
					arr[rear].setX(arr[rear].mygetParent().mygetX()+(int)(lineLength*Math.sin(foroddAngleTemp)));
					arr[rear].setY(arr[rear].mygetParent().mygetY()-(int)(lineLength*Math.cos(foroddAngleTemp)));
					arr[rear].setmyAngle(foroddAngleTemp);
					foroddAngleTemp-=forChildAngle;
					forevenAngleTemp+=forChildAngle;
					arr[rear].setLocation(arr[rear].mygetX(),arr[rear].mygetY());
				}
			}
			jp2c.add(arr[rear]);
			if(arr[rear].getLP()!=null)
				jp2c.remove(arr[rear].getLP());
			LPanel=new LinePanel(arr[rear]);
			jp2c.add(LPanel);
			arr[rear].setLP(LPanel);
			LPanel.repaint();
			arr[rear].setPaneSize();
			rear++;
			cur=arr[front].getChild();
			cnt=0;
			while(true) {
				if(cur.getSibbling()==null) {
					break;
				}
				else {
					cur=cur.getSibbling();
					arr[rear]=cur;
					if(opened==false) {
						if(arr[rear].getLevel()==1) {
							arr[rear].setX(arr[front].mygetX()+(int)(lineLength*Math.sin(forRootAngleTemp)));
							arr[rear].setY(arr[front].mygetY()-(int)(lineLength*Math.cos(forRootAngleTemp)));
							arr[rear].setmyAngle(forRootAngleTemp);
							forRootAngleTemp+=forRootAngle;
							arr[rear].setLocation(arr[rear].mygetX(),arr[rear].mygetY());
						}
						else {
							if(cnt%2==0) {
								arr[rear].setX(arr[front].mygetX()+(int)(lineLength*Math.sin(forevenAngleTemp)));
								arr[rear].setY(arr[front].mygetY()-(int)(lineLength*Math.cos(forevenAngleTemp)));
								arr[rear].setmyAngle(forevenAngleTemp);
								foroddAngleTemp-=forChildAngle;
								forevenAngleTemp+=forChildAngle;
								arr[rear].setLocation(arr[rear].mygetX(),arr[rear].mygetY());
								cnt++;
							}
							else if(cnt%2==1) {
								arr[rear].setX(arr[front].mygetX()+(int)(lineLength*Math.sin(foroddAngleTemp)));
								arr[rear].setY(arr[front].mygetY()-(int)(lineLength*Math.cos(foroddAngleTemp)));
								arr[rear].setmyAngle(foroddAngleTemp);
								foroddAngleTemp-=forChildAngle;
								forevenAngleTemp+=forChildAngle;
								arr[rear].setLocation(arr[rear].mygetX(),arr[rear].mygetY());
								cnt++;
							}
						}
					}
					jp2c.add(arr[rear]);
					if(arr[rear].getLP()!=null)
						jp2c.remove(arr[rear].getLP());
					LPanel=new LinePanel(arr[rear]);
					jp2c.add(LPanel);
					arr[rear].setLP(LPanel);
					LPanel.repaint();
					arr[rear].setPaneSize();
					rear++;
				}
			}
			front++;
		}
	}
	
	class newActionListener extends MouseAdapter implements ActionListener{
		public void mouseClicked(MouseEvent e) {
			newAction();
		}
		public void actionPerformed(ActionEvent e) {
			newAction();
		}
		public void newAction() {
			System.out.println("호출");
			 if(jta1.getText().equals("")==true || root.getChild()==null) {
				root = new Node();
				jta1.setText("");
				jp2c.removeAll();
				nodeText.setText("");
				nodeXPos.setText("");
				nodeYPos.setText("");
				nodeWidth.setText("");
				nodeHeight.setText("");
				nodeColor.setText("");
				jp2c.revalidate();
				jp2c.repaint();
		    }
			else if(jta1.getText().equals("")==false && root.getChild()!=null) {
				int result = JOptionPane.showConfirmDialog(null, "저장하시겠습니까?", "Mind Map", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				int result1 = -1;
				switch(result) {
					case JOptionPane.YES_OPTION:
						JSONObject obj = new JSONObject();
						boolean movechild=true;
						Node cur=root;
						String str="";
						JSONArray xarray=new JSONArray();
						JSONArray yarray=new JSONArray();
						JSONArray warray=new JSONArray();
						JSONArray harray=new JSONArray();
						JSONArray colorarray=new JSONArray();
						while(true){
							if(cur.getChild()!=null&&movechild==true) {
								cur=cur.getChild();
								str+=cur.mygetText()+"\n";
								xarray.add(cur.mygetX());
								yarray.add(cur.mygetY());
								warray.add(cur.getW());
								harray.add(cur.getH());
								colorarray.add(cur.getColor());
								continue;
							}
							if(cur.getSibbling()!=null) {
								movechild=true;
								cur=cur.getSibbling();
								str+=cur.mygetText()+"\n";
								xarray.add(cur.mygetX());
								yarray.add(cur.mygetY());
								warray.add(cur.getW());
								harray.add(cur.getH());
								colorarray.add(cur.getColor());
								continue;
							}
							else {
								movechild=false;
								cur=cur.mygetParent();
								if(cur==root)
									break;
							}
						}
						obj.put("string", str);
						obj.put("x", xarray);
						obj.put("y", yarray);
						obj.put("w", warray);
						obj.put("h", harray);
						obj.put("color", colorarray);
						/////////////////////////////////////////
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setFileFilter(new FileNameExtensionFilter("json", "json"));
					    fileChooser.setMultiSelectionEnabled(false);
					    if(fileChooser.showSaveDialog(myJFrame) == JFileChooser.APPROVE_OPTION) {
					    	try {
					    		while(true) {
					    			File f = new File(fileChooser.getSelectedFile().toString()+".json");
					    			if(f.isFile()) {
					    				result1 = JOptionPane.showConfirmDialog(fileChooser, "지정된 경로에 파일이 존재합니다. 덮어씌우시겠습니까?", "저장", JOptionPane.YES_NO_CANCEL_OPTION);
					    				if(result1==JOptionPane.YES_OPTION) {
							    			FileWriter file = new FileWriter(fileChooser.getSelectedFile().toString()+".json");
					    					file.write(obj.toJSONString());
					    					file.flush();
					    					file.close();
					    					tempPath=fileChooser.getSelectedFile().toString()+".json";
					    					break;
					    				}
					    				else if(result1==JOptionPane.NO_OPTION) {
					    					fileChooser.showSaveDialog(fileChooser);
					    				}
					    				else if(result1==JOptionPane.CANCEL_OPTION){
					    					System.out.println("Cancel");
					    					break;
					    				}
					    			}
					    			else {
						    			FileWriter file = new FileWriter(fileChooser.getSelectedFile().toString()+".json");
					    				file.write(obj.toJSONString());
						    			file.flush();
						    			file.close();
						    			tempPath=fileChooser.getSelectedFile().toString()+".json";
						    			break;
					    			}
					    		}
					    	} catch (IOException exps) { exps.printStackTrace(); }
					    }
					case JOptionPane.NO_OPTION:
					if(result1!=JOptionPane.CANCEL_OPTION)
					{
						root = new Node();
						jta1.setText("");
						jp2c.removeAll();
						nodeText.setText("");
						nodeXPos.setText("");
						nodeYPos.setText("");
						nodeWidth.setText("");
						nodeHeight.setText("");
						nodeColor.setText("");
						jp2c.revalidate();
						jp2c.repaint();
					}
					break;
				}
			}
		}
	}
	class openActionListener extends MouseAdapter implements ActionListener{
		public void mouseClicked(MouseEvent e) {
			openAction();
		}
		public void actionPerformed(ActionEvent e) {
			openAction();
		}
		public void openAction() {
			JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			jfc.setFileFilter(new FileNameExtensionFilter("json", "json"));
			int returnValue = jfc.showOpenDialog(myJFrame);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				try{
					jp2c.removeAll();
					JSONParser parser=new JSONParser();
					Object obj=parser.parse(new FileReader(jfc.getSelectedFile().toString()));
					tempPath=jfc.getSelectedFile().toString();
					JSONObject jsonobject=(JSONObject) obj;
					String str=(String)jsonobject.get("string");
					jta1.setText(str);
					JSONArray xarray=(JSONArray)jsonobject.get("x");
					JSONArray yarray=(JSONArray)jsonobject.get("y");
					JSONArray warray=(JSONArray)jsonobject.get("w");
					JSONArray harray=(JSONArray)jsonobject.get("h");
					JSONArray colorarray=(JSONArray)jsonobject.get("color");
					Iterator<Long> xiterator=xarray.iterator();
					Iterator<Long> yiterator=yarray.iterator();
					Iterator<Long> witerator=warray.iterator();
					Iterator<Long> hiterator=harray.iterator();
					Iterator<Long> coloriterator=colorarray.iterator();
					jp2c.removeAll();//패널에 올려진 데이터 제거
					contentPane.revalidate();
					contentPane.repaint();
					root=new Node();
					StringTokenizer st = new StringTokenizer(jta1.getText(), "\n");
					int ct = st.countTokens();
					int tabCnt;
					char tabChar;
					Node[] nodeArr = new Node[ct];
					String tempString;
					for(int i=0; i<ct; i++) {
						tabCnt = 0;
						tempString = st.nextToken();
						for(int j=0; ; j++) {
							tabChar = tempString.charAt(j);
							if(tabChar=='	')
								tabCnt++;
							else
								break;
						}
						long x=xiterator.next();
						long y=yiterator.next();
						long w=witerator.next();
						long h=hiterator.next();
						long color=coloriterator.next();
						Node node = new Node(tempString,(int)x,(int)y,(int)w,(int)h,(int)color);
						node.setFrame(myJFrame);
						nodeArr[tabCnt] = node;
						node.setLevel(tabCnt);
						if(tabCnt==0) {
							node.setParent(root);
							node.mygetParent().setNOC(node.mygetParent().getNOC()+1);
							if(root.getChild() == null)
								root.setChild(node);
							else
							{
								Node cur = root.getChild();
								while(cur.getSibbling() != null)
									cur = cur.getSibbling();
								cur.setSibbling(node);
							}
						}
						else
						{
							node.setParent(nodeArr[tabCnt-1]);
							node.mygetParent().setNOC(node.mygetParent().getNOC()+1);
							if(nodeArr[tabCnt-1].getChild() == null)
								nodeArr[tabCnt-1].setChild(node);
							else
							{
								Node cur = nodeArr[tabCnt-1].getChild();
								while(cur.getSibbling() != null)
									cur = cur.getSibbling();
								cur.setSibbling(node);
							}
						}
					}
					addTree(ct,true);
					st=null;
				}
				catch(FileNotFoundException exc) {
					exc.printStackTrace();
				}
				catch(IOException exc) {
					exc.printStackTrace();
				}
				catch(ParseException exc) {
					exc.printStackTrace();
				}
			}
			jp2c.repaint();
			jp2c.revalidate();
		}
	}
	class saveActionListener extends MouseAdapter implements ActionListener{
		public void mouseClicked(MouseEvent e) {
			saveAction();
		}
		public void actionPerformed(ActionEvent e) {
			saveAction();
		}
		public void saveAction() {
			int result1=-1;
			JSONObject obj = new JSONObject();
			boolean movechild=true;
			Node cur=root;
			String str="";
			JSONArray xarray=new JSONArray();
			JSONArray yarray=new JSONArray();
			JSONArray warray=new JSONArray();
			JSONArray harray=new JSONArray();
			JSONArray colorarray=new JSONArray();
			while(true){
				if(cur.getChild()!=null&&movechild==true) {
					cur=cur.getChild();
					str+=cur.mygetText()+"\n";
					xarray.add(cur.mygetX());
					yarray.add(cur.mygetY());
					warray.add(cur.getW());
					harray.add(cur.getH());
					colorarray.add(cur.getColor());
					continue;
				}
				if(cur.getSibbling()!=null) {
					movechild=true;
					cur=cur.getSibbling();
					str+=cur.mygetText()+"\n";
					xarray.add(cur.mygetX());
					yarray.add(cur.mygetY());
					warray.add(cur.getW());
					harray.add(cur.getH());
					colorarray.add(cur.getColor());
					continue;
				}
				else {
					movechild=false;
					cur=cur.mygetParent();
					if(cur==root)
						break;
				}
			}
			obj.put("string", str);
			obj.put("x", xarray);
			obj.put("y", yarray);
			obj.put("w", warray);
			obj.put("h", harray);
			obj.put("color", colorarray);
			/////////////////////////////////////////
			if(tempPath==null) {//첫 저장
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter("json", "json"));
				fileChooser.setMultiSelectionEnabled(false);
				if(fileChooser.showSaveDialog(myJFrame) == JFileChooser.APPROVE_OPTION) {
			   		try {
			   			while(true) {
			   				File f = new File(fileChooser.getSelectedFile().toString()+".json");
			   				if(f.exists()) {
			   					result1 = JOptionPane.showConfirmDialog(fileChooser, "지정된 경로에 파일이 존재합니다. 덮어씌우시겠습니까?", "저장", JOptionPane.YES_NO_CANCEL_OPTION);
			   					if(result1==JOptionPane.YES_OPTION) {
			   						FileWriter file = new FileWriter(fileChooser.getSelectedFile().toString()+".json");
			   						file.write(obj.toJSONString());
			   						file.flush();
				    				file.close();
				    				tempPath=fileChooser.getSelectedFile().toString()+".json";
				    				break;
			   					}
			   					else if(result1==JOptionPane.NO_OPTION) {
				    				fileChooser.showSaveDialog(fileChooser);
				    			}
				    			else if(result1==JOptionPane.CANCEL_OPTION){
				    				break;
				    			}
					   		}
			   				else {
			   					FileWriter file = new FileWriter(fileChooser.getSelectedFile().toString()+".json");
			    				file.write(obj.toJSONString());
			    				file.flush();
				    			file.close();
				    			tempPath=fileChooser.getSelectedFile().toString()+".json";
				    			break;
			    			}
			   			}
			   		} catch (IOException exps) { exps.printStackTrace(); }
				}
			}//경로x
			else {//경로 있을 때
				try {
					FileWriter file = new FileWriter(tempPath);
					file.write(obj.toJSONString());
					file.flush();
    				file.close();
				} catch (IOException exps) { exps.printStackTrace(); }
			}
		}
	}
	class saveAsActionListener extends MouseAdapter implements ActionListener{
		public void mouseClicked(MouseEvent e) {
			saveAsAction();
		}
		public void actionPerformed(ActionEvent e) {
			saveAsAction();
		}
		public void saveAsAction() {
			int result1=-1;
			JSONObject obj = new JSONObject();
			boolean movechild=true;
			Node cur=root;
			String str="";
			JSONArray xarray=new JSONArray();
			JSONArray yarray=new JSONArray();
			JSONArray warray=new JSONArray();
			JSONArray harray=new JSONArray();
			JSONArray colorarray=new JSONArray();
			while(true){
				if(cur.getChild()!=null&&movechild==true) {
					cur=cur.getChild();
					str+=cur.mygetText()+"\n";
					xarray.add(cur.mygetX());
					yarray.add(cur.mygetY());
					warray.add(cur.getW());
					harray.add(cur.getH());
					colorarray.add(cur.getColor());
					continue;
				}
				if(cur.getSibbling()!=null) {
					movechild=true;
					cur=cur.getSibbling();
					str+=cur.mygetText()+"\n";
					xarray.add(cur.mygetX());
					yarray.add(cur.mygetY());
					warray.add(cur.getW());
					harray.add(cur.getH());
					colorarray.add(cur.getColor());
					continue;
				}
				else {
					movechild=false;
					cur=cur.mygetParent();
					if(cur==root)
						break;
				}
			}
			obj.put("string", str);
			obj.put("x", xarray);
			obj.put("y", yarray);
			obj.put("w", warray);
			obj.put("h", harray);
			obj.put("color", colorarray);
			/////////////////////////////////////////
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileNameExtensionFilter("json", "json"));
			fileChooser.setMultiSelectionEnabled(false);
			if(fileChooser.showSaveDialog(myJFrame) == JFileChooser.APPROVE_OPTION) {
		   		try {
		   			while(true) {
		   				File f = new File(fileChooser.getSelectedFile().toString()+".json");
		   				if(f.exists()) {
		   					result1 = JOptionPane.showConfirmDialog(fileChooser, "지정된 경로에 파일이 존재합니다. 덮어씌우시겠습니까?", "저장", JOptionPane.YES_NO_CANCEL_OPTION);
		   					if(result1==JOptionPane.YES_OPTION) {
		   						FileWriter file = new FileWriter(fileChooser.getSelectedFile().toString()+".json");
		   						file.write(obj.toJSONString());
		   						file.flush();
			    				file.close();
			    				tempPath=fileChooser.getSelectedFile().toString()+".json";
			    				break;
		   					}
		   					else if(result1==JOptionPane.NO_OPTION) {
			    				fileChooser.showSaveDialog(fileChooser);
			    			}
			    			else if(result1==JOptionPane.CANCEL_OPTION){
			    				break;
			    			}
				   		}
		   				else {
		   					FileWriter file = new FileWriter(fileChooser.getSelectedFile().toString()+".json");
		    				file.write(obj.toJSONString());
		    				file.flush();
			    			file.close();
			    			tempPath=fileChooser.getSelectedFile().toString()+".json";
			    			break;
		    			}
		   			}
		   		} catch (IOException exps) { exps.printStackTrace(); }
			}
		}
	}
	class closeActionListener extends MouseAdapter{
	    public void mouseClicked (MouseEvent e) {
	    	if(e.getSource().equals(mb.getMenu(2)))
	    		mb.getMenu(2).setSelected(false);
	    	clickClose();
		}
	    public void clickClose() {
			if(jta1.getText().equals("")==true || root.getChild()==null) {
				System.exit(0);
			}
			else if(jta1.getText().equals("")==false && root.getChild()!=null) {
				int result = JOptionPane.showConfirmDialog(null, "작업 중인 마인드맵을 저장하시겠습니까?", "Mind Map", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				int result1 = -1;
				switch(result) {
					case JOptionPane.YES_OPTION:
						JSONObject obj = new JSONObject();
						boolean movechild=true;
						Node cur=root;
						String str="";
						JSONArray xarray=new JSONArray();
						JSONArray yarray=new JSONArray();
						JSONArray warray=new JSONArray();
						JSONArray harray=new JSONArray();
						JSONArray colorarray=new JSONArray();
						while(true) {
							if(cur.getChild()!=null&&movechild==true) {
								cur=cur.getChild();
								str+=cur.mygetText()+"\n";
								xarray.add(cur.mygetX());
								yarray.add(cur.mygetY());
								warray.add(cur.getW());
								harray.add(cur.getH());
								colorarray.add(cur.getColor());
								continue;
							}
							if(cur.getSibbling()!=null) {
								movechild=true;
								cur=cur.getSibbling();
								str+=cur.mygetText()+"\n";
								xarray.add(cur.mygetX());
								yarray.add(cur.mygetY());
								warray.add(cur.getW());
								harray.add(cur.getH());
								colorarray.add(cur.getColor());
								continue;
							}
							else {
								movechild=false;
								cur=cur.mygetParent();
								if(cur==root)
									break;
							}
						}
						obj.put("string", str);
						obj.put("x", xarray);
						obj.put("y", yarray);
						obj.put("w", warray);
						obj.put("h", harray);
						obj.put("color", colorarray);
						/////////////////////////////////////////
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setFileFilter(new FileNameExtensionFilter("json", "json"));
						fileChooser.setMultiSelectionEnabled(false);
						if(fileChooser.showSaveDialog(myJFrame) == JFileChooser.APPROVE_OPTION) {
							try {
								while(true) {
									File f = new File(fileChooser.getSelectedFile().toString()+".json");
									if(f.isFile()) {
										result1 = JOptionPane.showConfirmDialog(fileChooser, "지정된 경로에 파일이 존재합니다. 덮어씌우시겠습니까?", "저장", JOptionPane.YES_NO_CANCEL_OPTION);
										if(result1==JOptionPane.YES_OPTION) {
											FileWriter file = new FileWriter(fileChooser.getSelectedFile().toString()+".json");
											file.write(obj.toJSONString());
											file.flush();
											file.close();
											tempPath=fileChooser.getSelectedFile().toString()+".json";
											break;
										}
										else if(result1==JOptionPane.NO_OPTION) {
											fileChooser.showSaveDialog(fileChooser);
										}
										else if(result1==JOptionPane.CANCEL_OPTION){
											System.out.println("Cancel");
											break;
										}
									}
									else {
										FileWriter file = new FileWriter(fileChooser.getSelectedFile().toString()+".json");
										file.write(obj.toJSONString());
										file.flush();
										file.close();
										tempPath=fileChooser.getSelectedFile().toString()+".json";
										break;
									}
								}
							} catch (IOException exps) { exps.printStackTrace(); }
						}
					case JOptionPane.NO_OPTION:
						if(result1!=JOptionPane.CANCEL_OPTION)	{
							System.exit(0);
						}
						break;
				}
			}
		}
	}
	class applyActionListener extends MouseAdapter implements ActionListener{
		public void mouseClicked(MouseEvent e) {
			if(e.getSource().equals(mb.getMenu(2)))
				mb.getMenu(2).setSelected(false);
			applyAction();
		}
		public void actionPerformed(ActionEvent e) {
			applyAction();
		}
		public void applyAction() {
			jp2c.removeAll();
			contentPane.revalidate();
			contentPane.repaint();
			root=new Node();
			nodeText.setText("");
			nodeXPos.setText("");
			nodeYPos.setText("");
			nodeWidth.setText("");
			nodeHeight.setText("");
			nodeColor.setText("");
			int calladdtree = 1;
			StringTokenizer st = new StringTokenizer(jta1.getText(), "\n");
			int ct = st.countTokens();
			int tabCnt;
			char tabChar;
			Node[] nodeArr = new Node[ct];
			String tempString;
			for(int i=0; i<ct; i++) {
				tabCnt = 0;
				tempString = st.nextToken();
				for(int j=0; ; j++) {
					tabChar = tempString.charAt(j);
					if(tabChar=='	')
						tabCnt++;
					else
						break;
				}
				if(tabCnt==0) {
					Node node = new Node(tempString,jp2c.getWidth()/2-50,jp2c.getHeight()/2-35,100,70,0x00bfff);
					node.setLevel(tabCnt);
					node.setFrame(myJFrame);
					nodeArr[tabCnt] = node;
					node.setParent(root);
					node.mygetParent().setNOC(node.mygetParent().getNOC()+1);
					if(root.getChild() == null)
						root.setChild(node);
					else
					{
						Node cur = root.getChild();
						while(cur.getSibbling() != null)
							cur = cur.getSibbling();
						cur.setSibbling(node);
					}
				}
				else
				{
					if(nodeArr[tabCnt-1]==null) {
						jta1.append("\n잘못된 입력입니다.");
						calladdtree=0;
						break;
					}
					Node node = new Node(tempString);
					node.setLevel(tabCnt);
					node.setFrame(myJFrame);
					nodeArr[tabCnt] = node;
					node.setParent(nodeArr[tabCnt-1]);
					node.mygetParent().setNOC(node.mygetParent().getNOC()+1);
					if(nodeArr[tabCnt-1].getChild() == null)
						nodeArr[tabCnt-1].setChild(node);
					else
					{
						Node cur = nodeArr[tabCnt-1].getChild();
						while(cur.getSibbling() != null)
							cur = cur.getSibbling();
						cur.setSibbling(node);
					}
				}
			}
			if(root.getChild()!=null&&calladdtree==1)
				addTree(ct,false);
			st=null;
		}
	}
	class changeActionListener extends MouseAdapter implements ActionListener{
		public void mouseClicked(MouseEvent e) {
			if(e.getSource().equals(mb.getMenu(3)))
				mb.getMenu(3).setSelected(false);
			changeAction();
		}
		public void actionPerformed(ActionEvent e) {
			changeAction();
		}
		public void changeAction() {
			if(focusedNode!=null) {
				try {
					focusedNode.setX(Integer.parseInt(nodeXPos.getText()));
				}catch(Exception e) {nodeXPos.setText(new Integer(focusedNode.mygetX()).toString());}
				try {
					focusedNode.setY(Integer.parseInt(nodeYPos.getText()));
				}catch(Exception e) {nodeYPos.setText(new Integer(focusedNode.mygetY()).toString());}
				try {
					focusedNode.setW(Integer.parseInt(nodeWidth.getText()));
				}catch(Exception e) {nodeWidth.setText(new Integer(focusedNode.getW()).toString());}
				try {
					focusedNode.setH(Integer.parseInt(nodeHeight.getText()));
				}catch(Exception e) {nodeHeight.setText(new Integer(focusedNode.getH()).toString());}
				focusedNode.setB();
				if(focusedNode.getLP()!=null)
					jp2c.remove(focusedNode.getLP());
				if(focusedNode.getLevel()>0) {
					LinePanel focusLP=new LinePanel(focusedNode);
					focusedNode.setLP(focusLP);
					jp2c.add(focusLP);
					focusLP.repaint();
				}
				if(focusedNode.getChild()!=null) {
					Node cur=focusedNode.getChild();
					if(cur.getLP()!=null)
						jp2c.remove(cur.getLP());
					LinePanel childLP=new LinePanel(cur);
					cur.setLP(childLP);
					jp2c.add(childLP);
					cur.getLP().repaint();
					while(true) {
						if(cur.getSibbling()==null)break;
						else {
							cur=cur.getSibbling();
							if(cur.getLP()!=null)
								jp2c.remove(cur.getLP());
							childLP=new LinePanel(cur);
							cur.setLP(childLP);
							jp2c.add(childLP);
							cur.getLP().repaint();
						}
					}
				}
				jp2c.repaint();
				jp2c.revalidate();
				focusedNode.setFocusable(true);
				focusedNode.requestFocus();
////////////////////////////////////////////////////////////
			}
		}
	}
}
