package com.example.nick_yen.memory_english_tool.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.nick_yen.memory_english_tool.WordDao;
import com.example.nick_yen.memory_english_tool.WordDatabase;
import com.example.nick_yen.memory_english_tool.model.Word;

import java.util.List;

/**
 * Description:
 *
 * @Author N!ck Yen (Billy Qin)
 * @Date 2021/12/29
 * Copyright© [2021] [N!ck Yen]. All rights reserved.
 */
public class WordRepository {

    private LiveData<List<Word>> allWordsLive;
    private WordDao wordDao;

    public WordRepository(Context context) {
        WordDatabase wordDatabase = WordDatabase.getDatabase(context.getApplicationContext());
        wordDao = wordDatabase.getWordDao();
        allWordsLive = wordDao.getAllWordsLive(); // 系統自動放到副線程執行所以不用Async
    }

    public void insertWords(Word... words) {
        new InsertAsyncTask(wordDao).execute(words);
    }

    public void updateWords(Word... words) {
        new UpdateAsyncTask(wordDao).execute(words);
    }

    public void deleteWords(Word... words) {
        new DeleteAsyncTask(wordDao).execute(words);
    }

    public void deleteAllWords() {
        new DeleteAllAsyncTask(wordDao).execute();
    }

    public LiveData<List<Word>> getAllWordsLive() {
        return allWordsLive;
    }

    public LiveData<List<Word>> findWordsWithPattern(String pattern) {
        return wordDao.findWordsWithPattern("%" + pattern + "%");
    }


    static class InsertAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        public InsertAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.insertWords(words);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        public UpdateAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.updateWords(words);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        public DeleteAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.deleteWords(words);
            return null;
        }
    }

    static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private WordDao wordDao;

        public DeleteAllAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            wordDao.deleteAllWords();
            return null;
        }
    }
}
