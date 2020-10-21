package com.czlucius.scan.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.czlucius.scan.database.HistoryAdapter;
import com.czlucius.scan.database.HistoryDao;
import com.czlucius.scan.database.HistoryDatabase;
import com.czlucius.scan.databinding.FragmentHistoryBinding;


public class HistoryFragment extends Fragment {
    private FragmentHistoryBinding binding;
    private RecyclerView.LayoutManager layoutManager;
    private HistoryAdapter adapter;


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutManager = new LinearLayoutManager(getContext());
        binding.historyList.setLayoutManager(layoutManager);
        HistoryDao dao = HistoryDatabase.getInstance(getContext()).historyDao();
        new Thread(() -> {
            adapter = new HistoryAdapter(dao);
            view.post(() -> binding.historyList.setAdapter(adapter));
        }).start();

    }
}