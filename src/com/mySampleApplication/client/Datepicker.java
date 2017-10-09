package com.mySampleApplication.client;

import com.google.gwt.core.client.JsDate;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiConstructor;

import java.util.Date;

import static com.google.gwt.query.client.GQuery.$;

public class Datepicker extends DatepickerBase implements LeafValueEditor<Date>, CheckValidation,
        HasValueChangeHandlers<Date> {
    private boolean required;

    @UiConstructor
    public Datepicker(Type type) {
        super(type);

        addValueChangeHandler(new ValueChangeHandler<Date>() {
            public void onValueChange(ValueChangeEvent<Date> event) {
                setFieldValidated();
            }
        });
    }

    @Override
    public void onChange() {
        ValueChangeEvent.fire(this, getValue());
    }

    @Override
    public void setValue(Date value) {
        if (value == null) {
            datepicker.clearDates();
        }
        datepicker.setUTCDates(getJsDate(value));
    }

    @Override
    public Date getValue() {
        JsDate[] dates = datepicker.getUTCDates();
        if (dates.length > 0)
            return getDate(dates[0]);
        else
            return null;
    }

    @Override
    public void setNotNull(boolean notNull) {
        this.required = notNull;
    }

    @Override
    public void setMinLength(int minLength) {}

    @Override
    public boolean validateMessage() {
        if (required && getValue() == null) {
            setInvalidateMessage("Can not be empty");
            return false;
        }
        setFieldValidated();
        return true;
    }

    @Override
    public void setInvalidateMessage(String message) {
        $(this).find("input").addClass(VALIDATION_ERROR_STYLE).attr("title", message);

    }

    @Override
    public void setFieldValidated() {
        $(this).find("input").removeClass(VALIDATION_ERROR_STYLE).removeAttr("title");
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Date> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
}