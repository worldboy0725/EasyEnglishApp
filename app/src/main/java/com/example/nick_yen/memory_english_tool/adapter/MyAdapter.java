package com.example.nick_yen.memory_english_tool.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nick_yen.memory_english_tool.R;
import com.example.nick_yen.memory_english_tool.model.Word;
import com.example.nick_yen.memory_english_tool.viewmodel.WordViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @Author N!ck Yen (Billy Qin)
 * @Date 2022/1/3
 * Copyright© [2022] [N!ck Yen]. All rights reserved.
 */
public class MyAdapter extends ListAdapter<Word, MyAdapter.MyViewHolder> {

//    private List<Word> wordList = new ArrayList<>(); // 防止空指針 new一下
    private boolean useCardView;
    private WordViewModel wordViewModel;

    public MyAdapter(boolean useCardView, WordViewModel wordViewModel) {
        super(new DiffUtil.ItemCallback<Word>() {
            @Override
            public boolean areItemsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
                return (oldItem.getWord().equals(newItem.getWord())
                        && oldItem.getChineseMeaning().equals(newItem.getChineseMeaning())
                        && oldItem.isChineseInvisible() == newItem.isChineseInvisible());
            }
        });
        this.useCardView = useCardView;
        this.wordViewModel = wordViewModel;
    }

//    public void setWordList(List<Word> wordList) {
//        this.wordList = wordList;
//    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (useCardView) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_2, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_normal_2, parent, false);
        }
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.txtId.setText(String.valueOf(i + 1));
        holder.txtEnglish.setText(String.valueOf(getItem(i).getWord()));
        holder.txtChinese.setText(String.valueOf(getItem(i).getChineseMeaning()));
        holder.aSwitch.setOnCheckedChangeListener(null); // 因為recycler是回收視圖 如果這行不加 有可能點擊的地方被回收 其他選項狀態顯示錯誤
        if (getItem(i).isChineseInvisible()) {
            holder.txtChinese.setVisibility(View.GONE);
            holder.aSwitch.setChecked(true);
        } else {
            holder.txtChinese.setVisibility(View.VISIBLE);
            holder.aSwitch.setChecked(false);
        }
        holder.itemView.setOnClickListener(view -> holder.itemView.getContext().startActivity(new Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse("https://translate.google.com.tw/?hl=zh-TW&sl=en&tl=zh-TW&text="
                        + holder.txtEnglish.getText()))));
        holder.aSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                holder.txtChinese.setVisibility(View.GONE);
                getItem(i).setChineseInvisible(true);
            } else {
                holder.txtChinese.setVisibility(View.VISIBLE);
                getItem(i).setChineseInvisible(false);
            }
            wordViewModel.updateWords(getItem(i));
        });
    }

//    @Override
//    public int getItemCount() {
//        return wordList.size();
//    }


    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        super.onViewAttachedToWindow(holder); // 當viewHolder出現在螢幕上再設置一下序號
        holder.txtId.setText(String.valueOf(holder.getAdapterPosition()+1));
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder { // 內部類通常加static 防止內存泄露
        public TextView txtId, txtEnglish, txtChinese;
        public Switch aSwitch;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = itemView.findViewById(R.id.txtId);
            txtEnglish = itemView.findViewById(R.id.txtEnglish);
            txtChinese = itemView.findViewById(R.id.txtChinese);
            aSwitch = itemView.findViewById(R.id.switch2);
        }
    }
}
