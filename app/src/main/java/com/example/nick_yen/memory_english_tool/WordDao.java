package com.example.nick_yen.memory_english_tool;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.nick_yen.memory_english_tool.model.Word;

import java.util.List;

/**
 * Description:
 *
 * @Author N!ck Yen (Billy Qin)
 * @Date 2021/12/20
 * Copyright© [2021] [N!ck Yen]. All rights reserved.
 */
@Dao
public interface WordDao {
    @Insert
    void insertWords(Word... words);

    @Update
    void updateWords(Word... words);

    @Delete
    void deleteWords(Word... words);

    @Query("DELETE FROM WORD")
    void deleteAllWords();

    @Query("SELECT * FROM WORD ORDER BY ID DESC")
//    List<Word> getAllWords();
    LiveData<List<Word>> getAllWordsLive();

    @Query("SELECT * FROM WORD WHERE english_word LIKE :pattern ORDER BY ID DESC")
    LiveData<List<Word>> findWordsWithPattern(String pattern);
}
