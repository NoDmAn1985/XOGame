package ru.academits.nodman.xogame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ru.academits.nodman.support.Sound;
import ru.academits.nodman.support.Comp;
import ru.academits.nodman.support.Field;

public class Game extends AppCompatActivity {
    private int amountOfCellsInRow;
    int restOfCells;
    ImageView imageWins;
    ImageView imageOfPlayerCell;
    ImageView[][] buttons;
    TextView buttonNew;
    TextView buttonExit;
    char playerCell;
    char compCell;
    boolean isGameOver;
    boolean isPlayersMove;
    private int playerPosY;
    private int playerPosX;
    Comp comp;
    Field field;
    Sound sound;
    int playerCellImageFirst;
    int playerCellImage;
    int compCellImage;
    int playerScore;
    int compScore;
    boolean isWinnerGoX;
    int difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        field = new Field();
        amountOfCellsInRow = field.getAmountOfCellsInRow();

        isPlayersMove = getIntent().getStringExtra("sign").equals("cell_x");
        difficulty = getIntent().getIntExtra("difficulty", -1);
        isWinnerGoX = getIntent().getBooleanExtra("isWinnerGoX", true);

        buttons = new ImageView[amountOfCellsInRow][amountOfCellsInRow];
        buttons[0][0] = (ImageView) findViewById(R.id.button1x1);
        buttons[0][1] = (ImageView) findViewById(R.id.button1x2);
        buttons[0][2] = (ImageView) findViewById(R.id.button1x3);
        buttons[1][0] = (ImageView) findViewById(R.id.button2x1);
        buttons[1][1] = (ImageView) findViewById(R.id.button2x2);
        buttons[1][2] = (ImageView) findViewById(R.id.button2x3);
        buttons[2][0] = (ImageView) findViewById(R.id.button3x1);
        buttons[2][1] = (ImageView) findViewById(R.id.button3x2);
        buttons[2][2] = (ImageView) findViewById(R.id.button3x3);
        buttonNew = (TextView) findViewById(R.id.buttonNew);
        buttonExit = (TextView) findViewById(R.id.buttonExit);
        imageOfPlayerCell = (ImageView) findViewById(R.id.imageOfPlayerCell);
        imageWins = (ImageView) findViewById(R.id.imageWins);

        comp = new Comp(difficulty, compCell, playerCell, amountOfCellsInRow);
        sound = new Sound(this);
        sound.initSounds();

