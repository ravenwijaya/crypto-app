package com.raven.trcrypto.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.raven.trcrypto.Model.bank_account;
import com.raven.trcrypto.R;
import com.raven.trcrypto.WithdrawActivity;

import java.util.ArrayList;
import java.util.List;

public class RecycleBankAdapter extends RecyclerView.Adapter<RecycleBankAdapter.ViewHolder>{

    List<bank_account> lba;
    Context c;
    int checkedposition = 0;
    public RecycleBankAdapter(Context c, List<bank_account> acx){
        this.lba = acx;
        this.c = c;
    }

    public void setbankaccount(List<bank_account> acc){
        this.lba = new ArrayList<>();
        this.lba = acc;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecycleBankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        View v = inf.inflate(R.layout.bank_recycle,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull RecycleBankAdapter.ViewHolder holder, int position) {
            holder.bind(lba.get(position));
    }


    @Override
    public int getItemCount() {
        return lba.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView acc,nama,namalogo;
        ImageView logo;
        RadioButton dipilih;
        CardView pick_card_bank;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            acc = itemView.findViewById(R.id.account_number);
            nama = itemView.findViewById(R.id.account_name);
            logo  = itemView.findViewById(R.id.account_logo);
            namalogo = itemView.findViewById(R.id.namalogo);
            dipilih = itemView.findViewById(R.id.bank_dipilih);
            pick_card_bank = itemView.findViewById(R.id.pick_card_bank);
        }

        void bind(final bank_account bac){
            if(checkedposition ==-1){
                dipilih.setChecked(false);
            }else{
                if(checkedposition==getAdapterPosition()){
                    dipilih.setChecked(true);
                }else{
                    dipilih.setChecked(false);
                }
            }
            if(bac.getRekening().length()>27){
                String stacker = bac.getRekening().substring(0,27);
                acc.setText(stacker+"...");
            }else{
                acc.setText(bac.getRekening());
            }

            if(bac.getNama().length()>23){
                String stacker = bac.getNama().substring(0,23);
                nama.setText(stacker+"...");
            }else{
                nama.setText(bac.getNama());
            }

            String tentukan = bac.getLogo();
            if(tentukan.equals("0")){
                logo.setImageResource(R.drawable.ic_bca);
                namalogo.setText("BCA");
            }else if(tentukan.equals("1")){
                logo.setImageResource(R.drawable.ic_bni);
                namalogo.setText("BNI");
            }else if(tentukan.equals("2")){
                logo.setImageResource(R.drawable.ic_mandiri);
                namalogo.setText("MANDIRI");
            }else{
                logo.setImageResource(R.drawable.ic_bca);
                namalogo.setText("BCA");
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dipilih.setChecked(true);
                    if(checkedposition!=getAdapterPosition()){
                        notifyItemChanged(checkedposition);
                        checkedposition = getAdapterPosition();
                        //Toast.makeText(v.getContext(), "You're select :"+lba.get(checkedposition).getNama(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
    public bank_account getSelected(){
        if(checkedposition!=-1){
            return lba.get(checkedposition);
        }
        return null;
    }

}
