package com.kbeanie.multipicker.api.callbacks;

import com.kbeanie.multipicker.api.entity.ChosenImage;

import java.util.List;

public interface ImagePickerCallback extends PickerCallback {
    void onImagesChosen(List<ChosenImage> images);
}
