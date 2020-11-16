package org.vaadin.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
@PWA(name = "Project Base for Vaadin", shortName = "Project Base", enableInstallPrompt = false)
@CssImport(value = "./styles/dialog-styles.css", themeFor = "vaadin-dialog-overlay")
public class MainView extends VerticalLayout {

    public MainView() {
        Button button = new Button("Open dialog",
                e -> {
                    final Dialog dlg = new Dialog();
                    // workaround for https://github.com/vaadin/vaadin-dialog/issues/126
                    dlg.getElement().getThemeList().add("no-padding");
                    dlg.setWidth("500px");
                    dlg.setHeight("200px");
                    DialogHeaderBar header = new DialogHeaderBar(dlg);
                    header.setCaption("My Dialog");
                    dlg.add(header);
                    dlg.open();
                });

        add(button);
    }
}
