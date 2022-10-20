package com.lilangel.teamplay.dao.impl;

import com.lilangel.teamplay.dao.EmployerDao;
import com.lilangel.teamplay.models.Employer;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class EmployerDaoImpl implements EmployerDao {

    /**
     * словарь сотрудников
     */
    private final HashMap<Integer, Employer> employers;
    /**
     * счётчик для текущего Id сотрудника
     */
    private static Integer currentId;

    public EmployerDaoImpl() {
        this.employers = new HashMap<>();
        currentId = 0;
    }

    /**
     * возвращает  всех сотрудников
     *
     * @return словарь со всеми сотрудниками
     */
    @Override
    public HashMap<Integer, Employer> getAllEmployers() {
        return employers;
    }

    /**
     * удаляет из словаря сотрудника с заданным Id
     *
     * @param id Id сотрудника
     */
    @Override
    public void delete(Integer id) {
        employers.remove(id);
    }

    /**
     * добавляет сотрудника в словарь
     *
     * @param employer сотрудник
     */
    @Override
    public void add(Employer employer) {
        employer.setId(currentId++);
        employers.put(employer.getId(), employer);
    }

    /**
     * возвращает сотрудника, по переданному Id
     *
     * @param id Id сотрудника
     * @return сотрудник
     */
    @Override
    public Employer getById(Integer id) {
        return employers.get(id);
    }
}
