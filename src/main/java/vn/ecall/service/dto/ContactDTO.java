package vn.ecall.service.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class ContactDTO {

    @Size(max = 100)
    private String name;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    @Size(min = 5, max = 20)
    private String phone;

    private String subject;

    private String content;

    public ContactDTO(String name, String email, String phone, String subject, String content) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.subject = subject;
        this.content = content;
    }

    @Override
    public String toString() {
        return "ContactDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
