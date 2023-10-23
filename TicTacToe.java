package tictactoepac;
import java.util.Scanner;

public class TicTacToe {

	public static void main(String[] args) {
		
		Scanner firstIn = new Scanner(System.in);
		System.out.print("[SYS] Enter player name: ");
		String name = firstIn.nextLine();
		
		GameCode play = new GameCode(name);
		play.startGame();
		firstIn.close(); 
		
	}

}
