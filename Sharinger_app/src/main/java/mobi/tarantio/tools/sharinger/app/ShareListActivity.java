package mobi.tarantio.tools.sharinger.app;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.R.layout.list_content);


        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null && ("text/plain".equals(type))) {
            body = new IntentHandler(divider).handleSendText(intent);
            subject = intent.getStringExtra(EXTRA_SUBJECT);
            setTitle(body);
            if (body == null || body.length() == 0) {
                Toast.makeText(this, R.string.empty, Toast.LENGTH_SHORT).show();
            } else {
                share();
            }
        } else {
            Toast.makeText(this, R.string.empty, Toast.LENGTH_SHORT).show();
        }
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
        ShareIntentListAdapter adapter = (ShareIntentListAdapter) l.getAdapter();
        ResolveInfo info = adapter.getItem(position);
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        if (info.activityInfo != null) {
            intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
        }
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
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
                if (label != null) {
                    TextView textView = (TextView) view.findViewById(android.R.id.text1);
                    textView.setText(label);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                String description = String.valueOf(resolveInfo.nonLocalizedLabel);
                if (description != null) {
                    TextView textView = (TextView) view.findViewById(android.R.id.text2);
                    textView.setText(description);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Drawable icon = resolveInfo.activityInfo.applicationInfo.loadIcon(context.getPackageManager());
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
