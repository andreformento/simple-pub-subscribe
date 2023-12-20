package com.andreformento.pubsubcribe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
class EmployeeController {

    private Map<Long, Employee> employees = new HashMap<>();
    private AtomicLong employeeId = new AtomicLong(0);
    @Autowired
    private KafkaTemplate<Object, Object> template;


    @GetMapping("/employees")
    Collection<Employee> all() {
        return employees.values();
    }

    @PostMapping("/employees")
    Employee newEmployee(@RequestBody Employee newEmployee) {
        Long newid = employeeId.incrementAndGet();
        Employee employee = new Employee(newid, newEmployee.getCompensation());

        this.template.send("USER_EVALUATION_SUBMITTED", employee);

        return employees.put(newid, employee);
    }

    @GetMapping("/employees/{id}")
    Employee one(@PathVariable Long id) {
        return employees.get(id);
    }
}

