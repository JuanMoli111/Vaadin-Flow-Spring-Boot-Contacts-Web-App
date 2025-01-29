package com.example.application.views.list;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.example.application.data.Contact;
import com.example.application.service.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

@PageTitle("Contacts | Vaadin CRM")
@Route(value = "")
public class ListView extends VerticalLayout {

    Grid<Contact> grid = new Grid<>(Contact.class);

    TextField filterText = new TextField();

    ContactForm form;
    private CrmService service;

    public ListView(CrmService service) {
        
        this.service = service;

        addClassName("list-view");
        setSizeFull();


        configureGrid();
        configureForm();

        add(
            getToolbar(),
            getContent()
        );

        updateList();
        closeEditor();

    }

    private void closeEditor(){
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
    }


    private void updateList(){
        grid.setItems(service.findAllContacts(filterText.getValue()));
    }


    private Component getContent(){
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureForm(){

        form = new ContactForm(service.findAllCompanies(),service.findAllStatuses());
        form.setWidth("25em");
    }
        
    
    private Component getToolbar() {

        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());


        Button addContactButton = new Button("Add contact");
        HorizontalLayout toolbar = new HorizontalLayout(filterText,addContactButton);
        
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    Map<String,String> hash = null;
    
    

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.setColumns("firstName","lastName","email");
        
        grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status");
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Company");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));


        grid.asSingleSelect().addValueChangeListener(e -> editContact(e.getValue()));
        }

        ///isi alguien de-selecciona un contacto sera null, entonces cerrar el editor, de lo contrario
        /// 
        private void editContact(Contact contact){
            if(contact == null) {
                closeEditor();
            } else {
                form.setContact(contact);
                form.setVisible(true);
                addClassName("editing");
            }

        }
    

}
