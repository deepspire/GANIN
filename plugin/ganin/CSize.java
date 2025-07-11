package plugin.ganin;

import android.graphics.Insets;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.WindowInsets;
import android.view.WindowMetrics;
import com.ansca.corona.CoronaEnvironment;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;

//
public class CSize implements NamedJavaFunction {
    public String getName() {
        return "getSize";
    }

    public int invoke(LuaState luaState) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        if (Build.VERSION.SDK_INT >= 30) {
            WindowMetrics currentWindowMetrics = CoronaEnvironment.getCoronaActivity().getWindowManager().getCurrentWindowMetrics();
            Insets insetsIgnoringVisibility = currentWindowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars() | WindowInsets.Type.displayCutout());
            i6 = insetsIgnoringVisibility.right;
            i7 = insetsIgnoringVisibility.left;
            i8 = insetsIgnoringVisibility.top;
            i4 = insetsIgnoringVisibility.bottom;
            Rect bounds = currentWindowMetrics.getBounds();
            Size size = new Size(bounds.width(), bounds.height());
            i5 = size.getWidth();
            i3 = size.getHeight();
        } else {
            int identifier = CoronaEnvironment.getCoronaActivity().getResources().getIdentifier("status_bar_height", "dimen", "android");
            int dimensionPixelSize = identifier > 0 ? CoronaEnvironment.getCoronaActivity().getResources().getDimensionPixelSize(identifier) : 0;
            int identifier2 = CoronaEnvironment.getCoronaActivity().getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            if (identifier2 > 0) {
                i2 = CoronaEnvironment.getCoronaActivity().getResources().getDimensionPixelSize(identifier2);
                i = i2 - dimensionPixelSize;
            } else {
                i = 0;
                i2 = 0;
            }
            DisplayMetrics displayMetrics = new DisplayMetrics();
            CoronaEnvironment.getCoronaActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int i9 = displayMetrics.widthPixels;
            i3 = i2 + displayMetrics.heightPixels;
            i4 = i;
            i5 = i9;
            i6 = 0;
            i7 = 0;
            i8 = 0;
        }
        luaState.pushInteger(i5);
        luaState.pushInteger(i3);
        luaState.pushInteger(i6);
        luaState.pushInteger(i7);
        luaState.pushInteger(i8);
        luaState.pushInteger(i4);
        return 6;
    }
}
