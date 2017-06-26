/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Peja
 */
@FacesValidator("pas_val")
public class PasswrodValidator implements Validator {

    private static final String[] regex = new String[]{
        "^.*[A-Z].*$", //hasCapitalLetter
        "^.*[a-z].*[a-z].*[a-z].*$", //has3LowerLetters
        "^.*[0-9].*$", //hasDigits
        "^.*[\\.\\*\\-\\+#@].*$", //hasSpecialCharacters
        "^[A-Za-z].*$", // startsWithLetter
        "^.*[A-Za-z]{3,}.*$" //has3ConsecutiveLetters
    };

    private Pattern[] pattern; //6
    private static String[] message = new String[]{
        "should have min 1 uppercase letter",
        "should have min 3  lowercase letters",
        "should have min 1 digit",
        "should have min 1 special character",
        "should start with letter",
        "should not have 3 consecutive letters"
    };

       
    
    
    public PasswrodValidator(){
        pattern = new Pattern[regex.length];
     for (int i = 0; i<regex.length; i++ ) 
            pattern[i] = Pattern.compile(regex[i]);
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        if(value.toString().length() <8 ||value.toString().length()>12 )
            throw new ValidatorException(new FacesMessage("Invalid length"));
        
        Matcher matcher;
        
        for(int i=0; i<6; i++){
            matcher = pattern[i].matcher(value.toString());
            
            if(i<5 && !matcher.matches()){

			FacesMessage msg =
				new FacesMessage("Wrong password format",
						message[i]);
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
            
            if(i == 5 && matcher.matches()){
                	FacesMessage msg =
				new FacesMessage("Wrong password format",
						message[i]);
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
            }
            
        }
        
    }

}
