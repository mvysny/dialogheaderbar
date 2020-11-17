package org.vaadin.example;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.mvysny.kaributesting.v10.ButtonKt._click;
import static com.github.mvysny.kaributesting.v10.LocatorJ.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Uses Karibu-Testing for insanely fast Vaadin unit testing.
 * @author Martin Vysny <mavi@vaadin.com>
 */
public class DialogHeaderBarTest {
    @BeforeEach
    public void mockVaadin() {
        MockVaadin.setup();
    }
    @AfterEach
    public void tearDownVaadin() {
        MockVaadin.tearDown();
    }

    @Test
    public void smoke() {
        final Dialog dialog = new Dialog();
        DialogHeaderBar.addTo(dialog);
        dialog.open();
        _assertOne(DialogHeaderBar.class);
    }

    @Test
    public void closeButtonClosesTheDialog() {
        final Dialog dialog = new Dialog();
        dialog.open();
        assertTrue(dialog.isOpened());
        final DialogHeaderBar bar = DialogHeaderBar.addTo(dialog);
        _click(bar.getCloseButton());
        assertFalse(dialog.isOpened());
    }

    @Test
    public void caption() {
        final Dialog dialog = new Dialog();
        final DialogHeaderBar bar = DialogHeaderBar.addTo(dialog);
        dialog.open();
        assertEquals("", bar.getCaption());

        // set the caption to "Hello"
        bar.setCaption("Hello");
        assertEquals("Hello", bar.getCaption());
        _assertOne(Div.class, spec -> spec.withText("Hello"));

        // reset the caption back to an empty string
        bar.setCaption("");
        assertEquals("", bar.getCaption());
        _assertNone(Div.class, spec -> spec.withText("Hello"));
    }

    @Test
    public void maximize_smokeTest() {
        final Dialog dialog = new Dialog();
        final DialogHeaderBar bar = DialogHeaderBar.addTo(dialog);
        dialog.open();
        assertFalse(bar.isMaximized());
        assertNull(dialog.getWidth());
        assertNull(dialog.getHeight());

        bar.setMaximized(true);
        assertTrue(bar.isMaximized());
        assertEquals("100vw", dialog.getWidth());
        assertEquals("100vh", dialog.getHeight());

        bar.setMaximized(false);
        assertFalse(bar.isMaximized());
        assertNull(dialog.getWidth());
        assertNull(dialog.getHeight());
    }

    @Test
    public void maximize_doubleMaximize() {
        final Dialog dialog = new Dialog();
        final DialogHeaderBar bar = DialogHeaderBar.addTo(dialog);
        dialog.open();
        assertFalse(bar.isMaximized());
        assertNull(dialog.getWidth());
        assertNull(dialog.getHeight());

        bar.setMaximized(true);
        assertTrue(bar.isMaximized());
        assertEquals("100vw", dialog.getWidth());
        assertEquals("100vh", dialog.getHeight());

        bar.setMaximized(true);
        assertTrue(bar.isMaximized());
        assertEquals("100vw", dialog.getWidth());
        assertEquals("100vh", dialog.getHeight());

        bar.setMaximized(false);
        assertFalse(bar.isMaximized());
        assertNull(dialog.getWidth());
        assertNull(dialog.getHeight());
    }
}
