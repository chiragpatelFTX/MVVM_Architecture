package com.sql.projecttemplate.utils;

/**
 * @Author : CMPatel
 * @Created_On : 10/6/2017
 * @Purpose : Constants are delclared here.
 */
public class Constants {
    public static final String BASE_URL = "http://reqres.in/api/";
    public static final java.lang.String DATABASE_NAME = "template_db";

    public interface IntentKey {
        String KEY_USERID = "UserId";
        String KEY_ALBUMID = "AlbumId";
    }

    public static class ApiCodes {
        public static final int SUCCESS = 1;
        public static final int ERROR = 0;

        public static final String ERROR_KEY = "code";
        public static final String ERROR_MESSAGE_KEY = "error";
    }

    public interface Database {
        int INSERT_USERLIST = 1;
        int INSERT_ALBUMLIST = 2;
        int GET_ALBUM = 3;
        int GET_USER = 4;
        int UPDATE_ALBUM = 5;
        int DELETE_ALBUM = 6;
        int UPDATE_USER = 7;
        int DELETE_USER = 8;
    }

    public static class ApiMethods {
        public static final String REGISTER_URL = "register";
        public static final String LOGIN_URL = "login";
        public static final String USER_LIST_URL = "users";
        public static final String ALBUM_LIST_URL = "https://jsonplaceholder.typicode.com/albums";
    }
}
