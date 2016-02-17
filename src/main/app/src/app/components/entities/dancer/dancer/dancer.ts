import {Component} from 'angular2/core';
import {RouteParams} from 'angular2/router';
import {Observable} from "rxjs";
import {Videos} from "../../video/videos/videos";
import {DancerService} from "../../../../services/DancerService";

@Component({
    selector: 'dancer',
    template: require('./dancer.html'),
    styles: [require('./dancer.css')],
    providers: [],
    directives: [Videos],
    pipes: []
})
export class Dancer {
    id:string;
    dancer:any;

    constructor(private dancerService:DancerService, params:RouteParams) {
        this.dancerService.get(params.get('id')).subscribe((data) => {
            this.dancer = data;
        });
    }
}
