package com.sql.projecttemplate.utils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * To filter the list of child by given filter extension in file.
 */
public class FileFilterByExtension implements FilenameFilter {
    /**
     * Refer to the extension passed in constuctor
     */
    private String mExtension;
    private boolean misDirectory = false;

    public FileFilterByExtension(String aExtension) {
        mExtension = aExtension;
    }

    public FileFilterByExtension(boolean isDirectory) {
        misDirectory = isDirectory;
    }

    public void setExtension(String aExtension) {
        mExtension = aExtension;
    }

    @Override
    public boolean accept(File aDir, String aFilename) {
        if (!misDirectory)
            return aFilename.endsWith(mExtension);
        else
            return new File(aDir, aFilename).isDirectory();
    }

}
