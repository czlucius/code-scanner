package com.czlucius.scan.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.czlucius.scan.R;
import com.czlucius.scan.database.CodeMemento;
import com.czlucius.scan.database.HistoryDao;
import com.czlucius.scan.Utils;
import com.czlucius.scan.databinding.SingleHistoryItemBinding;
import com.czlucius.scan.objects.Code;

import java.text.DateFormat;
import java.util.List;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<CodeMemento> data;


    public HistoryAdapter(HistoryDao dao) {
        data = Utils.getAllSortLatest(dao.getAll());
    }

    public void invalidate(List<CodeMemento> newData) {
        data = newData;
        notifyDataSetChanged();
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.single_history_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SingleHistoryItemBinding binding = SingleHistoryItemBinding.bind(holder.itemView);
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        binding.historyItemDate.setText(dateFormat.format(data.get(position).getDate()));
        binding.historyItemSummary.setText(data.get(position).getDisplayData());
        binding.historyItemType.setText(data.get(position).getDataType().getTypeName());

        // Display UI for showing Code information
        holder.itemView.setOnClickListener(v -> {
            CodeMemento code = data.get(position);
            new ResultDisplayDialog(holder.itemView.getContext(), Code.fromHistoryElement(code)).show();
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
