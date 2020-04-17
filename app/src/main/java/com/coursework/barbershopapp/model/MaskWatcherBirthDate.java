package com.coursework.barbershopapp.model;

import android.text.Editable;
import android.text.TextWatcher;

public class MaskWatcherBirthDate implements TextWatcher {

    private boolean isRunning = false;
    private boolean isDeleting = false;
    private final String mask;

    public MaskWatcherBirthDate(String mask) {
        this.mask = mask;
    }

    public static MaskWatcherBirthDate buildCpf() {
        return new MaskWatcherBirthDate("##.##.####");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        isDeleting = count > after;
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (isRunning || isDeleting) {
            return;
        }
        isRunning = true;

        int editableLength = editable.length();
        if (editableLength < mask.length()) {
            if (mask.charAt(editableLength) != '#') {
                editable.append(mask.charAt(editableLength));
            }
        }
        isRunning = false;
    }
}
