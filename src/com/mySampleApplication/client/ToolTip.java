package com.mySampleApplication.client;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class ToolTip extends PopupPanel {

    final int VISIBLE_DELAY = 20000;

    Timer removeDelay;

    public ToolTip(String message, int x, int y) {
        super(true);
        this.setPopupPosition(x, y);
        this.add(new Label(message));
        removeDelay = new Timer() {
            public void run() {
                ToolTip.this.setVisible(false);
                ToolTip.this.removeFromParent();
            }
        };

        this.addCloseHandler(new CloseHandler<PopupPanel>() {

            @Override
            public void onClose(CloseEvent<PopupPanel> event) {
                removeDelay.cancel();

            }
        });
        this.setStyleName("toolTip");
        this.show();
    }

}

