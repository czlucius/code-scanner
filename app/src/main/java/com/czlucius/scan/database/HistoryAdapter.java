package com.czlucius.scan.database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.czlucius.scan.R;
import com.czlucius.scan.ui.ResultDisplayDialog;
import com.czlucius.scan.Utils;
import com.czlucius.scan.databinding.SingleHistoryItemBinding;
import com.czlucius.scan.objects.Code;
import com.czlucius.scan.objects.HistoryCode;

import java.text.DateFormat;
import java.util.List;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryVH> {
    private List<HistoryCode> data;
    private HistoryDao dao;


    public HistoryAdapter(HistoryDao dao) {
        // TODO set up RxJava support in the future.
        data = Utils.getAllSortLatest(dao.getAll());
    }



    public synchronized void inserted(int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                data = Utils.getAllSortLatest(dao.getAll());
                notifyItemInserted(position);
            }
        }).start();

    }

    @NonNull
    @Override
    public HistoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.single_history_item, parent, false);
        return new HistoryVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryVH holder, int position) {
        SingleHistoryItemBinding binding = SingleHistoryItemBinding.bind(holder.itemView);
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        binding.historyItemDate.setText(dateFormat.format(data.get(position).getDate()));
        binding.historyItemSummary.setText(data.get(position).getContents().getDisplayValue());
        binding.historyItemType.setText(data.get(position).getDataType().getTypeName());

        // Display UI for showing Code information
        holder.itemView.setOnClickListener(v -> {
            HistoryCode code = data.get(position);
            new ResultDisplayDialog(holder.itemView.getContext(), Code.fromHistoryElement(code)).show();
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class HistoryVH extends RecyclerView.ViewHolder {

        public HistoryVH(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface AdapterCallback {
        HistoryAdapter getHistoryAdapterAsync(HistoryDao dao);
    }
}
