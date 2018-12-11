package com.example.ashleyhwang.groceryfy.Recipe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ashleyhwang.groceryfy.DataModel.Recipe;
import com.example.ashleyhwang.groceryfy.R;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>  {

    private List<Recipe> stList;
    private String teemp2;
    private Context mContext; //need an activity context


    public RecipeAdapter(List<Recipe> stList){this.stList=stList;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvName;

        public ViewHolder(View itemLayoutView){
            super(itemLayoutView);

            tvName = (TextView) itemLayoutView.findViewById(R.id.newREcipe);
        }

    }

    @NonNull
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_row, null);

        //create ViewHolder;

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        mContext = parent.getContext(); //need this for later when you need another activity
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final int pos = i;
        viewHolder.tvName.setText(stList.get(pos).getDishname()); //displaying dish name;

        //        holder.tvName.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                int me = dgList.get(pos).getId();
//
//                Dog toSend = dgList.get(pos);
//                Intent i = new Intent(mContext, dogImage.class);
//                i.putExtra("dogObject", toSend);
//                ((Activity) mContext).startActivity(i);
//
//            }
//        });

    }



    @Override
    public int getItemCount() {
        return stList.size();}

}
