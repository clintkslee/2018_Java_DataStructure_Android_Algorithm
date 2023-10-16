import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

public class LinePanel extends JPanel {
	private Point[] Parray;// a1,a2,a3,a4,b1,b2,b3,b4;
	protected int x1, x2, y1, y2;
	private Node nodeForPaint;
	static final long serialVersionUID = 42L;
	int check;
	public LinePanel(Node node) {
		nodeForPaint = node;
		setCoordinatesForPaint();
		setShortestLine();
		setBounds(Math.min(x1, x2)-1, Math.min(y1, y2)-1, Math.abs(x1-x2)+2, Math.abs((y1-y2)+2));
		setBackground(new Color(255, 255, 255, 0));
	}
	public void setCoordinatesForPaint() {
		Parray=new Point[8];
		Parray[0] = new Point(nodeForPaint.mygetX()+nodeForPaint.getWidth()/2,nodeForPaint.mygetY());
		Parray[1] = new Point(nodeForPaint.mygetX()+nodeForPaint.getWidth(),nodeForPaint.mygetY()+nodeForPaint.getHeight()/2);
		Parray[2] = new Point(nodeForPaint.mygetX()+nodeForPaint.getWidth()/2,nodeForPaint.mygetY()+nodeForPaint.getHeight());
		Parray[3] = new Point(nodeForPaint.mygetX(),nodeForPaint.mygetY()+nodeForPaint.getHeight()/2);
		Parray[4] = new Point(nodeForPaint.mygetParent().mygetX()+nodeForPaint.mygetParent().getWidth()/2,nodeForPaint.mygetParent().mygetY());
		Parray[5] = new Point(nodeForPaint.mygetParent().mygetX()+nodeForPaint.mygetParent().getWidth(),nodeForPaint.mygetParent().mygetY()+nodeForPaint.mygetParent().getHeight()/2);
		Parray[6] = new Point(nodeForPaint.mygetParent().mygetX()+nodeForPaint.mygetParent().getWidth()/2,nodeForPaint.mygetParent().mygetY()+nodeForPaint.mygetParent().getHeight());
		Parray[7] = new Point(nodeForPaint.mygetParent().mygetX(),nodeForPaint.mygetParent().mygetY()+nodeForPaint.mygetParent().getHeight()/2);
	}
	public void setShortestLine() {
		double minimumLength=getLength(Parray[0],Parray[4]);
		for(int i=0;i<=3;i++) {
			for(int j=4;j<=7;j++) {
				double length=getLength(Parray[i],Parray[j]);
				if(length<minimumLength) {
					minimumLength=length;
					x1=(int)Parray[i].getX();
					x2=(int)Parray[j].getX();
					y1=(int)Parray[i].getY();
					y2=(int)Parray[j].getY();
				}
			}
		}
		if(x1>x2) {
			if(y1>y2)check=1;
			else check=2;
		}
		else {
			if(y1>y2)check=3;
			else check=4;
		}
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		if(check==1||check==4)
			g.drawLine(1,1,this.getWidth()-1,this.getHeight()-1);
		else if(check==2||check==3)
			g.drawLine(this.getWidth()-1, 1, 1, this.getHeight()-1);
	}
	public double getLength(Point a, Point b) {
		return (a.getX()-b.getX())*(a.getX()-b.getX())+(a.getY()-b.getY())*(a.getY()-b.getY());
	}
}
