package com.example.lab06.view;

import com.example.lab06.pojo.Wizard;
import com.example.lab06.pojo.Wizards;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Route("mainPage.it")
public class MainWizardView extends VerticalLayout {
    private TextField fullNameField;
    private NumberField moneyField;
    private RadioButtonGroup genderRadioGroup;
    private ComboBox positionSelectionBox, schoolSelectionBox, houseSelectionBox;
    private HorizontalLayout buttonLayout;
    private Button previousBtn, createBtn, updateBtn, deleteBtn, nextBtn;
    private Wizards wizards;
    private int wizardIndex = 0;

    public MainWizardView() {
        this.wizards = new Wizards();

        this.fullNameField = new TextField();
        this.fullNameField.setPlaceholder("Fullname");
        this.genderRadioGroup = new RadioButtonGroup("Gender :");
        this.genderRadioGroup.setItems("Male", "Female");
        this.positionSelectionBox = new ComboBox();
        this.positionSelectionBox.setItems("Student", "Teacher");
        this.moneyField = new NumberField("Dollars");
        this.moneyField.setPrefixComponent(new Paragraph("$"));
        this.schoolSelectionBox = new ComboBox();
        this.schoolSelectionBox.setItems("Hogwarts", "Beauxbatons", "Durmstrang");
        this.houseSelectionBox = new ComboBox();
        this.houseSelectionBox.setItems("Gryffindor", "Ravenclaw", "Hufflepuff", "Slytherin");

        this.buttonLayout = new HorizontalLayout();
        this.previousBtn = new Button("<<");
        this.previousBtn.addClickListener(e -> {
            this.wizardIndex = Math.max(this.wizardIndex-1, 0);
            this.updateField();
        });
        this.previousBtn.getStyle().set("cursor", "pointer");
        this.createBtn = new Button("Create");
        this.createBtn.addClickListener(e -> {
            String fullName = this.fullNameField.getValue();
            char sex = this.genderRadioGroup.getValue().equals("Male") ? 'm' : 'f';
            String position = this.positionSelectionBox.getValue().equals("Student") ? "student" : "teacher";
            int money = this.moneyField.getValue().intValue();
            String school = (String) this.schoolSelectionBox.getValue();
            String house = (String) this.houseSelectionBox.getValue();

            Wizard wiz = new Wizard(null, sex, fullName, school, house, money, position);

            WebClient
                .create()
                .post()
                .uri("http://localhost:8080/addWizard")
                .body(Mono.just(wiz), Wizard.class)
                .retrieve()
                .bodyToMono(Wizard.class)
                .block();
            this.wizardIndex = this.wizards.getWizards().size();
            this.fetchWizards();
        });
        this.createBtn.getStyle().set("cursor", "pointer");
        this.updateBtn = new Button("Update");
        this.updateBtn.addClickListener(e -> {
            String id = this.wizards.getWizards().get(this.wizardIndex).get_id();
            String fullName = this.fullNameField.getValue();
            char sex = this.genderRadioGroup.getValue().equals("Male") ? 'm' : 'f';
            String position = this.positionSelectionBox.getValue().equals("Student") ? "student" : "teacher";
            int money = this.moneyField.getValue().intValue();
            String school = (String) this.schoolSelectionBox.getValue();
            String house = (String) this.houseSelectionBox.getValue();

            WebClient
                .create()
                .post()
                .uri("http://localhost:8080/updateWizard")
                .body(Mono.just(new Wizard(id, sex, fullName, school, house, money, position)), Wizard.class)
                .retrieve()
                .bodyToMono(Wizard.class)
                .block();
            this.fetchWizards();
        });
        this.updateBtn.getStyle().set("cursor", "pointer");
        this.deleteBtn = new Button("Delete");
        this.deleteBtn.addClickListener(e -> {
           WebClient
               .create()
               .post()
               .uri("http://localhost:8080/deleteWizard")
               .body(Mono.just(this.wizards.getWizards().get(this.wizardIndex)), Wizard.class)
               .retrieve()
               .bodyToMono(Boolean.class)
               .block();
           this.wizardIndex = 0;
           this.fetchWizards();
        });
        this.deleteBtn.getStyle().set("cursor", "pointer");
        this.nextBtn = new Button(">>");
        this.nextBtn.addClickListener(e -> {
            this.wizardIndex = Math.min(this.wizardIndex+1, this.wizards.getWizards().size()-1);
            this.updateField();
        });
        this.nextBtn.getStyle().set("cursor", "pointer");
        this.buttonLayout.add(previousBtn, createBtn, updateBtn, deleteBtn, nextBtn);

        this.add(fullNameField, genderRadioGroup, positionSelectionBox, moneyField, schoolSelectionBox, houseSelectionBox, buttonLayout);
        this.fetchWizards();
    }

    private void fetchWizards() {
        List<Wizard> w = WebClient
                            .create()
                            .get()
                            .uri("http://localhost:8080/wizards")
                            .retrieve()
                            .bodyToMono(new ParameterizedTypeReference<List<Wizard>>() {})
                            .block();
        this.wizards.setWizards(w);
        this.updateField();
    }

    private void updateField() {
        this.previousBtn.setEnabled(!(this.wizardIndex == 0));
        this.nextBtn.setEnabled(!(this.wizardIndex == this.wizards.getWizards().size()-1));
        this.fullNameField.setValue(this.wizards.getWizards().get(this.wizardIndex).getName());
        this.genderRadioGroup.setValue(this.wizards.getWizards().get(this.wizardIndex).getSex() == 'm' ? "Male" : "Female");
        this.positionSelectionBox.setValue(this.wizards.getWizards().get(this.wizardIndex).getPosition().equals("student") ? "Student" : "Teacher");
        this.moneyField.setValue((double) this.wizards.getWizards().get(this.wizardIndex).getMoney());
        this.schoolSelectionBox.setValue(this.wizards.getWizards().get(this.wizardIndex).getSchool());
        this.houseSelectionBox.setValue(this.wizards.getWizards().get(this.wizardIndex).getHouse());
    }
}
