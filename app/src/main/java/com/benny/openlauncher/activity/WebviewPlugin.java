package com.benny.openlauncher.activity;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.benny.openlauncher.R;
import com.benny.openlauncher.util.LauncherModel;


public class WebviewPlugin {
    public Activity activity;
    public  View rootView;
    public WebView[] webViews=new WebView[5];
    private EditText urlInput;
    private int currentWebViewIndex=0;

    public LauncherModel launcherModel;
    public ConstraintLayout container;

    //for move
    private float dX, dY;
    private int lastAction;



    public WebviewPlugin(Activity activity,View rootView){
        this.activity=activity;
        this.rootView=rootView;
        this.launcherModel=new LauncherModel(activity.getApplicationContext());
        this.container =activity.findViewById(R.id.webcontainer);


        this.init();
    }

    public void init(){
        AllFunctionWebviewSetter.requestPermissions(activity);
    loadWeb();
    addEventListener();
    }

    private  void  loadWeb(){
      WebView  webview1 = activity.findViewById(R.id.webview1);
        WebView  webview2 = activity.findViewById(R.id.webview2);
        WebView  webview3 = activity.findViewById(R.id.webview3);
        WebView  webview4 = activity.findViewById(R.id.webview4);
        WebView webview5 = activity.findViewById(R.id.webview5);

        webViews = new WebView[]{webview1, webview2, webview3, webview4,webview5 };
        urlInput=activity.findViewById((R.id.url_input));


        // Load the initial URL into the first WebView
        webview1.setVisibility(View.VISIBLE);

        for (WebView webView:
                webViews) {
            AllFunctionWebviewSetter.setWebView(webView,activity, activity.getApplicationContext(),urlInput);
            setViewSizeByPercentageOfScreen(activity,webView,
                    launcherModel.webViewWidth,
                    launcherModel.webViewHeight
            );
        }
        webview1.loadUrl("https://h5test.pages.dev");
        Button openbutton=activity.findViewById(R.id.open_url_button);
        openbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webview1.loadUrl("https://h5test.pages.dev");
                showtoast("open",activity);
                openbutton.setBackgroundColor(R.id.color_indicator);
            }
        });

    }

    private void addEventListener() {
        WebView webView=webViews[currentWebViewIndex];
        WebSettings webSettings=webView.getSettings();

        final EditText urlInput = activity.findViewById(R.id.url_input);
        Button fullscreenButton = activity.findViewById(R.id.fullscreenButton);
        Button refreshButton = activity.findViewById(R.id.refresh_button);
        Button increaseHeightButton = activity.findViewById(R.id.increase_height_button);
        Button openUrlButton = activity.findViewById(R.id.open_url_button);
        Button changeWebViewButton = activity.findViewById(R.id.change_webview_button);
        Button hideUrlButton=activity.findViewById(R.id.hideUrlButton);
        Button decreaseHeightButton = activity.findViewById(R.id.decrease_height_button);
        Button menuButton = activity.findViewById(R.id.menuButton);
        Button actionButton = activity.findViewById(R.id.actionButton);


        SeekBar zoomSeekBar = activity.findViewById(R.id.zoomSeekbar);
        SeekBar textZoomSeekBar = activity.findViewById(R.id.textZoomSeekbar);
        SeekBar heightSeekbar=activity.findViewById(R.id.webViewHeight);
        SeekBar widthSeekbar=activity.findViewById(R.id.webViewWidth);

        widthSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the initial scale based on SeekBar progress
                setWebViewSize(progress,launcherModel.webViewHeight);
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        heightSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the initial scale based on SeekBar progress
                setWebViewSize(100,progress);
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        textZoomSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the initial scale based on SeekBar progress
                webView.getSettings().setTextZoom(progress);

            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        zoomSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the initial scale based on SeekBar progress
                webView.setInitialScale(progress);

            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });



        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleViewVisibility(activity.findViewById(R.id.subBrowserMenu));
            }
        });
        actionButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        lastAction = MotionEvent.ACTION_DOWN;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        float x = event.getRawX() ;
                        float y = event.getRawY() ;

                        // Update the LinearLayout's position
                        container.setX(x);
                        container.setY(y);
                        break;

                    case MotionEvent.ACTION_UP:
                        break;

                    default:
                        return true;
                }
                return true;
            }
        });


		hideUrlButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (urlInput.getVisibility() == View.VISIBLE) {
					// If the view is currently visible, hide it
					urlInput.setVisibility(View.GONE);
				} else {
					// If the view is currently hidden, show it
					urlInput.setVisibility(View.VISIBLE);
				}
			}
		});

        changeWebViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the current URL before switching
                launcherModel.urls[currentWebViewIndex] = urlInput.getText().toString();
                launcherModel.saveSettings();

                // Increment index to switch to the next WebView
                currentWebViewIndex = (currentWebViewIndex + 1) % webViews.length;
                changeWebViewButton.setText("▷"+currentWebViewIndex);

                // Show the next WebView and update the URL input field
                urlInput.setText(launcherModel.urls[currentWebViewIndex]);

                // Hide all WebViews
                for (WebView wv : webViews) {
                    wv.setVisibility(View.GONE);
                }
                webViews[currentWebViewIndex].setVisibility(View.VISIBLE);
            }


        });
        changeWebViewButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                launcherModel.urls[currentWebViewIndex] = urlInput.getText().toString();
                launcherModel.saveSettings();



                // Increment index to switch to the next WebView
                currentWebViewIndex = 0;
                changeWebViewButton.setText("▷"+currentWebViewIndex);

                // Show the next WebView and update the URL input field
                urlInput.setText(launcherModel.urls[currentWebViewIndex]);
                // Hide all WebViews
                for (WebView wv : webViews) {
                    wv.setVisibility(View.GONE);
                }
                webViews[currentWebViewIndex].setVisibility(View.VISIBLE);

                return true;
            }
        });


        openUrlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebView webView = webViews[currentWebViewIndex];
                webView.setVisibility(View.VISIBLE);
                String mobileUserAgent = "Mozilla/5.0 (Linux; Android 10; Pixel 3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Mobile Safari/537.36";
                webView.getSettings().setUserAgentString(mobileUserAgent);

                String url = urlInput.getText().toString().trim();
                if (!url.isEmpty()) {

                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        url = launcherModel.searchEnginUrl + url; // Add scheme if missing
                    }
                    webView.clearCache(true);
                    webView.loadUrl(url); // Load the URL in the WebView
                    launcherModel.urls[currentWebViewIndex] = url;
                    launcherModel.saveSettings();
                }
                showtoast("long click to change to desktop mode ", activity);
            }
        });

        openUrlButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                WebView webView = webViews[WebviewPlugin.this.currentWebViewIndex];
                String desktopUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";
                webView.getSettings().setUserAgentString(desktopUserAgent);
                webView.loadUrl(urlInput.getText().toString());
                return true;
            }
        });

        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle fullscreen mode

                if ((activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0) {
                    // Currently in fullscreen mode, revert back to normal mode
                    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                } else {
                    // Currently in normal mode, switch to fullscreen mode
                    activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    setViewSizeToFullScreen(webViews[currentWebViewIndex]);

                }

                int uiOptions = activity.getWindow().getDecorView().getSystemUiVisibility();
                int newUiOptions = uiOptions;



                newUiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;


                activity.getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

            }
        });

        increaseHeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcherModel.webViewHeight+=10;
                launcherModel.saveSettings();
                for (WebView webview :
                        webViews) {
                    setViewSizeByPercentageOfScreen(activity,webview,
                            launcherModel.webViewWidth,
                            launcherModel.webViewHeight
                    );
                }
            }
        });
        increaseHeightButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                launcherModel.currentScale+=20;
                launcherModel.saveSettings();
                WebView webView=webViews[currentWebViewIndex];
                webView.setInitialScale(launcherModel.currentScale);
                showtoast(String.valueOf(launcherModel.currentScale),activity);
                return true;
            }
        });

        decreaseHeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcherModel.webViewHeight-=10;
                launcherModel.saveSettings();
                for (WebView webview :
                        webViews) {
                    setViewSizeByPercentageOfScreen(activity,webview,
                            launcherModel.webViewWidth,
                            launcherModel.webViewHeight
                    );
                }
            }
        });
        decreaseHeightButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                launcherModel.currentScale-=20;
                launcherModel.saveSettings();
                WebView webView=webViews[currentWebViewIndex];
                webView.setInitialScale(launcherModel.currentScale);
                showtoast(String.valueOf(launcherModel.currentScale),activity);
                return true;
            }
        });

    }


    public void setWebViewSize(int  w, int h){
        launcherModel.webViewHeight=h;
        launcherModel.webViewWidth=w;
        launcherModel.saveSettings();
        for (WebView webview :
                webViews) {
            setViewSizeByPercentageOfScreen(activity,webview,
                    launcherModel.webViewWidth,
                    launcherModel.webViewHeight
            );
        }
    }

    private void toggleViewVisibility(View yourView) {
        if (yourView.getVisibility() == View.VISIBLE) {
            yourView.setVisibility(View.INVISIBLE);
        } else {
            yourView.setVisibility(View.VISIBLE);
        }
    }


    public static void setViewSizeByPercentageOfParent(View view, int widthPercentage, int heightPercentage) {
        if (view == null || !(view.getParent() instanceof View)) {
            return;
        }

        View parentView = (View) view.getParent();
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        if (widthPercentage > 0) {
            int parentWidth = parentView.getWidth();
            layoutParams.width = (int) (parentWidth * widthPercentage / 100.0);
        }

        if (heightPercentage > 0) {
            int parentHeight = parentView.getHeight();
            layoutParams.height = (int) (parentHeight * heightPercentage / 100.0);
        }

        view.setLayoutParams(layoutParams);
    }



    public static void setViewSizeByPercentageOfScreen(Context context, View view, int widthPercentage, int heightPercentage) {
        if (view == null) {
            return;
        }

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        if (widthPercentage > 0) {
            int screenWidth = displayMetrics.widthPixels;
            layoutParams.width = (int) (screenWidth * widthPercentage / 100.0);
        }

        if (heightPercentage > 0) {
            int screenHeight = displayMetrics.heightPixels;
            layoutParams.height = (int) (screenHeight * heightPercentage / 100.0);
        }

        view.setLayoutParams(layoutParams);
    }

    private void setViewSizeToFullScreen(View view) {
        // Get screen height and width
        int screenHeight = activity.getResources().getDisplayMetrics().heightPixels;
        int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;

        // Set the height and width of the view to 100% of the screen
        view.getLayoutParams().height = screenHeight;
        view.getLayoutParams().width = screenWidth;
    }

    public static void showtoast(String msg, Activity activity){
        Toast toast = Toast.makeText(activity.getApplicationContext(), " " + msg, Toast.LENGTH_SHORT);
        toast.show();


    }

}
