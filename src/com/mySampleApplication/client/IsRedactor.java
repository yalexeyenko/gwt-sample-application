package com.mySampleApplication.client;

import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Widget;

public class IsRedactor {

    public static final String LOCALE_COOKIE_NAME = "user_locale";

    public static final String DEFAULT_LOCALE = "en_GB";

    private boolean isRedactor = true;

    private Messages constantsLookup;

    private static IsRedactor instance;

    private static ToolTip shownedToolTip;

    private IsRedactor() {

    }

    public static IsRedactor getInstance() {
        if (instance == null) {
            instance = new IsRedactor();
        }
        return instance;
    }

    public boolean isRedactor() {
        return isRedactor;
    }

    public void setRedactor(Messages constantsLookup, boolean isRedactor) {
        this.isRedactor = isRedactor;
        this.constantsLookup = constantsLookup;
    }

    public String getMessage(String messageId) {
        if (constantsLookup != null) {
            String text = constantsLookup.getMessage(messageId);
            if (text != null) {
                return text;
            } else {
                return messageId;
            }
        }
        return "error";
    }

    public void setRedactorMessage(Widget widget, final String i18nId) {
        if (IsRedactor.getInstance().isRedactor()) {
            // widget.setTitle(i18nId);

            widget.addDomHandler(new MouseOverHandler() {

                @Override
                public void onMouseOver(MouseOverEvent event) {
                    if (shownedToolTip != null) {
                        shownedToolTip.hide();
                    }
                    shownedToolTip = new ToolTip(i18nId, event.getClientX(), event.getClientY());
                }
            }, MouseOverEvent.getType());
        }
    }

    public static void setLocale(String name) {
        Cookies.setCookie(LOCALE_COOKIE_NAME, name);
    }

    public static String getLocaleName() {
        String name = Cookies.getCookie(LOCALE_COOKIE_NAME);
        if (null == name) {
            name = DEFAULT_LOCALE;
        }
        return name;
    }

}

