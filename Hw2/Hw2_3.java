import java.util.Scanner;

abstract class Shape {
	private Shape next;
	public Shape() {
		next = null;
	}
	public void setNext(Shape obj) {
		next = obj;
	}
	public Shape getNext() {
		return next;
	}
	public abstract void draw();
}

class HwRect extends Shape {

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		System.out.println("Rect");
	}
	
}

class HwCircle extends Shape {

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		System.out.println("Circle");
	}
	
}

class HwLine extends Shape {

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		System.out.println("Line");
	}
	
}

class HwGraphicEditor {
	Shape head, tail;
	Scanner sc = new Scanner(System.in);;
	
	HwGraphicEditor(){
		head = null;
		tail = null;
	}
	public void insertShape() {
		System.out.print("Line(1), Rect(2), Circle(3)>>");
 		int n = this.sc.nextInt();
		Shape tmp;
		switch(n) {
		case 1:
			tmp = new HwLine();
			break;
		case 2:
			tmp = new HwRect();
			break;
		case 3:
			tmp = new HwCircle();
			break;
		default:
			System.out.println("다시 입력해주세요");
			return;
		}
		if(this.head==null) {
			this.head = tmp;
			this.tail = tmp;
		}
		else {
			this.tail.setNext(tmp);
			this.tail = tmp;
		}
	}
	
	public void deleteShape() {
		System.out.print("삭제할 도형의 위치>>");
		int n = sc.nextInt();
		if (n==1) {						//head인 경우
			if(head==tail) {
				head = null;
				tail = null;
			}
			else {
				head = head.getNext();
				return;
			}
		}
		else {
			Shape tmp = head;
			Shape prev = head;
			for(int i=1;i<n;i++) {
				prev = tmp;
				tmp = tmp.getNext();
			}
			if(tmp==null) {
				System.out.println("삭제할 수 없습니다.");
				return;
			}
			else {
				if(tmp.getNext()==null) {				//tai인 경우
					prev.setNext(null);
				}
				else {									//내부인 경우
					prev.setNext(tmp.getNext());
				}
			}
		}
	}
	public void printAll() {
		Shape r = head;
		while(r!=null) {
			r.draw();
			r = r.getNext();
		}
	}
}

public class Hw2_3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HwGraphicEditor gpEdit = new HwGraphicEditor();
		Scanner input = new Scanner(System.in);
		System.out.println("그래픽 에디터 beauty을 실행합니다.");
		while(true) {
			System.out.print("삽입(1), 삭제(2), 모두 보기(3), 종료(4) >> ");	
			int n = input.nextInt();
			switch(n) {
			case 1:
				gpEdit.insertShape();
				break;
			case 2:
				gpEdit.deleteShape();
				break;
			case 3:
				gpEdit.printAll();
				break;
			case 4:
				System.out.println("beauty을 종료합니다.");
				return;
			}
		}
	}

}
