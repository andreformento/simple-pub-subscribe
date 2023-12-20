package com.andreformento.pubsubcribe;

public class Employee {

    private Long id;
    private Long compensation;
    Employee(Long id, Long compensation) {
        this.id = id;
        this.compensation = compensation;
    }

    Employee(Long compensation) {
        this.compensation = compensation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompensation() {
        return compensation;
    }

    public void setCompensation(Long compensation) {
        this.compensation = compensation;
    }
}
