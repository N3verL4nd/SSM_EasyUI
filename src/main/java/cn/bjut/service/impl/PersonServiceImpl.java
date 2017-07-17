package cn.bjut.service.impl;

import cn.bjut.entity.Person;
import cn.bjut.mapper.PersonMapper;
import cn.bjut.service.PersonService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by N3verL4nd on 2017/7/5.
 */
@Service("personService")
public class PersonServiceImpl implements PersonService {
    private PersonMapper personMapper;

    @Autowired
    public void setPersonMapper(PersonMapper personMapper) {
        this.personMapper = personMapper;
    }

    @Override
    public List<Person> getAllPersons(int page, int rows) {
        PageHelper.startPage(page, rows);
        return personMapper.selectAllPersons();
    }

    @Override
    public List<Person> getAllPersons() {
        return personMapper.selectAllPersons();
    }

    @Override
    public int deletePerson(Integer id) {
        return personMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertPerson(Person person) {
        return personMapper.insert(person);
    }

    @Override
    public int updatePerson(Person person) {
        return personMapper.updateByPrimaryKey(person);
    }

    @Override
    public int getCount() {
        return personMapper.selectTotal();
    }
}
