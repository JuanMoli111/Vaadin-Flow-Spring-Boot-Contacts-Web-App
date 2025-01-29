package com.example.application.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.application.data.Company;
import com.example.application.data.Contact;
import com.example.application.data.Status;
import com.example.application.repository.CompanyRepository;
import com.example.application.repository.ContactRepository;
import com.example.application.repository.StatusRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;


@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(ContactRepository contactRepository, CompanyRepository companyRepository,
            StatusRepository statusRepository) {

        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (contactRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }

            logger.info("Generating demo data");
            int seed = 123;
            Random random = new Random(seed);

            // Generar empresas ficticias
            logger.info("... generating 5 Company entities...");
            List<Company> companies = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Company company = new Company();
                company.setName("Company " + (i + 1));
                companies.add(company);
            }
            companies = companyRepository.saveAll(companies);

            // Generar estados ficticios
            logger.info("... generating Status entities...");
            List<Status> statuses = statusRepository.saveAll(
                    Stream.of("Imported lead", "Not contacted", "Contacted", "Customer", "Closed (lost)")
                            .map(Status::new)
                            .collect(Collectors.toList()));

            // Generar contactos ficticios
            logger.info("... generating 50 Contact entities...");
            List<Contact> contacts = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                Contact contact = new Contact();
                contact.setFirstName("FirstName" + (i + 1));
                contact.setLastName("LastName" + (i + 1));
                contact.setEmail("contact" + (i + 1) + "@example.com");
                contact.setCompany(companies.get(random.nextInt(companies.size())));
                contact.setStatus(statuses.get(random.nextInt(statuses.size())));
                contacts.add(contact);
            }
            contactRepository.saveAll(contacts);

            logger.info("Generated demo data");
        };
    }
}
