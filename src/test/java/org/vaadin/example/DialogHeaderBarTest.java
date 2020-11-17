package org.vaadin.example;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.vaadin.flow.component.dialog.Dialog;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.mvysny.kaributesting.v10.ButtonKt._click;
import static com.github.mvysny.kaributesting.v10.LocatorJ.*;
import static org.junit.jupiter.api.Assertions.*;

/**
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
}
