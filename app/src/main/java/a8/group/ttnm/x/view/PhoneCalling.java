package a8.group.ttnm.x.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import a8.group.ttnm.x.R;

public class PhoneCalling extends AppCompatActivity implements View.OnClickListener {

    ArrayList<String> listOptions;
    ImageButton dialNum0, dialNum1, dialNum2, dialNum3, dialNum4, dialNum5, dialNum6, dialNum7, dialNum8, dialNum9, dialDelete;
    EditText txtNumber;
    ListView listOption;
    FloatingActionButton dialCall;
    String number = "";

    private void init() {
        dialNum0 = (ImageButton) findViewById(R.id.dial_num_0);
        dialNum0.setOnClickListener(this);
        dialNum1 = (ImageButton) findViewById(R.id.dial_num_1);
        dialNum1.setOnClickListener(this);
        dialNum2 = (ImageButton) findViewById(R.id.dial_num_2);
        dialNum2.setOnClickListener(this);
        dialNum3 = (ImageButton) findViewById(R.id.dial_num_3);
        dialNum3.setOnClickListener(this);
        dialNum4 = (ImageButton) findViewById(R.id.dial_num_4);
        dialNum4.setOnClickListener(this);
        dialNum5 = (ImageButton) findViewById(R.id.dial_num_5);
        dialNum5.setOnClickListener(this);
        dialNum6 = (ImageButton) findViewById(R.id.dial_num_6);
        dialNum6.setOnClickListener(this);
        dialNum7 = (ImageButton) findViewById(R.id.dial_num_7);
        dialNum7.setOnClickListener(this);
        dialNum8 = (ImageButton) findViewById(R.id.dial_num_8);
        dialNum8.setOnClickListener(this);
        dialNum9 = (ImageButton) findViewById(R.id.dial_num_9);
        dialNum9.setOnClickListener(this);
        dialDelete = (ImageButton) findViewById(R.id.btnDeleteNumber);
        dialDelete.setOnClickListener(this);
        dialCall = (FloatingActionButton) findViewById(R.id.fabCalling);
        dialCall.setOnClickListener(this);

        txtNumber = (EditText) findViewById(R.id.txtPhoneNumber);
        listOption = (ListView) findViewById(R.id.listOptions);


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
        int view = v.getId();
        if (view != R.id.btnDeleteNumber) {
            switch (number.length()) {
                case 4:
                    number = number + "-";
                    break;
                case 8:
                    number = number + "-";
                    break;
                case 12:
                    number = number + "-";
                    break;
            }
        }
        switch (view) {
            case R.id.fabCalling:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number.replaceAll("-", "")));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
                break;
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
            case R.id.btnDeleteNumber:
                //Log.d("HUYNHbeforeDelete",number);
                if(number.length() <= 0) break ;
                StringBuilder tempNumber = new StringBuilder(number);
                if(tempNumber.charAt(tempNumber.length()-1) == '-'){
                    tempNumber.replace(tempNumber.length()-2,tempNumber.length(),"");
                }
                else tempNumber.replace(tempNumber.length()-1,tempNumber.length(),"");
                number = tempNumber.toString();
                //Log.d("HUYNHafterDelete",number);
                break;
        }
        txtNumber.setText(number);
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(true);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
