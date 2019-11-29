package com.kbeanie.multipicker.api.callbacks;

import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;

import java.util.List;

public interface MediaPickerCallback extends PickerCallback {
    void onMediaChosen(List<ChosenImage> images, List<ChosenVideo> videos);
}
