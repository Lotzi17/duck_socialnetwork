package lab2_map.validator;

import lab2_map.domain.Duck;

public class DuckValidator implements ValidationStrategy<Duck>{
    @Override
    public void validate(Duck duck) {
        if( duck.getType()==null){
            throw new ValidationException("Duck type cannot be null");
        }
        if(duck.getSpeed()<=0){
            throw new ValidationException("Duck speed must be positive");
        }
        if(duck.getEndurance()<=0){
            throw new ValidationException("Duck endurance must be positive");
        }
}
}
