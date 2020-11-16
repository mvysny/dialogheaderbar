package org.vaadin.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.ArrayList;

/**
 * @author Martin Vysny <mavi@vaadin.com>
 */
public class DialogHeaderBar extends HorizontalLayout {
    private final Div caption = new Div();
    private final Dialog dialog;
    private boolean maximized = false;
    private String prevWidth = null;
    private String prevHeight = null;
    private final Button maximizeRestore = new Button(VaadinIcon.EXPAND_SQUARE.create());

    public void setCaption(String caption) {
        this.caption.setText(caption);
    }
    public DialogHeaderBar(Dialog dialog) {
        this.dialog = dialog;
        getStyle().set("color", "white");
        addClassName("draggable"); // in order for the Dialog to be draggable by the header bar
        setWidthFull();
        addAndExpand(caption);
        caption.getStyle().set("font-size", "var(--lumo-font-size-xl)");
        caption.getStyle().set("padding", "4px 16px");

        getStyle().set("background", "blue");
        getStyle().set("overflow", "hidden");  // close button overflows /facepalm
        add(maximizeRestore);
        maximizeRestore.addClickListener(e -> {
            if (maximized) {
                dialog.setWidth(prevWidth);
                dialog.setHeight(prevHeight);
                maximized = false;
            } else {
                prevWidth = dialog.getWidth();
                prevHeight = dialog.getHeight();
                dialog.setWidth("100vw");
                dialog.setHeight("100vh");
                maximized = true;
            }
            update();
        });

        final Button close = new Button(VaadinIcon.CLOSE_BIG.create());
        close.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL);
        add(close);
        close.addClickListener(e -> dialog.close());

        update();
    }

    private void update() {
        if (maximized) {
            maximizeRestore.setIcon(VaadinIcon.COMPRESS_SQUARE.create());
        } else {
            maximizeRestore.setIcon(VaadinIcon.EXPAND_SQUARE.create());
        }
        dialog.setDraggable(!maximized);
        dialog.setResizable(!maximized);
        caption.getStyle().set("cursor", maximized ? "default" : "move");
        center(dialog);
    }

    private static void center(Dialog dialog) {
        final ArrayList<String> styles = new ArrayList<>();
        if (dialog.getWidth() != null) {
            styles.add("width: " + dialog.getWidth());
        }
        if (dialog.getHeight() != null) {
            styles.add("height: " + dialog.getHeight());
        }
        dialog.getElement().executeJs("this.$.overlay.$.overlay.style = '" + String.join(";", styles) + "';");
    }
}
