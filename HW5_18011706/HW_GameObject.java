import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public abstract class HW_GameObject{
	HW_GameObject(){}
	abstract void draw(Graphics g);						//도형 그리기 메소드
	abstract void update(float dtime);					//도형 좌표 업데이트 메소드
	abstract boolean collisionResolution(HW_GameObject in);//충돌 해결 메소드
	abstract boolean isDead();							//도형의 생사여부 판별 메소드
}

class MySphere extends HW_GameObject {
	float x, y;
	float prev_x, prev_y;
	float vx, vy;
	float radius;
	float angleX, angleY;
	Color color;
	final float PI = 3.141592f;
	boolean isAlive;
	Dimension d;
	
	MySphere(Dimension in){
		radius = 4;
		x = in.width/2.0f;
		y = in.height * 5.5f/7.0f + radius;
		prev_x = x; prev_y = y;
		float speed = 200;
		angleX = (float)(Math.random()*360) *PI /180.0f ;
		angleY = (float)(Math.random()*360) *PI /180.0f ;
		vx = (float)(Math.cos(angleX));
		vy = (float)(Math.sin(angleY));
		if(vy > 0) vy =-vy;
		if(Math.abs(vx) > Math.abs(vy)) { 				//tan()를 활용해 첫 공의 최대각도를 135도에서 45도로 고정
			if(vx < 0) vx = vy;
			if(vx > 0) vx = -vy;
		}
		
		double length = Math.sqrt((double)(vx)*(double)(vx) + (double)(vy)*(double)(vy));				//벡터의 크기를 구함
		vx = vx/(float)length *speed;																	//벡터의 크기를 1로 설정하고
		vy = vy/(float)length *speed;																	//구한 크기를 바탕으로 속도를 조절 가능
		
		
		color = Color.red;
		isAlive = true;
		d = in;
	}
	
	MySphere(float _x, float _y, float _angleX, float _angleY, Dimension in){
		x = _x; y = _y;
		prev_x = x; prev_y = y;
		float speed = 200;
		angleX = _angleX; angleY = _angleY;
		vx = (float)(Math.cos(angleX));
		vy = (float)(Math.sin(angleY));
		double length = Math.sqrt((double)(vx)*(double)(vx) + (double)(vy)*(double)(vy));				//벡터의 크기를 구함
		vx = vx/(float)length *speed;																	//벡터의 크기를 1로 설정하고
		vy = vy/(float)length *speed;																	//구한 크기를 바탕으로 속도를 조절 가능
		radius = 4;
		color = Color.red;
		isAlive = true;
		d = in;
	}
	
	@Override
	void draw(Graphics g) {
		g.setColor(color);
		g.fillOval((int)(x-radius), (int)(y-radius), (int)(2*radius), (int)(2*radius));
	}

	@Override
	void update(float dtime) {
		prev_x = x;
		prev_y = y;
		x += vx*dtime;
		y += vy*dtime;
	}

	@Override
	boolean collisionResolution(HW_GameObject in) {
		if(in instanceof MyRectBlock) {
			var wall = (MyRectBlock) in;
			
			if(wall.isCollide(this)==false) {
				if(y+radius > (d.height)) isAlive = false;
				return false;
			}
			
			if(prev_y + radius < wall.y) {y = wall.y - radius; vy = -vy;}
			if(prev_y - radius > wall.y + wall.height) {y = wall.y + wall.height + radius; vy = -vy;}
			if(prev_x + radius < wall.x) {x = wall.x - radius; vx = -vx;}
			if(prev_x + radius > wall.x + wall.width) {x = wall.x + wall.width +radius; vx = -vx;}
		}
		
		if(in instanceof MyDynamicRect) {
			var racket = (MyDynamicRect) in;
			if(racket.isCollide(this)==false) {
				if(y+radius > (d.height)) isAlive = false;
				return false;
			}
			
			if(prev_y + radius < racket.y) {y = racket.y - radius; vy = -vy;}
			if(prev_y - radius > racket.y + racket.h) {y = racket.y + racket.h + radius; vy = -vy;}
			if(prev_x + radius < racket.x) {x = racket.x - radius; vx = -vx;}
			if(prev_x + radius > racket.x + racket.w) {x = racket.x + racket.w +radius; vx = -vx;}
		}
		
		if(in instanceof TargetRect) {
			var target = (TargetRect) in;
			if(target.isCollide(this)==false) {
				if(y+radius > d.height) isAlive = false;
				return false;
			}
			
			if(prev_y + radius < target.y) {y = target.y - radius; vy = -vy;}
			if(prev_y - radius > target.y + target.h) {y = target.y + target.h + radius; vy = -vy;}
			if(prev_x + radius < target.x) {x = target.x - radius; vx = -vx;}
			if(prev_x + radius > target.x + target.w) {x = target.x + target.w +radius; vx = -vx;}
			target.setDead();
			return true;
		}
		return false;
	}
	@Override
	boolean isDead() {
		return !isAlive;
		//공이 죽은건 공이 화면에서 사라지면
	}
}

