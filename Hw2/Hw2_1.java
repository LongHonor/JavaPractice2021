import java.util.Scanner;

class HwConcert{
	String S[];
	String A[];
	String B[];
	Scanner sc;
	
	HwConcert(){
		sc = new Scanner(System.in);
		S = new String[10];
		A = new String[10];
		B = new String[10];
		
		for (int i = 0; i<S.length;i++) {
			S[i] = "___";
			A[i] = "___";
			B[i] = "___";
		}
	}
	
	static void printSeat(String Seat[]) {
		for(int i = 0; i<Seat.length;i++) {
			System.out.print(" "+Seat[i]+" ");
		}
		System.out.println();
	}
	
	void printAll() {
		System.out.print("S>> ");
		HwConcert.printSeat(S);
		System.out.print("A>> ");
		HwConcert.printSeat(A);
		System.out.print("B>> ");
		HwConcert.printSeat(B);
		System.out.println("<<<조회를 완료하였습니다.>>>");
	}
	
	void choiceSeat() {
		while(true) {
			System.out.println("좌석구분 S(1), A(2), B(3) >> ");
			int n = sc.nextInt();
			switch(n) {
			case 1:
				System.out.print("S>>");
				printSeat(S);
				inputSeat(S);
				return;
			case 2:
				System.out.print("A>>");
				printSeat(A);
				inputSeat(A);
				return;
			case 3:
				System.out.print("B>>");
				printSeat(B);
				inputSeat(B);
				return;
			default:
				System.out.println("유효하지 않은 좌석입니다.");
			}
		}
	}
	
	void inputSeat(String seat[]) {
		System.out.print("이름>>");
		String name = sc.next();
		while(true) {
			System.out.print("번호>>");
			int num = sc.nextInt();
			num-=1;
			if(num>=0&&num<=9) {
				if(seat[num].equals("___")) {
					seat[num] = name;
					break;
				}
				else {
					System.out.println("다른 좌석을 선택해주세요");
				}
			}
			else {
				System.out.println("유효하지 않은 좌석번호 입니다.");
			}
		}
	}
	
	void cancelSeat() {
		while(true) {
			System.out.print("좌석 S:1, A:2, B:3 >>");
			int n = sc.nextInt();
			switch(n) {
			case 1:
				System.out.print("S>>");
				printSeat(S);
				outputSeat(S);
				return;
			case 2:
				System.out.print("A>>");
				printSeat(A);
				outputSeat(A);
				return; 
			case 3:
				System.out.print("B>>");
				printSeat(B);
				outputSeat(B);
				return;
			default:
				System.out.println("다시 입력해주세요.");
			}
		}
	}
	
	void outputSeat(String seat[]) {
		System.out.print("이름>> ");
		String name = sc.next();
		for(int i=0; i<seat.length;i++) {
			if(seat[i].equals(name)) {
				seat[i] = "___";
				break;
			}
		}
	}
}

public class Hw2_1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("명품 콘서트홀 예약 시스템입니다.");
		
		HwConcert concert = new HwConcert();
		
		while(true) {
			System.out.print("예약:1, 조회:2, 취소:3, 끝내기:4 >>");
			Scanner input = new Scanner(System.in);
			int n = input.nextInt();
			
			switch(n) {
			case 1:
				concert.choiceSeat();
				break;
			case 2:
				concert.printAll();
				break;
			case 3:
				concert.cancelSeat();
				break;
			case 4:
				input.close();
				break;
			default:
				System.out.println("다시 입력해주세요");
			}
		}
	}

}
