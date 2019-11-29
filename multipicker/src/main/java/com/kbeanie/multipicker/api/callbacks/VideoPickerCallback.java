package com.kbeanie.multipicker.api.callbacks;

import com.kbeanie.multipicker.api.entity.ChosenVideo;

import java.util.List;

public interface VideoPickerCallback extends PickerCallback {
    void onVideosChosen(List<ChosenVideo> videos);
}
