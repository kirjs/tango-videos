import {Directive, Attribute, HostBinding} from 'angular2/core';
import {CurrentUserService} from "../../services/ProfileService";

@Directive({
    selector: '[needs-permission]',
    pipes: [],
})
export class NeedsPermission {
    isHidden = false;
    @HostBinding('style.display') get hidden(){
        return this.isHidden ? 'none': '';

    }
    constructor(@Attribute("needs-permission") value, userService: CurrentUserService) {

    }
}
