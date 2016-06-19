import {Component, ViewChild} from '@angular/core';
import {Http} from '@angular/http';
import {Input} from '@angular/core';

import { REACTIVE_FORM_DIRECTIVES,  NgModel, FormControl, FormGroup, Validators, FormBuilder} from '@angular/forms'
import {CurrentUserService} from '../../../services/ProfileService';

@Component({
    selector: 'user-profile',
    template: require('./user-profile.html'),
    styles: [require('./user-profile.css')],
    directives: [NgModel, REACTIVE_FORM_DIRECTIVES]

})
export class UserProfile {
    @Input() src:any;
    user:any;

    authentificated:boolean = false;
    error:string;

    // Todo: use forms api once it actually works.
    @ViewChild('username', true) username;
    @ViewChild('password', true) password;



    constructor(private profileService:CurrentUserService   ) {
        this.profileService = profileService;

        profileService.getCurrentUser().subscribe((user)=> {
            this.user = user;
            this.authentificated = user.authentificated;
        });
    }

    logout() {
        this.profileService.logout().subscribe(() => {
            this.authentificated = false;
        });
    }

    authenticate() {
        this.profileService.login({username: this.username.nativeElement.value, password: this.password.nativeElement.value}).subscribe((user)=> {
            this.error = '';
            this.user = user;

            this.authentificated = user.authentificated;
        }, (data) => {
            //noinspection TypeScriptUnresolvedFunction
            this.error = data.json().type;
        });
    }
}
