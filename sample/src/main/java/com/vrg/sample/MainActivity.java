package com.vrg.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.vrg.flipview.DoubleFlipView;
import com.vrg.flipview.Pair;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DoubleFlipView<ImageItem> container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        List<ImageItem> items = new ArrayList<>();
        items.add(new ImageItem("Elephant", R.drawable.elephant));
        items.add(new ImageItem("Tiger", R.drawable.tiger));
        items.add(new ImageItem("Panda", R.drawable.panda));
        items.add(new ImageItem("Raccoon", R.drawable.racoon));
        ImageBinder leftBinder = new ImageBinder();
        ImageBinder rightBinder = new ImageBinder();

        List<Pair<ImageItem>> pairs = new ArrayList<>();
        pairs.add(new ImagePair(items.get(0), items.get(0)));
        pairs.add(new ImagePair(items.get(1), items.get(1)));
        pairs.add(new ImagePair(items.get(2), items.get(2)));
        pairs.add(new ImagePair(items.get(3), items.get(3)));

        container = findViewById(R.id.item);
        container.setLeftBinder(leftBinder);
        container.setRightBinder(rightBinder);
        container.setItems(pairs);
        container.setItemListener(new DoubleFlipView.OnItemClickListener<ImageItem>() {
            @Override
            public void onItemClick(int position, boolean left, ImageItem item) {

            }
        });
        container.notifyDatasetChanged();
    }
}

