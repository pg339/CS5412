package cornell.cs5412;

import android.content.Context;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MiscHelpers {
    public static int pixToDp(Context context, int pix) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pix * scale + 0.5f);
    }

    public static String formatDateForNetwork(Date date) {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return formatter.format(date);
    }
}
