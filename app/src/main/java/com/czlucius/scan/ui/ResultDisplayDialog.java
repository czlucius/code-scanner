package com.czlucius.scan.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.czlucius.scan.R;
import com.czlucius.scan.databinding.ResultsPageBinding;
import com.czlucius.scan.objects.actions.Action;
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
