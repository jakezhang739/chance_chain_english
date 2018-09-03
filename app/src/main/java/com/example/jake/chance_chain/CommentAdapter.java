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
            String display = displayTime(comList.get(c).upTime);
            viewHolder.ctimeTxt.setText(display);

            if (!comList.get(c).uPic.isEmpty()) {
                Picasso.get().load(comList.get(c).uPic).into(viewHolder.cuImg);
            }
        }

        }







    public String displayTime(String thatTime){
        Date currentTime = Calendar.getInstance().getTime();
        String dateString = DateFormat.format("yyyyMMddHHmmss", new Date(currentTime.getTime())).toString();
        int hr1,hr2,min1,min2;
        String sameday1,sameday2;
        sameday1=thatTime.substring(0,8);
        sameday2=dateString.substring(0,8);
        hr1=Integer.parseInt(thatTime.substring(8,10));
        hr2=Integer.parseInt(dateString.substring(8,10));
        min1=Integer.parseInt(thatTime.substring(10,12));
        min2=Integer.parseInt(dateString.substring(10,12));
        if(!sameday1.equals(sameday2)){
            Log.d("same ",sameday1 + " " + sameday2);
            return sameday1.substring(0,4)+"年"+sameday1.substring(4,6)+'月'+sameday1.substring(6,8)+"号";
        }
        else if(hr1!=hr2){
            Log.d("hr ",hr1+" "+hr2);
            return String.valueOf(hr2-hr1)+"小时前";
        }
        else if(min1!=min2){
            Log.d("min ", min1+" "+min2);
            return String.valueOf(min2-min1)+"分钟前";
        }
        else{
            return "刚刚";
        }



    }


}