class MyRectBlock extends HW_GameObject{
	int x, y, width, height;
	Color c;
	
	MyRectBlock(int _x, int _y, int _w, int _h, Color _c){
		x = _x; y = _y; width = _w; height = _h; c = _c;
	}

	@Override
	void draw(Graphics g) {
		g.setColor(c);
		g.fillRect(x, y, width, height);
	}

	@Override
	void update(float dtime) {
		return;
	}

	@Override
	boolean collisionResolution(HW_GameObject in) {
		return false;
	}
	
	boolean isCollide(HW_GameObject in) {
		MySphere b = (MySphere) in;
		if(b.y + b.radius > y && b.y - b.radius < y + height &&
			b.x + b.radius > x && b.x - b.radius < x + width)
			return true;
		
		return false;
	}
	@Override
	boolean isDead() {
		return false;
	}
}

class MyDynamicRect extends HW_GameObject{					//라켓
	float x, y;
	float vx;
	int w, h;
	Color c = null;
	Dimension d;
	
	MyDynamicRect(Dimension in){
		w = 80;	h = 20;
		x = in.width/2.0f - (w/2);
		y = in.height*5.5f/7.0f;
		vx = 0;
		c = Color.PINK;
		d = in;
	}
	@Override
	void draw(Graphics g) {
		g.setColor(c);
		g.fillRect((int)x,(int)y,w,h);
	}

	@Override
	void update(float dtime) {
		x += vx;
	}

	@Override
	boolean collisionResolution(HW_GameObject in) {
		MyRectBlock wall = (MyRectBlock) in;
		
		if(wall.y!=0) {										//상단 블럭이 아닌 경우
			if(wall.x==0) {
				if(x < wall.width) x = wall.width;
			}
			else {
				if(x > wall.x -w) x = wall.x - w;
			}
		}
		return false;
		
	}
	@Override
	boolean isDead() {
		return false;
	}
	
	boolean isCollide(HW_GameObject in) {
		MySphere b = (MySphere) in;
		if(b.y + b.radius > y && b.y - b.radius < y + h &&
			b.x + b.radius > x && b.x - b.radius < x + w)
			return true;
		
		return false;
	}
}

class TargetRect extends HW_GameObject{
	float x, y;
	float w, h;
	Color c;
	boolean dead = false;
//	boolean special = false;
	
	TargetRect(float _x, float _y, float _w, float _h){
		x = _x;
		y = _y;
		w = _w;
		h = _h;
		int colorValue = (int)(Math.random()*10);
		if(colorValue%4==0) {
			c = Color.yellow;
//			this.special = true;
		}
		else c = new Color(88, 101, 242);
	}

	@Override
	void draw(Graphics g) {
		g.setColor(c);
		g.fillRect((int)x,(int)y,(int)w,(int)h);
	}
	@Override
	boolean isDead() {
		return dead;
	}
	void setDead() {
		dead = true;
	}
	@Override
	void update(float dtime) {}
	@Override
	boolean collisionResolution(HW_GameObject in) {
		return false;}
	boolean isCollide(HW_GameObject in) {
		MySphere b = (MySphere) in;
		if(b.y + b.radius > y && b.y - b.radius < y + h &&
			b.x + b.radius > x && b.x - b.radius < x + w)
			return true;
		
		return false;
	}
}