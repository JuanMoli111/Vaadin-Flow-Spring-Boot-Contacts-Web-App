package com.example.application.views.list;

import java.util.List;

import com.example.application.data.entity.Company;
import com.example.application.data.entity.Contact;
import com.example.application.data.entity.Status;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class ContactForm extends FormLayout{

    private Contact contact;

    //Binder is something vaadin uses for binding between model objects and UI components
    Binder<Contact> binder = new BeanValidationBinder<>(Contact.class);


    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    EmailField email = new EmailField("Email");

    ComboBox<Status> status = new ComboBox<>("Status");
    ComboBox<Company> company = new ComboBox<>("Company");


    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");

    public ContactForm(List<Company> companies, List<Status> statuses) {
        
        addClassName("contact-form");
        
        binder.bindInstanceFields(this);

        company.setItems(companies);
        company.setItemLabelGenerator(Company::getName);

        status.setItems(statuses);
        status.setItemLabelGenerator(Status::getName);

        add(
            firstName,
            lastName,
            email,
            company,
            status,
            createButtonLayout()
        );

    }

    public void setContact(Contact contact){
        this.contact = contact;
        binder.readBean(contact);
    }

    private Component createButtonLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, contact)));
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));


        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save, delete, cancel);
    }   

    private void validateAndSave() {
        try {
            binder.writeBean(contact);
            fireEvent(new SaveEvent(this, contact));
        } catch (ValidationException e) {
            e.printStackTrace();
        } 
    }

    // Events
    //Creamos una clase base para los eventos que se emitan desde una clase ContactForm, 
    //Que sea hija de ComponentEvent que es el objeto que ofrece Vaadin para los eventos
    public static abstract class ContactFormEvent extends ComponentEvent<ContactForm> {

        //Salvamos el contacto que estamos trabajando sea guardarlo o eliminarlo etc
        private Contact contact;

        protected ContactFormEvent(ContactForm source, Contact contact) {
            super(source, false);
            this.contact = contact;
        }

        public Contact getContact(){
            return contact;
        }

    }

    public static class SaveEvent extends ContactFormEvent {

        SaveEvent(ContactForm source, Contact contact){
            super(source, contact);
        }

    }

    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(ContactForm source, Contact contact) {
            super(source, contact);
        }
    }

    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(ContactForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, 
                                                                ComponentEventListener<T> listener) {

        return getEventBus().addListener(eventType, listener);
    }


}
