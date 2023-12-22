package com.example.minesweeper;

import android.content.Context;
import android.graphics.Color;
import android.widget.TableLayout;
import android.widget.TableRow;


//BlockButton 클래스는 Button 클래스를 상속한다.
public class BlockButton extends androidx.appcompat.widget.AppCompatButton {
    //블럭의 위치
    public int x, y;
    //지뢰인지 아닌지 -> 10개만 랜덤으로 true
    public boolean mine;
    //깃발 꽂았는지 안 꽂았는지
    public boolean flag;
    //지뢰 주변에 몇 개인지
    public int neighborMines;
    //깃발 꽂힌 블럭 몇 개인지
    static int flags;
    // 지금 몇 개의 블럭 남았는지
    static int blocks;
    static int mines = 10;



    // 생성자
    public BlockButton(Context context, int x, int y) {
        //버튼의 생성자 호출
        super(context);
        //레이아웃파라미터 세팅
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        this.setLayoutParams(layoutParams);
        //블럭 남은 갯수 ++ -> 81개 블럭 생성되면 81개
        blocks++;
        //블럭이 생성될 때 반복문의 i,j가 x,y 좌표가 된다.
        this.x = x;
        this.y = y;
        // 초기값 설정
        this.mine = false;
        this.flag = false;
        this.neighborMines = 0;
    }

    public void toggleFlag() {
        // 선택한 블럭이 깃발 안 꽂혀있을 때
        if (!this.flag) {
            this.flag = true;
            this.setText("+");
            flags++;
            if(this.mine == true){
                mines--;
            }
        }
        // 선택한 블럭이 깃발 꽂혀있을 때 -> 깃발 취소
        else {
            this.flag = false;
            this.setText("");
            flags--;
        }
    }

    public boolean breakBlock() {
        // 지뢰인 블럭 break 할 때
        if(this.mine){
            this.setText("B");
            System.out.println(blocks);
            return true;
        }
         // 이웃인 블럭 중 지뢰가 한 개도 없을 때 -> 그냥 블럭 삭제
        else if(this.neighborMines == 0){
            this.setBackgroundColor(Color.parseColor("#00000000"));
            // 블럭이 unclickable하게 만들어줌
            this.setClickable(false);
            // 남아있는 블럭의 개수 줄이기
            blocks--;
            System.out.println("blocks"+blocks);
            return false;
        }
        // 이웃인 블럭 중 지뢰가 있는 경우 -> 이웃 중 지뢰 개수 표시
        else{
            this.setText( Integer.toString(neighborMines));
            this.setClickable(false);
            blocks--;
            System.out.println("blocks"+blocks);
            return false;
        }
    }

    public static boolean calculateNeighbor(BlockButton[][] buttons,int x, int y){
        if(x >= 0 && y >= 0 && x <=8 && y <= 8){
            if(buttons[x][y].mine){
                return true;
            }
            else
                return false;
        }
        else
            return false;
    }

    public static boolean calculate(int x, int y){
        if(x >= 0 && y >= 0 && x <= 8 && y <= 8){
            return true;
        }
        else
            return false;
    }


}
