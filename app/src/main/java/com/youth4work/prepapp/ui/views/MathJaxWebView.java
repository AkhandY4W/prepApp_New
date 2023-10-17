package com.youth4work.prepapp.ui.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class MathJaxWebView extends WebView {
    private String TAG = "PrepGuru";
    private static final float default_text_size = 18;
    private String display_text;
    private int text_color;
    private int text_size;
    private boolean clickable = false;
    private boolean enable_zoom_in_controls = false;
    public MathJaxWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        setBackgroundColor(Color.TRANSPARENT);
        getSettings().setJavaScriptEnabled(true);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
        getSettings().setBuiltInZoomControls(false);
        getSettings().setDisplayZoomControls(false);

    }

    public void setTextColor(String txtColr) {
        evaluateJavascript("javascript:document.body.style.setProperty(\"color\", \"" + txtColr + "\");", null);
    }

    public void setText(final String text,Boolean yesno) {
      loadDataWithBaseURL("http://bar",
               "<script type=\"text/x-mathjax-config\">" +
                       "  MathJax.Hub.Config({" +
                       "extensions: [\"tex2jax.js\"],messageStyle:\"none\"," +
                       "jax: [\"input/TeX\",\"output/HTML-CSS\"]," +
                       "tex2jax: {inlineMath: [['$','$'],['\\\\(','\\\\)']]}" +
                       "});" +
                       "</script>" +
                       "<script type=\"text/javascript\" async src=\"file:///android_asset/MathJax/MathJax.js?config=TeX-AMS-MML_HTMLorMML\"></script>" +
                       "" +
                       "</head>" +
                       "" +
                       "<body>" +
                       (yesno ? "<div style=\"text-align: center;line-height:12px;font:10px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;\">" + text + "</div>" : "<div style=\"text-align: center;font:10px;line-height:10px;\">" +text+ "</div>") +
                       "</body>" +
                       "</html>", "text/html", "utf-8", "");
        Log.v("text", text);
    }
    public void initiliaze(){
        loadDataWithBaseURL("http://bar", "<script type='text/x-mathjax-config'>"
                +"MathJax.Hub.Config({ "
                +"showMathMenu: false, "
                +"jax: ['input/TeX','output/HTML-CSS'], "
                +"extensions: ['tex2jax.js'], "
                +"TeX: { extensions: ['AMSmath.js','AMSsymbols.js',"
                +"'noErrors.js','noUndefined.js'] } "
                +"});</script>"
                +"<script type='text/javascript' "
                +"src='file:///android_asset/MathJax/MathJax.js'"
                +"></script><span id='math'></span>","text/html","utf-8","");

    }
    private String doubleEscapeTeX(String s) {
        String t="";
        for (int i=0; i < s.length(); i++) {
            if (s.charAt(i) == '\'') t += '\\';
            if (s.charAt(i) != '\n') t += s.charAt(i);
            if (s.charAt(i) == '\\') t += "\\";
        }
        return t;
    }
    public void setText(String txt){
        evaluateJavascript("javascript:document.getElementById('math').innerHTML='\\\\["
                +doubleEscapeTeX(txt)+"\\\\]';",null);
        loadUrl("javascript:MathJax.Hub.Queue(['Typeset',MathJax.Hub]);");
    }
    public void setClickable(boolean is_clickable)
    {
        this.setEnabled(true);
        this.clickable = is_clickable;
        this.enable_zoom_in_controls = !is_clickable;
        configurationSettingWebView(this.enable_zoom_in_controls);
        this.invalidate();
    }
    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
    private void configurationSettingWebView(boolean enable_zoom_in_controls)
    {
        this.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        WebSettings settings = this.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setDisplayZoomControls(enable_zoom_in_controls);
        settings.setBuiltInZoomControls(enable_zoom_in_controls);
        settings.setSupportZoom(enable_zoom_in_controls);
        this.setVerticalScrollBarEnabled(enable_zoom_in_controls);
        this.setHorizontalScrollBarEnabled(enable_zoom_in_controls);
        Log.d(TAG,"Zoom in controls:"+enable_zoom_in_controls);
    }

}