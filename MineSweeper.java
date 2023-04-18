import java.util.*;
/*
Owen Michener
April 18th, 2023
This was created just as a little side project as a challenge in one sitting.
This code is not meant to be anything special or crazy but more or less a test to see if I could get
a working prototype of minesweeper up and running on the console using java.
 */
public class MineSweeper {
    public static void main(String[] args) {
        boolean playing = true, won = false;
        int width = 10, height = 10;
        Scanner s = new Scanner(System.in);
        Board board = new Board(width, height);
        int numberOfBombs = 15;
        board.placeBombs(numberOfBombs);
        board.calculateBlocks();
        while(playing) {
            board.display();
            System.out.println("Input 2 numbers separated by a comma. EX: 2,3");
            String input = s.nextLine();
            String[] guess = input.split(",");
            int a = Integer.parseInt(guess[0]), b = Integer.parseInt(guess[1]);
            int i = board.grid[a][b].reveal();
            if(i == 0) revealMore(board, a, b);
            else playing = i == 1;
            if(Block.numberRevealed == width*height-numberOfBombs) {
                won = true;
                playing = false;
            }
        }
        board.revealAll();
        board.display();
        if(won) System.out.println("CONGRATS YOU WON!");
        else System.out.println("Sorry, you lost! Better luck next time!");
    }

    public static void revealMore(Board board, int a, int b) {
        for(int i = -1; i < 2; i++) {
            for(int j = -1; j < 2; j++) {
                if(!((a+i >= 0 && a+i < board.height) && (b+j >= 0 && b+j < board.width))) continue;
                if(board.grid[a+i][b+j].revealed) continue;
                int c = board.grid[a+i][b+j].reveal();
                if(c == 0) revealMore(board, a+i, b+j);
            }
        }
    }
}
class Board {
    int width, height;
    Block[][] grid;
     public Board(int w, int h) {
        width = w;
        height = h;
        grid = new Block[w][h];
        for (int i = 0; i < h; i++) for (int j = 0; j < w; j++) grid[j][i] = new Block("*");
    }
    public void placeBombs(int n) {
         Random r = new Random();
         for(int i =0; i < n; i++) {
             Block b;
             do { b = grid[r.nextInt(0, width)][r.nextInt(0, height)]; }
             while(b.numOfBombs == 9);
             b.numOfBombs = 9;
         }
    }
    public void calculateBlocks() {
         for(int i = 0; i < height; i++)
             for(int j = 0; j < width; j++) {
                 if(grid[j][i].numOfBombs == 9) continue;
                 for(int a = -1; a < 2; a++) {
                     for(int b = -1; b < 2; b++) {
                         if(!((i+a >= 0 && i+a < height) && (j+b >= 0 && j+b < width))) continue;
                         grid[j][i].numOfBombs += (grid[j+b][i+a].numOfBombs == 9) ? 1 : 0;
                     }
                 }
             }
    }
    public void revealAll() { for(int i = 0; i < width; i++) for(int j = 0; j < height; j++) grid[i][j].reveal(); }
    public void display() {
        for (int i = 0; i < height; i++) {
            System.out.print(i + "|");
            for (int j = 0; j < width; j++) {
                System.out.print(grid[j][i] + " ");
            }
            System.out.println();
        }
        System.out.print("  ");
        for (int j = 0; j < width; j++) System.out.print("__");
        System.out.println();
        System.out.print("  ");
        for (int j = 0; j < width; j++) System.out.print(j + " ");
        System.out.println();
    }
}
class Block {
    public static int numberRevealed = 0;
    String sign;
    int numOfBombs;
    boolean revealed = false;
    public Block(String s) { sign = s; }
    public int reveal() {
        if(!revealed) numberRevealed++;
        revealed = true;
        if(numOfBombs == 9) {
            sign = "x";
            return 2;
        }
        else if(numOfBombs == 0) {
            sign = " ";
            return 0;
        }
        return 1;
    }
    @Override
    public String toString() { return ((revealed && numOfBombs != 9 && numOfBombs != 0) ? (Integer.toString(numOfBombs)) : sign); }
}