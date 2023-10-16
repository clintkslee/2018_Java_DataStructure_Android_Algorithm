import javax.swing.JLabel;
import javax.swing.border.*;
import java.awt.Color;
import java.awt.event.*;
import java.awt.Cursor;
import java.awt.Dimension;

class Node extends JLabel{
	private String textName;
	private Node leftChild;
	private Node rightSibbling;
	private Node parent;
	private int tempx, tempy;
	private int sizecontrolpoint;
	private int x;
	private int y;
	private int w;
	private int h;
	private int color;
	private MyFrame frame;
	private int numOfChildren;
	private int nodeLevel;
	private double myAngle;
	private LinePanel LP;
	static final long serialVersionUID = 42L;
	public Node() { //for root node
		super();
		x=0;
		y=0;
		w=100; h=70;
		nodeLevel=-1;
		myAngle = 0;
		this.leftChild=null;
		this.rightSibbling=null;
		this.parent = null;
		this.setName(null);
		setBorder(new LineBorder(Color.BLACK));
		setOpaque(true);
		setBackground(new Color(0xBFFF));
		setHorizontalAlignment(JLabel.CENTER);
		setBounds(x, y, w, h);
		numOfChildren = 0;
	}
	public Node (String textName) {
		super(textName);
		this.textName = textName; 
		x = 0;
		y= 0;
		w=100; h=70;
		nodeLevel=0;
		myAngle = 0;
		color = 0x00BFFF;
		this.leftChild=null;
		this.rightSibbling=null;
		this.parent = null;
		setBorder(new LineBorder(Color.BLACK));
		setOpaque(true);
		setBackground(new Color(color));
		setHorizontalAlignment(JLabel.CENTER);
		setBounds(x, y, w, h);
		numOfChildren = 0;
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setFocusable(true);
				requestFocus();
				setAttributePane();
			}
			public void mousePressed(MouseEvent e) {
				tempx=e.getX();
				tempy=e.getY();
				if(tempx<=3) {
					if(tempy<=3)
						sizecontrolpoint=8;
					else if(tempy>=h-3)
						sizecontrolpoint=6;
					else
						sizecontrolpoint=7;
				}
				else if(tempx>=w-3) {
					if(tempy<=3)
						sizecontrolpoint=2;
					else if(tempy>=h-3)
						sizecontrolpoint=4;
					else
						sizecontrolpoint=3;
				}
				else if(tempy<=3)
					sizecontrolpoint=1;
				else if(tempy>=h-3)
					sizecontrolpoint=5;
				else
					sizecontrolpoint=0;
			}
			public void mouseReleased(MouseEvent e) {
				setPaneSize();
			}
		});
		this.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				int ex=e.getX();
				int ey=e.getY();
				switch(sizecontrolpoint) {
					case 0:
						x+=ex-tempx;
						y+=ey-tempy;
						setLocation(x,y);
						break;
					case 1:
						h-=ey-tempy;
						if(h<10)h=10;
						else y+=ey-tempy;
						setBounds(x,y,w,h);
						break;
					case 2:
						h-=ey-tempy;
						if(h<10)h=10;
						else y+=ey-tempy;
						w+=ex-tempx;
						if(w<10)w=10;
						tempx=ex;
						setBounds(x,y,w,h);
						break;
					case 3:
						w+=ex-tempx;
						if(w<10)w=10;
						tempx=ex;
						setBounds(x,y,w,h);
						break;
					case 4:
						w+=ex-tempx;
						if(w<10)w=10;
						tempx=ex;
						h+=ey-tempy;
						if(h<10)h=10;
						tempy=ey;
						setBounds(x,y,w,h);
						break;
					case 5:
						h+=ey-tempy;
						if(h<10)h=10;
						tempy=ey;
						setBounds(x,y,w,h);
						break;
					case 6:
						w-=ex-tempx;
						if(w<10)w=10;
						else x+=ex-tempx;
						h+=ey-tempy;
						if(h<10)h=10;
						tempy=ey;
						setBounds(x,y,w,h);
						break;
					case 7:
						w-=ex-tempx;
						if(w<10)w=10;
						else x+=ex-tempx;
						setBounds(x,y,w,h);
						break;
					case 8:
						w-=ex-tempx;
						if(w<10)w=10;
						else x+=ex-tempx;
						h-=ey-tempy;
						if(h<10)h=10;
						else y+=ey-tempy;
						setBounds(x,y,w,h);
						break;
				}
				setAttributePane();
				if(LP!=null) {
					frame.jp2c.remove(LP);
					LP=new LinePanel((Node)e.getSource());
					LP.repaint();
					frame.jp2c.add(LP);
				}
				if(getChild()!=null) {
					Node cur=getChild();
					if(cur.getLP()!=null)
						frame.jp2c.remove(cur.getLP());
					LinePanel childLP=new LinePanel(cur);
					cur.setLP(childLP);
					frame.jp2c.add(childLP);
					cur.getLP().repaint();
					while(true) {
						if(cur.getSibbling()==null)break;
						else {
							cur=cur.getSibbling();
							if(cur.getLP()!=null)
								frame.jp2c.remove(cur.getLP());
							childLP=new LinePanel(cur);
							cur.setLP(childLP);
							frame.jp2c.add(childLP);
							cur.getLP().repaint();
						}
					}
				}
				frame.jp2c.repaint();
				frame.revalidate();
			}
			public void mouseMoved(MouseEvent e) {
				int ex2=e.getX();
				int ey2=e.getY();
				if(ex2<=3) {
					if(ey2<=3)
						setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
					else if(ey2>=h-3)
						setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
					else
						setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
				}
				else if(ex2>=w-3) {
					if(ey2<=3)
						setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
					else if(ey2>=h-3)
						setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
					else
						setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
				}
				else if(ey2<=3)
					setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
				else if(ey2>=h-3)
					setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
				else
					setCursor(Cursor.getDefaultCursor());
			}
		});
		this.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				frame.focusedNode = (Node)frame.getFocusOwner();
				setBackground(new Color(~(color)&0xFFFFFF));
				setAttributePane();
			}
			public void focusLost(FocusEvent e) {
				setBackground(new Color(color));
				frame.nodeColor.setBackground(new Color(0xFF,0xFF,0xFF));
			}
		});
	}
	public Node(String textName, int x, int y, int w, int h, int color) {
		this(textName);
		this.x=x;
		this.y=y;
		this.w=w;
		this.h=h;
		this.color=color;
		setBounds(x, y, w, h);
		setBackground(new Color(color));
	}
	public String mygetText() {return textName;}
	public int mygetX() {return x;}
	public int mygetY() {return y;}
	public int getW() {return w;}
	public int getH() {return h;}
	public int getColor() {return color;}
	public Node getChild() {return leftChild;}
	public Node getSibbling() {return rightSibbling;}
	public Node mygetParent() {return parent;}
	public int getNOC() {return numOfChildren;}
	public double getmyAngle() {return myAngle;}
	public int getLevel() {return nodeLevel;}
	public void setX(int x) {this.x = x;}
	public void setY(int y) {this.y = y;}
	public void setW(int w) {this.w = w;}
	public void setH(int h) {this.h = h;}
	public void setColor(int color) {this.color = color;setBackground(new Color(color));}
	public void setChild(Node ref) {leftChild = ref;}
	public void setSibbling(Node ref) {rightSibbling = ref;}
	public void setParent(Node ref) {parent = ref;}
	public void setNOC(int num) {numOfChildren = num;}
	public void setmyAngle(double num) {myAngle = num;}
	public void setLevel(int lv) {nodeLevel = lv;}
	public void setFrame(MyFrame ref) {frame = ref;}
	public void setAttributePane() {
		if(frame.getFocusOwner()==this) {
			frame.nodeText.setText(textName.trim());
			frame.nodeXPos.setText(""+x);
			frame.nodeYPos.setText(""+y);
			frame.nodeWidth.setText(""+w);
			frame.nodeHeight.setText(""+h);
			frame.nodeColor.setText(String.format("%06X", color));
			frame.nodeColor.setBackground(new Color(color));
		}
	}
	public void setPaneSize() {
		if(x+w>frame.jp2c.getPreferredSize().getWidth()) {
			frame.jp2c.setPreferredSize(new Dimension(x+w,(int)frame.jp2c.getPreferredSize().getHeight()));
		}
		if(y+h>frame.jp2c.getPreferredSize().getHeight()) {
			frame.jp2c.setPreferredSize(new Dimension((int)frame.jp2c.getPreferredSize().getWidth(),y+h));
		}
		if(x<0) {
			int minusvalue=x-5;
			frame.jp2c.setPreferredSize(new Dimension((int)frame.jp2c.getPreferredSize().getWidth()-minusvalue,(int)frame.jp2c.getPreferredSize().getHeight()));
			Node cur=frame.root;
			boolean movechild=true;
			while(true) {
				LinePanel LPanel;
				if(cur.getChild()!=null&&movechild==true) {
					cur=cur.getChild();
					cur.setX(cur.getX()-minusvalue);
					cur.setBounds(cur.mygetX(),cur.mygetY(),cur.getW(),cur.getH());
					if(cur.mygetParent()!=frame.root) {
						if(cur.getLP()!=null)
							frame.jp2c.remove(cur.getLP());
						LPanel=new LinePanel(cur);
						frame.jp2c.add(LPanel);
						cur.setLP(LPanel);
						LPanel.repaint();
					}
					continue;
				}
				if(cur.getSibbling()!=null) {
					movechild=true;
					cur=cur.getSibbling();
					cur.setX(cur.getX()-minusvalue);
					cur.setBounds(cur.mygetX(),cur.mygetY(),cur.getW(),cur.getH());
					if(cur.mygetParent()!=frame.root) {
						if(cur.getLP()!=null)
							frame.jp2c.remove(cur.getLP());
						LPanel=new LinePanel(cur);
						frame.jp2c.add(LPanel);
						cur.setLP(LPanel);
						LPanel.repaint();
					}
					continue;
				}
				else {
					movechild=false;
					cur=cur.mygetParent();
					if(cur==frame.root)
						break;
				}
			}
		}
		if(y<0) {
			int minusvalue=y-5;
			frame.jp2c.setPreferredSize(new Dimension((int)frame.jp2c.getPreferredSize().getWidth(),(int)frame.jp2c.getPreferredSize().getHeight()-minusvalue));
			Node cur=frame.root;
			boolean movechild=true;
			while(true) {
				LinePanel LPanel;
				if(cur.getChild()!=null&&movechild==true) {
					cur=cur.getChild();
					cur.setY(cur.getY()-minusvalue);
					cur.setBounds(cur.mygetX(),cur.mygetY(),cur.getW(),cur.getH());
					if(cur.mygetParent()!=frame.root) {
						if(cur.getLP()!=null)
							frame.jp2c.remove(cur.getLP());
						LPanel=new LinePanel(cur);
						frame.jp2c.add(LPanel);
						cur.setLP(LPanel);
						LPanel.repaint();
					}
					continue;
				}
				if(cur.getSibbling()!=null) {
					movechild=true;
					cur=cur.getSibbling();
					cur.setY(cur.getY()-minusvalue);
					cur.setBounds(cur.mygetX(),cur.mygetY(),cur.getW(),cur.getH());
					if(cur.mygetParent()!=frame.root) {
						if(cur.getLP()!=null)
							frame.jp2c.remove(cur.getLP());
						LPanel=new LinePanel(cur);
						frame.jp2c.add(LPanel);
						cur.setLP(LPanel);
						LPanel.repaint();
					}
					continue;
				}
				else {
					movechild=false;
					cur=cur.mygetParent();
					if(cur==frame.root)
						break;
				}
			}
		}
		frame.jp2c.repaint();
		frame.jp2c.revalidate();
	}
	public void setB() {setBounds(x,y,w,h);}
	public void setLP(LinePanel LP) {this.LP=LP;}
	public LinePanel getLP() {return LP;}
}