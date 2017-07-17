package cn.bjut.controller;

import cn.bjut.entity.Person;
import cn.bjut.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by N3verL4nd on 2017/7/4.
 */
@Controller
public class TestController {
    private PersonService personService;

    @Autowired
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }



    @RequestMapping("/list")
    public String listPersons(Model model) {
        model.addAttribute("persons", personService.getAllPersons());
        return "list";
    }

    //返回json
    @ResponseBody
    @RequestMapping(value = "/listPersons", method = RequestMethod.POST)
    public Map<String, Object> getPersons(@RequestParam("page")Integer page, @RequestParam("rows")Integer rows) {
        System.out.println("page = " + page);
        System.out.println("rows = " + rows);
        Map<String, Object> map = new HashMap<>();
        map.put("total", personService.getCount());
        map.put("rows", personService.getAllPersons(page, rows));
        return map;
    }

    @RequestMapping(value = "/deletePerson", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Boolean> deletePerson(@RequestParam("delId") Integer id) {
        Map<String, Boolean> map = new HashMap<>();
        System.out.println("delId = " + id);
        int result = personService.deletePerson(id);
        if (result != 0) {
            map.put("success", true);
        } else {
            map.put("success", false);
        }
        return map;
    }

    @RequestMapping(value = "/newPerson", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Boolean> newPerson(Person person) {
        Map<String, Boolean> map = new HashMap<>();
        int result = personService.insertPerson(person);
        if (result == 0) {
            map.put("errorMsg", true);
        } else {
            map.put("errorMsg", false);
        }
        System.out.println("result = " + result);
        return map;
    }

    @RequestMapping(value = "/updatePerson", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Boolean> updatePerson(@RequestParam("id")Integer id, Person person) {
        Map<String, Boolean> map = new HashMap<>();
        System.out.println("id = " + id);
        int result = personService.updatePerson(person);
        if (result == 0) {
            map.put("errorMsg", true);
        } else {
            map.put("errorMsg", false);
        }
        System.out.println("result = " + result);
        return map;
    }
}
