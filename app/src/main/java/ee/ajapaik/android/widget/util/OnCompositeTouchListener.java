package ee.ajapaik.android.widget.util;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class OnCompositeTouchListener implements View.OnTouchListener {
    private List<View.OnTouchListener> m_listeners = new ArrayList<>();

    public OnCompositeTouchListener() { }

    public OnCompositeTouchListener(View.OnTouchListener[] listeners) {
        if(listeners != null) {
            for(View.OnTouchListener listener : listeners) {
                addListener(listener);
            }
        }
    }

    private void addListener(View.OnTouchListener listener) {
        m_listeners.add(listener);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean result = false;

        for(View.OnTouchListener listener : m_listeners) {
            if(listener.onTouch(v, event)) {
                result = true;
            }
        }

        return result;
    }
}