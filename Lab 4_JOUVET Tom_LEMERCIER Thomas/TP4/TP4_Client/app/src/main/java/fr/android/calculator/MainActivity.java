package fr.android.calculator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    public TextView operation;
    public TextView resultat;
    public Socket s;
    int cpt;
    float num1;
    float num2;
    float calcul;
    String tamp;
    String running;
    String result;
    boolean ops;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.operation = (TextView) findViewById(R.id.textView1);
        this.resultat = (TextView) findViewById(R.id.textView2);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        LinearLayout buttonsGroup = findViewById(R.id.actionButtons);

        Button Equal = new Button(this);
        Equal.setId(R.id.equalButton);
        Equal.setText("=");
        Equal.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                )
        );

        //Version Async
        Equal.setOnClickListener(buttonEqualAsync());

        buttonsGroup.addView(Equal);
        cpt = 0;
        num1 = 0;
        num2 = 0;
        calcul = 0;
        tamp = "";
        running = null;
        result = null;
        ops = false;
    }

    //Version Async
    View.OnClickListener buttonEqualAsync()  {
        return new View.OnClickListener() {
            public void onClick(View v) {
                new functEqual().execute(String.valueOf(operation.getText()));
            }
        };
    }

    private class functEqual extends AsyncTask<String, String, String> {
        protected String doInBackground(String... rslt) {
            cpt = 0;
            num1 = 0;
            num2 = 0;
            calcul = 0;
            tamp = "";
            running = rslt[0];
            result = null;
            ops = false;

            if(running.isEmpty()) {
                ops = true;
                result = "Empty operation !";
            } else {
                if (running.charAt(0) == '+' || running.charAt(0) == 'x' || running.charAt(0) == '/') {
                    ops = true;
                    result = "Operation problem !";
                }

                if(running.charAt(0) == '-') {
                    tamp += running.charAt(cpt);
                    cpt++;
                }
            }

            while(ops != true) {
                if(running.charAt(cpt) == '+' || running.charAt(cpt) == 'x' || running.charAt(cpt) == '/' || running.charAt(cpt) == '-') {
                    ops = true;
                    num1 = Integer.parseInt(tamp);

                    try {
                        s = new Socket("10.0.2.2", 9876);

                        DataInputStream dis = new DataInputStream(s.getInputStream());
                        DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                        dos.writeFloat(num1);
                        dos.writeChar(running.charAt(cpt));
                        num2 = Integer.parseInt(running.substring(cpt));
                        dos.writeFloat(num2);

                        result = String.valueOf(dis.readFloat());

                        s.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    tamp += running.charAt(cpt);
                    cpt++;

                    if(cpt == running.length() || cpt+1 == running.length()) {
                        ops = true;
                        result = "Operation problem !";
                    }
                }
            }
            return result;
        }

        protected void onProgressUpdate(String... values) {
            resultat.setText("Calculating ...");
        }

        protected void onPreExecute() {
            resultat.setText("Checking ...");
        }

        protected void onPostExecute(String result) {
            resultat.setText(result);
            result=null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void myClickHandler(View view){
        String running = String.valueOf(this.operation.getText());
        switch(view.getId()){
            case R.id.button1:
                running+="7";
                break;

            case R.id.button2:
                running+="8";
                break;

            case R.id.button3:
                running+="9";
                break;

            case R.id.button4:
                running+="+";
                break;

            case R.id.button5:
                running+="4";
                break;

            case R.id.button6:
                running+="5";
                break;

            case R.id.button7:
                running+="6";
                break;

            case R.id.button8:
                running+="-";
                break;

            case R.id.button9:
                running+="1";
                break;

            case R.id.button10:
                running+="2";
                break;

            case R.id.button11:
                running+="3";
                break;

            case R.id.button12:
                running+="x";
                break;

            case R.id.button13:
                running+="0";
                break;

            case R.id.button14:
                running+="/";
                break;
            case R.id.button15:
                if(running.length() > 0){
                    running = running.substring(0, running.length() - 1);
                }
                break;
        }

        this.operation.setText(running);
    }
    public boolean menu (MenuItem item)
    {
        Intent intent = new Intent(this,LastOperation.class);
        intent.putExtra("operation",running);
        intent.putExtra("resultat",result);
        startActivity(intent);
        return (true);
    }
}

