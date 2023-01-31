package com.example.safely;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Guardian extends AppCompatActivity implements ExampleDialog.ExampleDialogListener {
    private TextView txtname;
    private TextView txtphone;
    private Button button;
    private FirebaseUser fUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian);

        txtname = (TextView) findViewById(R.id.txtname);
        txtphone = (TextView) findViewById(R.id.txtphone);
        button = (Button) findViewById(R.id.addbtn);

        //Get current user
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        //retrieve data

        FirebaseDatabase.getInstance().getReference().child(fUser.getUid()).child("GuardianInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GuardianConstructor Guser = snapshot.getValue(GuardianConstructor.class);
                if(Guser != null){
                    txtname.setText(Guser.getGuardianName());
                    txtphone.setText(Guser.getGuardianPhone());
                }else{
                    txtname.setText("unknown");
                    txtphone.setText("unknown");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

    }
    public void openDialog(){
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(),"example dialog");
    }

    @Override
    public void applyTexts(String name, String phone) {
        txtname.setText(name);
        txtphone.setText(phone);
    }
}