package cn.bjut.service;

import cn.bjut.entity.Person;

import java.util.List;

/**
 * Created by N3verL4nd on 2017/7/5.
 */
public interface PersonService {
    List<Person> getAllPersons(int page, int rows);
    List<Person> getAllPersons();

    int deletePerson(Integer id);

    int insertPerson(Person person);

    int updatePerson(Person person);

    int getCount();
}
