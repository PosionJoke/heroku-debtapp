package pl.bykowski.rectangleapp.controller.todelete;

import java.util.List;

public interface EmployeeService {

    public Employee getEmployeeByName(String name);

    List<Employee> getAllEmployees();
}
