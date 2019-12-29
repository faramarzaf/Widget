package com.faramarz.tictacdev.widget;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class FloatingViewService extends Service implements View.OnClickListener {

    private WindowManager windowManager;
    private View floatingWidget;
    View collapsedView, expandedView;
    ImageView exitButton, playButton, nextButton, prevButton, closeButton, openActivityButton;

    public FloatingViewService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @SuppressLint({"RtlHardcoded", "InflateParams"})
    @Override
    public void onCreate() {
        super.onCreate();
        floatingWidget = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);
        WindowManager.LayoutParams params;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);


            params.gravity = Gravity.TOP | Gravity.LEFT;
            params.x = 0;
            params.y = 100;
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            windowManager.addView(floatingWidget, params);
        }else {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        }

        bind();
        clickEvents();

        //   -> Drag and Drop
        final WindowManager.LayoutParams finalParams = params;
        floatingWidget.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = finalParams.x;
                        initialY = finalParams.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int xDiff = (int) (event.getRawX() - initialTouchX);
                        int yDiff = (int) (event.getRawY() - initialTouchY);
                        if (xDiff < 10 && yDiff < 10) {
                            if (isViewCollapsed()) {
                                collapsedView.setVisibility(View.GONE);
                                expandedView.setVisibility(View.VISIBLE);
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        finalParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                        finalParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(floatingWidget, finalParams);
                        return true;
                }
                return false;
            }
        });
    }


    private void clickEvents() {
        exitButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        closeButton.setOnClickListener(this);
        openActivityButton.setOnClickListener(this);
    }

    private void bind() {
        collapsedView = floatingWidget.findViewById(R.id.collapse_view);
        expandedView = floatingWidget.findViewById(R.id.expanded_container);
        exitButton = floatingWidget.findViewById(R.id.exit_btn);
        playButton = floatingWidget.findViewById(R.id.play_btn);
        nextButton = floatingWidget.findViewById(R.id.next_btn);
        prevButton = floatingWidget.findViewById(R.id.prev_btn);
        closeButton = floatingWidget.findViewById(R.id.close_button);
        // 10 -> Close Floating Widget and back to activity
        openActivityButton = floatingWidget.findViewById(R.id.open_activity_button);
        floatingWidget.findViewById(R.id.root_container);
    }

    private boolean isViewCollapsed() {
        return floatingWidget == null || floatingWidget.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingWidget != null) windowManager.removeView(floatingWidget);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.exit_btn:
                stopSelf();
                break;
            case R.id.play_btn:
                Toast.makeText(FloatingViewService.this, "Playing the song.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.next_btn:
                Toast.makeText(FloatingViewService.this, "Playing next song.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.prev_btn:
                Toast.makeText(FloatingViewService.this, "Playing previous song.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.close_button:
                closeWidget();
                break;
            case R.id.open_activity_button:
                openApp();
                break;

        }

    }

    private void closeWidget() {
        collapsedView.setVisibility(View.VISIBLE);
        expandedView.setVisibility(View.GONE);

    }

    private void openApp() {
        Intent intent = new Intent(FloatingViewService.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        stopSelf();

    }
}
