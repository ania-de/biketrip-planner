package com.biketrip.biketrip_planner.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    //@Query("SELECT e from Employee where e.department = department ")


    List<Employee> findByDepartment(Department department);
}