        begin();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1x1:
                playerPosY = 0;
                playerPosX = 0;
                break;
            case R.id.button1x2:
                playerPosY = 0;
                playerPosX = 1;
                break;
            case R.id.button1x3:
                playerPosY = 0;
                playerPosX = 2;
                break;
            case R.id.button2x1:
                playerPosY = 1;
                playerPosX = 0;
                break;
            case R.id.button2x2:
                playerPosY = 1;
                playerPosX = 1;
                break;
            case R.id.button2x3:
                playerPosY = 1;
                playerPosX = 2;
                break;
            case R.id.button3x1:
                playerPosY = 2;
                playerPosX = 0;
                break;
            case R.id.button3x2:
                playerPosY = 2;
                playerPosX = 1;
                break;
            case R.id.button3x3:
                playerPosY = 2;
                playerPosX = 2;
                break;
            case R.id.buttonNew:
                restart();
                return;
            case R.id.buttonExit:
                finish();
                return;
            case R.id.imageWins:
                restart();
                return;
        }
        if (field.isEmpty(playerPosY, playerPosX)) {
            buttons[playerPosY][playerPosX].setImageDrawable(getResources().getDrawable(playerCellImageFirst));
            field.setCell(playerCell, playerPosY, playerPosX);
            sound.play("player_move");
            --restOfCells;
            play();
        } else {
            messageInToast(R.string.is_full, Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sound.stopSounds();
    }

    private void play() {
        if (field.isWin(playerCell, playerPosY, playerPosX)) {
            //Игрок победил
            sound.play("wins");
            messageInToast(R.string.player_wins, Toast.LENGTH_LONG);
            if (isWinnerGoX) {
                isPlayersMove = true;
            }
            ++playerScore;
            end();
        } else if (restOfCells != 0) {
            comp.move(restOfCells, playerPosY, playerPosX, field);
            --restOfCells;
            int compPosY = comp.getPosY();
            int compPosX = comp.getPosX();
            buttons[compPosY][compPosX].setImageDrawable(getResources().getDrawable(compCellImage));
            if (isPlayersMove) {
                buttons[playerPosY][playerPosX].setImageDrawable(getResources().getDrawable(playerCellImage));
            }
            field.setCell(compCell, compPosY, compPosX);
            if (field.isWin(compCell, compPosY, compPosX)) {
                //Компьютер победил
                sound.play("lost");
                messageInToast(R.string.comp_wins, Toast.LENGTH_LONG);
                if (isWinnerGoX) {
                    isPlayersMove = false;
                }
                ++compScore;
                end();
            }
        }
        if (!isGameOver) {
            if (restOfCells == 0) {
                //Ничья
                sound.play("nobody");
                messageInToast(R.string.draw_wins, Toast.LENGTH_LONG);
                if (isWinnerGoX) {
                    isPlayersMove = (playerCell == 'o');
                }
                end();
            }
        }
    }

    private void messageInToast(int resource, int length) {
        Toast.makeText(Game.this, getString(resource), length).show();
    }

    private void begin() {
        playerScore = 0;
        compScore = 0;
        changeScore();
        clearValues();
        setCells();
        imageWins.setVisibility(View.INVISIBLE);
        if (!isPlayersMove) {
            play();
        }
        messageInToast(R.string.players_move, Toast.LENGTH_SHORT);
    }

    private void restart() {
        field.clear();
        newField();
        clearValues();
        setCells();
        imageWins.setVisibility(View.INVISIBLE);
        if (!isPlayersMove) {
            play();
        }
    }

    private void newField() {
        for (int y = 0; y < amountOfCellsInRow; y++) {
            for (int x = 0; x < amountOfCellsInRow; x++) {
                buttons[y][x].setImageDrawable(getResources().getDrawable(R.drawable.states_cell_pressed));
            }
        }
    }

    private void end() {
        strikeOut();
        changeScore();
        isGameOver = true;
    }

    private void clearValues() {
        restOfCells = 9;
        isGameOver = false;
        sound.play("new");
    }

    private void setCells() {
        if (isPlayersMove) {
            this.playerCell = 'x';
            this.playerCellImage = R.drawable.states_x_pressed;
            this.playerCellImageFirst = R.drawable.cell_x;
            this.compCell = 'o';
            this.compCellImage = R.drawable.states_o_pressed;
        } else {
            this.playerCell = 'o';
            this.playerCellImage = R.drawable.states_o_pressed;
            this.playerCellImageFirst = R.drawable.cell_o;
            this.compCell = 'x';
            this.compCellImage = R.drawable.states_x_pressed;
        }
        imageOfPlayerCell.setImageDrawable(getResources().getDrawable(playerCellImage));
        comp.setCells(compCell, playerCell);
    }

    private void changeScore() {
        setTitle("[ " + this.getString(R.string.title_you) + " > " + playerScore + " : "
                + compScore + " < " + this.getString(R.string.title_comp) + " ]");
    }

    private void strikeOut() {
        int typeOfVictory = field.getTypeOfVictory();
        int src;
        switch (typeOfVictory) {
            case 0:
                src = R.drawable.wins_hor1;
                break;
            case 1:
                src = R.drawable.wins_hor2;
                break;
            case 2:
                src = R.drawable.wins_hor3;
                break;
            case 3:
                src = R.drawable.wins_vert1;
                break;
            case 4:
                src = R.drawable.wins_vert2;
                break;
            case 5:
                src = R.drawable.wins_vert3;
                break;
            case 6:
                src = R.drawable.wins_diag_down;
                break;
            case 7:
                src = R.drawable.wins_diag_up;
                break;
            default:
                src = R.drawable.wins_nobody;
                break;
        }
        imageWins.setImageDrawable(getResources().getDrawable(src));
        imageWins.setVisibility(View.VISIBLE);
    }
}
