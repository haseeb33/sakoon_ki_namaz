package zt.sakoonkinamaz.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import zt.sakoonkinamaz.broadcast.PrayerTime;
import zt.sakoonkinamaz.database.PrayersDataSource;
import zt.sakoonkinamaz.dialog.AddNewItemDialog;
import zt.sakoonkinamaz.enums.Prayer;
import zt.sakoonkinamaz.R;
import zt.sakoonkinamaz.adaper.Adapter;
import zt.sakoonkinamaz.bean.Bean;


public class MainActivity extends Activity {
    private Button addMore;
    private ListView list;
    private ArrayList<Bean> beanArray = null;
    private Context context;
    private PrayersDataSource prayersDataSource;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setContentView(R.layout.activity_main);
            LinearLayout l = (LinearLayout) findViewById(R.id.under_21_layout);
            l.setVisibility(View.GONE);
            if (getActionBar() != null)
                getActionBar().setTitle("Prayers Timing");
        } else {
            setContentView(R.layout.activity_main);
        }
        dbHandle();
        init();
        actions();
    }

    private void dbHandle() {
        prayersDataSource = new PrayersDataSource(this);
        prayersDataSource.open();
        beanArray = prayersDataSource.getAllPrayers();
    }

    private void actions() {
        adapter = new Adapter(context, beanArray, prayersDataSource);
        list.setAdapter(adapter);
//        setListHeight();
        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddNewItemDialog.class);
                startActivityForResult(i, 0);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(position);
            }
        });
        Intent startServiceIntent = new Intent(context, PrayerTime.class);
        context.startService(startServiceIntent);
    }

    private void init() {
        context = this;
        addMore = (Button) findViewById(R.id.add_more);
        list = (ListView) findViewById(R.id.list);
        if(beanArray == null) {
            beanArray = new ArrayList<>();
            for (Prayer p : Prayer.values()) {
                Bean b = new Bean(p, 0, 0);
                b.setName(b.getPrayer(context));
                beanArray.add(b);
                prayersDataSource.createPrayer(b);
            }
        }
//        else {
//            Toast.makeText(context, "This data is from DB", Toast.LENGTH_SHORT).show();
//        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i = new Intent(MainActivity.this, AddNewItemDialog.class);
        startActivityForResult(i, 0);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        prayersDataSource.close();
        super.onDestroy();
    }

    private void showDeleteDialog(final int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(context.getResources().getString(R.string.dialog_msg_delete));
        dialog.setPositiveButton(context.getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prayersDataSource.deletePrayer(beanArray.get(position));
//                        adapter.remove(beanArray.get(position));
//                        beanArray = prayersDataSource.getAllPrayers();
                        beanArray.remove(position);
                        adapter.changeBean(beanArray);
                        adapter.notifyDataSetChanged();

                    }
                });
        dialog.setNegativeButton(context.getResources().getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            String name = data.getExtras().getString("new_name");
            long s = data.getExtras().getLong("new_start_time");
            long e = data.getExtras().getLong("new_end_time");
            Bean b = new Bean(name, s, e);
            prayersDataSource.createPrayer(b);
            beanArray.add(b);
            adapter.changeBean(beanArray);
            adapter.notifyDataSetChanged();
        }

    }
}

