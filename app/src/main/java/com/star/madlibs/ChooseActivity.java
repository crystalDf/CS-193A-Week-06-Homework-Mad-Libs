package com.star.madlibs;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChooseActivity extends AppCompatActivity {

    private ListView mListView;

    private ArrayAdapter<String> mArrayAdapter;

    private String[] mStories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_choose);

        mListView = (ListView) findViewById(R.id.list_view);

        mStories = getResources().getStringArray(R.array.stories);

        mArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mStories);

        mListView.setAdapter(mArrayAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ChooseActivity.this, MakeupActivity.class);
                intent.putExtra("StoryId", position);
                startActivity(intent);
                finish();
            }
        });
    }
}
