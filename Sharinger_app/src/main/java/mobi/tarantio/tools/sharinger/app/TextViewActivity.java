package mobi.tarantio.tools.sharinger.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * ПОказывает на весь экран текст
 */
public class TextViewActivity  extends Activity {
    public static final String TEXT = "text";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_text_dialog);

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(getResources().getColor(R.color.statusbar_bg));


        Intent intent = getIntent();
        String text = intent.getStringExtra(TEXT);

        if (text != null) {
            ((TextView) findViewById(R.id.textView)).setText(text);
        }
    }
}
