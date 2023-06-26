package mainpackage.util;

public class PersonNotCreatedException extends RuntimeException{
    public PersonNotCreatedException(String msg){
        super(msg);//ошибка при создании человека передается RuntimeException
    }
}
