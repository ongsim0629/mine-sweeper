package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    BlockButton[][] buttons = new BlockButton[9][9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI 디자인에 필요한 애들
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        //9개의 테이블로우와 각각의 테이블로우 안에 9개의 버튼 만들기
        for (int i = 0; i < 9; i++) {
            TableRow tableRow = new TableRow(this);
            tableLayout.addView(tableRow);
            for (int j = 0; j < 9; j++) {
                buttons[i][j] = new BlockButton(this, i, j);
                tableRow.addView(buttons[i][j]);
            }
        }

        //지뢰 세팅에 필요한 애들
        Random random = new Random();
        int mineNum = 0;

        // 만들어진 81개의 블럭 중 10개를 골라서 지뢰로 설정
        while (mineNum < 10) {
            //0~8 숫자 얻기
            int ranX = random.nextInt(8);
            int ranY = random.nextInt(8);
            if (buttons[ranX][ranY].mine) {
                continue;
            } else {
                buttons[ranX][ranY].mine = true;
                mineNum++;
            }
        }


        // 현재 모든 블럭의 neighborMines는 0 -> 주변 지뢰있는지 확인하고 다시 초기화
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (BlockButton.calculateNeighbor(buttons, i - 1, j - 1))
                    buttons[i][j].neighborMines++;
                if (BlockButton.calculateNeighbor(buttons, i - 1, j))
                    buttons[i][j].neighborMines++;
                if (BlockButton.calculateNeighbor(buttons, i - 1, j + 1))
                    buttons[i][j].neighborMines++;
                if (BlockButton.calculateNeighbor(buttons, i, j - 1))
                    buttons[i][j].neighborMines++;
                if (BlockButton.calculateNeighbor(buttons, i, j + 1))
                    buttons[i][j].neighborMines++;
                if (BlockButton.calculateNeighbor(buttons, i + 1, j - 1))
                    buttons[i][j].neighborMines++;
                if (BlockButton.calculateNeighbor(buttons, i + 1, j))
                    buttons[i][j].neighborMines++;
                if (BlockButton.calculateNeighbor(buttons, i + 1, j + 1))
                    buttons[i][j].neighborMines++;
            }
        }


        //토글 버튼 설정에 필요한 애
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButton);

        //남은 Mines 바꾸기 위해서 필요한 애
        TextView textView = (TextView) findViewById(R.id.Mines);

        //토글 버튼의 상태에 따라서 toggleFlag()부를지 breakBlock() 부를지
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            buttons[i][j].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ((BlockButton) view).toggleFlag();
                                    textView.setText(String.valueOf(BlockButton.mines));
                                    if(BlockButton.mines==0){
                                        for(int i=0;i<9;i++) {
                                            for (int j = 0; j < 9; j++)
                                            {
                                                buttons[i][j].setClickable(false);
                                            }
                                        }
                                        gameWin();
                                    }
                                }
                            });
                        }
                    }
                }
                else{
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            buttons[i][j].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //BlockButton 클래스의 breakBlock이 true -> 지뢰 눌렀음
                                    if(((BlockButton) view).breakBlock()){
                                        //블럭 클릭 못하게 처리
                                        for(int k=0;k<9;k++) {
                                            for (int l = 0; l < 9; l++)
                                            {
                                                buttons[k][l].setClickable(false);
                                            }
                                        }
                                        gameOver();
                                    }
                                    //BlockButton 클래스의 breakBlock이 false -> 지뢰 아님
                                    //메인액티비티의 breakBlock 메소드로 이웃 0개면 나머지 까줘야된다
                                    else{
                                        // 이웃 0개
                                        if((breakBlock((BlockButton) view))){

                                        }

                                        // 이웃 0개 x
                                        else {

                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });

    }

    public boolean breakBlock(BlockButton button) {
        if (button.neighborMines == 0) {
            int x = button.x;
            int y = button.y;
            if(BlockButton.calculate(x - 1, y - 1) && buttons[x - 1][y - 1].isClickable()){
                buttons[x-1][y-1].breakBlock();
                if(buttons[x-1][y-1].neighborMines==0){
                    breakBlock(buttons[x-1][y-1]);
                }
            }
            if(BlockButton.calculate(x - 1, y) && buttons[x - 1][y].isClickable()){
                buttons[x-1][y].breakBlock();
                if(buttons[x-1][y].neighborMines==0){
                    breakBlock(buttons[x-1][y]);
                }
            }
            if(BlockButton.calculate(x - 1, y + 1) && buttons[x - 1][y + 1].isClickable()){
                buttons[x-1][y+1].breakBlock();
                if(buttons[x-1][y+1].neighborMines==0){
                    breakBlock(buttons[x-1][y+1]);
                }
            }
            if(BlockButton.calculate(x + 0, y - 1) && buttons[x][y - 1].isClickable()){
                buttons[x][y-1].breakBlock();
                if(buttons[x][y-1].neighborMines==0){
                    breakBlock(buttons[x][y-1]);
                }
            }
            if(BlockButton.calculate(x + 0, y + 1) && buttons[x][y + 1].isClickable()){
                buttons[x][y+1].breakBlock();
                if(buttons[x][y+1].neighborMines==0){
                    breakBlock(buttons[x][y+1]);
                }
            }
            if(BlockButton.calculate(x + 1, y - 1) && buttons[x + 1][y - 1].isClickable()){
                buttons[x+1][y-1].breakBlock();
                if(buttons[x+1][y-1].neighborMines==0){
                    breakBlock(buttons[x+1][y-1]);
                }
            }
            if(BlockButton.calculate(x + 1, y + 0) && buttons[x + 1][y].isClickable()){
                buttons[x+1][y].breakBlock();
                if(buttons[x+1][y].neighborMines==0){
                    breakBlock(buttons[x+1][y]);
                }
            }
            if(BlockButton.calculate(x + 1, y + 1) && buttons[x + 1][y + 1].isClickable()){
                buttons[x+1][y+1].breakBlock();
                if(buttons[x+1][y+1].neighborMines==0){
                    breakBlock(buttons[x+1][y+1]);
                }
            }
        }
        // 이웃한 블럭에 지뢰가 있는 경우 -> 그냥 BlockButton 클래스의 breakBlock 메소드의 기능으로 충분
        else {
            return false;
        }
        return false;
    }



    //시간 있으면 바꾸기
    public void gameOver(){
        Toast.makeText(this, "게임오버", Toast.LENGTH_SHORT).show();
    }

    public void gameWin(){
        Toast.makeText(this, "게임클리어", Toast.LENGTH_SHORT).show();
    }


}