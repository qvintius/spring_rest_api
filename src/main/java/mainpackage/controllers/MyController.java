package mainpackage.controllers;

import jakarta.validation.Valid;
import mainpackage.models.Person;
import mainpackage.services.PersonService;
import mainpackage.util.PersonErrorResponse;
import mainpackage.util.PersonNotCreatedException;
import mainpackage.util.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController //@Controller + @ResponseBody над каждым методом
@RequestMapping("/people")
public class MyController {
    private final PersonService personService;

    @Autowired
    public MyController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/sayHello")
    public String sayHello(){
        return "Hello world!";
    }

    @GetMapping("")//равносильно следующему методу
    public List<Person> getPeople(){
        return personService.findAll();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Person>> allPeople(){
        return ResponseEntity.ok(personService.findAll());
    }

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable("id") int id){
        return personService.findById(id);
    }

    @PostMapping("")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid Person person, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();//получить ошибки из bindingResult
            for (FieldError error:errors) {
                errorMsg.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage()).append("; ");
            }
            throw new PersonNotCreatedException(errorMsg.toString());//выбросить исключение с сообщением об ошибке
        }
        personService.save(person);
        return ResponseEntity.ok(HttpStatus.OK);//HTTP ответ с пустым телом и статусом 200
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> edit(@PathVariable("id") int id, @RequestBody @Valid Person person, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();//получить ошибки из bindingResult
            for (FieldError error:errors) {
                errorMsg.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage()).append("; ");
            }
            throw new PersonNotCreatedException(errorMsg.toString());//выбросить исключение с сообщением об ошибке
        }
        personService.update(id, person);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id){
        personService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e){
        PersonErrorResponse response = new PersonErrorResponse("Person with this id wasn't found", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException e){
        PersonErrorResponse response = new PersonErrorResponse(e.getMessage(), System.currentTimeMillis());//получает сообщение ошибки от PersonNotCreatedException
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
