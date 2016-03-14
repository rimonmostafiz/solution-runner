package com.tigerit.exam;

/**
 * @author Faisal Ahmed
 */
public class Applicant {
    private String name;

    private String email;

    private String repo;

    public Applicant(String name, String email, String repo) {
        this.name = name;
        this.email = email;
        this.repo = repo;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRepo() {
        return repo;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Applicant applicant = (Applicant) obj;
        return this.name.equals(applicant.getName())
                || this.email.equals(applicant.getEmail())
                || this.repo.equals(applicant.getRepo());
    }

    @Override public int hashCode() {
        final int prime = 31; int result = 1;
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }
}
