import { Control  } from 'angular2/common';
var regex = /^[a-zA-Z- ]+$/;
export function nameValidaters(control: Control) {
    if(!regex.test(control.value)){
        return {validCharacters: false};
    } else {
        // Null means valid
        return null;
    }
}