package org.vaadin.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
@PWA(name = "Project Base for Vaadin", shortName = "Project Base", enableInstallPrompt = false)
public class MainView extends VerticalLayout {

    public MainView() {
        Button button = new Button("Open dialog",
                e -> {
                    final Dialog dlg = new Dialog();
                    dlg.setWidth("500px");
                    dlg.setHeight("200px");
                    DialogHeaderBar.addTo(dlg).setCaption("My Dialog");
                    dlg.add(new VerticalLayout(new Span("Hello world!")));
                    dlg.open();
                });

        add(button);
    }
}
