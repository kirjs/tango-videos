import {Component} from 'angular2/core';
import {Http} from 'angular2/http';
import {Input} from "angular2/core";
import {CurrentUserService} from "../../services/ProfileService";
import { FORM_DIRECTIVES, ControlGroup, Control,  Validators, FormBuilder } from 'angular2/common';

@Component({
    selector: 'user-profile',
    template: require('./userProfile.html'),
    styles: [require('./userProfile.css')],
    directives: [FORM_DIRECTIVES]

})
export class UserProfile {
    @Input() src:any;
    user:any;
    loginForm:ControlGroup;
    username:Control;
    password:Control;
    authentificated:boolean = false;
    error:string;


    constructor(private profileService:CurrentUserService, private builder:FormBuilder) {
        this.profileService = profileService;
        this.username = new Control("admin", Validators.required);
        this.password = new Control("hashme", Validators.required);
        this.loginForm = builder.group({
            username: this.username,
            password: this.password
        });


        profileService.getCurrentUser().subscribe((user)=> {
            this.user = user;
            this.authentificated = user.authentificated;
        });
    }

    logout() {
        this.profileService.logout().subscribe((data) => {
            this.authentificated = false;
        });
    }

    authenticate() {
        this.profileService.login(this.loginForm.value).subscribe((user)=> {
            this.error = "";
            this.user = user;

            this.authentificated = user.authentificated;
        }, (data) => {
            //noinspection TypeScriptUnresolvedFunction
            this.error = data.json().type;
        });
    }
}
