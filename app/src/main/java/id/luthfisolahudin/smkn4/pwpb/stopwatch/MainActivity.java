package id.luthfisolahudin.smkn4.pwpb.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView textTime;

    Button buttonStart;
    Button buttonPause;
    Button buttonReset;
    Button buttonSave;

    ListView listSavedTime;

    List<String> listSavedTimeItems;

    Handler handler;

    ArrayAdapter<String> adapter;

    long startTime = 0L;
    long millisecondTime = 0L;
    long updateTime = 0L;
    long bufferTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textTime = findViewById(R.id.text_time);

        buttonStart = findViewById(R.id.button_start);
        buttonStart.setOnClickListener(view -> {
            startTime = SystemClock.uptimeMillis();
            handler.postDelayed(runnable, 0);
            buttonStart.setEnabled(false);
            buttonPause.setEnabled(true);
            buttonReset.setEnabled(false);
            buttonSave.setEnabled(true);
        });

        buttonPause = findViewById(R.id.button_pause);
        buttonPause.setOnClickListener(view -> {
            bufferTime += millisecondTime;
            handler.removeCallbacks(runnable);
            buttonStart.setEnabled(true);
            buttonPause.setEnabled(false);
            buttonReset.setEnabled(true);
        });

        buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(view -> {
            buttonPause.setEnabled(false);
            buttonReset.setEnabled(false);
            buttonSave.setEnabled(false);

            startTime = 0L;
            millisecondTime = 0L;
            bufferTime = 0L;
            updateTime = 0L;

            textTime.setText(R.string.initial_time);

            listSavedTimeItems.clear();

            adapter.notifyDataSetChanged();
        });

        buttonSave = findViewById(R.id.button_save);
        buttonSave.setOnClickListener(view -> {
            listSavedTimeItems.add(textTime.getText().toString());
            adapter.notifyDataSetChanged();
        });

        listSavedTime = findViewById(R.id.list_saved_time);

        listSavedTimeItems = new ArrayList<>();

        handler = new Handler();

        adapter = new ArrayAdapter<>(MainActivity.this, R.layout.simple_list_item_1, listSavedTimeItems);

        listSavedTime.setAdapter(adapter);
    }

    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            millisecondTime = SystemClock.uptimeMillis() - startTime;
            updateTime = bufferTime + millisecondTime;

            int second = (int) (updateTime / 1000);
            int minute = second / 60;
            second = second % 60;
            int millisecond = (int) (updateTime % 1000);

            String formatTime = getResources().getString(R.string.format_time);

            textTime.setText(String.format(formatTime, minute, second, millisecond));

            handler.postDelayed(this, 0);
        }
    };
}