package com.rs.videotring;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MYVIEWHOLDER> {

    List<Users> list;
    RvListener listener;

    public UserAdapter(List<Users> list, RvListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MYVIEWHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_rec,parent,false);
      return   new MYVIEWHOLDER(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MYVIEWHOLDER holder, int position) {

     Users users =   list.get(position);

        holder.tvMbl.setText(users.getMobile());
        holder.tvName.setText(users.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


 class MYVIEWHOLDER extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName, tvMbl ;
        Button btnCall;


        public MYVIEWHOLDER(@NonNull View itemView) {
            super(itemView);

            tvMbl = itemView.findViewById(R.id.tvMbl);
            tvName = itemView.findViewById(R.id.tvName);

            btnCall = itemView.findViewById(R.id.callBtn);

            itemView.setOnClickListener(this);
            btnCall.setOnClickListener(this);
        }

     @Override
     public void onClick(View v) {

            listener.Rvclick(v,getAdapterPosition()

            );
     }
 }
}
