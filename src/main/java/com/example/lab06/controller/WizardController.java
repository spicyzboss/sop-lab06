package com.example.lab06.controller;

import com.example.lab06.pojo.Wizard;
import com.example.lab06.repository.WizardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WizardController {
    @Autowired
    private WizardService wizardService;

    @GetMapping("/wizards")
    public ResponseEntity<List<Wizard>> getWizards() {
        return ResponseEntity.ok(this.wizardService.retrieveWizards());
    }

    @PostMapping("/addWizard")
    public ResponseEntity<Wizard> addWizard(@RequestBody Wizard wizard) {
        wizard.set_id(null);
        return ResponseEntity.ok(this.wizardService.addWizard(wizard));
    }

    @PostMapping("/updateWizard")
    public ResponseEntity<Wizard> updateWizard(@RequestBody Wizard wizard) {
        return ResponseEntity.ok(this.wizardService.updateWizard(wizard));
    }

    @PostMapping("/deleteWizard")
    public ResponseEntity<Boolean> deleteWizard(@RequestBody Wizard wizard) {
        return ResponseEntity.ok(this.wizardService.deleteWizard(wizard.get_id()));
    }
}
