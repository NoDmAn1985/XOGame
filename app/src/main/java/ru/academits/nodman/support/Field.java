package ru.academits.nodman.support;

public class Field {
    private final int AMOUNT_OF_CELLS_IN_ROW = 3;
    private char[][] map;
    private final char zero = '_';
    private final char cell_x = 'x';
    private final char cell_o = 'o';
    private int typeOfVictory = -1;

    public Field() {
        map = new char[AMOUNT_OF_CELLS_IN_ROW][AMOUNT_OF_CELLS_IN_ROW];
        clear();
    }

    public char getCell(int posY, int posX) {
        return map[posY][posX];
    }

    public int getAmountOfCellsInRow() {
        return AMOUNT_OF_CELLS_IN_ROW;
    }

    public char getCellX() {
        return cell_x;
    }

    public char getCellO() {
        return cell_o;
    }

    public boolean isEmpty(int posY, int posX) {
        return (map[posY][posX] == zero);
    }

    public boolean isCellContains(char charToSet, int posY, int posX) {
        return (map[posY][posX] == charToSet);
    }

    public void setCell(char charToSet, int posY, int posX) {
        map[posY][posX] = charToSet;
    }

    public boolean isWin(char charForTest, int posY, int posX) {
        int countCellsByY = 0;
        int countCellsByDown = 0;
        int countCellsByUp = 0;
        int countCellsByX = 0;
        for (int y = 0; y < AMOUNT_OF_CELLS_IN_ROW; y++) {
            //по вертикали
            if (map[y][posX] == charForTest) {
                ++countCellsByY;
                if (countCellsByY == AMOUNT_OF_CELLS_IN_ROW) {
                    switch (posX) {
                        case 0:
                            typeOfVictory = 3;
                            break;
                        case 1:
                            typeOfVictory = 4;
                            break;
                        case 2:
                            typeOfVictory = 5;
                            break;
                    }
                }
            }
            countCellsByX = 0;
            for (int x = 0; x < AMOUNT_OF_CELLS_IN_ROW; x++) {
                //по горизонтали
                if (map[posY][x] == charForTest) {
                    ++countCellsByX;
                    if (countCellsByX == AMOUNT_OF_CELLS_IN_ROW) {
                        switch (posY) {
                            case 0:
                                typeOfVictory = 0;
                                break;
                            case 1:
                                typeOfVictory = 1;
                                break;
                            case 2:
                                typeOfVictory = 2;
                                break;
                        }
                    }
                }
                // по диагонали \
                if (posX == posY && y == x && map[y][x] == charForTest) {
                    ++countCellsByDown;
                    if (countCellsByDown == AMOUNT_OF_CELLS_IN_ROW) {
                        typeOfVictory = 6;
                    }
                }
                // по диагонали /
                if (AMOUNT_OF_CELLS_IN_ROW - posY - 1 == posX && AMOUNT_OF_CELLS_IN_ROW - y - 1 == x
                        && map[y][x] == charForTest) {
                    ++countCellsByUp;
                    if (countCellsByUp == AMOUNT_OF_CELLS_IN_ROW) {
                        typeOfVictory = 7;
                    }
                }
            }
        }

        return (countCellsByX == AMOUNT_OF_CELLS_IN_ROW || countCellsByY == AMOUNT_OF_CELLS_IN_ROW
                || countCellsByDown == AMOUNT_OF_CELLS_IN_ROW || countCellsByUp == AMOUNT_OF_CELLS_IN_ROW);
    }

    public int getTypeOfVictory() {
        return typeOfVictory;
    }

    public void clear() {
        this.typeOfVictory = -1;
        for (int y = 0; y < AMOUNT_OF_CELLS_IN_ROW; y++) {
            for (int x = 0; x < AMOUNT_OF_CELLS_IN_ROW; x++) {
                map[y][x] = zero;
            }
        }
    }
}
