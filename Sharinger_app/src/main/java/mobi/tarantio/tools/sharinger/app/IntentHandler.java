package mobi.tarantio.tools.sharinger.app;

import android.content.Intent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Intent.EXTRA_SUBJECT;
import static android.content.Intent.EXTRA_TEXT;

/**
 * Created by kolipass on 23.02.14.
 */
public class IntentHandler {

    private String divider = " ";

    public IntentHandler() {
    }

    public IntentHandler(String divider) {
        this.divider = divider;
    }

    public String handleSendText(Intent intent, boolean whithoutBrackets) {
        String sharedBodyText = intent.getStringExtra(EXTRA_TEXT);
        String sharedTitleText = intent.getStringExtra(EXTRA_SUBJECT);

        String rawText = "";
        if (sharedBodyText != null || sharedTitleText != null) {
            String title = sharedTitleText != null && sharedBodyText != null && !sharedBodyText.contains(sharedTitleText) ? sharedTitleText : null;
            String body = sharedBodyText != null ? sharedBodyText : null;
            body = whithoutBrackets ? clearBracketsCloseUrl(body) : checkBracketsCloseUrl(body);

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

    public String checkBracketsCloseUrl(String body) {

        String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(body);
        while (m.find()) {
            String urlStr = m.group();
            if (urlStr.startsWith("(") && urlStr.endsWith(")")) {

                String urlWithoutBrackets = urlStr.substring(1, urlStr.length() - 1);
                body = body.replace(urlWithoutBrackets, addDivider(urlWithoutBrackets));
            }
        }

        return body;
    }

    /**
     * Метод нужен для удаления скобок вокруг url
     *
     * @param body текст сообщения
     * @return верент измененный body
     */

    public String clearBracketsCloseUrl(String body) {

        String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(body);
        while (m.find()) {
            String urlStr = m.group();
            if (urlStr.startsWith("(") && urlStr.endsWith(")")) {

                String urlWithoutBrackets = urlStr.substring(1, urlStr.length() - 1);
                body = body.replace(urlStr, urlWithoutBrackets);
            }
        }

        return body;
    }

    private String addDivider(String body) {
        return divider + body + divider;
    }
}
