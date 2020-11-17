package org.vaadin.example;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.mvysny.kaributesting.v10.LocatorJ.*;

/**
 * Uses Karibu-Testing for insanely fast Vaadin unit testing.
 * @author Martin Vysny <mavi@vaadin.com>
 */
public class MainViewTest {
    private static final Routes routes = new Routes().autoDiscoverViews("org.vaadin.example");

    @BeforeEach
    public void mockVaadin() {
        MockVaadin.setup(routes);
    }
    @AfterEach
    public void tearDownVaadin() {
        MockVaadin.tearDown();
    }

    @Test
    public void testDialog() {
        _click(_get(Button.class, spec -> spec.withCaption("Open dialog")));
        _assertOne(Dialog.class);
        _assertOne(DialogHeaderBar.class);
    }
}
