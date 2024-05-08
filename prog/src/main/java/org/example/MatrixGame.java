package org.example;


public class MatrixGame {
    private static final int GRID_SIZE = 5;
    private char[][] grid;

    public MatrixGame() {
        grid = new char[GRID_SIZE][GRID_SIZE];
        initializeGrid();
    }

    private void initializeGrid() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = '-';
            }
        }
    }

    public void displayGrid() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void updateGrid(int x, int y) {
        if (isValidMove(x, y)) {
            grid[x][y] = 'X';
        }
    }

    private boolean isValidMove(int x, int y) {
        if (x < 0 || x >= GRID_SIZE || y < 0 || y >= GRID_SIZE) {
            return false;
        }
        return grid[x][y] == '-';
    }

    public static void main(String[] args) {
        MatrixGame display = new MatrixGame();


        // Simulazione 2 giocatori
        display.updateGrid(2, 2);
        display.updateGrid(3, 3);

        display.displayGrid();

    }
}
