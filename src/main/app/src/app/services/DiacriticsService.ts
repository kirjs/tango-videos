var removeDiacritics = require('diacritics');


export class DiacriticsService {
    remove(string:String){
        return removeDiacritics(string);
    }
}