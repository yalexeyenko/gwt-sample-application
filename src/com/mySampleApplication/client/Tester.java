package com.mySampleApplication.client;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.DatePickerCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class Tester implements EntryPoint {

    private static interface GetValue<C> {
        C getValue(Contact contact);
    }

    // A simple data type that represents a contact.
    public static class Contact {
        private String address;
        private String name;
        private Date date;

        public Contact(String name, String address, Date date) {
            this.name = name;
            this.address = address;
            this.date = date;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }

    // Create a CellTable.
    CellTable<Contact> table = new CellTable<Contact>();

    // The list of data to display.
    private static List<Contact> CONTACTS = Arrays.asList(new Contact("John",
            "123 Fourth Road", new Date()), new Contact("Mary", "222 Lancer Lane", null));

    public void onModuleLoad() {



        // Create name column.
        TextColumn<Contact> nameColumn = new TextColumn<Contact>() {
            @Override
            public String getValue(Contact contact) {
                return contact.name;
            }
        };

        // Create address column.
        TextColumn<Contact> addressColumn = new TextColumn<Contact>() {
            @Override
            public String getValue(Contact contact) {
                return contact.address;
            }
        };


        DateTimeFormat dateFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT);
        Column<Contact, Date> dateColumn = new Column<Contact, Date>(new CustomDatepickerCell2(dateFormat)) {
            @Override
            public Date getValue(Contact object) {
                return object.getDate();
            }
        };
        dateColumn.setFieldUpdater(new FieldUpdater<Contact, Date>() {
            @Override
            public void update(int index, Contact object, Date value) {
                object.setDate(value);
            }
        });

        // Add the columns.
        table.addColumn(nameColumn, "Name");
        table.addColumn(addressColumn, "Address");
        table.addColumn(dateColumn, "Date");

        // Set the width of the table and put the table in fixed width mode.
        table.setWidth("100%", true);

        // Set the width of each column.
//        table.setColumnWidth(nameColumn, 35.0, Style.Unit.PCT);
//        table.setColumnWidth(addressColumn, 65.0, Style.Unit.PCT);
//        table.setColumnWidth(addressColumn, 25.0, Style.Unit.PCT);

        // Set the total row count. This isn't strictly necessary, but it affects
        // paging calculations, so its good habit to keep the row count up to date.
        table.setRowCount(CONTACTS.size(), true);

        // Push the data into the widget.
        table.setRowData(0, CONTACTS);

        // Add it to the root panel.
        RootPanel.get().add(table);
    }

    private <C> Column<Contact, C> addColumn(Cell<C> cell, String headerText,
                                             final GetValue<C> getter, FieldUpdater<Contact, C> fieldUpdater) {
        Column<Contact, C> column = new Column<Contact, C>(cell) {
            @Override
            public C getValue(Contact object) {
                return getter.getValue(object);
            }
        };
        column.setFieldUpdater(fieldUpdater);
        table.addColumn(column, headerText);
        return column;
    }
}
