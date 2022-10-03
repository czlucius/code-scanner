/*
 * Code Scanner. An android app to scan and create codes(barcodes, QR codes, etc)
 * Copyright (C) 2022 czlucius
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.czlucius.scan.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.czlucius.scan.R;
import com.czlucius.scan.Utils;
import com.czlucius.scan.database.CodeMemento;
import com.czlucius.scan.database.HistoryDao;
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
