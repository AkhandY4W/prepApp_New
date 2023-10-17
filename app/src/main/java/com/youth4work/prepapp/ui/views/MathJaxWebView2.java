package com.youth4work.prepapp.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MathJaxWebView2 extends WebView {
    public MathJaxWebView2(Context context) {
        super(context);

    }

    public MathJaxWebView2(Context context, AttributeSet attrs) {
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
    public void intiliazeWebView(){
        getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        setBackgroundColor(Color.TRANSPARENT);
        getSettings().setJavaScriptEnabled(true);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
        getSettings().setBuiltInZoomControls(false);
        getSettings().setDisplayZoomControls(false);
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
    public void setText(final String text,Boolean yesno) {
        evaluateJavascript("javascript:document.getElementById('math').innerHTML='\\\\["
                +(yesno ? "<div style=\"line-height:12px;font:10px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;\">" + doubleEscapeTeX(text) + "</div>" : "<div style=\"font:10px;line-height:10px;\">" +doubleEscapeTeX(text)+ "</div>") +"\\\\]';",null);
        loadUrl("javascript:MathJax.Hub.Queue(['Typeset',MathJax.Hub]);");
    }
    public MathJaxWebView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
