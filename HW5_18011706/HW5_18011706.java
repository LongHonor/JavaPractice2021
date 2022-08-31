import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

class PlayingPanel extends JPanel implements Runnable, KeyListener{
	float dtime = 1/30.0f;
	Thread t;
	LinkedList<HW_GameObject> objs = new LinkedList<>();
	LinkedList<MySphere> balls = new LinkedList<>();
	LinkedList<TargetRect> targets = new LinkedList<>();
	int row, level, score;
	HW5_18011706 frame;
	MyDynamicRect racket;
	Dimension d;
	final float PI = 3.141592f;
	int wallWidth = 20;									//외벽의 폭
	
	PlayingPanel(HW5_18011706 in){
		frame = in;
		d = frame.getContentPane().getSize();
		objs.add(new MyRectBlock(0,0,d.width,wallWidth, Color.red));											//상단 블럭
		objs.add(new MyRectBlock(0,wallWidth,wallWidth,d.width - wallWidth, Color.red));						//좌측 블럭
		objs.add(new MyRectBlock(d.width-wallWidth,wallWidth,wallWidth,d.width - wallWidth, Color.red));		//우측블럭
		
		racket = new MyDynamicRect(d);
		objs.add(racket);
		
		MySphere s = new MySphere(d);
		objs.add(s);
		balls.add(s);
		
		level = 1;
		row = 3 * level;
		score = 0;
		
		setTargetPosition();
		setBackground(Color.black);
		
		addKeyListener(this);
		setFocusable(true);
		requestFocus();

		t = new Thread(this);
		t.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int size = objs.size();

		for(int i=0;i<size;i++){
			HW_GameObject o = objs.get(i);
			o.draw(g);
		}
	}

	@Override
	public void run() {
		try {
			while(true) {
				collisionProcess();
				if(targets.size()==0) {
					level++;
					row = level*3;
					Thread.sleep(1000);
					initBalls();
					initRacket();
					setTargetPosition();
				}
				repaint();
				
				if(balls.size() == 0) {
					showEnding();
					frame.renewHighScore(score);
					break;
				}
				Thread.sleep((int)(dtime*1000));
			}
			
		}
		catch(InterruptedException e) {}
	}

	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) racket.vx = 10;
		if(e.getKeyCode() == KeyEvent.VK_LEFT) racket.vx = -10;
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) racket.vx = 0;
		if(e.getKeyCode() == KeyEvent.VK_LEFT) racket.vx = 0;
	}
	void initRacket() {
		racket.x = d.width/2.0f - (racket.w/2);
	}
	
	void initBalls() {
		balls.clear();
		Iterator it = objs.iterator();
		while(it.hasNext()) {
			Object o = it.next();
			if(o instanceof MySphere) it.remove();
		}
		
		MySphere ball = new MySphere(d);
		balls.add(ball);
		objs.add(ball);
	}
	
	void playingPanelRequestFocus() {
		this.requestFocusInWindow(); 
	}
	
	void spawnBall() {
		MySphere s = new MySphere(d);
		objs.add(s);
		balls.add(s);
	}
	
	void showEnding() {
		frame.card.show(frame.getContentPane(), "ending");
		frame.ending.endingPanelRequestFcous();
	}
	
	void trippleBall(MySphere ball) {
		MySphere s1 = new MySphere(ball.x, ball.y, ball.angleX +(PI/6.0f),ball.angleY,d);
		objs.add(s1);
		balls.add(s1);
		MySphere s2 = new MySphere(ball.x, ball.y, ball.angleX -(PI/6.0f),ball.angleY,d);
		objs.add(s2);
		balls.add(s2);
	}
	
	void collisionProcess() {
		for(HW_GameObject o : objs)
			o.update(dtime);
		int sizes = objs.size();
		for(int s=0;s<sizes;s++) {							//공이 다른 물체와 충돌하는가?
			HW_GameObject o1 = objs.get(s);
			if(o1 instanceof MySphere) {
				int size = objs.size();
				for(int i=0;i<size;i++) {
					HW_GameObject o2 = objs.get(i);
					if(o2 instanceof MySphere) continue;
					else {
						boolean isCol = o1.collisionResolution(o2);
						if(o2 instanceof TargetRect) {

//							if(((TargetRect) o2).special&&isCol) {
							if(((TargetRect) o2).c.equals(Color.yellow)&&isCol) {
								trippleBall((MySphere)o1);
								System.out.println(objs.size());
							}
						}
					}
				}
			}
			if(o1 instanceof MyDynamicRect) {					//라켓이 벽과 충돌할 때
				for(HW_GameObject o2 : objs) {
					if(o2 instanceof MyRectBlock)
						o1.collisionResolution(o2);
				}
			}
		}
		
		var it0 = objs.iterator();
		while(it0.hasNext()) {
			if(it0.next().isDead() == true)	it0.remove();
		}
		var it1 = balls.iterator();
		while(it1.hasNext()) {
			if(it1.next().isDead() == true) it1.remove();
		}
		var it2 = targets.iterator();
		while(it2.hasNext()) {
			if(it2.next().isDead()==true) {
				it2.remove();
				score+=10;
			}
		}
	}
	
	void setTargetPosition(){
		int targetAreaWidth = d.width -(2*wallWidth);
		int targetAreaHeight = (int)(d.height*0.55);
		int gap = 10;
		
		int w = (int)((targetAreaWidth-((row+1)*gap))/row);
		int h = (int)((targetAreaHeight - (row*gap))/row);
		
		for(int i = 0 ; i < row; i++) {
			for(int j = 0; j < row; j++) {
				int x = (j+1)*gap + j*w + wallWidth;
				int y = (i+1)*gap + i*h + wallWidth;
				TargetRect t = new TargetRect(x,y,w,h);
				
				objs.add(t);
				targets.add(t);
			}
		}
		repaint();
	}
}

