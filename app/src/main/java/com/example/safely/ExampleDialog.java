package com.example.safely;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ExampleDialog extends AppCompatDialogFragment {
    private EditText editname;
    private EditText editphone;
    private ExampleDialogListener listener;
    FirebaseDatabase db;
    FirebaseUser user;
    private DatabaseReference reference ;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);

        builder.setView(view)
                .setTitle("Add Guardian")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String name = editname.getText().toString();

                        String phone = editphone.getText().toString();

                        updateGuardian(name,phone);

                        listener.applyTexts(name,phone);


                    }
                });
        editname =view.findViewById(R.id.editname);
        editphone = view.findViewById(R.id.editphone);
        return builder.create();
    }

    private void updateGuardian(String nameg,String phoneg) {

        HashMap UserG = new HashMap();
        UserG.put("guardianName",nameg);
        UserG.put("guardianPhone",phoneg);
        user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        db = FirebaseDatabase.getInstance();
        reference = db.getReference(uid);
        reference.child("GuardianInfo").updateChildren(UserG).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                if (task.isSuccessful()){

                    editname.setText("");
                    editphone.setText("");
//                    Toast.makeText(Guardian.this,"Successfully Updated",Toast.LENGTH_SHORT).show();

                }else {

//                    Toast.makeText(UpdateData.this,"Failed to Update",Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener=(ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + "must implement ExampleDialogListener");
        }
    }

    public interface ExampleDialogListener{
        void applyTexts (String name, String phone);
    }
}