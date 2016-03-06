import {Component} from 'angular2/core';
import {RouteParams} from 'angular2/router';
import {Observable} from "rxjs";
import {Videos} from "../../video/videos/videos";
import {DancerService} from "../../../../services/DancerService";
import {AddableField} from "../../../common/addable-field/addable-field";

@Component({
    selector: 'dancer',
    template: require('./dancer.html'),
    styles: [require('./dancer.css')],
    providers: [],
    directives: [Videos, AddableField],
    pipes: []
})
export class Dancer {
    id:string;
    dancer:any;

    addPseudonym(name){
        this.dancerService.addDancer(this.dancer.id, name).subscribe((data)=> {
            this.dancer.pseudonyms = data;
        });

    }

    constructor(private dancerService:DancerService, params:RouteParams) {
        this.dancerService.get(params.get('id')).subscribe((data) => {
            this.dancer = data;
        });
    }
}
