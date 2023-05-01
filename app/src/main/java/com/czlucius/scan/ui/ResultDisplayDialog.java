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

import android.content.Context;

import androidx.annotation.NonNull;

import com.czlucius.scan.R;
import com.czlucius.scan.databinding.ResultsPageBinding;
import com.czlucius.scan.objects.Code;
import com.czlucius.scan.objects.actions.Action;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;

import java.text.DateFormat;

public class ResultDisplayDialog extends BottomSheetDialog {
    private ResultsPageBinding binding;
    private Code code;

    public ResultDisplayDialog(@NonNull Context context, Code code) {
        super(context);
        this.code = code;
        initialize();
        populateLayout();
    }

    private void initialize() {
        setCancelable(true);
        binding = ResultsPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }


    @Override
    protected void onStart() {
        super.onStart();
        getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void populateLayout() {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        binding.dateContents.setText(dateFormat.format(code.getTimeScanned()));
        binding.typeContents.setText(code.getDataType().getTypeName());
        binding.formatContents.setText(code.getFormatName(getContext()));
        binding.codeContentsText.setText(code.getData().getStringRepresentation());


        binding.actionsGroup.removeAllViews();
        Chip chip;
        for (Action obj : code.getDataType().getActions()) {
            chip = (Chip) getLayoutInflater()
                    .inflate(R.layout.template_chip, binding.actionsGroup, false);
            if (obj.getActionIcon() != null) {
                chip.setChipIconResource(obj.getActionIcon());
            }
            chip.setText(obj.getActionText());
            chip.setOnClickListener((v) -> {
                obj.performAction(getContext(), code.getData());
            });
            binding.actionsGroup.addView(chip);
        }

    }
}
