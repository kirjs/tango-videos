import {Directive, Attribute, HostBinding} from 'angular2/core';
import {CurrentUserService} from '../../services/ProfileService';

@Directive({
    selector: '[needs-permission]',
    pipes: [],
})
export class NeedsPermission {
    isShown = false;
    @HostBinding('style.display') get shown(){
        return this.isShown ? '': 'none';
    }

    constructor(@Attribute('needs-permission') permission, userService: CurrentUserService) {
        userService.permissionObservable.subscribe((permissions) => {
            this.isShown = permission in permissions;
        });
    }
}
