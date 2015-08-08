package com.star.madlibs;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MakeupActivity extends AppCompatActivity {

    private TextView mWordsLeftTextView;
    private EditText mEditText;
    private Button mOKButton;
    private TextView mIntroductionTextView;

    private TextToSpeech mTextToSpeech;
    private boolean mIsInited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_makeup);

        mWordsLeftTextView = (TextView) findViewById(R.id.words_left);
        mEditText = (EditText) findViewById(R.id.edit_text);
        mOKButton = (Button) findViewById(R.id.ok);
        mIntroductionTextView = (TextView) findViewById(R.id.introduction);

        Intent intent = getIntent();

        int position = intent.getIntExtra("StoryId", 0);

        final Story story = new Story(this, position);

        final int[] wordsLeft = {story.getCount()};

        mWordsLeftTextView.setText(wordsLeft[0] + " word(s) left");

        String wordToReplace = story.findNextWordToReplace();

        mEditText.setHint(wordToReplace);

        final String prompt = "Please type a/an " + wordToReplace;

        mIntroductionTextView.setText(prompt);

        mTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                mIsInited = true;
                mTextToSpeech.speak(prompt, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        mOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String word = mEditText.getText().toString();

                if (story.isInDictionary(word)) {

                    story.replaceWord(word);

                    wordsLeft[0]--;

                    if (wordsLeft[0] == 0) {
                        Intent intent = new Intent(MakeupActivity.this, StoryActivity.class);
                        intent.putExtra("story", story.getStory());

                        startActivity(intent);
                    } else {
                        mWordsLeftTextView.setText(wordsLeft[0] + " word(s) left");

                        String wordToReplace = story.findNextWordToReplace();

                        mEditText.setText("");
                        mEditText.setHint(wordToReplace);

                        String prompt = "Please type a/an " + wordToReplace;

                        mIntroductionTextView.setText(prompt);

                        if (mIsInited) {
                            mTextToSpeech.speak(prompt, TextToSpeech.QUEUE_FLUSH, null);
                        }

                        Toast.makeText(MakeupActivity.this, "Great! Keep going!",
                                Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(MakeupActivity.this, "Not in dictionary.",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    protected void onDestroy() {

        if (mTextToSpeech != null) {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
        }

        super.onDestroy();
    }
}
