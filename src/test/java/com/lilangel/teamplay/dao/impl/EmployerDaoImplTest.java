package com.lilangel.teamplay.dao.impl;

import com.lilangel.teamplay.models.Employer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс юнит-тестов для {@link EmployerDaoImpl}
 */
@SpringBootTest
public class EmployerDaoImplTest {

    private final EmployerDaoImpl employerDao;

    @Autowired
    public EmployerDaoImplTest(EmployerDaoImpl employerDao) {
        this.employerDao = employerDao;
    }

    /**
     * Тестирует метод {@link EmployerDaoImpl#getAllEmployers()}.
     * Метод проходит проверку, если возвращает всех заданных сотрудников
     */
    @Test
    public void getAllEmployersTest() {
        HashMap<Integer, Employer> employers = new HashMap<>();
        Employer employer1 = new Employer(1, "spiritRip@mail.ru", "Yawoslav", 15);
        Employer employer2 = new Employer(2, "DaxacRip@gmail.ru", "Nikita", 17);
        employers.put(1, employer1);
        employers.put(2, employer2);
        employerDao.add(employer1);
        employerDao.add(employer2);
        assertEquals(employers, employerDao.getAllEmployers());
    }

    /**
     * Тестирует метод {@link EmployerDaoImpl#delete(Integer)}.
     * Метод проходит проверку, если в пуле employerDao отсутствует заданный сотрудник
     */
    @Test
    public void deliteTest() {
        Employer employer = new Employer(3, "shivchenko@gmail.ru", "Oleg", 18);
        employerDao.add(employer);
        employerDao.delete(3);
        assertNull(employerDao.getById(3));
    }

    /**
     * Тестирует метод {@link EmployerDaoImpl#add(Employer)}.
     * Метод проходит проверку, если в пул employerDao добавлен заданный сотрудник
     */
    @Test
    public void addTest() {
        Employer employer = new Employer(4, "Kovalenko@gmail.ru", "Maria", 12);
        employerDao.add(employer);
        assertNotNull(employerDao.getById(4));
    }

    /**
     * Тестирует метод {@link EmployerDaoImpl#getById(Integer)}.
     * Метод проходит проверку, если по заданному id возвращает сотрудника с таким же id
     */
    @Test
    public void getByIdTest() {
        Employer employer = new Employer(5, "Sobol@gmail.ru", "Anna", 9);
        employerDao.add(employer);
        assertEquals(employer, employerDao.getById(5));
    }
}
