package zt.sakoonkinamaz;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends Activity implements View.OnClickListener {
    private Button done, exit;
    private ListView list;
    private ArrayList<Bean> beanArray;
    private Adapter adapter;
    private Context context;
    private TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setContentView(R.layout.activity_main);
            t = (TextView) findViewById(R.id.title);
            t.setVisibility(View.GONE);
            if (getActionBar() != null)
                getActionBar().setTitle("Prayers Timing");
        } else {
            setContentView(R.layout.activity_main);
        }
        init();
        actions();

    }

    private void actions() {
        adapter = new Adapter(context, beanArray);
        list.setAdapter(adapter);
        done.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    private void init() {
        context = this;
        done = (Button) findViewById(R.id.done);
        exit = (Button) findViewById(R.id.exit);
        list = (ListView) findViewById(R.id.list);
        beanArray = new ArrayList<Bean>();
        for (Prayer p : Prayer.values()) {
            Bean b = new Bean(p, 0, 0);
            b.setName(b.getPrayer(context));
            beanArray.add(b);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done:

                break;

            case R.id.exit:

                break;

            default:

                break;

        }
    }
}
