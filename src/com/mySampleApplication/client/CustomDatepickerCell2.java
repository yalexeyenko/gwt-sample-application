package com.mySampleApplication.client;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

import java.util.Date;
import java.util.Optional;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.KEYDOWN;

public class CustomDatepickerCell2 extends AbstractEditableCell<Date, Date> {

    interface Templates extends SafeHtmlTemplates {
        @Template("<div>" +
                "<input type=\"text\" value=\"{0}\" placeholder=\"{1}\" readonly />" +
                "<button id=\"clear_date_button\"/>" +
                "</div>")
        SafeHtml textDate(String dateString, String placeholder);
    }

    private static final Templates TEMPLATES = GWT.create(Templates.class);

    private static final int ESCAPE = 27;

    private final Datepicker datePicker;
    private final DateTimeFormat format;
    private int offsetX = 10;
    private int offsetY = 10;
    private Object lastKey;
    private Element lastParent;
    private int lastIndex;
    private int lastColumn;
    private Date lastValue;
    private PopupPanel panel;
    private final SafeHtmlRenderer<String> renderer;
    private ValueUpdater<Date> valueUpdater;

    /**
     * Constructs a new DatePickerCell that uses the date/time format given by
     * {@link DateTimeFormat#getFullDateFormat}.
     */
    @SuppressWarnings("deprecation")
    public CustomDatepickerCell2() {
        this(DateTimeFormat.getFullDateFormat(),
                SimpleSafeHtmlRenderer.getInstance());
    }

    /**
     * Constructs a new DatePickerCell that uses the given date/time format and a
     * {@link SimpleSafeHtmlRenderer}.
     *
     * @param format a {@link DateTimeFormat} instance
     */
    public CustomDatepickerCell2(DateTimeFormat format) {
        this(format, SimpleSafeHtmlRenderer.getInstance());
    }

    /**
     * Constructs a new DatePickerCell that uses the date/time format given by
     * {@link DateTimeFormat#getFullDateFormat} and the given
     * {@link SafeHtmlRenderer}.
     *
     * @param renderer a {@link SafeHtmlRenderer SafeHtmlRenderer<String>} instance
     */
    public CustomDatepickerCell2(SafeHtmlRenderer<String> renderer) {
        this(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_FULL), renderer);
    }

    /**
     * Constructs a new DatePickerCell that uses the given date/time format and
     * {@link SafeHtmlRenderer}.
     *
     * @param format   a {@link DateTimeFormat} instance
     * @param renderer a {@link SafeHtmlRenderer SafeHtmlRenderer<String>} instance
     */
    public CustomDatepickerCell2(DateTimeFormat format, SafeHtmlRenderer<String> renderer) {
        super(CLICK, KEYDOWN);
        if (format == null) {
            throw new IllegalArgumentException("format == null");
        }
        if (renderer == null) {
            throw new IllegalArgumentException("renderer == null");
        }
        this.format = format;
        this.renderer = renderer;

        this.datePicker = new Datepicker(DatepickerBase.Type.COMPONENT);
        this.panel = new PopupPanel(true, true) {
            @Override
            protected void onPreviewNativeEvent(Event.NativePreviewEvent event) {
                if (Event.ONKEYUP == event.getTypeInt()) {
                    if (event.getNativeEvent().getKeyCode() == ESCAPE) {
                        // Dismiss when escape is pressed
                        panel.hide();
                    }
                }
            }
        };
        panel.addCloseHandler(new CloseHandler<PopupPanel>() {
            public void onClose(CloseEvent<PopupPanel> event) {
                lastKey = null;
                lastValue = null;
                lastIndex = -1;
                lastColumn = -1;
                if (lastParent != null && !event.isAutoClosed()) {
                    // Refocus on the containing cell after the user selects a value, but
                    // not if the popup is auto closed.
                    lastParent.focus();
                }
                lastParent = null;
            }
        });
        panel.add(datePicker);

        // Hide the panel and call valueUpdater.update when a date is selected
        datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
            public void onValueChange(ValueChangeEvent<Date> event) {
                // Remember the values before hiding the popup.
                Element cellParent = lastParent;
                Date oldValue = lastValue;
                Object key = lastKey;
                int index = lastIndex;
                int column = lastColumn;
                panel.hide();

                // Update the cell and value updater.
                Date date = event.getValue();
                setViewData(key, date);
                setValue(new Context(index, column, key), cellParent, oldValue);
                if (valueUpdater != null) {
                    valueUpdater.update(date);
                }
            }
        });

//        initDateClear();
    }

    /**
     * Returns the underlying {@link DatePicker} widget used by this cell.
     */
    public Datepicker getDatePicker() {
        return datePicker;
    }

    @Override
    public boolean isEditing(Context context, Element parent, Date value) {
        return lastKey != null && lastKey.equals(context.getKey());
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, Date value,
                               NativeEvent event, ValueUpdater<Date> valueUpdater) {
        super.onBrowserEvent(context, parent, value, event, valueUpdater);
        if (CLICK.equals(event.getType())) {
            onEnterKeyDown(context, parent, value, event, valueUpdater);
        }
    }

    @Override
    public void render(Context context, Date value, SafeHtmlBuilder sb) {
        // Get the view data.
        Object key = context.getKey();
        Date viewData = getViewData(key);
        if (viewData != null && viewData.equals(value)) {
            clearViewData(key);
            viewData = null;
        }

        String s = null;
        if (viewData != null) {
            s = format.format(viewData);
        } else if (value != null) {
            s = format.format(value);
        }
        if (s != null) {
//            sb.append(renderer.render(s));
            sb.append(TEMPLATES.textDate(s, "choose date"));
        } else {
            sb.append(TEMPLATES.textDate("", "choose date"));
        }
    }

    @Override
    protected void onEnterKeyDown(Context context, Element parent, Date value,
                                  NativeEvent event, ValueUpdater<Date> valueUpdater) {
        this.lastKey = context.getKey();
        this.lastParent = parent;
        this.lastValue = value;
        this.lastIndex = context.getIndex();
        this.lastColumn = context.getColumn();
        this.valueUpdater = valueUpdater;

        Date viewData = getViewData(lastKey);
        Date date = (viewData == null) ? lastValue : viewData;

        Optional<Date> dateOptional = Optional.ofNullable(date);

//        dateOptional.ifPresent(datePicker::setCurrentMonth);

        datePicker.setValue(date);
        panel.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
            public void setPosition(int offsetWidth, int offsetHeight) {
                panel.setPopupPosition(lastParent.getAbsoluteLeft() + offsetX,
                        lastParent.getAbsoluteTop() + offsetY);
            }
        });
    }
}
