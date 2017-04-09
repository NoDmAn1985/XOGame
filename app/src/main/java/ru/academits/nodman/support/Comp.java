package ru.academits.nodman.support;

import java.util.Random;

public class Comp {
    private int amountOfCellsInRow;
    private int centerCell;
    private int maxNumberOfMoves;
    private char compCell;
    private char playerCell;
    private int posY;
    private int posX;
    private int difficulty;
    private Random random;

    public Comp(int difficulty, char compCell, char playerCell, int amountOfCellsInRow) {
        this.difficulty = difficulty;
        this.random = new Random();
        this.amountOfCellsInRow = amountOfCellsInRow;
        this.centerCell = amountOfCellsInRow / 2;
        this.maxNumberOfMoves = amountOfCellsInRow * amountOfCellsInRow;
        this.compCell = compCell;
        this.playerCell = playerCell;
    }

    public void move(int restCells, int playerPosY, int playerPosX, Field field) {
        switch (difficulty) {
            case 1:
                if (restCells == maxNumberOfMoves - 3 || restCells == maxNumberOfMoves - 4) {
                    difficulty3(restCells, playerPosY, playerPosX, field);
                } else {
                    difficulty1(field);
                }
                break;
            case 2:
                if (restCells < maxNumberOfMoves - 1) {
                    difficulty3(restCells, playerPosY, playerPosX, field);
                } else {
                    difficulty1(field);
                }
                break;
            default:
                difficulty3(restCells, playerPosY, playerPosX, field);
                break;
        }
    }

    private void difficulty1(Field field) {
        while (true) {
            posY = random.nextInt(amountOfCellsInRow);
            posX = random.nextInt(amountOfCellsInRow);
            if (field.isEmpty(posY, posX)) {
                break;
            }
        }
    }

