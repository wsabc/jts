package com.example.cs;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;

//@RunWith(SpringRunner.class)
public class CsServiceUT extends BaseUT {

    @TestConfiguration
    static class CsServiceTestContextConfiguration {

        @Bean
        public CsService csService() {
            return new CsService();
        }
    }

    @Autowired
    CsService csService;

    @MockBean
    EmployeeRepository employeeRepository;

    @Before
    public void setup() {
        Employee alex = new Employee();
        alex.setName("alex");

        Mockito.when(employeeRepository.findByName(alex.getName()))
                .thenReturn(alex);
    }

    @Test
    public void whenFindByName_thenReturnEmployee() {
        // given
        String name = "alex";

        // when
        Employee found = employeeRepository.findByName(name);

        // then
        assertThat(found.getName()).isEqualTo(name);
    }
}
