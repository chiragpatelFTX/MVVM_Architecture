package com.ftx.mvvm_template.views.listeners;

public interface AsyncTaskCompleteListener<T> {
    public void onTaskComplete(T result);
}