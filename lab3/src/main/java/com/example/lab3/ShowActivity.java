package com.example.lab3;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ShowActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        ListView lv1=findViewById(R.id.lv1);
        BmobQuery<PhoneMsg> query=new BmobQuery<>();
        query.findObjects(new FindListener<PhoneMsg>() {
            @Override
            public void done(List<PhoneMsg> list, BmobException e) {
                int  a=list.size();
                //String data[]=new String[a];
                List<String> ls1=new ArrayList<>();
                for(int i=0;i<a;i++)
                {
                   // data[i]=list.get(i).toString();
                    ls1.add(list.get(i).toString());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        ShowActivity.this,
                        android.R.layout.simple_list_item_single_choice,
                        ls1 );//
                lv1.setAdapter(adapter);
            }
        });
    }
}
