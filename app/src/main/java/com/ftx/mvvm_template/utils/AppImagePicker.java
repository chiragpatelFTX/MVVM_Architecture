package com.ftx.mvvm_template.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.kbeanie.multipicker.api.CacheLocation;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.ftx.mvvm_template.R;

import java.util.ArrayList;
import java.util.List;



/**
 * Name : AppImagePicker
 *<br> Purpose :AppImagePicker class to choose image from gallary or camera.
 */
public class AppImagePicker {
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    public Context context;

    private ImagePicker imagePicker;

    public String getPickerPath() {
        return pickerPath;
    }

    public CameraImagePicker getCameraPicker() {
        return cameraPicker;
    }

    public void setCameraPicker(CameraImagePicker cameraPicker) {
        this.cameraPicker = cameraPicker;
    }

    private  CameraImagePicker cameraPicker;
    public static String pickerPath;

    private static AppImagePicker appImagePicker;

    public static AppImagePicker getInstance(){
        if(appImagePicker == null){
            appImagePicker = new AppImagePicker();
        }
        return appImagePicker;

    }

    /**
     * Name : AppImagePicker openDialog
     *<br> Purpose : Open dialog to ask for camera or gallery.
     * @param context   : context object
     * @param callback  : callback implementation from activity/fragment.
     */
    public  void openDialog(final Context context, final ImagePickerCallback callback) {
        this.context = context;
        String[] imageArray = context.getResources().getStringArray(R.array.image_picker_type);

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getString(R.string.image_picker_title));
        dialog.setCancelable(true);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, imageArray);
        dialog.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                ImageChooser(context, which,callback);
            }
        });
        dialog.create().show();
    }

    /**
     * Name : AppImagePicker ImageChooser
     *<br> Purpose :     When we choose any one option from dialog then
     * this method will be executed. to capture image or take from gallery.
     * @param context   : context object
     * @param position  : option which we selected from dialog box.
     * @param callback  : callback which are implemented in activity/fragment.
     */
    private  void ImageChooser(Context context, int position, ImagePickerCallback callback) {
        switch (position) {
            case 0:
                if (isDeviceSupportCamera(context)) {
                    captureImage(context,callback);
                } else {
                    CommonUtils.showToast(context, context.getString(R.string.error_camera_not_available));
                }
                break;

            case 1:
                imageFromGallery(context,callback);
                break;
            default:
                break;
        }
    }

    /**
     * Name : AppImagePicker captureImage
     *<br> Purpose :This method will be called to capture image from camera.
     * @param context   : context object
     * @param callback  : callback object, at which we will receive our captured image.
     */
    private  void captureImage(Context context, ImagePickerCallback callback) {
        List<String> uriList = new ArrayList<>();
        cameraPicker = new CameraImagePicker((Activity) context);
        cameraPicker.setDebugglable(true);
        cameraPicker.setCacheLocation(CacheLocation.EXTERNAL_STORAGE_APP_DIR);
        cameraPicker.setImagePickerCallback(callback);
        cameraPicker.shouldGenerateMetadata(true);
        cameraPicker.shouldGenerateThumbnails(true);
        pickerPath = cameraPicker.pickImage();
    }

    /**
     * Name : AppImagePicker imageFromGallery
     *<br> Purpose :This method will be called to image from gallery..
     * @param context   : context object
     * @param callback  : callback object, at which we will receive our captured image.
     */
    private  void imageFromGallery(Context context, ImagePickerCallback callback) {
        imagePicker = new ImagePicker((Activity) context);
        imagePicker.setFolderName("Random");
        imagePicker.setRequestId(1234);
        imagePicker.ensureMaxSize(500, 500);
        imagePicker.shouldGenerateMetadata(true);
        imagePicker.shouldGenerateThumbnails(true);
        imagePicker.setImagePickerCallback(callback);
        Bundle bundle = new Bundle();
        bundle.putInt("android.intent.extras.CAMERA_FACING", 1);
        imagePicker.setCacheLocation(CacheLocation.EXTERNAL_STORAGE_APP_DIR);
       // imagePicker.setCacheLocation(getSavedCacheLocation(context));
        imagePicker.pickImage();
    }

    /**
     * Name : AppImagePicker isDeviceSupportCamera
     *<br> Purpose : Method to check does the device supports camera
     * @param context   : context object
     * @return  : return true if device supports camera.
     */
    private static boolean isDeviceSupportCamera(Context context) {
        // this device has a camera
        // no camera on this device
        return context.getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }


    public ImagePicker getImagePicker() {
    return imagePicker;
    }

    public void setImagePicker(ImagePicker imagePicker) {
        this.imagePicker = imagePicker;
    }
}
