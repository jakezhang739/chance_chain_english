package com.example.jake.chance_chain;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class CommentAdapter extends
        RecyclerView.Adapter<CommentAdapter.ViewHolder>
{

    private LayoutInflater mInflater;
    private List<commentClass> comList = new ArrayList<commentClass>();
    private Context mContext;



    public CommentAdapter(Context context, List<commentClass> cc)
    {
        this.mInflater = LayoutInflater.from(context);
        this.comList = cc;
        this.mContext = context;


    }




    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View arg0)
        {
            super(arg0);
        }

        ImageView cuImg;
        TextView cmTxt,cuidTxt,ctimeTxt;


    }

    @Override
    public int getItemCount()
    {
        return comList.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = mInflater.inflate(R.layout.comment,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.cmTxt=(TextView) view.findViewById(R.id.neiText);
        viewHolder.cuImg=(ImageView) view.findViewById(R.id.cTou);
        viewHolder.cuidTxt=(TextView) view.findViewById(R.id.cUser);
        viewHolder.ctimeTxt=(TextView) view.findViewById(R.id.cTime);


        Log.d("gallery adapter","v "+comList.get(0).cText);


        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i)
    {

        int c = comList.size()-1-i;
        if(comList.size()>0) {
            viewHolder.cmTxt.setText(comList.get(c).cText);

            viewHolder.cuidTxt.setText(comList.get(c).uId);
            AppHelper helper = new AppHelper();
            String display = helper.displayTime(comList.get(c).upTime);
            viewHolder.ctimeTxt.setText(display);

            if (!comList.get(c).uPic.isEmpty()) {
                Picasso.get().load(comList.get(c).uPic).into(viewHolder.cuImg);
            }
        }

        }



}
