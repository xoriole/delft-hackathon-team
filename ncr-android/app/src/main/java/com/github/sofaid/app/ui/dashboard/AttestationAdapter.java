package com.github.sofaid.app.ui.dashboard;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.sofaid.app.R;
import com.github.sofaid.app.models.internal.Attestation;

import java.util.List;

public class AttestationAdapter extends RecyclerView.Adapter<AttestationAdapter.MyViewHolder> {

    private List<Attestation> attestations;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAttestUser;
        public ImageView imgAttested;

        public MyViewHolder(View view) {
            super(view);
            tvAttestUser = (TextView) view.findViewById(R.id.tv_attest_user);
            imgAttested = (ImageView) view.findViewById(R.id.iv_attested);
        }
    }

    public AttestationAdapter(List<Attestation> attestations) {
        this.attestations = attestations;
    }

    public void setAttestations(List<Attestation> attestations){
        this.attestations = attestations;
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attest_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Attestation attestation = attestations.get(position);
        holder.tvAttestUser.setText(attestation.getAddress());
//        holder.imgAttested.setText(movie.getGenre());
    }

    @Override
    public int getItemCount() {
        return attestations.size();
    }
}