package com.example.nick_yen.memory_english_tool.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.nick_yen.memory_english_tool.databinding.FragmentAddBinding;
import com.example.nick_yen.memory_english_tool.model.Word;
import com.example.nick_yen.memory_english_tool.viewmodel.WordViewModel;

import org.jetbrains.annotations.NotNull;

public class AddFragment extends Fragment {

    private FragmentAddBinding binding;
    WordViewModel wordViewModel;
    InputMethodManager imm;

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        wordViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(WordViewModel.class);
        binding.btnSubmit.setEnabled(false);
        binding.editTextEnglish.requestFocus(); // 先獲取焦點才能秀出鍵盤
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.editTextEnglish,0);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String english = binding.editTextEnglish.getText().toString().trim();
                String chinese = binding.editTextChinese.getText().toString().trim();
                binding.btnSubmit.setEnabled(!english.isEmpty() && !chinese.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        binding.editTextEnglish.addTextChangedListener(textWatcher);
        binding.editTextChinese.addTextChangedListener(textWatcher);
        binding.btnSubmit.setOnClickListener(view -> {
            String english = binding.editTextEnglish.getText().toString().trim();
            String chinese = binding.editTextChinese.getText().toString().trim();
            Word word = new Word(english, chinese);
            wordViewModel.insertWords(word);
            Navigation.findNavController(view).navigateUp();
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}