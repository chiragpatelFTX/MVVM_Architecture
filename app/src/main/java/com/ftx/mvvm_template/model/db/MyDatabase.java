package com.ftx.mvvm_template.model.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;

import com.ftx.mvvm_template.model.dao.AlbumDao;
import com.ftx.mvvm_template.model.dao.UserDao;
import com.ftx.mvvm_template.model.db.models.AlbumModel;
import com.ftx.mvvm_template.model.db.models.UserModel;


/**
 * Name : MyDatabase
 *<br> Purpose : MyDatabase class extends RoomDatabase will be used to create SQLite Database using Room
 * @Database Annotation will contain All entitiy class followed by comma separated .
 * Specify database version using version keyword.
 */


@Database(entities = {UserModel.class, AlbumModel.class}, version = 1 )
public abstract class   MyDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract AlbumDao albumDao();


    /**
     * Each Migration has a start and end versions and Room runs these migrations to bring the database to the latest version.
     * If a migration item is missing between current version and the latest version, Room will clear the database and recreate
     * so even if you have no changes between 2 versions, you should still provide a Migration object to the builder.
     * A migration can handle more than 1 version (e.g. if you have a faster path to choose when going version 3 to 5 without
     * going to version 4). If Room opens a database at version 3 and latest version is >= 5, Room will use the migration object
     * that can migrate from 3 to 5 instead of 3 to 4 and 4 to 5.
     */
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE Users");
        }
    };

}
