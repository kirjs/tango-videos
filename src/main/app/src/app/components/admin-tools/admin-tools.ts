import {Component} from 'angular2/core';
import {NeedsPermission} from "../needs-permission/needs-permission";
import {AdminToolsService} from "../../services/AdminToolsService";

@Component({
    selector: 'admin-tools',
    template: require('./admin-tools.html'),
    styles: [require('./admin-tools.css')],
    providers: [AdminToolsService],
    directives: [NeedsPermission],
    pipes: []
})
export class AdminTools {
    constructor(private adminToolsService:AdminToolsService) {

    }

    renameDancerStatus:String = '';

    renameDancer(oldName, newName){
        this.renameDancerStatus = 'In progress';
        this.adminToolsService.renameDancer(oldName, newName).subscribe(()=>{
            this.renameDancerStatus = 'Done renaming ' + oldName + ' to ' + newName;
        });
    }
}
