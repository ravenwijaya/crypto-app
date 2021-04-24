package com.raven.trcrypto;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.raven.trcrypto.Adapter.BankAdapter;
import com.raven.trcrypto.Model.bank_item;

import java.util.ArrayList;

public class AddBankDialog extends AppCompatDialogFragment {
    private Spinner bank;
    private EditText account_bank,name_bank;
    private ArrayList<bank_item> listbank;
    private BankAdapter BA;
    private AddBankDialogListener listener;
    private int posisi_bank = 0;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inf = getActivity().getLayoutInflater();
        View view = inf.inflate(R.layout.addbank_dialog,null);
        builder
                .setView(view)
                .setTitle("Add bank account")
                .setIcon(R.drawable.ic_baseline_texture_24)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String akun = account_bank.getText().toString();
                        String nama  = name_bank.getText().toString();
                        int pos = posisi_bank;
                        listener.applytext(akun,nama,pos);
                    }
                });
        init_list_bank();
        bank = view.findViewById(R.id.bank);
        account_bank = view.findViewById(R.id.bank_acc);
        name_bank = view.findViewById(R.id.bank_name);

        BA = new BankAdapter(getActivity(),listbank);
        bank.setAdapter(BA);
        bank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bank_item bi = (bank_item) parent.getItemAtPosition(position);
                posisi_bank = position;
                //String clickedbankname = bi.getNama_bank();
                //Toast.makeText(getActivity(), Integer.toString(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return builder.create();
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddBankDialogListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + "Harus mengimplementasikan AddbankDialog Listener");
        }
    }

    public interface AddBankDialogListener {
        void applytext(String acc_bank, String nama_bank, int pos);
    }

    public void init_list_bank(){
        listbank = new ArrayList<>();
        listbank.add(new bank_item("Bank BCA", R.drawable.ic_bca));
        listbank.add(new bank_item("Bank BNI", R.drawable.ic_bni));
        listbank.add(new bank_item("Bank Mandiri", R.drawable.ic_mandiri));
    }
}
