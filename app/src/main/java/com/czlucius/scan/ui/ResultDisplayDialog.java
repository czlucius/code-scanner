package com.czlucius.scan.ui;

import android.content.Context;

import androidx.annotation.NonNull;

import com.czlucius.scan.R;
import com.czlucius.scan.databinding.ResultsPageBinding;
import com.czlucius.scan.objects.Action;
import com.czlucius.scan.objects.Code;
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

    public void initialize() {
        setCancelable(true);
        binding = ResultsPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }


    @Override
    protected void onStart() {
        super.onStart();
        getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void populateLayout() {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        binding.dateContents.setText(dateFormat.format(code.getTimeScanned()));
        binding.typeContents.setText(code.getDataType().getTypeName());
        binding.formatContents.setText(code.getFormatName());
        binding.codeContentsText.setText(code.getContents().getDisplayValue());


        binding.actionsGroup.removeAllViews();
        Chip chip;
        for (Action obj : code.getDataType().getActions()) {
            chip = (Chip) getLayoutInflater()
                    .inflate(R.layout.template_chip, binding.actionsGroup, false);
            chip.setChipIconResource(obj.getActionIcon());
            chip.setText(obj.getActionText());
            chip.setOnClickListener((v) -> {
                obj.doAction(getContext(), code.getContents());
            });
            binding.actionsGroup.addView(chip);
        }

    }



}
