package com.example.nick_yen.memory_english_tool.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nick_yen.memory_english_tool.Extension;
import com.example.nick_yen.memory_english_tool.R;
import com.example.nick_yen.memory_english_tool.adapter.MyAdapter;
import com.example.nick_yen.memory_english_tool.databinding.FragmentWordsBinding;
import com.example.nick_yen.memory_english_tool.model.Word;
import com.example.nick_yen.memory_english_tool.viewmodel.WordViewModel;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WordsFragment extends Fragment implements View.OnClickListener {

    private FragmentWordsBinding binding;
    private WordViewModel wordViewModel;
    private MyAdapter myAdapter, cardAdapter;
    private LiveData<List<Word>> filterWords;

    private static final String VIEW_TYPE_SP = "view_type_sp";
    private static final String IS_USING_CARD_VIEW = "is_using_card_view";
    private List<Word> allWords;
    private boolean undoAction;

    public WordsFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWordsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Extension.isLaunch){
            Navigation.findNavController(getView()).navigate(R.id.action_wordsFragment_to_splashFragment);
            Extension.isLaunch=false;
        }
        wordViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(WordViewModel.class);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        myAdapter = new MyAdapter(false, wordViewModel);
        cardAdapter = new MyAdapter(true, wordViewModel);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator() { // ???????????? ???????????????callback
            @Override
            public void onAnimationFinished(@NonNull RecyclerView.ViewHolder viewHolder) {
                super.onAnimationFinished(viewHolder);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) binding.recyclerView.getLayoutManager();
                if (linearLayoutManager != null) {
                    int firstPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                    for (int i = firstPosition; i <= lastPosition; i++) {
                        MyAdapter.MyViewHolder holder = (MyAdapter.MyViewHolder) binding.recyclerView.findViewHolderForAdapterPosition(i);
                        if (holder != null) {
                            holder.txtId.setText(String.valueOf(i + 1));
                        }
                    }
                }
            }
        });
        SharedPreferences sp = requireActivity().getSharedPreferences(VIEW_TYPE_SP, Context.MODE_PRIVATE);
        boolean viewType = sp.getBoolean(IS_USING_CARD_VIEW, false);
        if (viewType) {
            binding.recyclerView.setAdapter(cardAdapter);
        } else {
            binding.recyclerView.setAdapter(myAdapter);
            binding.recyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        }

        filterWords = wordViewModel.getAllWordsLive(); // ??????????????????
        filterWords.observe(getViewLifecycleOwner(), words -> { // ?????????????????????fragment
            int temp = myAdapter.getItemCount(); // adapter???????????????????????????????????????????????????????????? ???????????????????????? (????????????bug?????????sw1???????????????????????????adapter)
//            myAdapter.setWordList(words);
//            myAdapter.notifyDataSetChanged();
//            if (temp != words.size()) {
//            cardAdapter.setWordList(words);
//            myAdapter.notifyDataSetChanged();
//            myAdapter.notifyItemInserted(0);
//            }
            allWords = words;
            if (temp < words.size() && !undoAction)
                binding.recyclerView.smoothScrollBy(0, -200);
            undoAction = false;
            myAdapter.submitList(words); // ???????????????????????????????????????????????????????????????????????????????????????
            cardAdapter.submitList(words);
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.START | ItemTouchHelper.END) { // ????????? ??????,??????
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                Word wordToDelete = filterWords.getValue().get(viewHolder.getAdapterPosition());
                Word wordToDelete = allWords.get(viewHolder.getAdapterPosition());
                wordViewModel.deleteWords(wordToDelete);
                Snackbar.make(requireActivity().findViewById(R.id.wordsFragmentView)
                        , "?????????" + wordToDelete.getWord(), Snackbar.LENGTH_SHORT).setAction("??????",
                        view -> {
                            undoAction = true;
                            wordViewModel.insertWords(wordToDelete);
                        }).show();
            }

            final Drawable icon = ContextCompat.getDrawable(requireActivity(), R.drawable.ic_delete);
            final Drawable background = new ColorDrawable(Color.LTGRAY);

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;
                int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                int iconLeft, iconRight, iconTop, iconBottom;
                int backTop, backBottom, backLeft, backRight;
                backTop = itemView.getTop();
                backBottom = itemView.getBottom();
                iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                iconBottom = iconTop + icon.getIntrinsicHeight();
                if (dX > 0) {
                    backLeft = itemView.getLeft();
                    backRight = itemView.getLeft() + (int) dX;
                    background.setBounds(backLeft, backTop, backRight, backBottom);
                    iconLeft = itemView.getLeft() + iconMargin;
                    iconRight = iconLeft + icon.getIntrinsicWidth();
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                } else if (dX < 0) {
                    backRight = itemView.getRight();
                    backLeft = itemView.getRight() + (int) dX;
                    background.setBounds(backLeft, backTop, backRight, backBottom);
                    iconRight = itemView.getRight() - iconMargin;
                    iconLeft = iconRight - icon.getIntrinsicWidth();
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                } else {
                    background.setBounds(0, 0, 0, 0);
                    icon.setBounds(0, 0, 0, 0);
                }
                background.draw(c);
                icon.draw(c);
            }
        }).attachToRecyclerView(binding.recyclerView);
        binding.floatButton.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.switchViewType) {
            SharedPreferences sp = requireActivity().getSharedPreferences(VIEW_TYPE_SP, Context.MODE_PRIVATE);
            boolean viewType = sp.getBoolean(IS_USING_CARD_VIEW, false);
            SharedPreferences.Editor editor = sp.edit();
            if (viewType) {
                binding.recyclerView.setAdapter(myAdapter);
                binding.recyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
                editor.putBoolean(IS_USING_CARD_VIEW, false);
            } else {
                binding.recyclerView.setAdapter(cardAdapter);
                binding.recyclerView.removeItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
                editor.putBoolean(IS_USING_CARD_VIEW, true);
            }
            editor.apply();
        } else if (item.getItemId() == R.id.clearData) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("??????????????????????????????");
            builder.setPositiveButton("??????", (dialogInterface, i) -> wordViewModel.deleteAllWords());
            builder.setNegativeButton("??????", (dialogInterface, i) -> {
            });
            builder.create();
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setMaxWidth(1000); // ?????????????????????????????????SearchContent
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String pattern = s.trim();
                filterWords.removeObservers(getViewLifecycleOwner()); // ?????????????????????????????????????????????????????????????????????bug
                filterWords = wordViewModel.findWordsWithPattern(pattern);
                filterWords.observe(getViewLifecycleOwner(), words -> {
                    int temp = myAdapter.getItemCount(); // adapter???????????????????????????????????????????????????????????? ???????????????????????? (????????????bug?????????sw1???????????????????????????adapter)
//                    myAdapter.notifyDataSetChanged();
//            if (temp != words.size()) {
//                    myAdapter.notifyDataSetChanged();
//            }
                    allWords = words;
                    myAdapter.submitList(words); // ???????????????????????????????????????????????????????????????????????????????????????
                    cardAdapter.submitList(words);
                });
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.floatButton) {
            Navigation.findNavController(v).navigate(R.id.action_wordsFragment_to_addFragment);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}