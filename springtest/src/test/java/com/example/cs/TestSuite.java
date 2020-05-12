package com.example.cs;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        EmployeeRepositoryUT.class,
        CsServiceUT.class,
        CsControllerUT.class
})
public class TestSuite {
}