class TitlePanel extends JPanel {
	HW5_18011706 frame;
	
	TitlePanel(HW5_18011706 in){
		frame = in;
		this.setBackground(Color.black);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_SPACE) {
					//시작 소리가 들렸으면 좋겠다
					//패널이 바뀌도록하는 메소드
					frame.startSession();
				}
			}
		});
		
		setFocusable(true);
		requestFocus();
	}
	
	void titlePanelRequestFocus() {
		this.requestFocusInWindow();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Dimension d = getSize();
		super.paintComponent(g);
		
		g.setColor(Color.white);
		
		String title = new String("Break Out");
		g.setFont(new Font("Arial", Font.BOLD, 75));
		g.drawString(title, d.width* 1/7, d.height* 1/4);

		String press = new String("Press SPACE to start");
		g.setFont(new Font("Arial", Font.BOLD, 25));
		g.drawString(press, d.width * 1/4, d.height* 3/4);
	}
}

class EndingPanel extends JPanel {
	HW5_18011706 frame;
	EndingPanel(HW5_18011706 in){
		frame = in;
		setBackground(Color.black);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_SPACE) {
					frame.returnTitle();
				}
			}
		});
		
		setFocusable(true);
		requestFocus();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Dimension d = getSize();
		super.paintComponent(g);
		
		g.setColor(Color.white);
		
		String title = new String("Game Over");
		g.setFont(new Font("Arial", Font.BOLD, 75));
		g.drawString(title, d.width* 1/7, d.height* 1/4);
		
		String score = new String("HighScore :"+frame.highScore);
		String yourScore = new String("Your Score :"+frame.currentSessionScore);
		g.setFont(new Font("Arial",Font.BOLD,30));
		g.drawString(score, d.width * 1/3, d.height * 1/2);
		g.drawString(yourScore, d.width * 1/3, d.height * 1/2 +30);
		
		String press = new String("Press SPACE to return title");
		g.setFont(new Font("Arial", Font.BOLD, 30));
		g.drawString(press, d.width * 1/6, d.height* 4/5);
	}
	
	void endingPanelRequestFcous() {
		this.requestFocusInWindow();
	}
}

public class HW5_18011706 extends JFrame{
	TitlePanel title;
	PlayingPanel playing;
	EndingPanel ending;
	CardLayout card = new CardLayout();
	int highScore, currentSessionScore;
	Dimension d;
	
	HW5_18011706(){
		setTitle("HW5");
		setSize(600,600);
		d = this.getContentPane().getSize();
		
		highScore = 0; currentSessionScore = 0;
		
		this.setLayout(card);
		
		title = new TitlePanel(this);
		add(title,"title");

		ending = new EndingPanel(this);
		add(ending,"ending");
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new HW5_18011706();
	}
	
	void startSession() {
		playing = new PlayingPanel(this);
		add(playing,"playing");
		card.show(this.getContentPane(), "playing");
		playing.playingPanelRequestFocus();
	}
	
	void returnTitle() {
		card.show(this.getContentPane(), "title");
		title.titlePanelRequestFocus();
	}
	
	void renewHighScore(int in) {
		currentSessionScore = in;
		if(highScore < in) highScore = in;
	}
}
