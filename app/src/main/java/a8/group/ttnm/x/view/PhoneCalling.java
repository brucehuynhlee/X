package a8.group.ttnm.x.view;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import a8.group.ttnm.x.R;

public class PhoneCalling extends AppCompatActivity implements View.OnClickListener{

    ImageButton dialNum0 , dialNum1, dialNum2 , dialNum3 , dialNum4 , dialNum5 ,dialNum6,dialNum7,dialNum8,dialNum9,dialBack , dialCall , dial_Delete ;
    EditText txtNumber ;
    ListView listOption ;
    String number = "" ;
    private void init(){
        dialNum0 = (ImageButton)findViewById(R.id.dial_num_0);
        dialNum0.setOnClickListener(this);
        dialNum1 = (ImageButton)findViewById(R.id.dial_num_1);
        dialNum1.setOnClickListener(this);
        dialNum2 = (ImageButton)findViewById(R.id.dial_num_2);
        dialNum2.setOnClickListener(this);
        dialNum3 = (ImageButton)findViewById(R.id.dial_num_3);
        dialNum3.setOnClickListener(this);
        dialNum4 = (ImageButton)findViewById(R.id.dial_num_4);
        dialNum4.setOnClickListener(this);
        dialNum5 = (ImageButton)findViewById(R.id.dial_num_5);
        dialNum5.setOnClickListener(this);
        dialNum6 = (ImageButton)findViewById(R.id.dial_num_6);
        dialNum6.setOnClickListener(this);
        dialNum7 = (ImageButton)findViewById(R.id.dial_num_7);
        dialNum7.setOnClickListener(this);
        dialNum8 = (ImageButton)findViewById(R.id.dial_num_8);
        dialNum8.setOnClickListener(this);
        dialNum9 = (ImageButton)findViewById(R.id.dial_num_9);
        dialNum9.setOnClickListener(this);
        dialBack = (ImageButton)findViewById(R.id.dial_back);
        dialBack.setOnClickListener(this);
        txtNumber = (EditText)findViewById(R.id.txtNumber);
        listOption = (ListView)findViewById(R.id.listOption);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_calling);
        init();
        disableEditText(txtNumber);
    }

    @Override
    public void onClick(View v) {
        switch(number.length()){
            case 4:
                number = number + "-" ;
                break;
            case 8:
                number = number + "-" ;
                break;
            case 12:
                number = number + "-" ;
                break;
        }
        int view = v.getId() ;
        switch (view){
            case R.id.dial_num_0:
                number = number + "0";
                break;
            case R.id.dial_num_1:
                number = number + "1";
                break;
            case R.id.dial_num_2:
                number = number + "2";
                break;
            case R.id.dial_num_3:
                number = number + "3";
                break;
            case R.id.dial_num_4:
                number = number + "4";
                break;
            case R.id.dial_num_5:
                number = number + "5";
                break;
            case R.id.dial_num_6:
                number = number + "6";
                break;
            case R.id.dial_num_7:
                number = number + "7";
                break;
            case R.id.dial_num_8:
                number = number + "8";
                break;
            case R.id.dial_num_9:
                number = number + "9";
                break;
            case R.id.dial_back:
                if(number.length()<=0) break;
                StringBuilder builder = new StringBuilder(number);
                int selectStart = txtNumber.getSelectionStart();
                int selectEnd = txtNumber.getSelectionEnd();
                builder.replace(selectStart-1,selectStart,"");
                Toast.makeText(getApplicationContext(),selectStart + "and" + selectEnd + " bulier:" + builder.toString(),Toast.LENGTH_LONG).show();
                number = builder.toString();


        }
        txtNumber.setText(number);
    }
    private void disableEditText(EditText editText) {
        editText.setFocusable(true);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }
}
