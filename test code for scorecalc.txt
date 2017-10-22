package test;

import java.util.Scanner;

public class main 
{
	static Scanner sc = new Scanner(System.in);
	static int player1;
	static int player2;
	static int currentBatsmenNum=1;
	static int otherBatsmenNum = 2;
	static int out=0;
	static int total=0;
	static boolean flag=true;
	public static void main(String args[]) {
		try {
			while(flag) {
				System.out.println("Input:");
				String next = sc.next();
				if(next.equalsIgnoreCase("exit")) {
					flag=false;
					System.out.println("Total: " + total + " - " + out);
					System.out.println("Player1: " + player1);
					System.out.println("Player2: " + player2);
					System.exit(0);
				}
				if(numberOrNot(next)) {
					currentBatsmen(Integer.parseInt(next),currentBatsmenNum);
				}else {
					if(next.equalsIgnoreCase("W")) {
						out++;
						if(out==10) {
							flag=false;
							System.out.println("Total: " + total + " - " + out);
							System.out.println("Player1: " + player1);
							System.out.println("Player2: " + player2);
							System.exit(0);
						}
					}
				}
				System.out.println("Total: " + total + " - " + out + " ; " + "Striker: " + currentBatsmenNum + " ; "+ "Non-Striker: " + otherBatsmenNum);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	static void currentBatsmen(int next,int currentBatsmenNum) {
		try {
			if(currentBatsmenNum==1) {
				player1+=next;
			}else {
				player2+=next;
			}
			total+=next;
			if(next%2==0) {
				
			}else {
				main.currentBatsmenNum = otherBatsmenNum + currentBatsmenNum;
				otherBatsmenNum = main.currentBatsmenNum - otherBatsmenNum;
				main.currentBatsmenNum = main.currentBatsmenNum - otherBatsmenNum;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	static boolean numberOrNot(String input)
	{
	    try
	    {
	        Integer.parseInt(input);
	    }
	    catch(NumberFormatException ex)
	    {
	        return false;
	    }
	    return true;
	}
}
