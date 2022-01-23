package com.example.nick_yen.memory_english_tool;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.nick_yen.memory_english_tool.model.Word;

/**
 * Description:
 *
 * @Author N!ck Yen (Billy Qin)
 * @Date 2021/12/20
 * Copyright© [2021] [N!ck Yen]. All rights reserved.
 */
// 若有多個Entity，則應該寫多個Dao，entities = {Word.class, Word.class} 有多個可逗號分隔
@Database(entities = {Word.class}, version = 5, exportSchema = false)  // version數據庫發生變動，會通知
public abstract class WordDatabase extends RoomDatabase {

    private static WordDatabase getInstance;

    public static synchronized WordDatabase getDatabase(Context context) {
        if (null == getInstance) {
            getInstance = Room.databaseBuilder(context.getApplicationContext(), WordDatabase.class, "word_database")
//                    .fallbackToDestructiveMigration() // 破壞式遷移會清空所有數據 相當於創建一個新結構的數據庫
                    .addMigrations(MIGRATION_4_5)
                    .build();
        }
        return getInstance;
    }

    public abstract WordDao getWordDao();

    // 若上線後需要對數據庫結構發生改變必須實作一個數據庫遷移，否則會造成舊版本數據可能會丟失數據
    static final Migration MIGRATION_2_3 = new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // sqlLite目前只有針對增加數據的遷移方式
            database.execSQL("ALTER TABLE word ADD COLUMN bar_data INTEGER NOT NULL DEFAULT 1"); // sqlLite裡沒有布爾值，要用整數據存取布爾值
        }
    };

    // 若是數據要刪除 要用創建新的表，裡面不包含刪除的，再將原有的數據庫複製過去，再將舊的數據庫刪除，新的數據庫改名字
    static final Migration MIGRATION_3_4 = new Migration(3,4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE word_temp (id INTEGER PRIMARY KEY NOT NULL, english_word TEXT," +
                    "chinese_meaning TEXT)"); // sqlLite裡的字串為TEXT不是String
            database.execSQL("INSERT INTO word_temp (id, english_word, chinese_meaning) " +
                    "SELECT id, english_word, chinese_meaning FROM word");
            database.execSQL("DROP TABLE word");
            database.execSQL("ALTER TABLE word_temp RENAME to word");
        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4,5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // sqlLite目前只有針對增加數據的遷移方式
            database.execSQL("ALTER TABLE word ADD COLUMN chinese_invisible INTEGER NOT NULL DEFAULT 0"); // 0不隱藏
        }
    };

}
