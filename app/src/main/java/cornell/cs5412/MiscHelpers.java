package cornell.cs5412;

import android.content.Context;

public class MiscHelpers {
    public static int pixToDp(Context context, int pix) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pix * scale + 0.5f);
    }
}
