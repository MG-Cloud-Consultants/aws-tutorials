package de.margul.awstutorials.rds.iam.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.margul.awstutorials.rds.iam.repository.DemoEntity;
import de.margul.awstutorials.rds.iam.repository.DemoRepository;

@RestController
@RequestMapping("/entities")
public class DemoDbService {

    @Autowired
    private DemoRepository repository;

    @PostMapping("")
    public void createEntity(@RequestBody String name) {
        DemoEntity en = new DemoEntity(name, "This is " + name);
        en.setName(name);
        en.setDescription("This is " + name);
        repository.save(en);
    }

    @GetMapping("{name}")
    public Optional<DemoEntity> getEntityByName(@PathVariable String name) {

        return repository.findById(name);
    }

}
