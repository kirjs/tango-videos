import {Directive, Attribute, HostBinding, Input} from '@angular/core';
import {CurrentUserService} from "../../../services/ProfileService";


@Directive({
    selector: '[needsPermission]'
})
export class NeedsPermission {
    isShown = false;

    @HostBinding('hidden') get hidden(){
        return !this.isShown;
    }

    @Input() set needsPermission(permission: string){
        this.userService.permissionObservable.subscribe((permissions) => {
            this.isShown = permission in permissions;
        });
    };

    constructor(private userService: CurrentUserService) {}
}
