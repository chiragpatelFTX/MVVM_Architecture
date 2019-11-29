package com.kbeanie.multipicker.api.callbacks;

import com.kbeanie.multipicker.api.entity.ChosenContact;

public interface ContactPickerCallback extends PickerCallback {
    void onContactChosen(ChosenContact contact);
}
