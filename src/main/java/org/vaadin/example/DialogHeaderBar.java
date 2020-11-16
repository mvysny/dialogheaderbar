package org.vaadin.example;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.ArrayList;
import java.util.Locale;

/**
 * @author Martin Vysny <mavi@vaadin.com>
 */
public class DialogHeaderBar extends HorizontalLayout {
    private final Div caption = new Div();
    private final Dialog dialog;
    private boolean maximized = false;
    private String prevWidth = null;
    private String prevHeight = null;
    private final HeaderBarButton maximizeRestore = new HeaderBarButton();

    public void setCaption(String caption) {
        this.caption.setText(caption);
    }
    public DialogHeaderBar(Dialog dialog) {
        this.dialog = dialog;
        getElement().getStyle().set("user-select", "none"); // prevent caption selection when dragging the dialog
        addClassName("draggable"); // in order for the Dialog to be draggable by the header bar
        setWidthFull();
        addAndExpand(caption);
        caption.getStyle().set("font-size", "var(--lumo-font-size-l)");
        caption.getStyle().set("padding", "4px 16px");

        getStyle().set("border-bottom", "solid 1px var(--lumo-contrast-10pct)");
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

        final HeaderBarButton close = new HeaderBarButton(VaadinIcon.CLOSE_BIG);
        add(close);
        close.addClickListener(e -> dialog.close());

        update();
    }

    private static class HeaderBarButton extends Icon {
        public HeaderBarButton() {
            setSize("16px");
            getElement().getStyle().set("margin", "8px");
            getElement().getStyle().set("cursor", "pointer");
        }

        public HeaderBarButton(VaadinIcon icon) {
            this();
            setIcon(icon);
        }

        public void setIcon(VaadinIcon icon) {
            getElement().setAttribute("icon", "vaadin:" + icon.name().toLowerCase(Locale.ENGLISH).replace('_', '-'));
        }
    }

    private void update() {
        if (maximized) {
            maximizeRestore.setIcon(VaadinIcon.COMPRESS_SQUARE);
        } else {
            maximizeRestore.setIcon(VaadinIcon.EXPAND_SQUARE);
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
