

import entities.Address;
import entities.Employee;
import entities.Project;
import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Engine implements Runnable {

    private final EntityManager entityManager;
    Scanner scanner;

    public Engine(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.scanner = new Scanner(System.in);
    }


    public void run() {

        this.removeObjects();
//        this.containsEmployee();
//        this.employeesWithSalaryOver50000();
//        this.employeesFromDepartment();
//        this.addNewAddressAndAddToEmployee();
//        this.addressesWithEmployeeCount();
//        this.addressesWithEmployeeCountShort();
//        this.getEployeeWithProject();
//        this.findLatest10Projects();
//        this.increaseSalaries();
//        this.removeTowns();
//        this.findEmployeesByFirstName();
//        this.employeesMaximumSalaries();

        this.entityManager.close();



    }




    private void removeObjects() {

        try {
            List<Town> towns = this.entityManager
                    .createQuery("FROM Town", Town.class)
                    .getResultList();


            for (Town town : towns) {
                if (town.getName().length() > 5) {
                    this.entityManager.detach(town);

                }
            }

            this.entityManager.getTransaction().begin();

            for (Town town : towns) {
                if (this.entityManager.contains(town)) {
                    System.out.print(town.getName() + " -> ");

                    town.setName(town.getName().toLowerCase());

                    System.out.println(town.getName());
                }
            }

            this.entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            this.entityManager.getTransaction().rollback();
        }

    }

    private void containsEmployee() {

        System.out.println("Enter employee names:");
        String name = scanner.nextLine().trim();


        this.entityManager.getTransaction().begin();

        try {

            Employee employee = this.entityManager
                    .createQuery("FROM Employee WHERE CONCAT(firstName,' ', lastName) = :name ", Employee.class)
                    .setParameter("name", name)
                    .getSingleResult();

            System.out.println("Yes");
        } catch (NoResultException e) {
//            e.printStackTrace();
            System.out.println("No");
        }

        this.entityManager.getTransaction().commit();
    }


    private void employeesWithSalaryOver50000() {


        try {
            this.entityManager.getTransaction().begin();
            this.entityManager
                    .createQuery("FROM Employee WHERE salary > :value", Employee.class)
                    .setParameter("value", BigDecimal.valueOf(50000))
                    .getResultList()
                    .forEach(em -> System.out.println(em.getFirstName()));
            this.entityManager.getTransaction().commit();

        } catch (NoResultException e) {
            e.printStackTrace();
            this.entityManager.getTransaction().rollback();
        }




    }


    private void employeesFromDepartment() {

        try {
            this.entityManager.getTransaction().begin();

            this.entityManager
                    .createQuery("FROM Employee WHERE department.name = 'Research and Development' ORDER BY salary, id", Employee.class)
                    .getResultList()
                    .forEach(employee -> System.out.printf("%s %s from %s - $%.2f%n",
                            employee.getFirstName(), employee.getLastName(),
                            employee.getDepartment().getName(), employee.getSalary()));

            this.entityManager.getTransaction().commit();

        }catch (Exception e){
            e.printStackTrace();
            this.entityManager.getTransaction().rollback();
        }
    }


    private void addNewAddressAndAddToEmployee() {

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine().trim();

        try {
            this.entityManager.getTransaction().begin();

            Employee employee = this.entityManager
                    .createQuery("FROM Employee WHERE lastName = :lastName", Employee.class)
                    .setParameter("lastName", lastName)
                    .getSingleResult();



            //Checks if town Sofia already exists in DB
            Town sofia = null;

            List<Town> towns = this.entityManager
                    .createQuery("FROM Town", Town.class)
                    .getResultList();

            for (Town town : towns) {
                if (town.getName().equalsIgnoreCase("Sofia")) {
                    sofia = town;

                }
            }

            if (null == sofia){
                sofia = new Town();
                sofia.setName("Sofia");
                this.entityManager.persist(sofia);
            }

            //Checks if address Vitoshka 15 already exists in DB
            Address address = null;
            List<Address> addresses = this.entityManager
                    .createQuery("FROM Address ", Address.class)
                    .getResultList();

            for (Address addr : addresses) {
                if (addr.getText().equalsIgnoreCase("Vitoshka 15") && addr.getTown()==sofia) {
                    address = addr;

                }
            }

            if (null == address){
                address = new Address();
                address.setText("Vitoshka 15");
                address.setTown(sofia);
                this.entityManager.persist(address);
            }


            employee.setAddress(address);

            this.entityManager.getTransaction().commit();

            System.out.printf("%s %s changed address to %s, %s%n",
                    employee.getFirstName(), employee.getLastName(),
                    employee.getAddress().getText(), employee.getAddress().getTown().getName());

        } catch (Exception e) {
            e.printStackTrace();
            this.entityManager.getTransaction().rollback();
        }
    }

    private void addressesWithEmployeeCount() {

        try{
            this.entityManager.getTransaction().begin();


            List<Object[]> addresses = this.entityManager.createNativeQuery("SELECT a.address_text, t.name, COUNT(e.employee_id) as count_e, t.town_id\n" +
                    "FROM addresses a\n" +
                    "JOIN towns t\n" +
                    "ON t.town_id = a.town_id\n" +
                    "JOIN employees e\n" +
                    "ON e.address_id = a.address_id\n" +
                    "GROUP BY e.address_id\n" +
                    "ORDER BY count_e DESC, t.town_id LIMIT 10" )
                    .getResultList();

            for (Object[] address :
                    addresses) {
                System.out.printf("%s, %s - %s employees%n",
                        address[0], address[1], address[2]);
            }


            this.entityManager.getTransaction().commit();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    private void addressesWithEmployeeCountShort() {

        this.entityManager.getTransaction().begin();

        List<Address> addresses = this.entityManager
                .createQuery("SELECT a FROM Address a ORDER BY a.employees.size DESC, a.town.id ASC",Address.class)
                .setMaxResults(10).getResultList();

        for (Address address : addresses) {

            System.out.printf("%s, %s - %d employees%n",
                    address.getText(),
                    address.getTown().getName(),
                    address.getEmployees().size());
        }

        this.entityManager.getTransaction().commit();
    }


    private void getEployeeWithProject() {
        System.out.print("Enter employee id: ");
        Integer id = Integer.valueOf(scanner.nextLine());

        try {

            this.entityManager.getTransaction().begin();


            List<Object[]> projects = this.entityManager.createNativeQuery("SELECT first_name, last_name, job_title, p.name\n" +
                    "FROM employees e\n" +
                    "JOIN employees_projects ep\n" +
                    "ON e.employee_id = ep.employee_id\n" +
                    "JOIN projects p \n" +
                    "ON p.project_id = ep.project_id\n" +
                    "WHERE e.employee_id = :employee_id\n" +
                    "ORDER BY p.name ;" )
                    .setParameter("employee_id", id)
                    .getResultList();

            System.out.printf("%s %s - %s%n", projects.get(0)[0], projects.get(0)[1], projects.get(0)[2]);

            for (Object[] project :
                    projects) {
                System.out.printf("\t%s%n",
                        project[3]);
            }


            this.entityManager.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void findLatest10Projects() {

        try{
            this.entityManager.getTransaction().begin();


           this.entityManager.createQuery("FROM Project ORDER BY startDate DESC, name", Project.class )
                    .setMaxResults(10)
                    .getResultList()
                    .forEach(p-> System.out.printf("Project name: %s\n" +
                    " \tProject Description: %s\n" +
                    " \tProject Start Date:%s\n" +
                    " \tProject End Date: %s\n",
                    p.getName(),
                    p.getDescription(),
                    p.getStartDate(),
                    p.getEndDate()));


            this.entityManager.getTransaction().commit();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    private void increaseSalaries() {
        this.entityManager.getTransaction().begin();

        this.entityManager
                .createQuery("SELECT e FROM Employee AS e " +
                        "WHERE e.department.name IN ('Engineering', 'Tool Design', 'Marketing', 'Information Services') " +
                        "ORDER BY e.id", Employee.class)
                .getResultList()
                .forEach(employee -> {
                    employee.setSalary(employee.getSalary().multiply(BigDecimal.valueOf(1.12)));
                    System.out.printf("%s %s($%.2f)%n", employee.getFirstName(),
                            employee.getLastName(), employee.getSalary());
                });

        this.entityManager.getTransaction().commit();

    }

    private void removeTowns() {

        System.out.print("Enter town name: ");

        String townName = scanner.nextLine().trim();

        try {

            this.entityManager.getTransaction().begin();

            Town town = this.entityManager.createQuery("FROM Town WHERE name = :townName", Town.class)
                    .setParameter("townName", townName)
                    .getSingleResult();

            List<Address> addresses = this.entityManager
                    .createQuery("FROM Address WHERE town.name = :townName", Address.class)
                    .setParameter("townName", townName)
                    .getResultList();

            String output = String.format("%d address%s in %s deleted%n",
                    addresses.size(), (addresses.size() != 1) ? "es" : "", town.getName());

            addresses.forEach(address -> {
                for (Employee employee : address.getEmployees()) {
                    employee.setAddress(null);
                }
                address.setTown(null);
                this.entityManager.remove(address);
            });

            this.entityManager.remove(town);

            this.entityManager.getTransaction().commit();

            System.out.println(output);

        } catch (Exception e) {
            e.printStackTrace();
            this.entityManager.getTransaction().rollback();
        }


    }

    private void findEmployeesByFirstName() {


        System.out.print("Enter first name staring pattern: ");
        String pattern = scanner.nextLine().trim();

        try {
            this.entityManager.getTransaction().begin();

            this.entityManager
                    .createQuery("FROM Employee WHERE firstName LIKE :pattern", Employee.class)
                    .setParameter("pattern", pattern + "%")
                    .getResultList()
                    .forEach(employee -> System.out.printf("%s %s - %s - ($%.2f)%n", employee.getFirstName(),
                            employee.getLastName(), employee.getJobTitle(), employee.getSalary()));

            this.entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            this.entityManager.getTransaction().rollback();
        }
    }


    private void employeesMaximumSalaries() {

        try{
            this.entityManager.getTransaction().begin();


            List<Object[]> departments = entityManager.createNativeQuery("SELECT d.name, max(e.salary) AS 'max'\n" +
                    "FROM departments d\n" +
                    "JOIN employees e\n" +
                    "ON e.department_id = d.department_id\n" +
                    "GROUP BY d.name\n" +
                    "HAVING `max` NOT BETWEEN 30000 AND 70000\n" +
                    "ORDER BY `max` DESC;" )
                    .getResultList();

            for (Object[] department :
                    departments) {
                System.out.printf("%s - %s%n",
                        department[0], department[1]);
            }


            this.entityManager.getTransaction().commit();
        }
        catch(Exception e){
            e.printStackTrace();
            this.entityManager.getTransaction().rollback();
        }

    }






}
