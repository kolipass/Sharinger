package mobi.tarantio.tools.sharinger.app;

import android.content.Intent;

import static android.content.Intent.EXTRA_SUBJECT;
import static android.content.Intent.EXTRA_TEXT;

/**
 * Created by kolipass on 23.02.14.
 */
public class IntentHandler {

    private String divider;

    public IntentHandler(String divider) {
        this.divider = divider;
    }

    public String handleSendText(Intent intent) {
        String sharedBodyText = intent.getStringExtra(EXTRA_TEXT);
        String sharedTitleText = intent.getStringExtra(EXTRA_SUBJECT);

        String rawText = "";
        if (sharedBodyText != null || sharedTitleText != null) {
            String title = sharedTitleText != null && sharedBodyText != null && !sharedBodyText.contains(sharedTitleText) ? sharedTitleText : null;
            String body = sharedBodyText != null ? sharedBodyText : null;
            body = checkBracketsCloseUrl(body);

            rawText = title != null ? title : "";
            if (rawText.length() > 0) {
                rawText += divider;
            }
            rawText += body != null ? body : "";
        }
        return rawText;
    }

    /**
     * Метод нужен для добавления пробела перед закрывающей скобкой
     *
     * @param body текст сообщения
     * @return верент измененный body
     */

    private String checkBracketsCloseUrl(String body) {
        if (body != null && body.length() > 0) {
            int pos = body.indexOf(")");
            while (pos != -1) {
                body = body.substring(0, pos) + divider + body.substring(pos, body.length());

                pos = body.indexOf(")", pos + divider.length() + 1);
            }
        }
        return body;
    }
}
