package com.example.lab06.repository;

import com.example.lab06.pojo.Wizard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WizardService {
    @Autowired
    private WizardRepository repository;

    public List<Wizard> retrieveWizards() {
        return this.repository.findAll();
    }

    public Wizard addWizard(Wizard wizard) {
        return this.repository.save(wizard);
    }

    public Wizard updateWizard(Wizard wizard) {
        return this.repository.save(wizard);
    }

    public boolean deleteWizard(String _id) {
        this.repository.deleteById(_id);
        return true;
    }
}
