package com.github.sofaid.app.ui.dashboard;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.sofaid.app.R;
import com.github.sofaid.app.ethereum.ContractService;
import com.github.sofaid.app.models.internal.Attestation;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class AttestationFragment extends Fragment {

    private AttestationViewModel mViewModel;
    private RecyclerView recyclerView;
    private AttestationAdapter adapter;
    private List<Attestation> attestations;

    private ContractService contractService;

    public static AttestationFragment newInstance(ContractService contractService) {
        AttestationFragment fragment = new AttestationFragment();
        fragment.setContractService(contractService);
        return fragment;
    }

    public AttestationFragment(){
        attestations = new ArrayList<>();
    }

    public void setContractService(ContractService contractService) {
        this.contractService = contractService;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attestation_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_attestations);

        adapter = new AttestationAdapter(attestations);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchFromBlockchain();
    }

    public void fetchFromBlockchain(){
        if(contractService==null)
            return;
        List<Attestation> attestation = new ArrayList<>();
        contractService.getAttestations(contractService.getCredentials().getAddress()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("Completed");
                        adapter.setAttestations(attestation);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("Error!");
                    }

                    @Override
                    public void onNext(List list) {
                        System.out.println("List:"+list);
                        for(String address: (List<String>) list){
                            attestation.add(new Attestation(address, true));
                        }
                    }
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchFromBlockchain();
    }

}
