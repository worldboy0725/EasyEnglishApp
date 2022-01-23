package com.example.nick_yen.memory_english_tool.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.nick_yen.memory_english_tool.model.Word;
import com.example.nick_yen.memory_english_tool.repository.WordRepository;

import java.util.List;

/**
 * Description:
 *
 * @Author N!ck Yen (Billy Qin)
 * @Date 2021/12/29
 * Copyright© [2021] [N!ck Yen]. All rights reserved.
 */
// viewModel的職責為管理介面，數據有關的，須建一個repository來管理
public class WordViewModel extends AndroidViewModel { // 會採用AndroidViewModel是因為有需要用Context類

    private WordRepository wordRepository;

    public WordViewModel(@NonNull Application application) {
        super(application);
        wordRepository = new WordRepository(application);
    }

    public LiveData<List<Word>> getAllWordsLive() {
        return wordRepository.getAllWordsLive();
    }

    public LiveData<List<Word>> findWordsWithPattern(String pattern){
        return wordRepository.findWordsWithPattern(pattern);
    }

    public void insertWords(Word... words){
        wordRepository.insertWords(words);
    }

    public void updateWords(Word... words){
        wordRepository.updateWords(words);
    }

    public void deleteWords(Word... words){
        wordRepository.deleteWords(words);
    }
    public void deleteAllWords(){
        wordRepository.deleteAllWords();
    }

}
