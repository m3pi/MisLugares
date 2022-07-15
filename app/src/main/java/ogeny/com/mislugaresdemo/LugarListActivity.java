package ogeny.com.mislugaresdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ogeny.com.mislugaresdemo.adapters.LugarAdapter;

public class LugarListActivity extends AppCompatActivity {
    private RecyclerView revLugaresList;
    public LugarAdapter lugarAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lugar_list);

        revLugaresList = (RecyclerView) findViewById(R.id.rev_lugares_list);
        //lugarAdapter = new LugarAdapter(this, ScrollingActivity.iLugar);
        lugarAdapter = new LugarAdapter(ScrollingActivity.iLugar);
        revLugaresList.setAdapter(lugarAdapter);

        layoutManager = new LinearLayoutManager(this);
        revLugaresList.setLayoutManager(layoutManager);

        // para el vento click
        lugarAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LugarListActivity.this, LugarInfoActivity.class);
                intent.putExtra("id", (long) revLugaresList.getChildAdapterPosition(view));
                startActivity(intent);
            }
        });
    }

}