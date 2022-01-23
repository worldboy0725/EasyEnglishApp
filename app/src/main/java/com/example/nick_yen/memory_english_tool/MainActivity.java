package com.example.nick_yen.memory_english_tool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.nick_yen.memory_english_tool.adapter.MyAdapter;
import com.example.nick_yen.memory_english_tool.databinding.ActivityMainBinding;
import com.example.nick_yen.memory_english_tool.model.Word;
import com.example.nick_yen.memory_english_tool.viewmodel.WordViewModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;
    private NavController navController;
//    private WordViewModel wordViewModel;
//
//    private MyAdapter myAdapter, cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.theme));
        navController = Navigation.findNavController(findViewById(R.id.fragment));
        if(!Extension.isLaunch)
        NavigationUI.setupActionBarWithNavController(this, navController);
//        wordViewModel = new ViewModelProvider(this,
//                new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(WordViewModel.class);
//        initView();
//        wordViewModel.getAllWordsLive().observe(this, words -> {
//            int temp = myAdapter.getItemCount(); // adapter會刷新這裡的也會刷新，所以做一個長度相同 只變更數據不刷新 (但好像有bug有時候sw1切換不會刷新另一個adapter)
//            myAdapter.setWordList(words);
//            myAdapter.notifyDataSetChanged();
////            if (temp != words.size()) {
//                cardAdapter.setWordList(words);
//                myAdapter.notifyDataSetChanged();
////            }
////            StringBuilder sb = new StringBuilder();
////            words.forEach(x -> sb.append(x.getId()).append(": ").append(x.getWord()).append(" = ").append(x.getChineseMeaning()).append("\n"));
////            binding.textView.setText(sb);
//
//        });
//
//        binding.btnInsert.setOnClickListener(this);
//        binding.btnUpdate.setOnClickListener(this);
//        binding.btnClear.setOnClickListener(this);
//        binding.btnDelete.setOnClickListener(this);
    }

//    private void initView() {
//        myAdapter = new MyAdapter(false, wordViewModel);
//        cardAdapter = new MyAdapter(true, wordViewModel);
//        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        binding.recyclerView.setAdapter(myAdapter);
//
//        binding.switch1.setOnCheckedChangeListener((compoundButton, b) -> {
//            if (b) {
//                binding.recyclerView.setAdapter(cardAdapter);
//            } else {
//                binding.recyclerView.setAdapter(myAdapter);
//            }
//        });
//    }

//    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.btnInsert:
//                String[] english = {
//                        "Hello",
//                        "World",
//                        "Android",
//                        "Google",
//                        "Studio",
//                        "Project",
//                        "Database",
//                        "Recycler",
//                        "View",
//                        "String",
//                        "Value",
//                        "Integer"
//                };
//                String[] chinese = {
//                        "你好",
//                        "世界",
//                        "安卓系統",
//                        "谷歌公司",
//                        "工作室",
//                        "項目",
//                        "數據庫",
//                        "回收",
//                        "視圖",
//                        "字串",
//                        "數值",
//                        "整數類型"
//                };
//                for (int i = 0; i < english.length; i++) {
//                    wordViewModel.insertWords(new Word(english[i], chinese[i]));
//                }
//                break;
//            case R.id.btnUpdate:
//                Word word = new Word("Hi", "你好啊!");
//                word.setId(112);
//                wordViewModel.updateWords(word);
//                break;
//            case R.id.btnClear:
//                wordViewModel.deleteAllWords();
//                break;
//            case R.id.btnDelete:
//                Word word3 = new Word("Hi", "你好啊!");
//                word3.setId(112);
//                wordViewModel.deleteWords(word3);
//                break;
//        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(R.id.fragment).getWindowToken(),0);
        navController.navigateUp();
        return super.onSupportNavigateUp();
    }
}