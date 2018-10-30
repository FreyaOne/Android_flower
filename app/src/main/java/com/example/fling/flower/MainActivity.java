package com.example.fling.flower;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DialogTitle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final int SHOW_RESPONSE = 0;
    private RecyclerView recyclerView;
    private DemoAdapter adapter;
    private ImageView ivImage;
    private ArrayList<String> arrayList = new ArrayList<>();

//    @RequiresApi(api = AlertDialog.Builder.VERSION_CODES.LOLLIPOP)

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter = new DemoAdapter());
        MyThread thread = new MyThread();
        thread.start();
        try{
            thread.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        final ArrayList<String> list = new ArrayList<>();
        for(String url :thread.getArrayList()){
            list.add(url);
        }
        adapter.replaceAll(list);

        adapter.onItemClickListener = new DemoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {    //瀑布流图片点击事件
                Bundle bundle = new Bundle();
                bundle.putString("url",list.get(position));
                Intent intent = new Intent(MainActivity.this,showBigImage.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        };
    }


    class MyThread extends Thread{

            public void run() {     //多线程获取url添加到数组
                String url;
                ArrayList<String> list;
                for(int i =1;i<=10;i++){
                    url = "http://10.0.2.2/sc_api.php?page=" + i;
//                    Log.e("url",url);
                    String response = NetConnection.get(url);
                    list = parseJSONWithJSONObject(response);
                    for(String u : list){
                        if(u != null){
                            arrayList.add(u);
                        }else{
                            Log.e("error","添加错误");
                        }
//                        Log.e("arrayList",arrayList.toString());
                    }
                }
//                url = "http://10.0.2.2/sc_api.json";
//                String response = NetConnection.get(url);
//                list=parseJSONWithJSONObject(response);
//                for(String u : list){
//                    if(u != null){
//                        arrayList.add(u);
//                        Log.e("arrayList",arrayList.toString());
//                    }else{
//                        Log.e("error","添加错误");
//                    }
//                }
            }

            public ArrayList<String> getArrayList(){
                return arrayList;
            }
    }

    private ArrayList<String> parseJSONWithJSONObject(String jsonData){    //转换返回的Json数据为数组类型
        ArrayList<String> arrayList = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray data = jsonObject.getJSONArray("data");
            for(int i=0;i<data.length();i++){
                JSONObject object = data.getJSONObject(i);
//                String url = object.getString("url");
                String image = object.getString("image");
                arrayList.add(image);
//                String name = object.getString("name");
//                Log.e("MainActivity",url);
//                Log.e("MainActivity",image);
//                Log.e("MainActivity",name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;
    }
}