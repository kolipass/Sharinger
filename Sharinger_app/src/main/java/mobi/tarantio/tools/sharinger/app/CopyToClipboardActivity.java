package mobi.tarantio.tools.sharinger.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class CopyToClipboardActivity extends Activity {

    private String divider = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_share);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null && ("text/plain".equals(type))) {
            String text = new IntentHandler(divider).handleSendText(intent, false);
            if (text == null || text.length() == 0) {
                Toast.makeText(this, R.string.empty, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                copyToClipboard(text);
            }
        } else {
            Toast.makeText(this, R.string.empty, Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @SuppressLint("NewApi")
    private void copyToClipboard(String text) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("label", text);
            clipboard.setPrimaryClip(clip);
        }
    }
}
