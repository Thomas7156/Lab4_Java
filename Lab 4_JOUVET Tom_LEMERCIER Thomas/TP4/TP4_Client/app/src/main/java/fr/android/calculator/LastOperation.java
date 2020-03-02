package fr.android.calculator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LastOperation extends AppCompatActivity {

    String calcul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle before = getIntent().getExtras();
        calcul="";
        if(before != null) {
            calcul = before.getString("operation") + "=" + before.getString("resultat");
        }
        TextView last_op = findViewById(R.id.last_operation);
        last_op.setText(calcul);
    }

    public boolean retour(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return (true);
    }

    public void Web(View view) {
        EditText edit = (EditText)findViewById(R.id.url_web);
        String url_web = edit.getText().toString();
        Uri web = Uri.parse(url_web);
        Intent intent = new Intent(Intent.ACTION_VIEW, web);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
