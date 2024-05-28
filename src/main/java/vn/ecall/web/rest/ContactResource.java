package vn.ecall.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.ecall.service.MailService;
import vn.ecall.service.dto.ContactDTO;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class ContactResource {

    private final Logger log = LoggerFactory.getLogger(ContactResource.class);

    private final MailService mailService;

    public ContactResource(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/contact")
    public ResponseEntity sendRequestContact(@Valid @RequestBody ContactDTO contact) {
        mailService.sendContactEmail(contact);
        return ResponseEntity.noContent().build();
    }
}
