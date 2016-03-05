import {Component} from 'angular2/core';
import {Observable} from 'rxjs';
import {AdminToolsService} from "../../../services/AdminToolsService";
import {NeedsPermission} from "../../common/needs-permission/needs-permission";
import {ChannelManager} from "./channel-manager/channel-manager";
import {DancerService} from "../../../services/DancerService";
import {NgAutocompleteContainer} from "../../common/autocomplete/ng2-autocomplete";
import {NgAutocompleteInput} from "../../common/autocomplete/ng2-autocomplete";

export interface Stat {
    name: string;
    value: string;
}

@Component({
    selector: 'admin-tools',
    template: require('./admin-tools.html'),
    styles: [require('./admin-tools.css')],
    providers: [AdminToolsService],
    directives: [NeedsPermission, ChannelManager, NgAutocompleteContainer, NgAutocompleteInput],
    pipes: []
})
export class AdminTools {

    stats$:Observable<Array<Stat>>;
    private dancersSource;


    constructor(private adminToolsService:AdminToolsService, dancers:DancerService) {
        this.stats$ = this.adminToolsService.stats();
        this.dancersSource = dancers.listNames().map(result=> {
            return result.map((dancer)=> dancer.name);
        });
    }

    renameDancerStatus:String = '';

    renameDancer(oldName, newName) {
        this.renameDancerStatus = 'In progress';
        this.adminToolsService.renameDancer(oldName.trim(), newName.trim()).subscribe((count)=> {
            this.renameDancerStatus = 'Done renaming ' + oldName + ' to ' + newName + '. [' + count + ' instances]';
        });
    }
}
