import {Component, Input} from 'angular2/core';
import {DancerService} from "../../services/DancerService";
import {RouteParams} from 'angular2/router';
import {Videos} from "../videos/videos";

@Component({
    selector: 'dancer',
    templateUrl: 'app/components/dancer/dancer.html',
    styleUrls: ['app/components/dancer/dancer.css'],
    providers: [],
    directives: [Videos],
    pipes: []
})
export class Dancer {
    id:string;
    dancer: any;

    constructor(private dancerService:DancerService, params:RouteParams) {
        this.id = params.get('id');

    }

    ngOnInit() {
        this.dancerService.get(this.id).subscribe((data) => {
            this.dancer = data;
        });
    }

}
