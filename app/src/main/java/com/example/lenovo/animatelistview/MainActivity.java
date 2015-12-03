package com.example.lenovo.animatelistview;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.LineNumberInputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    ArrayList<String> itemList;
    LayoutInflater inflater;
    ListView listView;
    MyHandler handler;
    MyAdapter adapter;
    AddRunnable addRunnable;

    int j=100000;

    int i=0;

    Timer timer;
    boolean timerStarted;

    long disappearDuration=9000;
    long addItemTime=System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        inflater=LayoutInflater.from(MainActivity.this);
        itemList=new ArrayList<>();
        for (int i=0;i<10;i++){
            itemList.add("item "+i);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                listView.setAlpha(0.5f);
            }
        });




        listView = (ListView) findViewById(R.id.listview);
        adapter=new MyAdapter();
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:

                        listView.setAlpha(1);

                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;

            }
        });

      /*  AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50);
        set.addAnimation(animation);

        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f,Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(100);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        listView.setLayoutAnimation(controller);*/


      /*  AlphaAnimation alphaAnimation=new AlphaAnimation(1,0);
        alphaAnimation.setDuration(5000);
        LayoutAnimationController layoutAnimationController=new LayoutAnimationController(new AlphaAnimation(1,0),2000);
        layoutAnimationController.setInterpolator(new DecelerateInterpolator());*/

        //   listView.setLayoutAnimation(layoutAnimationController);
        // TranslateAnimation f;

        listView.setAdapter(adapter);

        final long firstPos=listView.getFirstVisiblePosition();

        listView.post(new Runnable() {
            @Override
            public void run() {
                long lastVisiblePos = listView.getLastVisiblePosition();
                Log.v("TAG", "---first pos is " + firstPos + " last visible pos is " + lastVisiblePos);
            }
        });





        MyRunnable myRunnable=new MyRunnable();
        handler=new MyHandler();
        addRunnable=new AddRunnable();
        handler.postDelayed(addRunnable,3000);
        handler.postDelayed(myRunnable,4000);

    }

    public void startTimer(){

        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long nowTime=System.currentTimeMillis();
                long dis=nowTime-addItemTime;
                float alpha= (float) (dis*100.0/(disappearDuration*100.0));

                Log.v("TAG", "----dis is " + dis + "alpha is  " + alpha);
                long firstVisiblePos=listView.getFirstVisiblePosition();
                long lastVisiblePos=listView.getLastVisiblePosition();

                long visibleCount=lastVisiblePos-firstVisiblePos+1;
                for (int i= (int) firstVisiblePos;i<=lastVisiblePos;i++){
                    int percent= (int) (firstVisiblePos/(visibleCount));
                    View view=listView.getChildAt((int) firstVisiblePos);
                    float newAlpha=(float)0.5*(percent+alpha);
                    Log.v("TAG","  ===  ALPHA IS "+newAlpha);
                    if (newAlpha<0.2){
                        newAlpha= (float) 0.2;
                    }
                    if (view==null){
                        break;
                    }
                    view.setAlpha(newAlpha);

                }

                View view=listView.getChildAt((int) firstVisiblePos);
                Log.v("TAG","----firstvisible view---view is "+ view +"first visible pos is "+firstVisiblePos);

                listView.getItemAtPosition((int) firstVisiblePos);



              //  final float finalAlpha = alpha;
               /* runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAlpha(1- finalAlpha);
                    }
                });*/

            }
        },300,300);

    }

    class AddRunnable implements Runnable{

        @Override
        public void run() {
            j--;
            itemList.add("item" + j);
            addItemTime=System.currentTimeMillis();

           // listView.setAlpha(1);


            adapter.notifyDataSetChanged();
            listView.setSelection(adapter.getCount());
            listView.getChildAt(listView.getLastVisiblePosition()).setAlpha(1);
            if (!timerStarted){
                startTimer();
            }
            if (j==0){
                return;
            }
            handler.postDelayed(this,9000);

        }
    }

    class MyRunnable implements Runnable {


        @Override
        public void run() {
            itemList.remove(0);
            adapter.notifyDataSetChanged();


            i++;
            if (i>=10){
                return;
            }
            handler.postDelayed(this,10000);
        }
    }

    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView==null){
                convertView=inflater.inflate(R.layout.item,null);
                viewHolder=new ViewHolder(convertView);
                convertView.setTag(viewHolder);

            }else{
                viewHolder= (ViewHolder) convertView.getTag();
            }
            viewHolder.textView.setText(itemList.get(position));
            return convertView;
        }

        class ViewHolder {
            TextView textView;
            View view;
            public ViewHolder(View view){
                this.view=view;
                textView= (TextView) view.findViewById(R.id.item);

            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
