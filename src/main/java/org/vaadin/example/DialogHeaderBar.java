package org.vaadin.example;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A component which implements the Dialog header bar. The header bar by default
 * shows both the close button and the maximize/restore button.
 * <ul>
 *     <li>call {@link #setCloseVisible(boolean)} to hide the close button,</li>
 *     </li>call {@link #setMaximizeVisible(boolean)} to hide the maximize button,</li>
 *     <li>call {@link #setCaption(String)} to set the header bar caption (defaults to an empty string)</li>
 * </ul>
 *
 * <h3>Integration with Dialog</h3>
 * Simply call the {@link #addTo(Dialog)} which integrates the header bar into the dialog.
 * <p></p>
 * Warning: this will remove the dialog content padding; simply wrap the contents with
 * a <code>VerticalLayout</code> which adds padding of its own.
 *
 * @author Martin Vysny <mavi@vaadin.com>
 */
@CssImport(value = "./styles/dialog-styles.css", themeFor = "vaadin-dialog-overlay")
public class DialogHeaderBar extends HorizontalLayout {
    private final Div caption = new Div();
    @NotNull
    private final Dialog dialog;
    private boolean maximized = false;
    private String prevWidth = null;
    private String prevHeight = null;
    private final HeaderBarButton maximizeRestoreButton = new HeaderBarButton();
    private final HeaderBarButton closeButton = new HeaderBarButton(VaadinIcon.CLOSE_BIG);

    /**
     * Sets the dialog caption.
     *
     * @param caption the caption, or blank string to remove the caption.
     * @return this
     */
    @NotNull
    public DialogHeaderBar setCaption(@NotNull String caption) {
        this.caption.setText(caption);
        return this;
    }

    /**
     * Returns the dialog caption.
     *
     * @return the caption, or blank string when there's no caption (the default).
     */
    @NotNull
    public String getCaption() {
        return this.caption.getText();
    }

    /**
     * Makes the close button visible or invisible.
     *
     * @param isCloseVisible
     * @return this
     */
    @NotNull
    public DialogHeaderBar setCloseVisible(boolean isCloseVisible) {
        getCloseButton().setVisible(isCloseVisible);
        return this;
    }

    /**
     * @return true if the close button is visible (the default).
     */
    public boolean isCloseVisible() {
        return getCloseButton().isVisible();
    }

    /**
     * Makes the maximize/restore button visible or invisible.
     *
     * @param isMaximizeVisible
     * @return this
     */
    @NotNull
    public DialogHeaderBar setMaximizeVisible(boolean isMaximizeVisible) {
        getMaximizeRestoreButton().setVisible(isMaximizeVisible);
        return this;
    }

    /**
     * @return true if the maximize/restore button is visible (the default).
     */
    public boolean isMaximizeVisible() {
        return getMaximizeRestoreButton().isVisible();
    }

    /**
     * Returns the owner dialog.
     *
     * @return the owner dialog.
     */
    @NotNull
    public Dialog getDialog() {
        return dialog;
    }

    /**
     * Creates a new header bar, inserts it into the dialog, reconfigures the dialog
     * to have no content padding, and returns the header bar.
     *
     * @param dialog the dialog to control, not null.
     * @return the header bar.
     */
    @NotNull
    public static DialogHeaderBar addTo(@NotNull Dialog dialog) {
        if (dialog.getChildren().anyMatch(it -> it instanceof DialogHeaderBar)) {
            throw new IllegalArgumentException("Parameter dialog: invalid value " + dialog + ": already has a header bar");
        }

        // remove padding from content, to have no border around the header bar.
        // workaround for https://github.com/vaadin/vaadin-dialog/issues/126
        dialog.getElement().getThemeList().add("no-padding");

        final DialogHeaderBar headerBar = new DialogHeaderBar(dialog);
        dialog.addComponentAsFirst(headerBar);
        return headerBar;
    }

    /**
     * Creates the header bar.
     *
     * @param dialog the header bar will control this dialog. However, the header
     *               bar will not insert automatically into the dialog; use the {@link #addTo(Dialog)} to do that.
     */
    public DialogHeaderBar(@NotNull Dialog dialog) {
        this.dialog = dialog;
        getElement().getStyle().set("user-select", "none"); // prevent caption selection when dragging the dialog
        addClassName("draggable"); // in order for the Dialog to be draggable by the header bar
        setWidthFull();
        addAndExpand(caption);
        caption.getStyle().set("font-size", "var(--lumo-font-size-l)");
        caption.getStyle().set("padding", "4px 16px");

        getStyle().set("border-bottom", "solid 1px var(--lumo-contrast-10pct)");
        add(maximizeRestoreButton);
        maximizeRestoreButton.addClickListener(e -> {
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

        add(closeButton);
        closeButton.addClickListener(e -> dialog.close());

        update();
    }

    static class HeaderBarButton extends Icon {
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
            maximizeRestoreButton.setIcon(VaadinIcon.COMPRESS_SQUARE);
        } else {
            maximizeRestoreButton.setIcon(VaadinIcon.EXPAND_SQUARE);
        }
        dialog.setDraggable(!maximized);
        dialog.setResizable(!maximized);
        caption.getStyle().set("cursor", maximized ? "default" : "move");
        center(dialog);
    }

    /**
     * Utility function, see https://github.com/vaadin/vaadin-dialog/issues/220 for more details.
     *
     * @param dialog
     */
    private static void center(@NotNull Dialog dialog) {
        final ArrayList<String> styles = new ArrayList<>();
        if (dialog.getWidth() != null) {
            styles.add("width: " + dialog.getWidth());
        }
        if (dialog.getHeight() != null) {
            styles.add("height: " + dialog.getHeight());
        }
        dialog.getElement().executeJs("this.$.overlay.$.overlay.style = '" + String.join(";", styles) + "';");
    }

    @NotNull
    HeaderBarButton getCloseButton() {
        return closeButton;
    }

    @NotNull
    HeaderBarButton getMaximizeRestoreButton() {
        return maximizeRestoreButton;
    }
}
