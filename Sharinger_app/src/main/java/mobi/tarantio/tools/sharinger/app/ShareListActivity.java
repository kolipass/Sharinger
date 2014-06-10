package mobi.tarantio.tools.sharinger.app;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.EXTRA_SUBJECT;

public class ShareListActivity extends ListActivity {
    private String divider = " ";
    private String subject;
    private String body;
    private ListHeader listHeader;

    private class ListHeader {
        TextView editText;
        LinearLayout linearLayout;
        String text = null;

        private ListHeader() {
            linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.list_header, null);
            if (linearLayout != null) {
                editText = (TextView) linearLayout.findViewById(R.id.editText);
            }
        }

        public View getView(String text) {
            this.text = text;
            editText.setText(text);
            return linearLayout;
        }

        public String getText() {
            if (editText != null) {
                return String.valueOf(editText.getText());
            } else return text;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_content);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(getResources().getColor(R.color.statusbar_bg));


        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();


        if (Intent.ACTION_SEND.equals(action) && type != null && ("text/plain".equals(type))) {
            receiveExtraIntent(intent);
        } else if (Intent.ACTION_CHOOSER.equals(action)) {
            Parcelable pa = intent.getParcelableExtra(Intent.EXTRA_INTENT);
            if (pa != null) {
                if (!(pa instanceof Intent)) {
                    Log.w("ChooseActivity", "Initial intent #"
                            + " not an Intent: " + pa);
                    finish();
                    return;
                }
                receiveExtraIntent((Intent) pa);
            } else {
                Toast.makeText(this, R.string.empty, Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            //TEST
            body = "Куча гифов http://developerslife.ru/";
            setListTitle(body);
            share();
//            Toast.makeText(this, R.string.empty, Toast.LENGTH_SHORT).show();
        }
    }

    private void receiveExtraIntent(Intent intent) {
        body = new IntentHandler(divider).handleSendText(intent);
        subject = intent.getStringExtra(EXTRA_SUBJECT);
        setListTitle(body);
        if (body == null || body.length() == 0) {
            Toast.makeText(this, R.string.empty, Toast.LENGTH_SHORT).show();
        } else {
            share();
        }
    }

    private void setListTitle(String body) {
        listHeader = new ListHeader();
        getListView().addHeaderView(listHeader.getView(body), null, true);
    }

    public void share() {
        Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        try {
            List<ResolveInfo> activities = this.getPackageManager().queryIntentActivities(sendIntent, 0);
            if (activities != null && !activities.isEmpty()) {

                ShareIntentListAdapter adapter = new ShareIntentListAdapter(this, R.layout.list_item
                        , removeCurrentPackage(activities));
                setListAdapter(adapter);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        if (position == 0 && listHeader != null) {
            Intent intent = new Intent(ShareListActivity.this, TextViewActivity.class);
            intent.putExtra(TextViewActivity.TEXT, listHeader.getText());
            startActivity(intent);
            return;
        }

        ShareIntentListAdapter adapter = (ShareIntentListAdapter) getListAdapter();
        ResolveInfo info = adapter.getItem((int) id);
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        if (info.activityInfo != null) {
            intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
        }
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, listHeader != null ? listHeader.getText() : body);
        startActivity(intent);
        finish();
    }

    private List<ResolveInfo> removeCurrentPackage(List<ResolveInfo> activities) {
        List<ResolveInfo> resolveInfos = new ArrayList<ResolveInfo>();
        for (ResolveInfo info : activities) {
            if (info.activityInfo != null && !info.activityInfo.packageName.contains("sharinger")) {
                resolveInfos.add(info);
            }
        }
        return resolveInfos;
    }

    public class ShareIntentListAdapter extends BaseAdapter {
        private Context context;
        private List<ResolveInfo> items;
        private int layoutId;
        private LayoutInflater inflater;

        public ShareIntentListAdapter(Context context, int layoutId, List<ResolveInfo> items) {
            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.items = items;
            this.layoutId = layoutId;
        }

        @Override
        public int getCount() {
            return items != null && !items.isEmpty() ? items.size() : 0;
        }

        @Override
        public ResolveInfo getItem(int position) {
            return items != null && !items.isEmpty() ? items.get(position) : null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = inflater.inflate(layoutId, null);
            }
            ResolveInfo resolveInfo = getItem(position);

            try {
                String label = resolveInfo.loadLabel(context.getPackageManager()).toString();
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                if (label != null && textView != null) {
                    textView.setText(label);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                String description = String.valueOf(resolveInfo.nonLocalizedLabel);
                TextView textView = (TextView) view.findViewById(android.R.id.text2);
                if (description != null && textView != null) {
                    textView.setText(description);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Drawable icon = resolveInfo.activityInfo.loadIcon(context.getPackageManager());
                if (icon != null) {
                    ImageView image = (ImageView) view.findViewById(android.R.id.icon);
                    image.setImageDrawable(icon);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return view;
        }
    }

}
