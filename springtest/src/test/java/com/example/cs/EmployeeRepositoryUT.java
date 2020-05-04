package com.example.cs;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

//@RunWith(SpringRunner.class)
@DataJpaTest
public class EmployeeRepositoryUT extends BaseUT {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    EmployeeRepository employeeRepository;

    @Test
    public void whenFindByName_thenReturnEmployee() {
        // given
        Employee alex = new Employee();
        alex.setName("alex");
        entityManager.persist(alex);
        entityManager.flush();

        // when
        Employee found = employeeRepository.findByName(alex.getName());

        // then
        assertThat(found.getName()).isEqualTo(alex.getName());
    }
}
