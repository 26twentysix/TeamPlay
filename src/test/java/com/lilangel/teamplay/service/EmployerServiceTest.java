package com.lilangel.teamplay.service;

import com.lilangel.teamplay.service.impl.EmployerServiceImpl;
import com.lilangel.teamplay.exception.EmployerNotFoundException;
import com.lilangel.teamplay.models.Employer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс юнит-тестов для {@link EmployerService}
 */
@SpringBootTest
@Transactional
public class EmployerServiceTest {

    private final EmployerService employerService;

    @Autowired
    public EmployerServiceTest(EmployerService employerService) {
        this.employerService = employerService;
    }

    /**
     * Тестирует метод {@link EmployerServiceImpl#saveNewEmployer(String, String, Integer)}
     * Метод проходит проверку, если возвращается идентификатор созданного сотрудника
     */
    @Test
    public void saveNewEmployerTest() {
        Integer createdId = employerService.saveNewEmployer("John Doe", "johndoe@test.com", 1);
        assertNotNull(createdId);
    }

    /**
     * Тестирует метод {@link  EmployerServiceImpl#deleteById(Integer)}
     * Метод проходит проверку, если после удаления сотрудника, запрос на получение сотрудника по идентификатору
     * удаленного бросает исключение {@link EmployerNotFoundException}
     *
     * @throws EmployerNotFoundException если сотрудник не был найден
     */
    @Test
    public void deleteByIdTest() throws EmployerNotFoundException {
        Integer createdId = employerService.saveNewEmployer("John Doe", "johndoe@test.com", 1);
        employerService.deleteById(createdId);
        assertThrows(EmployerNotFoundException.class, () -> employerService.getById(createdId));
    }

    /**
     * Тестирует метод {@link EmployerServiceImpl#deleteById(Integer)} с передачей неправильного идентификатора
     * Метод проходит проверку, если на попытку удаления сотрудника по несуществующему идентификатору
     * бросается исключение {@link EmployerNotFoundException}
     */
    @Test
    public void deleteByWrongIdTest() {
        Integer createdId = employerService.saveNewEmployer("John Doe", "johndoe@test.com", 1);
        assertThrows(EmployerNotFoundException.class, () -> employerService.deleteById(createdId + 1));
    }

    /**
     * Тестирует метод {@link EmployerServiceImpl#getAll()}
     * Метод проходит проверку, если возвращается ненулевой список сотрудников
     */
    @Test
    public void getAllTest() {
        employerService.saveNewEmployer("John Doe", "johndoe@test.com", 1);
        employerService.saveNewEmployer("Jane Doe", "janedoe@test.com", 1);
        List<Employer> employers = employerService.getAll();
        assertNotNull(employers);
    }

    /**
     * Тестирует метод {@link EmployerServiceImpl#getById(Integer)}
     * Метод проходит проверку, если получается получить сотрудника по идентификатору, полученному после создания нового
     * @throws EmployerNotFoundException если сотрудник не был найден
     */
    @Test
    public void getByIdTest() throws EmployerNotFoundException {
        Integer createdId = employerService.saveNewEmployer("John Doe", "johndoe@test.com", 1);
        employerService.getById(createdId);
    }

    /**
     * Тестирует метод {@link EmployerServiceImpl#getById(Integer)} с передачей неправильного идентификатора
     * Метод проходит проверку, если попытка получения сотрудника по несуществующему идентификатору бросает исключение
     * {@link EmployerNotFoundException}
     */
    @Test
    public void getByWrongIdTest() {
        Integer createdId = employerService.saveNewEmployer("John Doe", "johndoe@test.com", 1);
        assertThrows(EmployerNotFoundException.class, () -> employerService.getById(createdId + 1));
    }
}
