package com.example.android.pick;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import static android.R.attr.mode;

public class MainActivity extends AppCompatActivity implements TimePickerFragment.CallBacks,
        DatePickerFragment.CallBacks,
        PopupMenu.OnMenuItemClickListener{

    private static final String TAG = "MainActivity";
    private android.view.ActionMode mActionMode;

    private Button mTimeBtn;
    private Button mDateBtn;

    private ListView listView;

    private String[] fruits = {
            "Apple",
            "Banana",
            "Cherry",
            "Orange",
            "Mango",
            "Watermelon"
    };

    private String selectedWord = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        mTimeBtn = (Button)findViewById(R.id.time_pick);
        mDateBtn = (Button)findViewById(R.id.date_pick);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, fruits);
        listView.setAdapter(adapter);

        mDateBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (mActionMode != null) {
                    return false;
                }
                mActionMode = startActionMode(mActionModeCallback);
                view.setSelected(true);
                return true;
            }
        });
        //
        //registerForContextMenu(listView);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
//        menu.setHeaderTitle("ContextMenu");
//        menu.add("Press Me to see Item's name");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        selectedWord = ((TextView) info.targetView).getText().toString();

        Toast.makeText(this,"You have pressed " + selectedWord  ,Toast.LENGTH_LONG).show();
        return true;
    }

    public void showTimePickDialog(View view) {

        TimePickerFragment newFragment = new TimePickerFragment();

        newFragment.setTimeDialogFragmentListener(this);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View view) {

        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setDateDialogFragmentListener(this);

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void setTime(Calendar date) {


        Date date1 = date.getTime();

        int hour24 = date.get(Calendar.HOUR_OF_DAY);   // HOUR-12小时制
        //int hour12 = date.get(Calendar.HOUR);
        // 当前分
        int minute = date.get(Calendar.MINUTE);   // 当前秒
        //int second = date.get(Calendar.SECOND);

        mTimeBtn.setText(hour24+":"+minute);


    }

    @Override
    public void setDate(Calendar date) {

        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH) + 1;
        int day = date.get(Calendar.DAY_OF_MONTH);
        mDateBtn.setText(year+":"+month+":"+day);

    }


    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {

                case R.id.menu_save:
                    Toast.makeText(getApplicationContext(), "You have pressed " + selectedWord  ,Toast.LENGTH_LONG).show();
                    mode.finish();
                    return true;

                case R.id.menu_archive:
                    Toast.makeText(getApplicationContext(), "You have pressed1 " + selectedWord  ,Toast.LENGTH_LONG).show();
                    mode.finish();
                    return true;

                default:
                    return false;

            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };

    //popup

    public void showPopup(View view) {

        PopupMenu popupMenu = new PopupMenu(this, view);

        popupMenu.setOnMenuItemClickListener(this);

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.context_menu, popupMenu.getMenu());
        popupMenu.show();



    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_save:
                Log.i(TAG, "111");

                return true;

            case R.id.menu_archive:
                Toast.makeText(MainActivity.this, "You have pressed1 " + selectedWord  ,Toast.LENGTH_LONG).show();

                return true;

            default:
                return false;

        }
    }

    public void openSettings(View view) {

        startActivity(new Intent(this, SettingsActivity.class));
    }

    /**
     * 通知
     * @param view
     */
    public void showNotification(View view) {

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_profile)
                .setContentTitle("My notification")
                .setContentText("Hello World!");


        /**
         *
         *
         * 根据可启动 Activity 的 Intent 创建返回栈：
         创建 Intent 以启动 Activity。
         通过调用 TaskStackBuilder.create() 创建堆栈生成器。
         通过调用 addParentStack() 将返回栈添加到堆栈生成器。 对于在清单文件中所定义层次结构内的每个 Activity，返回栈均包含可启动 Activity 的 Intent 对象。此方法还会添加一些可在全新任务中启动堆栈的标志。
         注：尽管 addParentStack() 的参数是对已启动 Activity 的引用，但是方法调用不会添加可启动 Activity 的 Intent，而是留待下一步进行处理。

         通过调用 addNextIntent()，添加可从通知中启动 Activity 的 Intent。 将在第一步中创建的 Intent 作为 addNextIntent() 的参数传递。
         如需，请通过调用 TaskStackBuilder.editIntentAt() 向堆栈中的 Intent 对象添加参数。有时，需要确保目标 Activity 在用户使用“返回”导航回它时会显示有意义的数据。
         通过调用 getPendingIntent() 获得此返回栈的 PendingIntent。 然后，您可以使用此 PendingIntent 作为 setContentIntent() 的参数。
         */
        Intent resultIntent = new Intent(this, ResultActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(ResultActivity.class);

        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(101, mBuilder.build());



    }



























    //popup点击事件
}
