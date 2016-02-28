import {Component} from 'angular2/core';
import {Observable} from 'rxjs';
import {AdminToolsService} from "../../../services/AdminToolsService";
import {NeedsPermission} from "../../common/needs-permission/needs-permission";
import {ChannelManager} from "./channel-manager/channel-manager";

export interface Stat {
    name: string;
    value: string;
}

@Component({
    selector: 'admin-tools',
    template: require('./admin-tools.html'),
    styles: [require('./admin-tools.css')],
    providers: [AdminToolsService],
    directives: [NeedsPermission, ChannelManager],
    pipes: []
})
export class AdminTools {

    stats$:Observable<Array<Stat>>;

    constructor(private adminToolsService:AdminToolsService) {
        this.stats$ = this.adminToolsService.stats();
    }

    renameDancerStatus:String = '';

    renameDancer(oldName, newName) {
        this.renameDancerStatus = 'In progress';
        this.adminToolsService.renameDancer(oldName, newName).subscribe((count)=> {
            this.renameDancerStatus = 'Done renaming ' + oldName + ' to ' + newName + '. ['+count+' instances]';
        });
    }
}
