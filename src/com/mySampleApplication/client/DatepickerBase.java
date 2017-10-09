package com.mySampleApplication.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsDate;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextBox;

import java.util.Date;

public abstract class DatepickerBase extends FlowPanel {

    private final DateTimeFormat localeDateFormat;

    public static enum Type {
        INLINE, COMPONENT
    }

    protected final Datepicker datepicker;

    public DatepickerBase(Type type) {
        if (type == Type.COMPONENT) {
            initComponent();
        }

        String format = GWTDateUtils.getLocaleDependentDateFormat(IsRedactor.getLocaleName());
        localeDateFormat = DateTimeFormat.getFormat(format);
        datepicker = configure(getElement(), format.replaceAll("MM", "mm"));
    }

    private void initComponent() {
        addStyleName("date");
        getElement().getStyle().setProperty("display", "inline-block");

        TextBox text = new TextBox();

        add(text);
        InlineLabel w = new InlineLabel();
        w.addStyleName("btn icon-datepicker");
        add(w);
    }

    public void setMultiSelect(boolean multiSelect) {
        datepicker.option("multidate", multiSelect);
    }

    public void setStartDate(Date date) {
        if (date == null) {
            datepicker.setStartDate("");
        } else {
            datepicker.setStartDate(localeDateFormat.format(date));
        }
    }

    public void setEndDate(Date date) {
        if (date == null) {
            datepicker.setEndDate("");
        } else {
            datepicker.setEndDate(localeDateFormat.format(date));
        }
    }

    private native Datepicker configure(Element e, String format) /*-{
        var that = this;
        $wnd.jQuery(e).datepicker({
            autoclose: true,
            weekStart: 1,
            'format': format
        }).on('change', function () {
            that.@com.mySampleApplication.client.DatepickerBase::onChange()();
        });
        return $wnd.jQuery(e).data('datepicker');
    }-*/;

    protected JsDate getJsDate(Date date) {
        if (date == null)
            return null;
        JsDate jsDate = JsDate.create(date.getTime());
        jsDate.setUTCHours(0, 0, 0, 0);
        return jsDate;
    }

    protected Date getDate(JsDate value) {
        return new Date((long) value.getTime());
    }

    public abstract void onChange();

    static class Datepicker extends JavaScriptObject {
        protected Datepicker() {}

        public final native void setUTCDates(JsDate... dates) /*-{
            this.setUTCDates(dates);
        }-*/;

        public final native JsDate[] getUTCDates() /*-{
            return this.getUTCDates();
        }-*/;

        public final native void clearDates() /*-{
            this.clearDates();
        }-*/;

        public final native void option(String name, boolean value) /*-{
            this.o[name] = value;
            this._o[name] = value;
        }-*/;

        public final native void setStartDate(String date) /*-{
            this.setStartDate(date);
        }-*/;

        public final native void setEndDate(String date) /*-{
            this.setEndDate(date);
        }-*/;
    }
}