    private void difficulty3(int restCells, int playerPosY, int playerPosX, Field field) {
        if (restCells >= maxNumberOfMoves - 1) {
            if (field.isEmpty(centerCell, centerCell)) {
                posY = centerCell;
                posX = centerCell;
            } else if (field.isEmpty(centerCell + 1, centerCell + 1)) {
                posY = centerCell + 1;
                posX = centerCell + 1;
            }
        } else {
            int lastPosY = posY;
            int lastPosX = posX;

            int emptyPosY = 0;
            int emptyPosX = 0;

            int countCompCellsByHorizon = 0;
            int countCompCellsByVertical = 0;
            int countCompCellsByDiagonalDown = 0;
            int countCompsCellsByDiagonalUp = 0;

            int countPlayersCellsByHorizon = 0;
            int countPlayersCellsByVertical = 0;
            int countPlayersCellsByDiagonalDown = 0;
            int countPlayersCellsByDiagonalUp = 0;

            boolean isCompCellOnHorizon = false;
            boolean isCompCellOnVertical = false;
            boolean isCompCellOnDiagonalDown = (playerPosX != playerPosY);
            boolean isCompCellOnDiagonalUp = (amountOfCellsInRow - playerPosY - 1 != playerPosX);

            boolean isHorizonFree = true;
            boolean isVerticalFree = true;
            boolean isDiagonalDownFree = (lastPosX == lastPosY);
            boolean isDiagonalUpFree = (amountOfCellsInRow - lastPosY - 1 == lastPosX);

            boolean wasEmptyFound = false;

            for (int y = 0; y < amountOfCellsInRow; y++) {
                for (int x = 0; x < amountOfCellsInRow; x++) {
                    //для дебага
                    if (restCells <= 1 && y == 2 && x == 2) {
                        int i = 0;
                        i = i - 1;
                    }

                    //если всё не сработает, чтобы долго не искал
                    if (!wasEmptyFound && field.isEmpty(y, x)) {
                        emptyPosY = y;
                        emptyPosX = x;
                        wasEmptyFound = true;
                    }

                    //поиск 2-х ячеек игрока
                    if (!isCompCellOnVertical && x == playerPosX) {
                        if (field.isCellContains(playerCell, y, playerPosX)) {
                            ++countPlayersCellsByVertical;
                        } else if (field.isCellContains(compCell, y, playerPosX)) {
                            countPlayersCellsByVertical = 0;
                            isCompCellOnVertical = true;
                        }
                    }
                    if (!isCompCellOnHorizon && y == playerPosY) {
                        if (field.isCellContains(playerCell, playerPosY, x)) {
                            ++countPlayersCellsByHorizon;
                        } else if (field.isCellContains(compCell, playerPosY, x)) {
                            countPlayersCellsByHorizon = 0;
                            isCompCellOnHorizon = true;
                        }
                    }
                    if (!isCompCellOnDiagonalDown && y == x) {
                        if (field.isCellContains(playerCell, y, x)) {
                            ++countPlayersCellsByDiagonalDown;
                        } else if (field.isCellContains(compCell, y, x)) {
                            countPlayersCellsByDiagonalDown = 0;
                            isCompCellOnDiagonalDown = true;
                        }
                    }
                    if (!isCompCellOnDiagonalUp && amountOfCellsInRow - y - 1 == x) {
                        if (field.isCellContains(playerCell, y, x)) {
                            ++countPlayersCellsByDiagonalUp;
                        } else if (field.isCellContains(compCell, y, x)) {
                            countPlayersCellsByDiagonalUp = 0;
                            isCompCellOnDiagonalUp = true;
                        }
                    }
                    //проверка на возможные незащищённые ходы-направления
                    if (isVerticalFree && x == lastPosX) {
                        if (field.isCellContains(compCell, y, lastPosX)) {
                            ++countCompCellsByVertical;
                        } else if (field.isCellContains(playerCell, y, lastPosX)) {
                            countCompCellsByVertical = 0;
                            isVerticalFree = !isVerticalFree;
                        }
                    }
                    if (isHorizonFree && y == lastPosY) {
                        if (field.isCellContains(compCell, lastPosY, x)) {
                            ++countCompCellsByHorizon;
                        } else if (field.isCellContains(playerCell, lastPosY, x)) {
                            countCompCellsByHorizon = 0;
                            isHorizonFree = !isHorizonFree;
                        }
                    }
                    if (isDiagonalDownFree && y == x) {
                        if (field.isCellContains(compCell, y, x)) {
                            ++countCompCellsByDiagonalDown;
                        } else if (field.isCellContains(playerCell, y, x)) {
                            countCompCellsByDiagonalDown = 0;
                            isDiagonalDownFree = !isDiagonalDownFree;
                        }
                    }
                    if (isDiagonalUpFree && amountOfCellsInRow - y - 1 == x) {
                        if (field.isCellContains(compCell, y, x)) {
                            ++countCompsCellsByDiagonalUp;
                        } else if (field.isCellContains(playerCell, y, x)) {
                            countCompsCellsByDiagonalUp = 0;
                            isDiagonalUpFree = !isDiagonalUpFree;
                        }
                    }
                }
            }

            while (true) {
                // победный ход
                if (countCompCellsByHorizon == 2) {
                    posY = lastPosY;
                    posX = random.nextInt(amountOfCellsInRow);
                } else if (countCompCellsByVertical == 2) {
                    posY = random.nextInt(amountOfCellsInRow);
                    posX = lastPosX;
                } else if (countCompCellsByDiagonalDown == 2) {
                    posY = random.nextInt(amountOfCellsInRow);
                    posX = posY;
                } else if (countCompsCellsByDiagonalUp == 2) {
                    posY = random.nextInt(amountOfCellsInRow);
                    posX = amountOfCellsInRow - posY - 1;
                    // защитный ход
                } else if (countPlayersCellsByHorizon == 2) {
                    posY = playerPosY;
                    posX = random.nextInt(amountOfCellsInRow);
                } else if (countPlayersCellsByVertical == 2) {
                    posY = random.nextInt(amountOfCellsInRow);
                    posX = playerPosX;
                } else if (countPlayersCellsByDiagonalDown == 2) {
                    posY = random.nextInt(amountOfCellsInRow);
                    posX = posY;
                } else if (countPlayersCellsByDiagonalUp == 2) {
                    posY = random.nextInt(amountOfCellsInRow);
                    posX = amountOfCellsInRow - posY - 1;
                    // обычный ход
                } else if (isHorizonFree) {
                    posY = lastPosY;
                    posX = playerPosX;
                    if (!field.isEmpty(posY, posX)) {
                        posX = random.nextInt(amountOfCellsInRow);
                    }
                } else if (isVerticalFree) {
                    posY = playerPosY;
                    posX = lastPosX;
                    if (!field.isEmpty(posY, posX)) {
                        posY = random.nextInt(amountOfCellsInRow);
                    }
                } else if (isDiagonalUpFree) {
                    posY = playerPosY;
                    posX = amountOfCellsInRow - posY - 1;
                    if (!field.isEmpty(posY, posX)) {
                        posX = playerPosX;
                        posY = amountOfCellsInRow - posX - 1;
                        if (!field.isEmpty(posY, posX)) {
                            posY = random.nextInt(amountOfCellsInRow);
                        }
                    }
                } else if (isDiagonalDownFree) {
                    posY = playerPosY;
                    posX = posY;
                    if (!field.isEmpty(posY, posX)) {
                        posX = playerPosX;
                        posY = posX;
                        if (!field.isEmpty(posY, posX)) {
                            posY = random.nextInt(amountOfCellsInRow);
                            posX = posY;
                        }
                    }
                    //рандомный ход
                } else {
                    posY = playerPosY;
                    posX = playerPosX - 1;
                    if (posX < 0 || !field.isEmpty(posY, posX)) {
                        posX = playerPosX + 1;
                        if (posX > amountOfCellsInRow - 1 || !field.isEmpty(posY, posX)) {
                            posX = playerPosX;
                            posY = playerPosY - 1;
                            if (posY < 0 || !field.isEmpty(posY, posX)) {
                                posY = playerPosY + 1;
                                if (posY > amountOfCellsInRow - 1 || !field.isEmpty(posY, posX)) {
                                    posY = emptyPosY;
                                    posX = emptyPosX;
                                }
                            }
                        }
                    }
                }
                if (field.isEmpty(posY, posX)) {
                    break;
                }
            }
        }
    }

    public int getPosY() {
        return posY;
    }

    public int getPosX() {
        return posX;
    }

    public void setCells(char compCell, char playerCell) {
        this.compCell = compCell;
        this.playerCell = playerCell;
    }
}
