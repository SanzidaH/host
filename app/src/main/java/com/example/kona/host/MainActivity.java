package com.example.kona.host;

import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView textname,textnameemail;
    private EditText edittextname,edittextnameemail;
    private Button buttonOrder;

    FirebaseDatabase database;
    DatabaseReference myRef;

    String dataTitle, dataMessage;
    public EditText title, message;

    //database reference
    DatabaseReference databaseprofile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textname = (TextView) findViewById(R.id.textname);
        edittextname = (EditText) findViewById(R.id.edittextname);
//        title = (EditText) findViewById(R.id.Title);
//        message = (EditText) findViewById(R.id.Message);
        buttonOrder = (Button) findViewById(R.id.buttonOrder);



        buttonOrder.setOnClickListener(this);

        databaseprofile = FirebaseDatabase.getInstance().getReference("Order");



        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("konahoque@gmail.com","password")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                            textname.setText("Success!!");
                        DatabaseReference notificationref =  FirebaseDatabase.getInstance().getReference("Order");
                    }
                });

        // Handle possible data accompanying notification message.
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                if (key.equals("title")) {
                    dataTitle=(String)getIntent().getExtras().get(key);
                }
                if (key.equals("message")) {
                    dataMessage = (String)getIntent().getExtras().get(key);;
                }
            }
            showAlertDialog();
        }

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("messages");

        title = (EditText) findViewById(R.id.Title);
        message = (EditText) findViewById(R.id.Message);
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message");
        builder.setMessage("title: " + dataTitle + "\n" + "message: " + dataMessage);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    public void subscribeToTopic(View view) {
        FirebaseMessaging.getInstance().subscribeToTopic("notifications");
        Toast.makeText(this, "Subscribed to Topic: Notifications", Toast.LENGTH_SHORT).show();
    }

    public void sendMessage(View view) {
        myRef.push().setValue(new Message(title.getText().toString(), message.getText().toString()));
        Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();


    }



}

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View view) {

        if(view==buttonOrder){
            //Add to Activity
            FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");
            Toast.makeText(this, "You gave a Order",Toast.LENGTH_SHORT).show();
        }


    }
}
