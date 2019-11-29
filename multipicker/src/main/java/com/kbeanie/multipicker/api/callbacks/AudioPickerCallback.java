package com.kbeanie.multipicker.api.callbacks;

import com.kbeanie.multipicker.api.entity.ChosenAudio;

import java.util.List;

public interface AudioPickerCallback extends PickerCallback {
    void onAudiosChosen(List<ChosenAudio> audios);
}
