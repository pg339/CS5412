package cornell.cs5412;

import android.content.Context;

/**
 * Created by Pablo on 5/6/2016.
 */
public class MiscHelpers {
    public static int pixToDp(Context context, int pix) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pix * scale + 0.5f);
    }
}
